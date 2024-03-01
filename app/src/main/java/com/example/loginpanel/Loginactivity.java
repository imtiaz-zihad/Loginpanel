package com.example.loginpanel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class Loginactivity extends AppCompatActivity {
    EditText login_username,login_password;
    Button login_button;
    TextView signinredirecttext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginactivity);
        login_username =findViewById(R.id.login_username);
        login_password =findViewById(R.id.login_password);
        login_button =findViewById(R.id.login_button);
        signinredirecttext =findViewById(R.id.signinredirecttext);

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if (!validusername() | !validpassword()){



                }else {
                    checkuser();
                }

            }
        });

        signinredirecttext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Loginactivity.this, SignupActivity2.class);
                startActivity(intent);
            }
        });


        login_username.addTextChangedListener(loginTextWatcher);
        login_password.addTextChangedListener(loginTextWatcher);
    }




    //==================================disable buttomn======================
    private TextWatcher loginTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String inputname = login_username.getText().toString().trim();
            String inputpass = login_password.getText().toString().trim();
            login_button.setEnabled(!inputname.isEmpty() && !inputpass.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }

    };

    //==================================disable buttomn======================





    public Boolean validusername(){
        String val = login_username.getText().toString();
        if (val.isEmpty()){
            login_username.setError("user cannot be empty");
            return false;
        }else {
            login_username.setError(null);
            return true;
        }

    }


    public Boolean validpassword(){
        String val = login_password.getText().toString();
        if (val.isEmpty()){
            login_password.setError("password cannot be empty");
            return false;
        }else {
            login_password.setError(null);
            return true;
        }

    }

    public void  checkuser(){
        String userUsername = login_username.getText().toString().trim();
        String userPassword = login_password.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUserDatabase = reference.orderByChild("username").equalTo(userUsername);


        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                if (snapshot.exists()){

                    login_username.setError(null);
                    String passwordFromDB =snapshot.child(userUsername).child("password").getValue(String.class);

                    if (passwordFromDB.equals(userPassword)){
                        login_username.setError(null);

                        String nameFromDB = snapshot.child(userUsername).child("name").getValue(String.class);
                        String emailFromDB = snapshot.child(userUsername).child("email").getValue(String.class);
                        String usernameFromDB = snapshot.child(userUsername).child("username").getValue(String.class);

                        Intent intent = new Intent(Loginactivity.this, MainActivity.class);


                        intent.putExtra("name", nameFromDB);
                        intent.putExtra("email", emailFromDB);
                        intent.putExtra("username", usernameFromDB);
                        intent.putExtra("password", passwordFromDB);


                        startActivity(intent);
                    }else {
                        login_password.setError("Invalid");
                        login_password.requestFocus();
                    }

                }else {
                    login_username.setError("user does not exist");
                    login_username.requestFocus();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}