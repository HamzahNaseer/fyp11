package com.example.tourismof.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tourismof.ClickPost;
import com.example.tourismof.FirstActivity;
import com.example.tourismof.R;
import com.example.tourismof.adapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationMenu;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;


public class HomeFragment extends Fragment {
    private FirebaseRecyclerAdapter<Posts, PostsVIewHolder> adapter, madapter;
    private String Userrrrr;
    private RecyclerView postList,recyclerView;
    ArrayList<Posts> list;
    private FirebaseAuth mAuth;
    private DatabaseReference UserRef, PostRef, LLikesRef;
    ImageView saerch_kro;
    SearchView search_edit_text;
    private Context ctx;
    private boolean likeCheck;
    BottomNavigationView navigation;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);


        mAuth = FirebaseAuth.getInstance();
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        PostRef = FirebaseDatabase.getInstance().getReference().child("Posts");
        LLikesRef = FirebaseDatabase.getInstance().getReference().child("Likes");

        navigation=view.findViewById(R.id.bottom_nav_view);
      //  saerch_kro=view.findViewById(R.id.searchutton);
        search_edit_text=view.findViewById(R.id.searchView);
        postList = (RecyclerView) view.findViewById(R.id.recycler_view);
        postList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        postList.setLayoutManager(linearLayoutManager);


  search_edit_text.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
      @Override
      public boolean onQueryTextSubmit(String query) {
          if (query != null)
                {

                    search(query);
                }
                else {
                    onStart();
                }
          return true;
      }

      @Override
      public boolean onQueryTextChange(String newText) {
          if (newText != null)
          {

              search(newText);
          }
          else {
              onStart();
          }
          return true;
      }
  });

//        saerch_kro.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//                String str = search_edit_text.getText().toString().trim().toLowerCase();
//                if (str != null)
//                {
//
//                    main_menu(str);
//                }
//                else {
//                    onStart();
//                }
//            }
//        });




        return view;


    }
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getActivity().getMenuInflater();
//        inflater.inflate(R.menu.main_menu, menu);
//
//        setupSearchView(menu);
//        return true;
//    }
//
//    private void setupSearchView(Menu menu) {
//    }

    private void search(String str) {

        FirebaseRecyclerOptions<Posts> options = new FirebaseRecyclerOptions.Builder<Posts>()
                .setQuery(PostRef.orderByChild("location").startAt(str).endAt(str + "\uf8ff"), Posts.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Posts, PostsVIewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final PostsVIewHolder holder, final int position, @NonNull Posts model) {
                final String Userids = getRef(position).getKey();
                PostRef.child(Userids).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists())
                        {
                            if(dataSnapshot.hasChild("postimage")){
                                final String profileimage = dataSnapshot.child("profileimage").getValue().toString();
                                final String location = dataSnapshot.child("location").getValue().toString();
                                final String descriptionnn = dataSnapshot.child("description").getValue().toString();
                                final String postimageee = dataSnapshot.child("postimage").getValue().toString();
                                final String uid = dataSnapshot.child("uid").getValue().toString();
                                final String price = dataSnapshot.child("price").getValue().toString();

                                holder.PostDescription.setText(descriptionnn);
                                holder.Post_location.setText(location);
                                holder.Post_price.setText(price);
                                Picasso.get().load(postimageee).into(holder.PostImage);

                                holder.setLikeButtonStatus(getRef(position).getKey());

                                holder.imageButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        likeCheck = true;
                                        LLikesRef.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                // notifyDataSetChanged();
                                                if (likeCheck) {
                                                    if (dataSnapshot.child(mAuth.getCurrentUser().getUid()).hasChild(getRef(position).getKey())) {
                                                        LLikesRef.child(mAuth.getCurrentUser().getUid()).child(getRef(position).getKey()).setValue(null);
                                                        likeCheck = false;
                                                    } else if(likeCheck!=false) {

                                                        LLikesRef.child(mAuth.getCurrentUser().getUid()).child(getRef(position).getKey()).child("postimage").setValue(postimageee);
                                                        LLikesRef.child(mAuth.getCurrentUser().getUid()).child(getRef(position).getKey()).child("description").setValue(descriptionnn);
                                                        LLikesRef.child(mAuth.getCurrentUser().getUid()).child(getRef(position).getKey()).child("price").setValue(price);
                                                        LLikesRef.child(mAuth.getCurrentUser().getUid()).child(getRef(position).getKey()).child("uid").setValue(uid);
                                                        LLikesRef.child(mAuth.getCurrentUser().getUid()).child(getRef(position).getKey()).child("profileimage").setValue(profileimage);
                                                        LLikesRef.child(mAuth.getCurrentUser().getUid()).child(getRef(position).getKey()).child("location").setValue(location);
                                                        likeCheck = false;
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                    }
                                });


                                // holder.username.setText(fullnameee);
                                //Picasso.get().load(profileimage).placeholder(R.drawable.profile).into(holder.profileimage);
                                holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(getActivity(), ClickPost.class);
                                        //  Toast.makeText(getContext(), "Please Enter Email", Toast.LENGTH_SHORT).show();

                                        intent.putExtra("postKey", Userids);
                                        intent.putExtra("ownerID",dataSnapshot.child("uid").getValue().toString());
                                        getContext().startActivity(intent);
                                    }
                                });

                            }}

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
                        .inflate(R.layout.all_posts_layout, parent, false);

                PostsVIewHolder viewHolder = new PostsVIewHolder(view);
                return viewHolder;
            }
        };

        postList.setAdapter(adapter);
        adapter.startListening();
    }

//    @Override
//    public void onBackPressed() {
//        new AlertDialog.Builder(getContext())
//                .setTitle("Really Exit?")
//                .setMessage("Are you sure you want to exit?")
//                .setNegativeButton(android.R.string.no, null)
//                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//
//                    public void onClick(DialogInterface arg0, int arg1) {
//                        // LoginActivity.super.onBackPressed();
//                        finish();
//                    }
//                }).create().show();
//    }

//    private void main_menu(String str) {
//        ArrayList<Posts> mylist=new ArrayList<>();
////        for(Posts object: list)
////        {
////            if(object.getlocation().toLowerCase().contains(str))
////            {
////
////                mylist.add(object);
////            }
////
////        }
//
//
//
//      com.example.tourismof.adapter adapterm= new adapter(mylist);
//        postList.setAdapter(adapterm);
//
//    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Posts> options = new FirebaseRecyclerOptions.Builder<Posts>()
                .setQuery(PostRef, Posts.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Posts, PostsVIewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final PostsVIewHolder holder, final int position, @NonNull Posts model) {
                final String Userids = getRef(position).getKey();
                PostRef.child(Userids).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists())
                        {
                            if(dataSnapshot.hasChild("postimage")){
                                final String profileimage = dataSnapshot.child("profileimage").getValue().toString();
                                final String location = dataSnapshot.child("location").getValue().toString();
                                final String descriptionnn = dataSnapshot.child("description").getValue().toString();
                                final String postimageee = dataSnapshot.child("postimage").getValue().toString();
                                final String uid = dataSnapshot.child("uid").getValue().toString();
                                final String price = dataSnapshot.child("price").getValue().toString();

                                holder.PostDescription.setText(descriptionnn);
                                holder.Post_location.setText(location);
                                holder.Post_price.setText(price);
                                Picasso.get().load(postimageee).into(holder.PostImage);

                                holder.setLikeButtonStatus(getRef(position).getKey());

                                holder.imageButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        likeCheck = true;
                                        LLikesRef.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                // notifyDataSetChanged();
                                                if (likeCheck) {
                                                    if (dataSnapshot.child(mAuth.getCurrentUser().getUid()).hasChild(getRef(position).getKey())) {
                                                        LLikesRef.child(mAuth.getCurrentUser().getUid()).child(getRef(position).getKey()).setValue(null);
                                                        likeCheck = false;
                                                    } else if(likeCheck!=false) {

                                                        LLikesRef.child(mAuth.getCurrentUser().getUid()).child(getRef(position).getKey()).child("postimage").setValue(postimageee);
                                                        LLikesRef.child(mAuth.getCurrentUser().getUid()).child(getRef(position).getKey()).child("description").setValue(descriptionnn);
                                                        LLikesRef.child(mAuth.getCurrentUser().getUid()).child(getRef(position).getKey()).child("price").setValue(price);
                                                        LLikesRef.child(mAuth.getCurrentUser().getUid()).child(getRef(position).getKey()).child("uid").setValue(uid);
                                                        LLikesRef.child(mAuth.getCurrentUser().getUid()).child(getRef(position).getKey()).child("profileimage").setValue(profileimage);
                                                        LLikesRef.child(mAuth.getCurrentUser().getUid()).child(getRef(position).getKey()).child("location").setValue(location);
                                                        likeCheck = false;
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                    }
                                });


                                // holder.username.setText(fullnameee);
                                //Picasso.get().load(profileimage).placeholder(R.drawable.profile).into(holder.profileimage);
                                holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(getActivity(), ClickPost.class);
                                        //  Toast.makeText(getContext(), "Please Enter Email", Toast.LENGTH_SHORT).show();

                                        intent.putExtra("postKey", Userids);
                                        intent.putExtra("ownerID",dataSnapshot.child("uid").getValue().toString());
                                        getActivity().startActivity(intent);
                                    }
                                });

                            }}

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
                        .inflate(R.layout.all_posts_layout, parent, false);

                PostsVIewHolder viewHolder = new PostsVIewHolder(view);
                return viewHolder;
            }
        };

        postList.setAdapter(adapter);
        adapter.startListening();

//        if(search_edit_text !=null)
//        {
//         //   search_edit_text.on
//
//        }
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

        public PostsVIewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.Card_view_linear);
            Post_price = itemView.findViewById(R.id.post_price);
            Post_location = itemView.findViewById(R.id.post_location);
            PostDescription = (TextView) itemView.findViewById(R.id.post_description);
            PostImage = (ImageView) itemView.findViewById(R.id.post_image);
            imageButton = itemView.findViewById(R.id.image_btn);
            LikesRefrence = FirebaseDatabase.getInstance().getReference().child("Likes");
            mAuth = FirebaseAuth.getInstance();


        }

        public void setLikeButtonStatus(final String PostKey){
            LikesRefrence.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if(dataSnapshot.child(mAuth.getCurrentUser().getUid()).hasChild(PostKey)){
                        LikesCount = (int) dataSnapshot.child(mAuth.getCurrentUser().getUid()).getChildrenCount();
                        imageButton.setImageResource(R.drawable.liked_post_icon);
                    }
                    else {
                        LikesCount = (int) dataSnapshot.child(mAuth.getCurrentUser().getUid()).getChildrenCount();
                        imageButton.setImageResource(R.drawable.unlike_post);

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

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
