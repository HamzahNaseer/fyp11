package com.example.tourismof;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class Post_title extends AppCompatActivity {

    private Button Next;
    private EditText title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_title);

        title = findViewById(R.id.post_title);
        Next = findViewById(R.id.post_title_button);

        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Post_title.this, Price.class);
                intent.putExtra("title", title.getText().toString());
                startActivity(intent);
            }
        });
    }
}
