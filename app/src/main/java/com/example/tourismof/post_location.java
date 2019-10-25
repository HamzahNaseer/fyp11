package com.example.tourismof;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class post_location extends AppCompatActivity {

    private Button btn;
    private EditText location;
    private String title,price,description;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_location);

        btn = findViewById(R.id.Location_button);
        location = findViewById(R.id.Location_of_post);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras != null) {
            title = extras.getString("title");
            price = extras.getString("price");
            description = extras.getString("description");

        }


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newintent = new Intent(post_location.this, PostActivity.class);
                newintent.putExtra("title", title);
                newintent.putExtra("price", price);
                newintent.putExtra("description", description);
                newintent.putExtra("location", location.getText().toString());
                startActivity(newintent);
            }
        });



    }
}
