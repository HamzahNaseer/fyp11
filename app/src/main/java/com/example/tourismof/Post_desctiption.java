package com.example.tourismof;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class Post_desctiption extends AppCompatActivity {

    private Button Next;
    private EditText description;
    private String title, price;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_desctiption);

        Next = findViewById(R.id.description_button);
        description = findViewById(R.id.post_description_1);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras != null) {
            title = extras.getString("title");
            price = extras.getString("price");
        }


        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sendUserToLocationActivity();
            }
        });




    }

    private void sendUserToLocationActivity() {
        Intent newintent = new Intent(Post_desctiption.this, post_location.class);
        newintent.putExtra("title", title);
        newintent.putExtra("price", price);
        newintent.putExtra("description", description.getText().toString());
        startActivity(newintent);
    }
}
