package com.example.memeit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.memeit.Auth.EditProfile;
import com.example.memeit.Auth.Login;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Profile extends AppCompatActivity {

    Button editprofile;
    private BottomNavigationView bottomNav;
    ImageView appbarsinout;
    FirebaseUser user;
    EditText setName, setEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Toolbar implementation
        Toolbar appbar = findViewById(R.id.appbar);
        setSupportActionBar(appbar);
        getSupportActionBar().setTitle("MemeIt");
        appbarsinout = appbar.findViewById(R.id.appbarsignout);
        appbarsinout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Profile.this,"Signed Out",Toast.LENGTH_SHORT).show();
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
            }
        });
        // End Toolbar
        // start user info
        user = FirebaseAuth.getInstance().getCurrentUser();
        setEmail = findViewById(R.id.setEmail);
        setName = findViewById(R.id.setName);
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            setName.setText(name);
            String email = user.getEmail();
            setEmail.setText(email);
            Uri photoUrl = user.getPhotoUrl();

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();
        }
        // end user info
        //  start bottom nav
        bottomNav= findViewById(R.id.botton_navigation);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                switch (item.getItemId()) {
                    case R.id.postings:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        break;
                    case R.id.saved:
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        // end bottom nav
        editprofile= findViewById(R.id.editprofile);
        editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), EditProfile.class));
            }
        });
    }
}