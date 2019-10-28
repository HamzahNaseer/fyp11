package com.example.tourismof;

import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tourismof.Fragments.APIService;
import com.example.tourismof.Notifications.Client;
import com.example.tourismof.Notifications.Data;
import com.example.tourismof.Notifications.MyResponse;
import com.example.tourismof.Notifications.Sender;
import com.example.tourismof.Notifications.Token;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Chat extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LinearLayout linearLayout;
    private ImageButton chat_profile_sender, send_message_button;
    private TextView text_message;
    private String message_senderID, message_receiverID, getMessage_receiverName, postKey, saveCurrentTime, saveCurrentDate,send_message_to;
    private FirebaseAuth mAuth;
    private DatabaseReference rootRef,ContactRef;

    private final List<Messages> messagesList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private MessagesAdapter messagesAdapter;


    APIService apiService;
    boolean notify=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_layout);

        ContactRef = FirebaseDatabase.getInstance().getReference().child("Contacts");

  apiService= Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        chat_profile_sender = findViewById(R.id.sender_image_button);
        send_message_button = findViewById(R.id.send_message_button);
        text_message = findViewById(R.id.chat_text_message);
        recyclerView = findViewById(R.id.recycler_view_chat);
        mAuth = FirebaseAuth.getInstance();
        message_receiverID = getIntent().getExtras().get("ownerID").toString();

        message_senderID = mAuth.getCurrentUser().getUid();
        rootRef = FirebaseDatabase.getInstance().getReference();

        messagesAdapter = new MessagesAdapter(messagesList);
        recyclerView = findViewById(R.id.recycler_view_chat);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(messagesAdapter);

        send_message_button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                notify=true;
                sendMessage();
            }
        });

        fetchMessages();


    }

    private void fetchMessages() {

        rootRef.child("Messages").child(message_senderID).child(message_receiverID)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        if (dataSnapshot.exists()) {
                            Messages messages = dataSnapshot.getValue(Messages.class);
                            messagesList.add(messages);
                            messagesAdapter.notifyDataSetChanged();
                        }

                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void sendMessage() {
        String message = text_message.getText().toString();

        if (message.isEmpty()) {
            Toast.makeText(Chat.this, "Please write a message", Toast.LENGTH_SHORT).show();
        } else {



            String message_sender_ref = "Messages/" + message_senderID + "/" + message_receiverID;
            String message_Receiver_ref = "Messages/" + message_receiverID + "/" + message_senderID;
            Calendar calFordDate = Calendar.getInstance();
            SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
            saveCurrentDate = currentDate.format(calFordDate.getTime());

            Calendar calFordTime = Calendar.getInstance();
            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
            saveCurrentTime = currentTime.format(calFordDate.getTime());

            DatabaseReference user_message_key = rootRef.child("Messages").child(message_senderID)
                    .child(message_receiverID).push();

            String message_push_id = user_message_key.getKey();

            ContactRef.child(message_senderID).child(message_receiverID).child("date").setValue(saveCurrentDate);
            ContactRef.child(message_receiverID).child(message_senderID).child("date").setValue(saveCurrentDate);


            Map messageTextBody = new HashMap();
            messageTextBody.put("message", message);
            messageTextBody.put("time", saveCurrentTime);
            messageTextBody.put("date", saveCurrentDate);
            messageTextBody.put("type", "text");
            messageTextBody.put("from", message_senderID);

            Map messageBodyDetails = new HashMap();

            messageBodyDetails.put(message_Receiver_ref + "/" + message_push_id, messageTextBody);
            messageBodyDetails.put(message_sender_ref + "/" + message_push_id, messageTextBody);

            rootRef.updateChildren(messageBodyDetails).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {

                        Toast.makeText(Chat.this, "Message Sent!", Toast.LENGTH_SHORT).show();
                        text_message.setText("");
                    } else {
                        String message = task.getException().getMessage();
                        Toast.makeText(Chat.this, "error " + message, Toast.LENGTH_SHORT).show();
                        text_message.setText("");
                    }

                }
            });

//
//            CreateMessagePortal();

        }

       final  String msg=message;
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Users").child(message_senderID);
     reference.addValueEventListener(new ValueEventListener() {
         @Override
         public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

             if(notify) {
                 sendNotification(message_receiverID, dataSnapshot.child("username").getValue().toString(), msg);
             }
             notify=false;

         }

         @Override
         public void onCancelled(@NonNull DatabaseError databaseError) {

         }
     });
    }

    private void sendNotification(String message_receiverID, String username, String msg) {

        DatabaseReference tokens=FirebaseDatabase.getInstance().getReference("Tokens");
        Query query=tokens.orderByKey().equalTo(message_receiverID);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren())
                {

                    Token token= snapshot.getValue(Token.class);
                    Data data=new Data(message_senderID, R.mipmap.ic_launcher, username+": "+msg,"New Message",
                            message_receiverID);


                    Sender sender=new Sender(data,token.getToken());
                    apiService.sendNotification(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if(response.code() ==200)
                                    {
                                        if(response.body().success !=1)
                                        {
                                            Toast.makeText(Chat.this, "Failed", Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {

                                }
                            });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
