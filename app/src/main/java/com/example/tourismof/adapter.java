package com.example.tourismof;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.tourismof.Fragments.HomeFragment;
import com.example.tourismof.Fragments.Posts;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class adapter extends  RecyclerView.Adapter<adapter.PostsVIewHolder> {
 ArrayList<Posts> list;
    private boolean likeCheck;
  public adapter(ArrayList<Posts> list)
  {

      this.list=list;
  }

    @NonNull
    @Override
    public PostsVIewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.all_posts_layout, parent, false);

        return new PostsVIewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostsVIewHolder holder, int position) {

        holder.PostDescription.setText(list.get(position).getDescription());
        holder.Post_location.setText(list.get(position).location);
        holder.Post_price.setText(list.get(position).getprice());
        Picasso.get().load(list.get(position).getPostimage()).into(holder.PostImage);





    }

    @Override
    public int getItemCount() {
        return list.size();
    }

     class PostsVIewHolder extends RecyclerView.ViewHolder {
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
