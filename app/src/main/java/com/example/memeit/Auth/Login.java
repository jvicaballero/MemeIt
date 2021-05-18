package com.example.memeit.Auth;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.memeit.MainActivity;
import com.example.memeit.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class Login extends AppCompatActivity {
    EditText etUsername, etPassword;
    Button etButton;
    ProgressBar progressBar2;
    TextView etNew;
    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "GoogleActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.loguser);
        etPassword = findViewById(R.id.logpass);
        etNew = findViewById(R.id.lognew);
        etButton = findViewById(R.id.logbutton);
        progressBar2 = findViewById(R.id.progressBar2);


        progressBar2.setVisibility(View.INVISIBLE);

        etNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Register.class));
            }
        });

        etButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email =  etUsername.getText().toString().trim();
                String password =  etPassword.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    etUsername.setError("Email Required");
                    progressBar2.setVisibility(View.INVISIBLE);
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    etPassword.setError("Password is empty");
                    progressBar2.setVisibility(View.INVISIBLE);
                    return;
                }
                if(password.length()<6){
                    etPassword.setError("Password Must be >= 6 Characters");
                    progressBar2.setVisibility(View.INVISIBLE);
                    return;
                }

                progressBar2.setVisibility(View.VISIBLE);

                //Instead of passing username to login, use the email. Should still work this way
                ParseUser.logInInBackground(email, password, new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if( e!= null){
                            Toast.makeText(Login.this, "Issue with login ", Toast.LENGTH_SHORT).show();
                            Log.e("ParseLogin", "Issue with Login" , e );
                        }
                        else {
                            ParseUser currentUser = ParseUser.getCurrentUser();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            Log.i("ParseLogin" , "CurrentUserLoggedinParse: " + currentUser.getEmail());

                        }
                        progressBar2.setVisibility(View.INVISIBLE);

                    }
                });


            }
        });

    }
}