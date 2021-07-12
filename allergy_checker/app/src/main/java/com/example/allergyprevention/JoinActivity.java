package com.example.allergyprevention;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class JoinActivity extends AppCompatActivity {

    public int dupliacte_check = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        CheckBox[] check_box = new CheckBox[22];
        String[] check_id = {"난류", "우유", "토마토", "복숭아", "밀", "잣", "대두", "메밀", "땅콩", "호두", "돼지고기", "닭고기", "쇠고기", "고등어", "게", "새우", "오징어", "굴", "홍합", "전복", "아황산", "기타"};
        Integer[] checkbox_name = {R.id.egg, R.id.milk, R.id.tomato, R.id.peach, R.id.wheat, R.id.pine, R.id.soybean, R.id.buckwheat, R.id.peanut, R.id.walnut, R.id.pork, R.id.chicken, R.id.beef, R.id.mackerel, R.id.crab, R.id.shrimp, R.id.squid, R.id.oyster, R.id.mussel, R.id.abalone, R.id.sulfuric, R.id.others};
        final dbManager dbHelper = new dbManager(getApplicationContext(), "user_db", null, 1);

        for (int i = 0 ; i < checkbox_name.length ; i++){
            check_box[i] = (CheckBox) findViewById(checkbox_name[i]);
        }

        EditText id_edit = (EditText) findViewById(R.id.joid_id);
        EditText passwd = (EditText) findViewById(R.id.join_passwd);
        EditText passwd_c = (EditText) findViewById(R.id.confilm_passwd);

        Button duplicate_btn = (Button) findViewById(R.id.duplicate);
        duplicate_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = id_edit.getText().toString();

                if (s.equals("")) {
                    Toast.makeText(getApplicationContext(), "아이디를 입력하세요.", Toast.LENGTH_LONG).show();
                }
                else if (dbHelper.duplicate_conflim(s)){
                    Toast.makeText(getApplicationContext(), "이미 사용중인 아이디 입니다.", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), "사용 가능한 아이디 입니다.", Toast.LENGTH_LONG).show();
                    dupliacte_check = 1;
                }
            }
        });

        Button join_execute_btn = (Button) findViewById(R.id.join_execute);
        join_execute_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dupliacte_check == 0)
                    Toast.makeText(getApplicationContext(), "아이디 중복 확인을 하십시오.", Toast.LENGTH_LONG).show();
                else if (!passwd.getText().toString().equals(passwd_c.getText().toString()))
                    Toast.makeText(getApplicationContext(), "비밀번호 확인이 일치하지 않습니다.", Toast.LENGTH_LONG).show();
                else {
                    int confilm = 0;
                    int i = 0;
                    for (i = 0; i < check_box.length; i++) {
                        if (check_box[i].isChecked() == true) {
                            confilm = 1;
                        }
                    }

                    if (confilm == 0){
                        AlertDialog.Builder builder = new AlertDialog.Builder(JoinActivity.this);
                        builder.setTitle("확인");
                        builder.setMessage("알레르기 선택이 없습니다. 진행하시겠습니까?");
                        builder.setPositiveButton("진행", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                insert_data(dbHelper, id_edit.getText().toString(), passwd.getText().toString(), check_box, check_id, 0);
                                Toast.makeText(getApplicationContext(), "회원등록 완료", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(intent);
                            }
                        });
                        builder.setNegativeButton("다시 선택", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(), "알레르기 성분을 선택하세요.", Toast.LENGTH_LONG).show();
                            }
                        });

                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }
                    else{
                        if (check_box[21].isChecked()) {
                            EditText others_t = findViewById(R.id.others_name);
                            if (others_t.getText().toString().equals("") || others_t.getText().toString() == null){
                                Toast.makeText(getApplicationContext(), "기타 알레르기 성분을 입력하세요.", Toast.LENGTH_LONG).show();
                            }
                            else{
                                insert_data(dbHelper, id_edit.getText().toString(), passwd.getText().toString(), check_box, check_id, 1);
                                Toast.makeText(getApplicationContext(), "회원등록 완료", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(intent);
                            }
                        }
                        else {
                            insert_data(dbHelper, id_edit.getText().toString(), passwd.getText().toString(), check_box, check_id, 1);
                            Toast.makeText(getApplicationContext(), "회원등록 완료", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intent);
                        }
                    }
                }
            }
        });
    }

    private void insert_data(dbManager dbHelper, String id, String passwd, CheckBox[] c, String[] s, int mode){
        if (mode == 1){
            String allergy_list = "";

            for (int i = 0 ; i < c.length - 1; i++){
                if (c[i].isChecked()){
                    allergy_list = allergy_list.concat(s[i] + ",");
                }
            }
            if (c[21].isChecked()){
                EditText others_allergy = findViewById(R.id.others_name);
                allergy_list = allergy_list.concat(others_allergy.getText().toString());
            }

            dbHelper.insert(id, passwd, allergy_list);
        }
        else{
            dbHelper.insert(id, passwd, "None");
        }
    }
}
