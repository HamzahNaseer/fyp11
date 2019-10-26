package com.example.tourismof;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class new_password extends AppCompatActivity {
    private DatabaseReference UserRef;
    FirebaseAuth mAuth;
    FirebaseUser current_id;
    EditText e1,e2;
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);


         mAuth = FirebaseAuth.getInstance();
        Intent intent = getIntent();
        e1=findViewById(R.id.edit_code11);
        btn=findViewById(R.id.code_btn11);
        Bundle bd = intent.getExtras();
        if(bd !=null) {
            current_id = (FirebaseUser) bd.get("user");
            Toast.makeText(this, ""+ current_id, Toast.LENGTH_SHORT).show();
             btn.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     current_id.updatePassword(e1.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                         @Override
                         public void onSuccess(Void aVoid) {
                             Toast.makeText(new_password.this, "Password has been changed", Toast.LENGTH_SHORT).show();
                             Intent intent=new Intent(new_password.this, LoginActivity.class);
                             startActivity(intent); }
                     });
                 }
             });




        }

    }
}
