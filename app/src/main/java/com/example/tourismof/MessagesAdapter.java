package com.example.tourismof;

import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.messageViewHolder> {


    private List<Messages> userMessages_list;
    private FirebaseAuth mAuth;
    private DatabaseReference userDatabaseRef;




    public MessagesAdapter(List<Messages> userMessages_list)
    {
        this.userMessages_list = userMessages_list;
    }

    public class messageViewHolder extends RecyclerView.ViewHolder {



        public TextView sender_message_text, receiver_message_text;
        public CircleImageView receiver_image;

        public messageViewHolder(@NonNull View itemView) {
            super(itemView);

            sender_message_text = itemView.findViewById(R.id.sender_message_text);
            receiver_message_text = itemView.findViewById(R.id.receiver_message_text);
            receiver_image = itemView.findViewById(R.id.sender_circle_image);
        }
    }

    @NonNull
    @Override
    public messageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_layout_user, parent, false);

        mAuth = FirebaseAuth.getInstance();
        return new messageViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull final messageViewHolder holder, int position) {

        String message_senderID = mAuth.getCurrentUser().getUid();
        Messages messages = userMessages_list.get(position);

        String fromUserID = messages.getFrom();
        String typeMessage = messages.getType();

        userDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users").child(fromUserID);
        userDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String image = dataSnapshot.child("Profile").getValue().toString();
                    Picasso.get().load(image).into(holder.receiver_image);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    if(typeMessage.equals("text")){
        holder.receiver_message_text.setVisibility(View.INVISIBLE);
        holder.receiver_image.setVisibility(View.INVISIBLE);

        if(fromUserID.equals(message_senderID)){
            holder.sender_message_text.setBackgroundResource(R.drawable.sender_message_text_background);
            holder.sender_message_text.setTextColor(Color.WHITE);
            holder.sender_message_text.setGravity(Gravity.LEFT);
            holder.sender_message_text.setText(messages.getMessage());
        }
        else
        {
            holder.sender_message_text.setVisibility(View.INVISIBLE);

            holder.receiver_message_text.setVisibility(View.VISIBLE);
            holder.receiver_image.setVisibility(View.VISIBLE);

            holder.receiver_message_text.setBackgroundResource(R.drawable.receiver_message_text_backgrount);
            holder.receiver_message_text.setTextColor(Color.BLACK);
            holder.receiver_message_text.setGravity(Gravity.LEFT);
            holder.receiver_message_text.setText(messages.getMessage());
        }
    }

    }

    @Override
    public int getItemCount() {
        return userMessages_list.size();
    }
}
