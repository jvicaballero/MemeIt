package com.example.memeit.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.memeit.Auth.Login;
import com.example.memeit.R;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProfileFragment extends Fragment {
    public static final String TAG = "ProfileFragment";
    private AlertDialog.Builder dialogbuilder;
    private AlertDialog dialog;
    Button changeemail, changepass, savenewpassword, saveemail, changeuser, saveuser, logout;
    private EditText newpassword, confirmnewpassword;
    private EditText newemail, confirmnewemail;
    private EditText newuser, confirmuser;
    private ImageView profileimage;
    EditText setName, setEmail;
    ParseUser parseUser = ParseUser.getCurrentUser();
    ParseObject Images;

    public ProfileFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // start user info
        ParseUser currentUser = ParseUser.getCurrentUser();
        setEmail = view.findViewById(R.id.setEmail);
        setName = view.findViewById(R.id.setName);

        changeemail= view.findViewById(R.id.changeemail);
        changeuser= view.findViewById(R.id.changename);
        changepass= view.findViewById(R.id.changepass);
        profileimage = view.findViewById(R.id.profileimage);

        if (currentUser != null) {
            // Name, email address, and profile photo Url
            String name = String.valueOf(currentUser.getUsername());
            setName.setText(name);
            String email = currentUser.getEmail();
            setEmail.setText(email);

            // profile image
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Images");
            query.whereEqualTo("objectId","wqfO7R3BoA");
            query.getFirstInBackground(new GetCallback<ParseObject>() {
                public void done(ParseObject player, ParseException e) {
                    if (e == null) {
                        ParseFile file = player.getParseFile("Profilepicture");
                        file.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] data, ParseException e) {
                                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                profileimage.setImageBitmap(bitmap);
                            }
                        });
                    } else {
                        Log.e(TAG,e.toString());
                        // Something is wrong
                    }
                }
            });
        }

        logout= view.findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOutInBackground(new LogOutCallback(){
                    @Override
                    public void done(ParseException e) {
                        if(e != null) {
                            return;
                        }
                        Intent i= new Intent(getActivity(), Login.class);
                        Toast.makeText(getContext(), "Logged out", Toast.LENGTH_SHORT).show();
                        startActivity(i);
                    }
                });
            }
        });

        changeuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewContactDialoguser();
            }
        });

        changepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewContactDialogpass();
            }
        });

        changeemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewContactDialogemail();
            }
        });
    }

    // user
    public void createNewContactDialoguser(){
        dialogbuilder = new AlertDialog.Builder(getContext());
        final View conactPopupView = getLayoutInflater().inflate(R.layout.popupuser, null);
        newuser = (EditText) conactPopupView.findViewById(R.id.newuser);
        confirmuser = (EditText) conactPopupView.findViewById(R.id.confirmuser);
        saveuser  = (Button) conactPopupView.findViewById(R.id.saveuser);
        // get the old value of old username
        String olduser = ParseUser.getCurrentUser().getUsername();
        dialogbuilder.setView(conactPopupView);
        dialog = dialogbuilder.create();
        dialog.show();

        saveuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(newuser.getText().toString().equals(confirmuser.getText().toString())){
                    // check if the old user name is being used
                    if(olduser.equals(newuser.getText().toString())){
                        newuser.setError("Cannot use old username");
                    }
                    // check the length of username
                    if(newuser.length()==0){
                        newuser.setError("Username Cannot be empty");
                    }
                    else {
                        parseUser.setUsername(newuser.getText().toString());
                        parseUser.saveInBackground();
                        Toast.makeText(getActivity(), "Username Updated", Toast.LENGTH_SHORT).show();
                        setName.setText(newuser.getText().toString());
                        dialog.dismiss();
                    }
                }
                else{
                    Toast.makeText(getActivity(),"Username failed to Updated" ,Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
        });
    }

    // pass
    public void createNewContactDialogpass(){
        dialogbuilder = new AlertDialog.Builder(getContext());
        final View conactPopupView = getLayoutInflater().inflate(R.layout.popuppassword, null);
        newpassword = (EditText) conactPopupView.findViewById(R.id.newpassword);
        confirmnewpassword = (EditText) conactPopupView.findViewById(R.id.confirmnewpassword);
        savenewpassword  = (Button) conactPopupView.findViewById(R.id.savepassword);
        parseUser = ParseUser.getCurrentUser();

        dialogbuilder.setView(conactPopupView);
        dialog = dialogbuilder.create();
        dialog.show();

        savenewpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(newpassword.getText().toString().equals(confirmnewpassword.getText().toString())){
                    // check the length of password
                    String password =  newpassword.getText().toString().trim();
                    Log.i("lol", String.valueOf(password.length()));
                    if(password.length()<6){
                        newpassword.setError("Password Must be >= 6 Characters");
                    }
                    if(newpassword.getText().toString().isEmpty()){
                        newpassword.setError("Password cannot be empty");
                    }
                    else {
                        parseUser.setPassword(newpassword.getText().toString());
                        parseUser.saveInBackground();
                        Toast.makeText(getActivity(), "Password Updated", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }
                else{
                    Toast.makeText(getActivity(),"Password failed to Updated" ,Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
        });
    }




    // email
    public void createNewContactDialogemail(){
        dialogbuilder = new AlertDialog.Builder(getContext());
        final View conactPopupView = getLayoutInflater().inflate(R.layout.popupemail, null);
        newemail = (EditText) conactPopupView.findViewById(R.id.newuser);
        confirmnewemail = (EditText) conactPopupView.findViewById(R.id.confirmuser);
        saveemail  = (Button) conactPopupView.findViewById(R.id.saveuser);
        // get the old value of old email
        String oldemail = ParseUser.getCurrentUser().getEmail();
        dialogbuilder.setView(conactPopupView);
        dialog = dialogbuilder.create();
        dialog.show();

        saveemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(newemail.getText().toString().equals(confirmnewemail.getText().toString())){
                    if(oldemail.equals(newemail.getText().toString())){
                        newemail.setError("Cannot use old email");
                    }
                    if(newemail.getText().toString().isEmpty()){
                        newemail.setError("Email cannot be empty");
                    }
                    else {
                        parseUser.setEmail(newemail.getText().toString());
                        parseUser.saveInBackground();
                        Toast.makeText(getActivity(), "Email Updated", Toast.LENGTH_SHORT).show();
                        setEmail.setText(newemail.getText().toString());
                        dialog.dismiss();
                    }
                }
                //shouldn't this part be different, correspond to the password button? This is still in the change email section.
                else{
                    Toast.makeText(getActivity(),"Email failed to Updated",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
        });
    }
}
