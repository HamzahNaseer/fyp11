package com.example.tourismof;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class Change_password extends AppCompatActivity {
    private ProgressDialog loadingBar;

    private FirebaseAuth mAuth;
    private DatabaseReference UsersRef;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();



    private StorageReference UserProfileImageRef;
       String Current_ID;
    EditText old_pass,new_pass,confirm_pass;
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        Toast.makeText(Change_password.this, "aagya= "+user, Toast.LENGTH_SHORT).show();


        loadingBar = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();
        Current_ID=mAuth.getCurrentUser().getUid();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(Current_ID);
        old_pass=findViewById(R.id.old_password);
        new_pass=findViewById(R.id.new_password);
        confirm_pass=findViewById(R.id.confirm_password1);
         btn=findViewById(R.id.btn_save_password);


         btn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 if(TextUtils.isEmpty(old_pass.getText().toString()))
                 {
                     old_pass.setError("Empty!!");
                 }
                 else if (TextUtils.isEmpty(new_pass.getText().toString()))
                 {
                     new_pass.setError("Empty!!");
                 }

                 else if(TextUtils.isEmpty(confirm_pass.getText().toString()))
                 {
                     confirm_pass.setError("Empty!!");
                 }
                 else check_old_password();

             }
         });

    }

    private void check_old_password() {

        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("email"))
                {
                    String email=dataSnapshot.child("email").getValue().toString();
                    AuthCredential credential = EmailAuthProvider
                            .getCredential(email, old_pass.getText().toString());
                    user.reauthenticate(credential).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            change_password();
                        }

                    });


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void change_password() {

        if(new_pass.getText().toString().equals(confirm_pass.getText().toString()))
        {

            loadingBar.setTitle("Saving Information");
            loadingBar.setMessage("Please wait, while we are Saving Changes...");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);
            user.updatePassword(confirm_pass.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(Change_password.this, "Password has been changed", Toast.LENGTH_SHORT).show();
                    Intent intent =new Intent(Change_password.this, FirstActivity.class);
                    intent.putExtra("hostt", "account");
                    startActivity(intent);
                }
            });


        }
        else confirm_pass.setError("Passwords does not match");
    }
}
