package www.kookmin.ac.kr.mobilefinalproject;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;

public class ListingActivity extends AppCompatActivity{

    Intent intent;
    String location, doing, accident;

    Button buttonBack;
    RadioGroup rg;

    SQLiteDatabase db;
    MySQLiteOpenHelper helper;

    ListView listview;
    ArrayList<String> arrLog = new ArrayList<String>();
    ArrayAdapter<String> Adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing);

        helper = new MySQLiteOpenHelper(ListingActivity.this, // 현재 화면의 context
                "Life.db", // 파일명
                null, // 커서 팩토리
                1); // 버전 번호

        // 전달된 인텐드와 내용
        intent = getIntent();
        location = intent.getStringExtra("location");
        doing = intent.getStringExtra("doing");
        accident = intent.getStringExtra("accident");

        listview = (ListView)findViewById(R.id.listv);
        listview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        Adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, arrLog);

        insert(location, doing, accident);
        selectAll();

        buttonBack = (Button)findViewById(R.id.btnBack);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ListingActivity.this, location + " " + doing + " " + accident, Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        rg = (RadioGroup)findViewById(R.id.rgroup);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioBtnAll: {
                        //Toast.makeText(ListingActivity.this, "모두", Toast.LENGTH_SHORT).show();

                        break;
                    }
                    case R.id.radioBtnAction: {
                        //Toast.makeText(ListingActivity.this, "행동", Toast.LENGTH_SHORT).show();

                        select("코딩",1);
                        break;
                    }
                    case R.id.radioBtnAccident: {
                        //Toast.makeText(ListingActivity.this, "사건", Toast.LENGTH_SHORT).show();
                        select("춥다",2);
                        break;
                    }
                }
            }
        });
    }

    // insert
    public void insert(String location, String doing, String accident) {
        db = helper.getWritableDatabase(); // db 객체를 얻어온다. 쓰기 가능

        ContentValues values = new ContentValues();
        // db.insert의 매개변수인 values가 ContentValues 변수이므로 그에 맞춤
        // 데이터의 삽입은 put을 이용한다.
        values.put("Location", location);
        values.put("Action", doing);
        values.put("Accident", accident);
        db.insert("lifelog", null, values);
    }

    public void select(String searchWord, int index) {
        db = helper.getReadableDatabase();
        String content = "";

        arrLog.clear();
        if(index == 0) {
            Cursor cursor = db.rawQuery("select * from lifelog where Location = '" + searchWord + "'", null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    content = cursor.getString(1) + ", " + cursor.getString(2) + ", " + cursor.getString(3);
                    arrLog.add(content);

                    Log.d("db", content);
                }
            }
        }else if(index == 1){
            Cursor cursor = db.rawQuery("select * from lifelog where Action = '" + searchWord + "'", null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    content = cursor.getString(1) + ", " + cursor.getString(2) + ", " + cursor.getString(3);
                    arrLog.add(content);
                    Log.d("db", content);
                }
            }
        }else if(index == 2){
            Cursor cursor = db.rawQuery("select * from lifelog where Accident = '" + searchWord + "'", null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    content = cursor.getString(1) + ", " + cursor.getString(2) + ", " + cursor.getString(3);
                    arrLog.add(content);
                    Log.d("db", content);
                }
            }
        }

        listview.setAdapter(Adapter);
    }
    // select
    public void selectAll() {
        db = helper.getReadableDatabase(); // db객체를 얻어온다. 읽기 전용
        Cursor c = db.query("lifelog", null, null, null, null, null, null);

        /*
         * 위 결과는 select * from student 가 된다. Cursor는 DB결과를 저장한다. public Cursor
         * query (String table, String[] columns, String selection, String[]
         * selectionArgs, String groupBy, String having, String orderBy)
         */

        arrLog.clear();
        if(c.getCount() > 0) {
            while (c.moveToNext()) {
                String Location = c.getString(c.getColumnIndex("Location"));
                String Action = c.getString(c.getColumnIndex("Action"));
                String Accident = c.getString(c.getColumnIndex("Accident"));

                String content = Location + ", " + Action + ", " + Accident;
                arrLog.add(content);

                Log.i("db", "위치 : " + Location + ", 하는 일 : " + Action + ", 사건 : " + Accident);
            }
        }

        listview.setAdapter(Adapter);
    }

}
