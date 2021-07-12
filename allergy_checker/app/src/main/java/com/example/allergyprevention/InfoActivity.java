package com.example.allergyprevention;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class InfoActivity extends AppCompatActivity {

    String userid;
    String userallergy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        userid = bundle.getString("userId");
        userallergy = bundle.getString("userAllergy");
    }

    public void backhome_clicked(View v){
        Intent backintent = new Intent(getApplicationContext(), ContentActivity.class);
        backintent.putExtra("userId", userid);
        backintent.putExtra("userAllergy", userallergy);
        startActivity(backintent);
    }

    public void info_btn1_clicked(View v){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://100.daum.net/encyclopedia/view/75XX84400015"));
        startActivity(intent);
    }

    public void info_btn2_clicked(View v){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://post.naver.com/viewer/postView.nhn?volumeNo=3860538&memberNo=22718804&searchKeyword=%EC%8B%9D%ED%92%88%20%EC%95%8C%EB%A0%88%EB%A5%B4%EA%B8%B0&searchRank=13"));
        startActivity(intent);
    }

    public void info_btn3_clicked(View v){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://post.naver.com/viewer/postView.nhn?volumeNo=30692201&memberNo=45564485&searchKeyword=%EC%8B%9D%ED%92%88%20%EC%95%8C%EB%A0%88%EB%A5%B4%EA%B8%B0&searchRank=2"));
        startActivity(intent);
    }

    public void info_btn4_clicked(View v){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://cafe.naver.com/safeallergy"));
        startActivity(intent);
    }

    public void info_btn5_clicked(View v){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.mfds.go.kr/index.do"));
        startActivity(intent);
    }
}
