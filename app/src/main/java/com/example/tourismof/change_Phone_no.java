package com.example.tourismof;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class change_Phone_no extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference UsersRef;
    private StorageReference UserProfileImageRef;
    Button btn;
    EditText old_num,new_num;
    String Current_ID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change__phone_no);



        mAuth = FirebaseAuth.getInstance();
        Current_ID=mAuth.getCurrentUser().getUid();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(Current_ID);
        btn=findViewById(R.id.btn_save);
         old_num=findViewById(R.id.old_num);
         new_num=findViewById(R.id.new_num);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String old,neww;
                old=old_num.getText().toString().trim();
                neww=new_num.getText().toString().trim();

                if(TextUtils.isEmpty(old))
                {
                   old_num.setError("Empty!!");
                }
                else if (TextUtils.isEmpty(neww))
                {
                    new_num.setError("Empty!!");
                }
                else check_old_phone();
            }
        });
    }

    private void check_old_phone() {

        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("ph_no"))
                {
                    if(dataSnapshot.child("ph_no").getValue().toString().matches(old_num.getText().toString().trim()))
                    {
                        change_num();
                    }
                    else
                        old_num.setError("Incorrect !");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void change_num() {
        UsersRef.child("ph_no").setValue(new_num.getText().toString().trim()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(change_Phone_no.this, "Phone Number has been updated", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(change_Phone_no.this, FirstActivity.class);
                intent.putExtra("hostt", "account");
                startActivity(intent);

            }
        });


    }
}
