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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class EditProfile extends AppCompatActivity {
    public static final String TAG = "EditProfile";
    private AlertDialog.Builder dialogbuilder;
    private AlertDialog dialog;
    Button changeemail, changepass, savenewpassword, saveemail;
    private EditText newpassword, confirmnewpassword;
    private EditText newemail, confirmnewemail;
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
                        Toast.makeText(EditProfile.this, "Password Updated", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }
                else{
                    Toast.makeText(EditProfile.this,"Password failed to Updated" + newpassword + confirmnewpassword,Toast.LENGTH_SHORT).show();
                }
            }
        });
//        parseUser.saveInBackground(new SaveCallback() {
//            @Override
//            public void done(ParseException e) {
//                if (null == e) {
//                    // report about success
//                    Toast.makeText(EditProfile.this,"Password Updated",Toast.LENGTH_SHORT).show();
//                } else {
//                    // report about error
//                    Toast.makeText(EditProfile.this,"Password failed to Updated",Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
    }

    public void createNewContactDialogemail(){
        dialogbuilder = new AlertDialog.Builder(this);
        final View conactPopupView = getLayoutInflater().inflate(R.layout.popupemail, null);
        newemail = (EditText) conactPopupView.findViewById(R.id.newemail);
        confirmnewemail = (EditText) conactPopupView.findViewById(R.id.confirmemail);
        saveemail  = (Button) conactPopupView.findViewById(R.id.saveemail);

        dialogbuilder.setView(conactPopupView);
        dialog = dialogbuilder.create();
        dialog.show();
        saveemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(newemail.getText().toString().equals(confirmnewemail.getText().toString())){
                    parseUser.setEmail(newemail.getText().toString());
                    parseUser.saveInBackground();
                    Toast.makeText(EditProfile.this,"Email Updated",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
                //shouldn't this part be different, correspond to the password button? This is still in the change email section.
                else{
//                    Toast.makeText(EditProfile.this,"Email failed to Updated",Toast.LENGTH_SHORT).show();
                    Log.i(TAG , newpassword.getText().toString() + confirmnewpassword.getText().toString());
//                    Log.e(TAG , error);
                }
            }
        });
//        parseUser.saveInBackground(new SaveCallback() {
//            @Override
//            public void done(ParseException e) {
//                if (null == e) {
//                    // report about success
//                    Toast.makeText(EditProfile.this,"Email Updated",Toast.LENGTH_SHORT).show();
//                } else {
//                    // report about error
//                    Toast.makeText(EditProfile.this,"Email failed to Updated",Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

    }
}