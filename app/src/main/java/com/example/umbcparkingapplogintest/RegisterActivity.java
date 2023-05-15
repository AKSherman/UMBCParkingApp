package com.example.umbcparkingapplogintest;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


public class RegisterActivity extends AppCompatActivity {

    // creating variables for our edit text and buttons.
    private EditText userNameEdt, passwordEdt, campusIdEdt, emailEdt;
    private Button registerBtn;

    private SignInClient oneTapClient;
    private BeginSignInRequest signUpRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Parse.initialize(new Parse.Configuration.Builder(RegisterActivity.this)
                .applicationId(getString(R.string.back4app_app_id))
                .clientKey(getString(R.string.back4app_client_key))
                .server(getString(R.string.back4app_server_url))
                .build());

        oneTapClient = Identity.getSignInClient(RegisterActivity.this);
        signUpRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        // Your server's client ID, not your Android client ID.
                        .setServerClientId(getString(R.string.your_web_client_id))
                        // Show all accounts on the device.
                        .setFilterByAuthorizedAccounts(false)
                        .build())
                .build();

        // initializing our edit text  and buttons.
        userNameEdt = findViewById(R.id.idEdtUserName);
        passwordEdt = findViewById(R.id.idEdtPassword);
        campusIdEdt = findViewById(R.id.idEdtCampusID);
        emailEdt = findViewById(R.id.idEdtEmail);
        registerBtn = findViewById(R.id.idBtnRegister);

        // adding on click listener for our button.
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // on below line we are getting data from our edit text.
                String userName = userNameEdt.getText().toString();
                String password = passwordEdt.getText().toString();
                String campusID = campusIdEdt.getText().toString();
                String userEmail = emailEdt.getText().toString();

                // checking if the entered text is empty or not.
                if (TextUtils.isEmpty(userName) && TextUtils.isEmpty(password)) {
                    Toast.makeText(RegisterActivity.this, "Please enter user name and password", Toast.LENGTH_SHORT).show();
                }

                // calling a method to register a user.
                registerUser(userName, password, userEmail);
            }
        });
    }

    private void registerUser(String userName, String password, String userEmail) {

        // on below line we are creating
        // a new user using parse user.
        ParseUser user = new ParseUser();

        // Set the user's username and password,
        // which can be obtained from edit text
        user.setUsername(userName);
        user.setPassword(password);
        user.setEmail(userEmail);


        // calling a method to register the user.
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                // on user registration checking if
                // the error is null or not.
                if (e == null) {
                    // if the error is null we are displaying a toast message and
                    // redirecting our user to new activity and passing the user name.
                    Toast.makeText(RegisterActivity.this, "User Registered successfully", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(RegisterActivity.this, HomeActivity.class);
                    i.putExtra("username", userName);
                    startActivity(i);
                } else {
                    // if we get any error then we are logging out
                    // our user and displaying an error message
                    ParseUser.logOut();
                    Toast.makeText(RegisterActivity.this, "Fail to Register User..", Toast.LENGTH_SHORT).show();
                    Log.d("error",e.toString());
                }
            }
        });
    }


}
