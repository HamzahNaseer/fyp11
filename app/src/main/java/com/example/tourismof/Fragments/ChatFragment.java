package com.example.tourismof.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.tourismof.Chat;
import com.example.tourismof.Chat_Contacts;
import com.example.tourismof.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class ChatFragment extends Fragment {

    private RecyclerView listOfContacts;
    private DatabaseReference ContactsRef, UsersRef;
    private FirebaseAuth mAuth;


    public ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View ContactView  = inflater.inflate(R.layout.fragment_chat, container, false);
        mAuth =FirebaseAuth.getInstance();
        listOfContacts = ContactView.findViewById(R.id.cont_list);
        listOfContacts.setLayoutManager(new LinearLayoutManager(getContext()));
        ContactsRef = FirebaseDatabase.getInstance().getReference().child("Contacts").child(mAuth.getCurrentUser().getUid());
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        return ContactView;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions options =
                new FirebaseRecyclerOptions.Builder<Chat_Contacts>()
                .setQuery(ContactsRef, Chat_Contacts.class)
                .build();

        FirebaseRecyclerAdapter<Chat_Contacts, ContactsViewHolder> adapter =
                new FirebaseRecyclerAdapter<Chat_Contacts, ContactsViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final ContactsViewHolder holder, int position, @NonNull Chat_Contacts model) {

                        final String usersIDs = getRef(position).getKey();

                        UsersRef.child(usersIDs).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.hasChild("Profile")) {

                                    String Name = dataSnapshot.child("fullname").getValue().toString();
                                    String Image = dataSnapshot.child("Profile").getValue().toString();

                                    holder.userName.setText(Name);
                                    Picasso.get().load(Image).into(holder.imageView);
                                    holder.user_status.setText("Status");

                                    holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent intent = new Intent(getActivity(), Chat.class);
                                            intent.putExtra("ownerID", usersIDs);
                                            getActivity().startActivity(intent);
                                        }
                                    });

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }

                    @NonNull
                    @Override
                    public ContactsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                            View view = LayoutInflater.from(parent.getContext())
                                    .inflate(R.layout.all_cotacts_layout,parent,false);
                            ContactsViewHolder viewHolder = new ContactsViewHolder(view);
                            return viewHolder;
                    }
                };

            listOfContacts.setAdapter(adapter);
            adapter.startListening();



    }



    public static class ContactsViewHolder extends RecyclerView.ViewHolder {

        TextView userName, user_status;
        ImageView imageView;
        LinearLayout linearLayout;


        public ContactsViewHolder(@NonNull View itemView) {
            super(itemView);


            userName = itemView.findViewById(R.id.usercontact_name);
            user_status = itemView.findViewById(R.id.status);
            imageView = itemView.findViewById(R.id.User_contact_image);
            linearLayout = itemView.findViewById(R.id.contact_linear_layout);
        }
    }
}
