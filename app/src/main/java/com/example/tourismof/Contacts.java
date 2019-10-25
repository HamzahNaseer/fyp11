package com.example.tourismof;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Contacts extends AppCompatActivity {

    private String ownerID;
    private RecyclerView recyclerView;
    private DatabaseReference ContactsRef,UsersRef;
    private FirebaseAuth mAuth;
    private FirebaseRecyclerAdapter<Chat_Contacts, ContactsViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        recyclerView = findViewById(R.id.Contacts_recyclerView);
        mAuth = FirebaseAuth.getInstance();

        ContactsRef = FirebaseDatabase.getInstance().getReference().child("Contacts").child(mAuth.getCurrentUser().getUid());
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");


        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Contacts.this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);


        sendUserToContacts();


    }

    private void sendUserToContacts() {

                FirebaseRecyclerOptions<Chat_Contacts> options = new FirebaseRecyclerOptions.Builder<Chat_Contacts>()
                .setQuery(ContactsRef, Chat_Contacts.class)
                .build();

                adapter = new FirebaseRecyclerAdapter<Chat_Contacts, ContactsViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final ContactsViewHolder holder, int position, @NonNull Chat_Contacts model) {


                        final String Userids = getRef(position).getKey();

                        UsersRef.child(Userids).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){

                                    final String dsUsername = dataSnapshot.child("fullname").getValue().toString();
                                    final String dsprofileImage = dataSnapshot.child("Profile").getValue().toString();

                                    holder.userName_contact.setText(dsUsername);
                                    Picasso.get().load(dsprofileImage).into(holder.imageView);
                                    holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent intent = new Intent(Contacts.this, Chat.class);

                                            intent.putExtra("UserID_from_contacts", Userids);
                                            startActivity(intent);
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
                        return  viewHolder;
                    }
                };

                recyclerView.setAdapter(adapter);
                adapter.startListening();


    }



    public class ContactsViewHolder extends RecyclerView.ViewHolder {

        View view;

        TextView userName_contact, status;
        ImageView imageView;
        LinearLayout linearLayout;





        public ContactsViewHolder(@NonNull View itemView) {
            super(itemView);
            view= itemView;

            userName_contact = itemView.findViewById(R.id.usercontact_name);
            status = itemView.findViewById(R.id.status);
            imageView = itemView.findViewById(R.id.User_contact_image);
            linearLayout = itemView.findViewById(R.id.contact_linear_layout);

        }
    }

}
