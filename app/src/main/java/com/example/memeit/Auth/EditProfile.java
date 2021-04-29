package com.example.memeit.Auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.memeit.MainActivity;
import com.example.memeit.Profile;
import com.example.memeit.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseUser;

public class EditProfile extends AppCompatActivity {
    public static final String TAG = "EditProfile";
    private AlertDialog.Builder dialogbuilder;
    private AlertDialog dialog;
    Button changeemail, changepass, savenewpassword, saveemail, changeuser, saveuser;
    private EditText newpassword, confirmnewpassword;
    private EditText newemail, confirmnewemail;
    private EditText newuser, confirmuser;
    private String newpass, newemail1;
    private BottomNavigationView bottomNav;
    ParseUser parseUser = ParseUser.getCurrentUser();
    ImageView appbarsinout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Toolbar implementation
        Toolbar appbar = findViewById(R.id.appbar);
        setSupportActionBar(appbar);
        getSupportActionBar().setTitle("MemeIt");
        appbarsinout = appbar.findViewById(R.id.appbarsignout);
        appbarsinout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.getCurrentUser().logOut();
                Toast.makeText(EditProfile.this,"Signed Out",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
            }
        });
        // End Toolbar

        changeemail = findViewById(R.id.changeemail);
        changepass = findViewById(R.id.changepass);
        changeuser = findViewById(R.id.changeuser);
        bottomNav= findViewById(R.id.botton_navigation);

        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                switch (item.getItemId()) {
                    case R.id.postings:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        break;
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(), Profile.class));
                        break;
                    case R.id.saved:
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

        changeemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewContactDialogemail();
            }
        });
        changepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewContactDialogpass();
            }
        });
        changeuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewContactDialoguser();
            }
        });


    }

    public void createNewContactDialoguser(){
        dialogbuilder = new AlertDialog.Builder(this);
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
                if(String.valueOf(newuser).equals(String.valueOf(confirmuser))){
                    // check if the old user name is being used
                    if(olduser.equals(newuser.getText().toString())){
                        newuser.setError("Cannot use old username");
                    }
                    // check the length of username
                    if(newuser.length()==0){
                        newuser.setError("Username Cannot be empty");
                    }
                    else {
                        parseUser.setUsername(String.valueOf(newuser));
                        parseUser.saveInBackground();
                        Toast.makeText(EditProfile.this, "Username Updated", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }
                else{
                    Toast.makeText(EditProfile.this,"Password failed to Updated" + newpassword + confirmnewpassword,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void createNewContactDialogpass(){
        dialogbuilder = new AlertDialog.Builder(this);
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
                if(String.valueOf(newpassword).equals(String.valueOf(confirmnewpassword))){
                    // check the length of password
                    if(newpassword.length()<6){
                        newpassword.setError("Password Must be >= 6 Characters");
                    }
                    if(newpassword.getText().toString().isEmpty()){
                        newpassword.setError("Password cannot be empty");
                    }
                    else {
                        parseUser.setPassword(String.valueOf(newpassword));
                        parseUser.saveInBackground();
                        Toast.makeText(EditProfile.this, "Password Updated", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }
                else{
                    Toast.makeText(EditProfile.this,"Password failed to Updated" + newpassword + confirmnewpassword,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void createNewContactDialogemail(){
        dialogbuilder = new AlertDialog.Builder(this);
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
                if(String.valueOf(newuser).equals(String.valueOf(confirmuser))){
                if(oldemail.equals(newemail.getText().toString())){
                    newemail.setError("Cannot use old email");
                }
                if(newuser.getText().toString().isEmpty()){
                    newemail.setError("Email cannot be empty");
                }
                else {
                    parseUser.setPassword(String.valueOf(newpassword));
                    parseUser.saveInBackground();
                    Toast.makeText(EditProfile.this, "Password Updated", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
                }
                //shouldn't this part be different, correspond to the password button? This is still in the change email section.
                else{
//                    Toast.makeText(EditProfile.this,"Email failed to Updated",Toast.LENGTH_SHORT).show();
                    Log.i(TAG , newpassword.getText().toString() + confirmnewpassword.getText().toString());
//                    Log.e(TAG , error);
                }
            }
        });
    }
}