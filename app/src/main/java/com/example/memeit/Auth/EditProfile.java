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

public class EditProfile extends AppCompatActivity {
    public static final String TAG = "EditProfile";
    private AlertDialog.Builder dialogbuilder;
    private AlertDialog dialog;
    Button changeemail, changepass, savenewpassword, saveemail;
    private EditText newpassword, confirmnewpassword;
    private EditText newemail, confirmnewemail;
    private String newpass, newemail1;
    private BottomNavigationView bottomNav;
    FirebaseUser user;
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
                Toast.makeText(EditProfile.this,"Signed Out",Toast.LENGTH_SHORT).show();
                FirebaseAuth.getInstance().signOut();
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
                        Toast.makeText(EditProfile.this,"Signed Out",Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(getApplicationContext(), Login.class));
                        finish();
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
        user = FirebaseAuth.getInstance().getCurrentUser();

        dialogbuilder.setView(conactPopupView);
        dialog = dialogbuilder.create();
        dialog.show();

        savenewpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(newpassword.getText().toString()!=confirmnewpassword.getText().toString()){
                    newpass = newpassword.getText().toString();
                    user.updatePassword(newpass)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "User password updated.");
                                        Toast.makeText(EditProfile.this,"Password Changed",Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }
                                    else{
                                        Log.d(TAG, "User password failed to update.");
                                    }
                                }
                            });
                }
                else{
                    Log.d(TAG, "button not being clicked");
                }
            }
        });
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
                if(newemail.getText().toString()!=confirmnewemail.getText().toString()) {
                    newemail1 = newpassword.getText().toString();
                    user.updateEmail(newemail1)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "User email address updated.");
                                        Toast.makeText(EditProfile.this,"Email Changed",Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }
                                    else{
                                        Log.d(TAG, "User email failed to update.");
                                    }
                                }
                            });
                }
                else{
                    Log.d(TAG, "button not being clicked");
                }
            }
        });
    }
}