package com.example.tourismof.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.tourismof.FirstActivity;
import com.example.tourismof.PostActivity;
import com.example.tourismof.Post_title;
import com.example.tourismof.R;
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

import java.nio.channels.InterruptedByTimeoutException;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;


public class Fragment_post extends Fragment {
    private Toolbar mToolbar;
    private ProgressDialog loadingBar;

    private ImageView SelectPostImage;
    private Button UpdatePostButton;
    private EditText PostDescription;

    private static final int Gallery_Pick = 1;
    private Uri ImageUri;
    private String Description;

    private StorageReference PostsImagesRefrence;
    private DatabaseReference UsersRef, PostsRef;
    private FirebaseAuth mAuth;
    Button btn;
    private String saveCurrentDate, saveCurrentTime, postRandomName, downloadUrl, current_user_id;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_post, container, false);
        btn=view.findViewById(R.id.btn_post);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), Post_title.class);
                startActivity(intent);
            }
        });
//        mAuth = FirebaseAuth.getInstance();
//        current_user_id = mAuth.getCurrentUser().getUid();
//
//        PostsImagesRefrence = FirebaseStorage.getInstance().getReference();
//        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
//        PostsRef = FirebaseDatabase.getInstance().getReference().child("Posts");
//
//
//        SelectPostImage = view.findViewById(R.id.post_image);
//        UpdatePostButton = view.findViewById(R.id.Add_post_button);
//        PostDescription = view.findViewById(R.id.post_description);
//        loadingBar = new ProgressDialog(getContext());
//
//
//        SelectPostImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                OpenGallery();
//            }
//        });
//
//
//        UpdatePostButton.setOnClickListener(new View.OnClickListener() {
//            @RequiresApi(api = Build.VERSION_CODES.N)
//            @Override
//            public void onClick(View v) {
//                ValidatePostInfo();
//            }
//        });

        return view;

    }
//    @RequiresApi(api = Build.VERSION_CODES.N)
//    private void ValidatePostInfo()
//    {
//        Description = PostDescription.getText().toString();
//
//        if(ImageUri == null)
//        {
//            Toast.makeText(getContext(), "Please select post image...", Toast.LENGTH_SHORT).show();
//        }
//        else if(TextUtils.isEmpty(Description))
//        {
//            Toast.makeText(getContext(), "Please say something about your image...", Toast.LENGTH_SHORT).show();
//        }
//        else
//        {
//            loadingBar.setTitle("Add New Fragment_post");
//            loadingBar.setMessage("Please wait, while we are updating your new post...");
//            loadingBar.show();
//            loadingBar.setCanceledOnTouchOutside(true);
//
//            StoringImageToFirebaseStorage();
//        }
//    }
//    @RequiresApi(api = Build.VERSION_CODES.N)
//    private void StoringImageToFirebaseStorage()
//    {
//        Calendar calFordDate = Calendar.getInstance();
//        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
//        saveCurrentDate = currentDate.format(calFordDate.getTime());
//
//        Calendar calFordTime = Calendar.getInstance();
//        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
//        saveCurrentTime = currentTime.format(calFordDate.getTime());
//
//        postRandomName = saveCurrentDate + saveCurrentTime;
//
//
//        final StorageReference filePath = PostsImagesRefrence.child("Fragment_post Images").child(ImageUri.getLastPathSegment() +"image "+ postRandomName + ".jpg");
//        filePath.putFile(ImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                    @Override
//                    public void onSuccess(Uri uri) {
//
//                        PostsRef.child(current_user_id + postRandomName).child("postimage").setValue(String.valueOf(uri)).addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void aVoid) {
//                                Toast.makeText(getContext(), "Fragment_post Added Successfully", Toast.LENGTH_SHORT).show();
//                                SavingPostInformationToDatabase();
//
//                            }
//                        });
//
//                    }
//                });
//            }
//        });
//
//    }
//
//    private void SavingPostInformationToDatabase()
//    {
//        UsersRef.child(current_user_id).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot)
//            {
//                if(dataSnapshot.exists())
//                {
//                    String userFullName = dataSnapshot.child("fullname").getValue().toString();
//                    String userProfileImage = dataSnapshot.child("Profile").getValue().toString();
//                    Toast.makeText(getContext(), "New Fragment_post is updated successfully.", Toast.LENGTH_SHORT).show();
//
//                    HashMap postsMap = new HashMap();
//                    postsMap.put("uid", current_user_id);
//                    postsMap.put("date", saveCurrentDate);
//                    postsMap.put("time", saveCurrentTime);
//                    postsMap.put("description", Description);
//                    postsMap.put("profileimage", userProfileImage);
//                    postsMap.put("fullname", userFullName);
//                    PostsRef.child(current_user_id + postRandomName).
//                            updateChildren(postsMap)
//                            .addOnCompleteListener(new OnCompleteListener() {
//                                @Override
//                                public void onComplete(@NonNull Task task)
//                                {
//                                    if(task.isSuccessful())
//                                    {
//                                        SendUserToMainActivity();
//                                        Toast.makeText(getContext(), "New Fragment_post is updated successfully.", Toast.LENGTH_SHORT).show();
//                                        loadingBar.dismiss();
//                                    }
//                                    else
//                                    {
//                                        Toast.makeText(getContext(), "Error Occured while updating your post.", Toast.LENGTH_SHORT).show();
//                                        loadingBar.dismiss();
//                                    }
//                                }
//                            });
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//    }
//    private void OpenGallery() {
//        Intent galleryIntent = new Intent();
//        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
//        galleryIntent.setType("image/*");
//        startActivityForResult(galleryIntent, Gallery_Pick);
//    }
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data)
//    {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if(requestCode==Gallery_Pick && resultCode==RESULT_OK && data!=null)
//        {
//            ImageUri = data.getData();
//            SelectPostImage.setImageURI(ImageUri);
//        }
//    }
//    private void SendUserToMainActivity()
//    {
//        Intent mainIntent = new Intent(getContext(), FirstActivity.class);
//        startActivity(mainIntent);
//    }

}
