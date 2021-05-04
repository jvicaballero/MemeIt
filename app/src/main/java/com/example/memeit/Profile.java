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
import com.parse.ParseUser;

public class Profile extends AppCompatActivity {

    Button editprofile;
    private BottomNavigationView bottomNav;
    ImageView appbarsinout;
    EditText setName, setEmail;
    private ImageView profileimage1;


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
                ParseUser.getCurrentUser().logOut();
                Toast.makeText(Profile.this,"Signed Out",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
            }
        });
        // End Toolbar
        // start user info
        ParseUser currentUser = ParseUser.getCurrentUser();
        setEmail = findViewById(R.id.setEmail);
        setName = findViewById(R.id.setName);
        if (currentUser != null) {
            // Name, email address, and profile photo Url
            String name = String.valueOf(currentUser.getUsername());
            setName.setText(name);
            String email = currentUser.getEmail();
            setEmail.setText(email);
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
        // set user profile picture
        profileimage1 = findViewById(R.id.profimage1);

        // end user profile
        // start edit profile
        editprofile= findViewById(R.id.editprofile);
        editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), EditProfile.class));
            }
        });
        // end user profile
    }
}