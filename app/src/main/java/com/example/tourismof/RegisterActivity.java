package com.example.tourismof;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.telecom.PhoneAccount;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthSettings;

public class RegisterActivity extends AppCompatActivity {

    private TextView Email, Password, cofirmPassword;
    private Button createButton;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        loadingBar = new ProgressDialog(this);
        Email = findViewById(R.id.register_email);
        Password = findViewById(R.id.register_password);
        cofirmPassword = findViewById(R.id.register_confirm_password);
        createButton = findViewById(R.id.register_create_account);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewAccount();
            }
        });



    }

    private void createNewAccount() {
        String email = Email.getText().toString();
        String password = Password.getText().toString();
        String confirmPassword = cofirmPassword.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Please Enter Email", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Please Enter Password", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(getApplicationContext(), "Please Confirm Password", Toast.LENGTH_SHORT).show();
        } else if (!(password.equals(confirmPassword))) {
            Toast.makeText(getApplicationContext(), "Password Doesn't Match, Please Enter Same Password", Toast.LENGTH_SHORT).show();
        } else {

            loadingBar.setTitle("Creating New Account");
            loadingBar.setMessage("Please Wait while we are authenticating");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                sendUserToSetupActivity();
                                Toast.makeText(getApplicationContext(), "Authenticated Successfully", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            } else {
                                String message = task.getException().getMessage();
                                Toast.makeText(getApplicationContext(), "Error" + message, Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                            }
                        }
                    });


        }

    }

    private void sendUserToSetupActivity() {
        Intent setupIntent = new Intent(RegisterActivity.this, edit_profile.class);
        setupIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        setupIntent.putExtra("from","login");
        startActivity(setupIntent);
        finish();
    }
}
