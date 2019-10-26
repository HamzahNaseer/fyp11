package com.example.tourismof.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tourismof.ClickPost;
import com.example.tourismof.R;
import com.example.tourismof.edit_mypost;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class Fragment_mypost extends Fragment {
    private FirebaseRecyclerAdapter<Posts, PostsVIewHolder> adapter;
    private String Userrrrr;
    private RecyclerView postList;
    private FirebaseAuth mAuth;
    private DatabaseReference UserRef, PostRef, LikesRef;
    private Context ctx;
    private boolean likeCheck;
    private ProgressDialog loadingBar;

    public Fragment_mypost() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment_mypost, container, false);


        mAuth = FirebaseAuth.getInstance();
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        PostRef = FirebaseDatabase.getInstance().getReference().child("Posts");
        LikesRef = FirebaseDatabase.getInstance().getReference().child("Likes");

        postList = (RecyclerView) view.findViewById(R.id.recycler_view);
        postList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        postList.setLayoutManager(linearLayoutManager);

//        DisplayAllUsersPosts();


        return view;


    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Posts> options = new FirebaseRecyclerOptions.Builder<Posts>()
                .setQuery(PostRef.orderByChild("uid").startAt(mAuth.getCurrentUser().getUid()).endAt(mAuth.getCurrentUser().getUid()), Posts.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Posts, PostsVIewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final PostsVIewHolder holder, final int position, @NonNull Posts model) {
                final String Userids = getRef(position).getKey();
                PostRef.child(Userids).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(dataSnapshot.exists()){

                            final String profileimage = dataSnapshot.child("profileimage").getValue().toString();
                            final String location = dataSnapshot.child("location").getValue().toString();
                            final String descriptionnn = dataSnapshot.child("description").getValue().toString();
                            //String fullnameee=dataSnapshot.child("fullname").getValue().toString();
                            final String postimageee = dataSnapshot.child("postimage").getValue().toString();
                            //String time=dataSnapshot.child("time").getValue().toString();
                            final String uid = dataSnapshot.child("uid").getValue().toString();
                            final String price = dataSnapshot.child("price").getValue().toString();

                            holder.PostDescription.setText(descriptionnn);
                            holder.Post_location.setText(location);
                            holder.Post_price.setText(price);

                          //  holder.setLikeButtonStatus(getRef(position).getKey());



//                            holder.imageButton.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//                                    likeCheck = true;
//                                    LikesRef.addValueEventListener(new ValueEventListener() {
//                                        @Override
//                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                            if (likeCheck) {
//                                                if (dataSnapshot.child(getRef(position).getKey()).hasChild(mAuth.getCurrentUser().getUid())) {
//                                                    notifyDataSetChanged();
//                                                    LikesRef.child(getRef(position).getKey()).child(mAuth.getCurrentUser().getUid()).setValue(null);
//                                                    //notifyDataSetChanged();
//                                                    likeCheck = false;
//                                                } else {
//
//                                                    LikesRef.child(getRef(position).getKey()).child(mAuth.getCurrentUser().getUid()).setValue("Liked");
////                                                        notifyDataSetChanged();
////                                                        LikesRef.child(mAuth.getCurrentUser().getUid()).child(getRef(position).getKey()).child("description").setValue(descriptionnn);
////                                                        LikesRef.child(mAuth.getCurrentUser().getUid()).child(getRef(position).getKey()).child("profileimage").setValue(profileimage);
////                                                        LikesRef.child(mAuth.getCurrentUser().getUid()).child(getRef(position).getKey()).child("price").setValue(price);
////                                                        LikesRef.child(mAuth.getCurrentUser().getUid()).child(getRef(position).getKey()).child("uid").setValue(uid);
////                                                        LikesRef.child(mAuth.getCurrentUser().getUid()).child(getRef(position).getKey()).child("postimage").setValue(postimageee);
////                                                        LikesRef.child(mAuth.getCurrentUser().getUid()).child(getRef(position).getKey()).child("location").setValue(location);
//                                                    // notifyDataSetChanged();
//                                                    likeCheck = false;
//                                                }
//                                            }
//                                        }
//
//                                        @Override
//                                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                        }
//                                    });
//                                }
//                            });
//                                holder.imageButton.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View view) {
//                                        likeCheck = true;
//                                        LikesRef.addValueEventListener(new ValueEventListener() {
//                                            @Override
//                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                                if (likeCheck) {
//                                                    if (dataSnapshot.child(mAuth.getCurrentUser().getUid()).hasChild(getRef(position).getKey())) {
//                                                        notifyDataSetChanged();
//                                                        LikesRef.child(mAuth.getCurrentUser().getUid()).child(getRef(position).getKey()).setValue(null);
//                                                        //notifyDataSetChanged();
//                                                        likeCheck = false;
//                                                    } else {
//                                                        notifyDataSetChanged();
//                                                        LikesRef.child(mAuth.getCurrentUser().getUid()).child(getRef(position).getKey()).child("description").setValue(descriptionnn);
//                                                        LikesRef.child(mAuth.getCurrentUser().getUid()).child(getRef(position).getKey()).child("profileimage").setValue(profileimage);
//                                                        LikesRef.child(mAuth.getCurrentUser().getUid()).child(getRef(position).getKey()).child("price").setValue(price);
//                                                        LikesRef.child(mAuth.getCurrentUser().getUid()).child(getRef(position).getKey()).child("uid").setValue(uid);
//                                                        LikesRef.child(mAuth.getCurrentUser().getUid()).child(getRef(position).getKey()).child("postimage").setValue(postimageee);
//                                                        LikesRef.child(mAuth.getCurrentUser().getUid()).child(getRef(position).getKey()).child("location").setValue(location);
//                                                        // notifyDataSetChanged();
//                                                        likeCheck = false;
//                                                    }
//                                                }
//                                            }
//
//                                            @Override
//                                            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                            }
//                                        });
//                                    }
//                                });


                            // holder.username.setText(fullnameee);
                            //Picasso.get().load(profileimage).placeholder(R.drawable.profile).into(holder.profileimage);
                            Picasso.get().load(postimageee).placeholder(R.drawable.profile).into(holder.PostImage);
                            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(getActivity(), ClickPost.class);
                                 //   Toast.makeText(getContext(), "Please Enter Email", Toast.LENGTH_SHORT).show();

                                    intent.putExtra("postKey", Userids);
                                    intent.putExtra("ownerID",dataSnapshot.child("uid").getValue().toString());
                                    getActivity().startActivity(intent);
                                }
                            });
                            holder.verticle_dots.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //Creating the instance of PopupMenu
                                    PopupMenu popup = new PopupMenu(getContext(), v);
                                    //Inflating the Popup using xml file
                                    popup.getMenuInflater()
                                            .inflate(R.menu.popup, popup.getMenu());

                                    //registering popup with OnMenuItemClickListener
                                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                        public boolean onMenuItemClick(MenuItem item) {
                                            String selected = item.getTitle().toString();
                                            if (selected.matches("Edit"))
                                            {
                                                Intent intent = new Intent(getActivity(), edit_mypost.class);
                                                intent.putExtra("postKey", Userids);
                                                getActivity().startActivity(intent);
                                            }
                                            if(selected.matches("Delete"))
                                            {
                                                PostRef.child(Userids).removeValue();
                                            }


                                            return true;
                                        }
                                    });

                                    popup.show(); //showing popup main_menu
                                }
                            });


//                             holder.setDate(model.getDate());
//                             holder.setTime(model.getTime());
//                             holder.setFullname(model.getFullname());
//                             holder.setDescription(model.getDescription());
//                             holder.setProfileimage(getActivity().getApplicationContext(), model.getProfileimage());
//                             holder.setPostimage(getActivity().getApplicationContext(), model.getPostimage());

//                         else{
//                             String dateee=dataSnapshot.child("date").getValue().toString();
//                             String descriptionnn=dataSnapshot.child("description").getValue().toString();
//                             String fullnameee=dataSnapshot.child("fullname").getValue().toString();
//                             String time=dataSnapshot.child("time").getValue().toString();
//                             String uid=dataSnapshot.child("uid").getValue().toString();
//                             holder.PostDescription.setText(descriptionnn);
//                             holder.PostDate.setText(dateee);
//                             holder.username.setText(fullnameee);
//                         }


                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @NonNull
            @Override
            public PostsVIewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.my_posts_layout, parent, false);

                PostsVIewHolder viewHolder = new PostsVIewHolder(view);
                return viewHolder;
            }
        };

        postList.setAdapter(adapter);
        adapter.startListening();
    }

    public static class PostsVIewHolder extends RecyclerView.ViewHolder {
        TextView PostDescription, username, PostTime, PostDate, Post_location, Post_price;
        CircleImageView profileimage;
        ImageView PostImage;
        LinearLayout linearLayout;
        ImageButton imageButton;
        String currentUID;
        DatabaseReference LikesRefrence;
        FirebaseAuth mAuth;
        int LikesCount;
        ImageButton verticle_dots;

        public PostsVIewHolder(@NonNull View itemView) {
            super(itemView);
//            username= itemView.findViewById(R.id.post_username);
//            profileimage = (CircleImageView) itemView.findViewById(R.id.post_profile_image);
//            PostTime = (TextView) itemView.findViewById(R.id.post_Time);
//            PostDate = (TextView) itemView.findViewById(R.id.post_Date);
            linearLayout = itemView.findViewById(R.id.Card_view_linear);
            Post_price = itemView.findViewById(R.id.post_price);
            Post_location = itemView.findViewById(R.id.post_location);
            PostDescription = (TextView) itemView.findViewById(R.id.post_description);
            PostImage = (ImageView) itemView.findViewById(R.id.post_image);
            imageButton = itemView.findViewById(R.id.image_btn);
            LikesRefrence = FirebaseDatabase.getInstance().getReference().child("Likes");
            mAuth = FirebaseAuth.getInstance();
            verticle_dots=(ImageButton)itemView.findViewById(R.id.dots);



        }

//        public void setLikeButtonStatus(final String PostKey){
//            LikesRefrence.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                    if(dataSnapshot.child(PostKey).hasChild(mAuth.getCurrentUser().getUid())){
//                        LikesCount = (int) dataSnapshot.child(PostKey).getChildrenCount();
//                        imageButton.setImageResource(R.drawable.liked_post_icon);
//                    }
//                    else {
//                        LikesCount = (int) dataSnapshot.child(PostKey).getChildrenCount();
//                        imageButton.setImageResource(R.drawable.unlike_post);
//
//                    }
//
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });
//        }

//        Model class method of getting data
//
//        public void setFullname(String fullname) {
//            username.setText(fullname);
//        }
//
//        public void setProfileimage(Context ctx, String profileimage) {
//            Picasso.get().load(profileimage).into(image);
//        }
//
//        public void setTime(String time) {
//            PostTime.setText("    " + time);
//        }
//
//        public void setDate(String date) {
//            PostDate.setText("    " + date);
//        }
//
//        public void setDescription(String description) {
//            PostDescription.setText(description);
//        }
//
//        public void setPostimage(Context ctx, String postimage) {
//            Picasso.get().load(postimage).into(PostImage);
//        }
    }

}