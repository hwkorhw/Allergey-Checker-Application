package com.example.allergyprevention;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ContentActivity extends AppCompatActivity {

    private static final int MY_PERMISSION_CAMERA = 1111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        Intent Myintent = getIntent();
        Bundle bundle = Myintent.getExtras();
        String user_id = bundle.getString("userId");
        String user_allergy = bundle.getString("userAllergy");
        TextView userid_text = findViewById(R.id.userId_textview);
        Button search_btn = findViewById(R.id.search_btn);
        Button camera_btn = findViewById(R.id.camera_btn);
        Button Info_btn = findViewById(R.id.info_btn);

        userid_text.setText(user_id);

        search_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent search_intent = new Intent(getApplicationContext(), SearchActivity.class);
                search_intent.putExtra("userId", user_id);
                search_intent.putExtra("userAllergy", user_allergy);
                startActivity(search_intent);
            }
        });

        camera_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent camera_intent = new Intent(getApplicationContext(), CameraActivity.class);
                camera_intent.putExtra("userId", user_id);
                camera_intent.putExtra("userAllergy", user_allergy);
                startActivity(camera_intent);
            }
        });

        Info_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent info_intent = new Intent(getApplicationContext(), InfoActivity.class);
                info_intent.putExtra("userId", user_id);
                info_intent.putExtra("userAllergy", user_allergy);
                startActivity(info_intent);
            }
        });
    }
}
