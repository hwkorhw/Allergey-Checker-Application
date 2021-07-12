package com.example.allergyprevention;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    final String TAG = "SearchActivity";
    private String requestUrl;
    ArrayList<item> list = new ArrayList<item>();
    item stuff = null;
    EditText edit;
    RecyclerView recyclerView;
    String user_id;
    String user_allergy;
    String key="55jAcdbw8tG8HYDtgNjfjiVK%2F2MSuKSFDV%2FH5k5NSWfsmu68PadZwQffhMq0zPjmfj6Wae1si2LWUdBIte4iKQ%3D%3D";
    String data;
    String item_name;
    String[] userAllergy_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Intent Myintent = getIntent();
        Bundle bundle = Myintent.getExtras();
        user_id = bundle.getString("userId");
        user_allergy = bundle.getString("userAllergy");
        userAllergy_list = user_allergy.split(",");

        edit= (EditText)findViewById(R.id.edit);
        edit.setPrivateImeOptions("defaultInputmode=korean; ");
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

//        AsyncTask
    }

    public void backhome_clicked(View v){
        Intent backintent = new Intent(getApplicationContext(), ContentActivity.class);
        backintent.putExtra("userId", user_id);
        backintent.putExtra("userAllergy", user_allergy);
        startActivity(backintent);
    }

    public void seach_clicked(View v){
        item_name = edit.getText().toString();

        MyAsyncTask myAsyncTask = new MyAsyncTask();
        myAsyncTask.execute();
    }//mOnClick method..

    public class MyAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            list.clear();
            try {
                item_name = URLEncoder.encode(item_name,"UTF-8");
            } catch (UnsupportedEncodingException e) {
            }

            requestUrl = "http://apis.data.go.kr/B553748/CertImgListService/getCertImgListService?serviceKey=" + key + "&prdlstNm=" + item_name;
            try {
                URL url = new URL(requestUrl);
                InputStream is = url.openStream();
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser parser = factory.newPullParser();
                parser.setInput(new InputStreamReader(is, "UTF-8"));

                String tag;
                int eventType = parser.getEventType();

                while(eventType != XmlPullParser.END_DOCUMENT){
                    switch (eventType){
                        case XmlPullParser.START_DOCUMENT:
                            break;
                        case XmlPullParser.END_DOCUMENT:
                            break;
                        case XmlPullParser.START_TAG:
                            tag= parser.getName();//테그 이름 얻어오기
                            if(tag.equals("item")){
                                stuff = new item();
                            }
                            if (tag.equals("prdlstNm")) stuff.setName(parser.nextText());
                            if (tag.equals("rawmtrl")) stuff.setNutrite(parser.nextText());
                            if (tag.equals("allergy")) stuff.setAllergy(parser.nextText());
                            break;
                        case XmlPullParser.END_TAG:
                            String endTag = parser.getName();
                            if(endTag.equals("item")) {
                                list.add(stuff);
                            }
                            break;
                    }
                    eventType = parser.next();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            MyAdapter adapter = new MyAdapter(getApplicationContext(), list);

            adapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View v, int position) {
                    item clickItem = adapter.getItem(position);
                    AlertDialog.Builder builder = new AlertDialog.Builder(SearchActivity.this);
                    String item_allergy = clickItem.getAllergy();
                    String item_nutrite = clickItem.getNutrite();
                    StringBuilder result = new StringBuilder();
                    if (!item_allergy.equals("알수없음")){
                        result.append(item_allergy);
                        if (result.indexOf("함유") != -1){
                            result.delete(result.indexOf("함유"), result.indexOf("함유") + 2);
                        }
                    }
                    for (int i = 0 ; i < userAllergy_list.length ; i++){
                        if ((item_nutrite.indexOf(userAllergy_list[i]) > -1) && (result.indexOf(userAllergy_list[i]) == -1)){
                            result.append(userAllergy_list[i]);
                        }
                    }

                    if (result.length() == 0){
                        builder.setTitle("주의");
                        builder.setMessage("선택하신 상품에는 알레르기 성분이 포함되지 않았습니다.");
                    }
                    else{
                        builder.setTitle("주의");
                        builder.setMessage("선택하신 상품에 알레르기 성분 \'" + result + "\' 이 포함되어있습니다.");
                    }
                    builder.setPositiveButton("확인", null);
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            });

            recyclerView.setAdapter(adapter);
        }
    }
}
