package www.kookmin.ac.kr.mobilefinalproject;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button buttonSave;
    Button buttonMap;

    SQLiteDatabase db;
    String dbName = "Life.db"; // name of Database;
    String tableName = "lifeLogTable"; // name of Table;
    int dbMode = Context.MODE_PRIVATE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // // Database 생성 및 열기
        db = openOrCreateDatabase(dbName,dbMode,null);
        // 테이블 생성
        createTable();

        buttonSave = (Button)findViewById(R.id.btnSave);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListingActivity.class);
                intent.putExtra("location", "서울특별시");
                intent.putExtra("doing", "코딩");
                intent.putExtra("accident", "나홀로깨어있다");

                startActivity(intent);
            }
        });
    }



    // Table 생성
    public void createTable() {
        try {
            String sql = "create table " + tableName + "(Id INTEGER PRIMARY KEY autoincrement, "
                    + "Location TEXT NOT NULL,"
                    + "Doing TEXT NOT NULL,"
                    + "Accident TEXT NOT NULL"
                    + ")";
            db.execSQL(sql);
        } catch (android.database.sqlite.SQLiteException e) {
            Log.d("Lab sqlite","error: "+ e);
        }
    }

    // Table 삭제
    public void removeTable() {
        String sql = "drop table " + tableName;
        db.execSQL(sql);
    }

    // Data 추가
    public void insertData(String location, String doing, String accident) {
        String sql = "insert into " + tableName + " values(NULL, '" + location + "', '" + doing + "', '" + accident + "' ";
        db.execSQL(sql);
    }

    // Data 읽기(꺼내오기)
    public void selectData(int index) {
        String sql = "select * from " + tableName + " where id = " + index + ";";
        Cursor result = db.rawQuery(sql, null);

        // result(Cursor 객체)가 비어 있으면 false 리턴
        if (result.moveToFirst()) {
            int id = result.getInt(0);
            String name = result.getString(1);
            Toast.makeText(this, "index= " + id + " name=" + name, Toast.LENGTH_LONG).show();
            Log.d("lab_sqlite", "\"index= \" + id + \" name=\" + name ");
        }
        result.close();
    }
//
//    // 모든 Data 읽기
//    public void selectAll() {
//        String sql = "select * from " + tableName + ";";
//        Cursor results = db.rawQuery(sql, null);
//        results.moveToFirst();
//
//        while (!results.isAfterLast()) {
//            int id = results.getInt(0);
//            String name = results.getString(1);
////            Toast.makeText(this, "index= " + id + " name=" + name, Toast.LENGTH_LONG).show();
//            Log.d("lab_sqlite", "index= " + id + " name=" + name);
//
//            nameList.add(name);
//            results.moveToNext();
//        }
//        results.close();
//    }
}
