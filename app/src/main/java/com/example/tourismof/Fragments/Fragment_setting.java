package com.example.tourismof.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import de.hdodenhof.circleimageview.CircleImageView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.tourismof.Change_email;
import com.example.tourismof.Change_password;
import com.example.tourismof.LoginActivity;
import com.example.tourismof.R;
import com.example.tourismof.change_Phone_no;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class Fragment_setting extends Fragment {
TextView Signout,email,password,phone_number;
    private CircleImageView profile_image;
    private DatabaseReference mRef;
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_fragment_setting, container, false);
    profile_image=view.findViewById(R.id.profile);
         Signout=view.findViewById(R.id.signout);
         email=view.findViewById(R.id.email);
        phone_number=view.findViewById(R.id.change_phone);
        password=view.findViewById(R.id.password);
         mAuth = FirebaseAuth.getInstance();
         String currentUser = mAuth.getCurrentUser().getUid();
         mRef = FirebaseDatabase.getInstance().getReference().child("Users");


      email.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent intent=new Intent(getContext(), Change_email.class);
              startActivity(intent);

          }
      });


      password.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent intent=new Intent(getContext(), Change_password.class);
              startActivity(intent);
          }
      });
      phone_number.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent intent=new Intent(getContext(), change_Phone_no.class);
              startActivity(intent);
          }
      });
         Signout.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 SharedPreferences settings = getContext().getSharedPreferences("MY_PREFS_NAME", Context.MODE_PRIVATE);
                 settings.edit().clear().commit();
                 mAuth.getInstance().signOut();

                 Intent intent=new Intent(getActivity(), LoginActivity.class);

                 startActivity(intent);


             }
         });
        mRef.child(currentUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                if(dataSnapshot.hasChild("Profile")){

                    String ProfileImage = dataSnapshot.child("Profile").getValue().toString();
                    Picasso.get().load(ProfileImage).placeholder(R.drawable.profile).into(profile_image);
                }}

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    return view;
    }

}
