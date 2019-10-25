package com.example.tourismof.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.widget.TextView;

import com.example.tourismof.FirstActivity;
import com.example.tourismof.edit_profile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.tourismof.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;
import static com.example.tourismof.LoginActivity.MY_PREFS_NAME;

public class ProfileFragment extends Fragment {

    public ProfileFragment() {
        // Required empty public constructor
    }

    static int abb;
    Button host;
    private Button button;
    private FirebaseAuth mAuth;
    private DatabaseReference mRef;
    ImageView edit;
    private CircleImageView profile_image;
    private TextView userName, Gender, Country, ph_no, bio;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        mAuth = FirebaseAuth.getInstance();
        String currentUser = mAuth.getCurrentUser().getUid();
        mRef = FirebaseDatabase.getInstance().getReference().child("Users");
        profile_image = view.findViewById(R.id.profile);
        edit=view.findViewById(R.id.edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendusertoEditActivity();

            }
        });
        host=view.findViewById(R.id.Host);
        SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        abb = prefs.getInt("First", 1);
        if(abb%2 !=0)
        {
            host.setText("Switch to Host");

        }
        else
            host.setText("Switch to Guest");

        host.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                intent = new Intent(getContext(), FirstActivity.class);
                SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);


                if(abb%2 !=0 ) {
                    intent.putExtra("hostt", 12);
                    abb++;
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putInt("First", abb);
                    editor.apply();
                }
                else {
                    intent.putExtra("hostt", 14);
                    host.setText("Switch to Host");
                    abb++;
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putInt("First", abb);
                    editor.apply();
                }
                startActivity(intent);
            }
        });
        userName = view.findViewById(R.id.Full_Name);
        Gender = view.findViewById(R.id.male);
        Country = view.findViewById(R.id.location);
        ph_no = view.findViewById(R.id.Phone_no);
        bio=view.findViewById(R.id.bio);



        mRef.child(currentUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("gender")){
                    String ggender = dataSnapshot.child("gender").getValue().toString();
                    Gender.setText(ggender);
                }

                if(dataSnapshot.hasChild("username")){
                    String user_name = dataSnapshot.child("username").getValue().toString();
                    userName.setText(user_name);
                }

                if(dataSnapshot.hasChild("country")){
                    String useremail = dataSnapshot.child("country").getValue().toString();
                    Country.setText(useremail);
                }

                if(dataSnapshot.hasChild("ph_no")){
                    String phone = dataSnapshot.child("ph_no").getValue().toString();
                    ph_no.setText(phone);
                }
                if(dataSnapshot.hasChild("Bio")){
                    String Bio = dataSnapshot.child("Bio").getValue().toString();
                    bio.setText(Bio);
                }

                if(dataSnapshot.hasChild("Profile")){

                    String ProfileImage = dataSnapshot.child("Profile").getValue().toString();
                    Picasso.get().load(ProfileImage).into(profile_image);
                }}

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        return view;
    }


    private void sendusertoEditActivity() {

        Intent intent = new Intent(getActivity(), edit_profile.class);
        startActivity(intent);
  }
        }

