package com.example.memeit.Auth;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import com.example.memeit.MainActivity;
import com.example.memeit.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class Register extends AppCompatActivity {

    public static final String TAG = "Register";
    EditText etUsername, etEmail, etPassword, etPassword2;
    Button etButton;
    TextView etAlready;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    FirebaseUser user;
    ImageView profileImage;
    ParseFile photo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        etUsername = findViewById(R.id.loguser);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.logpass);
        etPassword2 = findViewById(R.id.etPassword2);
        etButton = findViewById(R.id.logbutton);
        etAlready = findViewById(R.id.etAlready);
        profileImage = findViewById(R.id.profimage);
        user = FirebaseAuth.getInstance().getCurrentUser();

        fAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);

        progressBar.setVisibility(View.INVISIBLE);

        if(fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        etAlready.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });


        etButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email =  etEmail.getText().toString().trim();
                String password =  etPassword.getText().toString().trim();
                String username = etUsername.getText().toString();
                if(TextUtils.isEmpty(email)){
                    etEmail.setError("Email Required");
                    progressBar.setVisibility(View.INVISIBLE);
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    etPassword.setError("Password is empty");
                    progressBar.setVisibility(View.INVISIBLE);
                    return;
                }
                if(password.length()<6){
                    etPassword.setError("Password Must be >= 6 Characters");
                    progressBar.setVisibility(View.INVISIBLE);
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                // profile picture logic
                Bitmap bitmapImage = ((BitmapDrawable) profileImage.getDrawable()).getBitmap(); // profile pic is the imageview
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] byteArray = stream.toByteArray();

                //update user db in parse also before starting mainACtivity
                parseHandleSignupUser(email, username, password, byteArray);

            }
        });

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // OPEN GALLERY
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent,1000);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000){
            if(resultCode == Activity.RESULT_OK){
                Uri imageuri = data.getData();
                profileImage.setImageURI(imageuri);
                Log.i(TAG, "set in profileImage" + profileImage);
            }
        }
    }

    private void parseHandleSignupUser(String email, String username, String password, byte[] byteArray){

        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);


        // profile picture
        ParseFile file = new ParseFile("image.png", byteArray);

        file.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(null == e){
                    user.put("profpic" , file);

                }
                else{
                    Log.i(TAG, "No file uploaded");

                }
            }
        });

        Log.i(TAG, "Check contents of byteArray" + byteArray);

        ParseObject Images = new ParseObject("Images");
        Images.put("Profilepicture", file);
        Images.put("test", "lol");
//        Images.add("user", user.getObjectId());
//        Images.saveInBackground();


        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Images.put("User_ID" , ParseUser.getCurrentUser());
                    Images.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e == null){
                                Log.i(TAG, "New Record For images is saved!");
                            }
                            else{
                                Log.e(TAG, "Issue with adding new Image record " + e);
                            }
                        }
                    });
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));

                } else {
                    ParseUser.logOut();
                    Toast.makeText(Register.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}