package com.example.tourismof;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tourismof.Fragments.ProfileFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthSettings;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;


public class edit_profile extends AppCompatActivity {
    private EditText UserName, FullName, CountryName, Bio, Phone_no;
    private Button SaveInformationbuttion;
    private CircleImageView ProfileImage, ProfileImagePick;
    private ProgressDialog loadingBar;
    private Spinner Country_spinner,Gender;

    private FirebaseAuth mAuth;
    private DatabaseReference UsersRef;
    private StorageReference UserProfileImageRef;

    String currentUserID;
    final static int Gallery_Pick = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);


        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID);
        UserProfileImageRef = FirebaseStorage.getInstance().getReference().child("Profile Images");

        Country_spinner = findViewById(R.id.sp_country_edit_profile);
        Gender = findViewById(R.id.gender);
        UserName = (EditText) findViewById(R.id.name_edit_profile);
        FullName = (EditText) findViewById(R.id.full_name_edit_profile);
        Bio = (EditText) findViewById(R.id.bio_profile);
        Phone_no = findViewById(R.id.phone_edit_profile);
        SaveInformationbuttion = (Button) findViewById(R.id.btn_edit_profile);
        ProfileImage = (CircleImageView) findViewById(R.id.circle_profile_image);
        ProfileImagePick = findViewById(R.id.circle_image);
        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        if (bd == null)
        {get_and_set_edittext_values();}

        ProfileImagePick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UploadView(view);
            }
        });

        loadingBar = new ProgressDialog(this);


        SaveInformationbuttion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveAccountSetupInformation();
            }
        });



    }

    private void
    get_and_set_edittext_values() {
        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if (dataSnapshot.hasChild("Profile")) {
                        String img = dataSnapshot.child("Profile").getValue().toString();
                        Picasso.get().load(img).placeholder(R.drawable.profile).into(ProfileImage);
                        if(dataSnapshot.hasChild("fullname"))
                        {  String fullname=dataSnapshot.child("fullname").getValue().toString();
                         FullName.setText(fullname);}
                         String username=dataSnapshot.child("username").getValue().toString();
                         UserName.setText(username);
                        String phone=dataSnapshot.child("ph_no").getValue().toString();
                          Phone_no.setText(phone);
                        String bio=dataSnapshot.child("Bio").getValue().toString();
                        Bio.setText(bio);
                        String country=dataSnapshot.child("country").getValue().toString();
                    } else {
                        //Toast.makeText(edit_profile.this, "Please Add Profile Image", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void SaveAccountSetupInformation() {
        String username = UserName.getText().toString();
        String fullname = FullName.getText().toString();
        String About_user_bio = Bio.getText().toString();
        String Phone_number = Phone_no.getText().toString();
        String country = Country_spinner.getSelectedItem().toString();
        String Gender_item= Gender.getSelectedItem().toString();

        if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, "Please write your username...", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(fullname)) {
            Toast.makeText(this, "Please write your full name...", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(Phone_number)) {
            Toast.makeText(this, "Please write your Phone Number...", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(About_user_bio)) {
            Toast.makeText(this, "Please write something about yourself...", Toast.LENGTH_SHORT).show();
        } else {
            loadingBar.setTitle("Saving Information");
            loadingBar.setMessage("Please wait, while we are creating your new Account...");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);



            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            FirebaseAuthSettings firebaseAuthSettings = firebaseAuth.getFirebaseAuthSettings();

// Configure faking the auto-retrieval with the whitelisted numbers.
            firebaseAuthSettings.setAutoRetrievedSmsCodeForPhoneNumber(Phone_number, "123456");
            HashMap userMap = new HashMap();
            userMap.put("username", username);
            userMap.put("fullname", fullname);
            userMap.put("country", country);
            userMap.put("gender", Gender_item);
            userMap.put("Bio", About_user_bio);
            userMap.put("email", mAuth.getCurrentUser().getEmail());
            userMap.put("ph_no", Phone_number);
            UsersRef.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                        SendUserToMainActivity();
                       // Toast.makeText(edit_profile.this, "your Account is created Successfully.", Toast.LENGTH_LONG).show();
                        loadingBar.dismiss();
                    } else {
                        String message = task.getException().getMessage();
                        //Toast.makeText(edit_profile.this, "Error Occured: " + message, Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                }
            });
        }
    }


    private void SendUserToMainActivity() {
        Intent mainIntent = new Intent(edit_profile.this, FirstActivity.class);
        mainIntent.putExtra("hostt","profile_fragment");
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }

    public void UploadView(View view) {

        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, Gallery_Pick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Gallery_Pick) {
            if (resultCode == RESULT_OK) {
                Uri imageData = data.getData();
                 ProfileImage.setImageURI(imageData);
                final StorageReference ImageName = UserProfileImageRef.child(currentUserID + ".jpg");
                ImageName.putFile(imageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        ImageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {


                                UsersRef.child("Profile").setValue(String.valueOf(uri)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                       // Toast.makeText(edit_profile.this, "Error Resolved ", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });

                       // Toast.makeText(edit_profile.this, "Error Occured: ", Toast.LENGTH_SHORT).show();

                    }
                });

            }
        }
    }


}

