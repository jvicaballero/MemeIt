package com.example.memeit.fragments;

import android.content.Intent;
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
import androidx.fragment.app.Fragment;

import com.example.memeit.Auth.EditProfile;
import com.example.memeit.Auth.Login;
import com.example.memeit.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class ProfileFragment extends Fragment {
    Button changeemail, changename, changepass, logout;
    EditText setName, setEmail, setPass, curPass;
    ParseUser parseUser = ParseUser.getCurrentUser();

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
        setPass = view.findViewById(R.id.setPass);
        curPass = view.findViewById(R.id.curPass);

        changeemail= view.findViewById(R.id.changeemail);
        changename= view.findViewById(R.id.changename);
        changepass= view.findViewById(R.id.changepass);

        if (currentUser != null) {
            // Name, email address, and profile photo Url
            String name = String.valueOf(currentUser.getUsername());
            setName.setText(name);
            String email = currentUser.getEmail();
            setEmail.setText(email);
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
                        Toast.makeText(getContext(), "Logged out!", Toast.LENGTH_SHORT).show();
                        startActivity(i);
                    }
                });
            }
        });

        changepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newpassword= setPass.getText().toString();
                // check the length of password
                if(newpassword.length()<6){
                    Toast.makeText(getContext(),"Password Must be >= 6 Characters",Toast.LENGTH_SHORT).show();
                }
                if(newpassword.isEmpty()){
                    Toast.makeText(getContext(),"Password cannot be empty",Toast.LENGTH_SHORT).show();
                }
                else {
                    parseUser.setPassword(newpassword);
                    Toast.makeText(getContext(), "Password Updated", Toast.LENGTH_SHORT).show();
                    setPass.setText("");
                    curPass.setText("");
                }
            }
        });

        changeemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newemail= setEmail.getText().toString();
                if(newemail.isEmpty()){
                    Toast.makeText(getContext(), "Email can't be empty!", Toast.LENGTH_SHORT).show();
                }
                else{
                    parseUser.setEmail(newemail);
                    parseUser.saveInBackground();
                    Toast.makeText(getContext(),"Email Updated",Toast.LENGTH_SHORT).show();
                    setEmail.setText("");
                }
            }
        });



    }

}
