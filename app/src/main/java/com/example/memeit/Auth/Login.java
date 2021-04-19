package com.example.memeit.Auth;

import android.content.Intent;
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

public class Login extends AppCompatActivity {
    EditText etUsername, etPassword;
    Button etButton;
    ProgressBar progressBar2;
    TextView etNew;
    private SignInButton googlesignin;
    FirebaseAuth fAuth;
    private GoogleSignInClient mGoogleSignInClient;
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
        fAuth = FirebaseAuth.getInstance();
        progressBar2 = findViewById(R.id.progressBar2);
        googlesignin = findViewById(R.id.googlesignin);

        progressBar2.setVisibility(View.INVISIBLE);

        // google sign in
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        googlesignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.googlesignin:
                        signIn();
                        break;
                    // ...
                }

            }
        });

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


                fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Login.this, "Sign in Successful.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }
                        else{
                            Toast.makeText(Login.this, "Error Logging in" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar2.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });

    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            Toast.makeText(Login.this, "Signed in.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Login.this, MainActivity.class);
            startActivity(intent);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
        }
    }
}