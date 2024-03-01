package com.example.loginpanel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity2 extends AppCompatActivity {
    EditText signup_password,signup_email,signup_username,signup_name;
    Button button;
    TextView loginredirecttext;
    FirebaseDatabase database;
    DatabaseReference reference;

    GoogleSignInClient mGoogleSignInClient;
    private static int RC_SIGN_IN =100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup2);
        signup_name = findViewById(R.id.signup_name);
        signup_email = findViewById(R.id.signup_email);
        signup_username = findViewById(R.id.signup_username);
        signup_password = findViewById(R.id.signup_password);
        button = findViewById(R.id.button);
        loginredirecttext = findViewById(R.id.loginredirecttext);



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                database = FirebaseDatabase.getInstance();// mumbai
                reference = database.getReference("users");//mumbai

                String name = signup_name.getText().toString();
                String email = signup_email.getText().toString();
                String username = signup_username.getText().toString();
                String password = signup_password.getText().toString();

                HelperClass helperClass = new HelperClass(name, email,username,password);
              //  reference.child(name).setValue(helperClass);
                reference.child(username).setValue(helperClass);

                Toast.makeText(SignupActivity2.this, "You have signup successfully!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SignupActivity2.this, Loginactivity.class);
                startActivity(intent);
                signOut();


            }
        });



        loginredirecttext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SignupActivity2.this, Loginactivity.class);
                startActivity(intent);

            }
        });


        signup_name.addTextChangedListener(loginTextWatcher);
        signup_email.addTextChangedListener(loginTextWatcher);
        signup_username.addTextChangedListener(loginTextWatcher);
        signup_password.addTextChangedListener(loginTextWatcher);



        //========================google lof in==================

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Check for existing Google Sign In account, if the user is already signed in
// the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        //updateUI(account);

        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();

            }
        });

        //========================google lof in==================



    }///=============on create finish ======================

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {


                        finish();

                    }
                });
    }

    //========================google lof in==================
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

            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
            if (acct != null) {
                String personName = acct.getDisplayName();
                String personGivenName = acct.getGivenName();
                String personFamilyName = acct.getFamilyName();
                String personEmail = acct.getEmail();
                String personId = acct.getId();
                Uri personPhoto = acct.getPhotoUrl();



            }

            startActivity(new Intent(SignupActivity2.this, MainActivity.class));

            // Signed in successfully, show authenticated UI.
            // updateUI(account);
        } catch (ApiException e) {



        }
    }

    //========================google lof in==================


    private TextWatcher loginTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String inputname = signup_name.getText().toString().trim();
            String inputemail = signup_email.getText().toString().trim();
            String inputusername = signup_username.getText().toString().trim();
            String inputpass = signup_password.getText().toString().trim();
            button.setEnabled(!inputname.isEmpty() && !inputpass.isEmpty() &&!inputemail.isEmpty() && !inputusername.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }



};


}





