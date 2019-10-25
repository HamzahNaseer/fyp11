package com.example.tourismof;

import android.app.ProgressDialog;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

public class edit_mypost extends AppCompatActivity {

    private ProgressDialog loadingBar;
    private DatabaseReference postRef, UsersRef;
    private FirebaseAuth mAuth;

    private EditText text1, text2, text3,text4,text5;
    private ImageView imageView;
    private String image, price, description, location,title;
    private String postKey;
    private Button update_button;
    private String saveCurrentDate, saveCurrentTime, postRandomName,current_user_id;
    private StorageReference PostsImagesRefrence;

    private Uri ImageUri;

    private static final int Gallery_pick = 1;

    ImageButton SelectPostImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_mypost);
        text1 = findViewById(R.id.location_post1);
        text2 = findViewById(R.id.price_post1);
        text3 = findViewById(R.id.description_post1);
        text4 = findViewById(R.id.title_post1);
        imageView = findViewById(R.id.image_post1);
        update_button=findViewById(R.id.btn_edit_post);
        SelectPostImage=findViewById(R.id.circle_image);
        loadingBar = new ProgressDialog(this);

        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        postKey = getIntent().getExtras().get("postKey").toString();
        PostsImagesRefrence = FirebaseStorage.getInstance().getReference();
        postRef = FirebaseDatabase.getInstance().getReference().child("Posts").child(postKey);
        mAuth = FirebaseAuth.getInstance();
        current_user_id = mAuth.getCurrentUser().getUid();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");

           get_and_set_edittext_values();

        SelectPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        });


    update_button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            loadingBar.setTitle("Saving Changes");
            loadingBar.setMessage("Please wait, while we are updating your Post...");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);


          location=  text1.getText().toString();
            price=text2.getText().toString();
            description=text3.getText().toString();
            title=text4.getText().toString();
            ValidatePostInfo();

        }
    });

    }

    private void ValidatePostInfo() {

//       if(description==null)
//        {
//              text3.setError("Fill this");        }
//              else if(TextUtils.isEmpty(price))
//        {
//                 text2.setError("Fill this");
//        }
//        else if(TextUtils.isEmpty(location))
//        {
//            text1.setError("Fill this");
//        }
//           else if(TextUtils.isEmpty(location))
//           {
//           text4.setError("Fill this");
//           }
//        else {
//      loadingBar.setTitle("Update Post");
//      loadingBar.setMessage("Please wait, while we are updating your new post...");
//      loadingBar.show();
//      loadingBar.setCanceledOnTouchOutside(true);
        SavingPostInformationToDatabase();


        //}
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void StoringImageToFirebaseStorage()
    {
        Calendar calFordDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
         saveCurrentDate = currentDate.format(calFordDate.getTime());

        Calendar calFordTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
        saveCurrentTime = currentTime.format(calFordDate.getTime());

        postRandomName = saveCurrentDate + saveCurrentTime;


        final StorageReference filePath = PostsImagesRefrence.child("Post Images").child(ImageUri.getLastPathSegment()+ "image "+ postRandomName + ".jpg");
        filePath.putFile(ImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        postRef.child("postimage").setValue(String.valueOf(uri)).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(edit_mypost.this, "Post updated Successfully", Toast.LENGTH_SHORT).show();
                              // SavingPostInformationToDatabase();

                            }
                        });



                    }
                });
            }
        });

    }

    private void SavingPostInformationToDatabase() {
        UsersRef.child(current_user_id).addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    Calendar calFordDate = Calendar.getInstance();
                    SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
                    saveCurrentDate = currentDate.format(calFordDate.getTime());

                    Calendar calFordTime = Calendar.getInstance();
                    SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
                    saveCurrentTime = currentTime.format(calFordDate.getTime());

                    postRandomName = saveCurrentDate + saveCurrentTime;
                    String userFullName = dataSnapshot.child("fullname").getValue().toString();
                    String userProfileImage = dataSnapshot.child("Profile").getValue().toString();
                    Toast.makeText(edit_mypost.this, "New Post is updated successfully.", Toast.LENGTH_SHORT).show();

                    HashMap postsMap = new HashMap();
                    postsMap.put("uid", current_user_id);
                    postsMap.put("date", saveCurrentDate);
                    postsMap.put("time", saveCurrentTime);
                    postsMap.put("description", description);
                    postsMap.put("profileimage", userProfileImage);
                    postsMap.put("fullname", userFullName);
                    postsMap.put("price", price);
                    postsMap.put("title", title);
                    postsMap.put("location", location);
                    postRef.updateChildren(postsMap)
                            .addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task)
                                {
                                    if(task.isSuccessful())
                                    {
                                        SendUserToMainActivity();
                                       // Toast.makeText(edit_mypost.this, "New Post is updated successfully.", Toast.LENGTH_SHORT).show();
                                       // loadingBar.dismiss();
                                    }
                                    else
                                    {
                                        Toast.makeText(edit_mypost.this, "Error Occured while updating your post.", Toast.LENGTH_SHORT).show();
                                       // loadingBar.dismiss();
                                    }
                                }
                            });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void SendUserToMainActivity()  {
        Intent mainIntent = new Intent(edit_mypost.this, FirstActivity.class);
        mainIntent.putExtra("hostt","my_post");
        startActivity(mainIntent);
    }

    private void OpenGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, Gallery_pick);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==Gallery_pick && resultCode==RESULT_OK && data!=null)
        {
            ImageUri = data.getData();
            imageView.setImageURI(ImageUri);
            StoringImageToFirebaseStorage();

        }
    }
    private void get_and_set_edittext_values() {

        postRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                if(dataSnapshot1.exists()){
                    description = dataSnapshot1.child("description").getValue().toString();
                    price = dataSnapshot1.child("price").getValue().toString();
                    location = dataSnapshot1.child("location").getValue().toString();
                    image = dataSnapshot1.child("postimage").getValue().toString();
                    title = dataSnapshot1.child("title").getValue().toString();
                 //   Toast.makeText(edit_mypost.this, "title is = "+ title, Toast.LENGTH_SHORT).show();
                    text1.setText(location);
                    text2.setText(price);
                    text3.setText(description);
                    text4.setText(title);
                    Picasso.get().load(image).into(imageView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
