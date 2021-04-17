package com.example.memeit.Auth;

import android.content.Intent;
import android.os.Bundle;

import com.example.memeit.MainActivity;
import com.example.memeit.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class Register extends AppCompatActivity {
    EditText etUsername, etEmail, etPassword, etPassword2;
    Button etButton;
    TextView etAlready;
    FirebaseAuth fAuth;
    ProgressBar progressBar;


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

                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Register.this, "User Created.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }
                        else{
                            Toast.makeText(Register.this, "Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });


    }
}