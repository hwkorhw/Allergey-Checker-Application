package com.example.allergyprevention;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final dbManager dbHelper = new dbManager(getApplicationContext(), "user_db", null, 1);

        Button Login_btn = (Button) findViewById(R.id.Login);
        Login_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                EditText id_edit = (EditText) findViewById(R.id.input_Id);
                EditText passwd_edit = (EditText) findViewById(R.id.input_Passwd);
                String[] select_result = new String[3];

                select_result = dbHelper.select(id_edit.getText().toString());

                if(select_result[0].equals("None")){
                    Toast.makeText(getApplicationContext(), "아이디 혹은 비밀번호가 틀립니다.", Toast.LENGTH_LONG).show();
                }
                else{
                    if(select_result[0].equals(id_edit.getText().toString()) && select_result[1].equals(passwd_edit.getText().toString())){
                        Toast.makeText(getApplicationContext(), "로그인 성공", Toast.LENGTH_LONG).show();
                        Intent Myintent = new Intent(getApplicationContext(), ContentActivity.class);
                        Myintent.putExtra("userId", select_result[0].toString());
                        Myintent.putExtra("userAllergy", select_result[2].toString());
                        startActivity(Myintent);
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "아이디 혹은 비밀번호가 틀립니다.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        Button Join_btn = (Button) findViewById(R.id.Join);
        Join_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), JoinActivity.class);
                startActivity(intent);
            }
        });
    }
}
