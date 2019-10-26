package com.example.tourismof;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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

public class Change_email extends AppCompatActivity {
EditText email_edit;
String Current_ID;
Button btn;
    private ProgressDialog loadingBar;

    private FirebaseAuth mAuth;
    private DatabaseReference UsersRef;
    private StorageReference UserProfileImageRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_email);

        loadingBar = new ProgressDialog(this);

  email_edit=findViewById(R.id.email);
btn=findViewById(R.id.btn_save);
        mAuth = FirebaseAuth.getInstance();
        Current_ID=mAuth.getCurrentUser().getUid();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(Current_ID);
        get_and_set_edit_text();

             btn.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     changeemail();
                 }
             });

    }

    private void changeemail() {
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        if(user !=null)
        {
            loadingBar.setTitle("Saving Information");
            loadingBar.setMessage("Please wait, while we are Saving Changes...");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);
            user.updateEmail(email_edit.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(Change_email.this, "Email has been updated", Toast.LENGTH_SHORT).show();

                    UsersRef.child("email").setValue(email_edit.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(Change_email.this, "Email has been updated", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(Change_email.this, FirstActivity.class);
                            intent.putExtra("hostt", "account");
                            startActivity(intent);
                        }
                    });
                }
            });


        }

    }

    private void get_and_set_edit_text() {

        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("email"))
                {

                String email=dataSnapshot.child("email").getValue().toString();
                email_edit.setText(email);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
