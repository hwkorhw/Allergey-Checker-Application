package com.example.allergyprevention;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class dbManager extends SQLiteOpenHelper {

    // DBHelper 생성자로 관리할 DB 이름과 버전 정보를 받음
    public dbManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    // DB를 새로 생성할 때 호출되는 함수
    @Override
    public void onCreate(SQLiteDatabase db) {
        // 새로운 테이블 생성
        db.execSQL("CREATE TABLE IF NOT EXISTS user_table (id varchar(20) PRIMARY KEY, passwd varchar(50), allergy_component varchar(200));");
    }

    // DB 업그레이드를 위해 버전이 변경될 때 호출되는 함수
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insert(String id, String passwd, String allergy) {
        // 읽고 쓰기가 가능하게 DB 열기
        SQLiteDatabase db = getWritableDatabase();
        // DB에 입력한 값으로 행 추가
        db.execSQL("INSERT INTO user_table VALUES(\'" + id + "\',\'" + passwd + "\',\'" + allergy + "\');");
        db.close();
    }

    public void update(String id, String allergy) {
        SQLiteDatabase db = getWritableDatabase();
        // 입력한 항목과 일치하는 행의 가격 정보 수정
        db.execSQL("UPDATE user_table SET allergy_component =\'" + allergy + "\' WHERE id=\'" + id + "\';");
        db.close();
    }

    public void delete(String id) {
        SQLiteDatabase db = getWritableDatabase();
        // 입력한 항목과 일치하는 행 삭제
        db.execSQL("DELETE FROM user_table WHERE id=\'" + id + "\';");
        db.close();
    }

    public String[] select(String id){
        String[] result = new String[3];
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM user_table WHERE id = \'" + id + "\';";
        Cursor cursor = db.rawQuery(sql, null);
        while(cursor.moveToFirst()){
            result[0] = cursor.getString(0);
            result[1] = cursor.getString(1);
            result[2] = cursor.getString(2);

            return result;
        }
        result[0] = "None";
        result[1] = "None";
        result[2] = "None";

        return result;
    }

    public boolean duplicate_conflim(String s) {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT id FROM user_table WHERE id = \'" + s + "\';";
        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery(sql, null);
        if(cursor.moveToFirst()) return true;
        else return false;
    }
}
