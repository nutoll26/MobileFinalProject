package www.kookmin.ac.kr.mobilefinalproject;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

public class ListingActivity extends AppCompatActivity{

    Intent intent;
    String location, doing, accident;

    Button buttonBack;
    RadioGroup rg;

    SQLiteDatabase db;
    MySQLiteOpenHelper helper;

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

        insert(location, doing, accident);

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
                        select();
                        break;
                    }
                    case R.id.radioBtnAction: {
                        //Toast.makeText(ListingActivity.this, "행동", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case R.id.radioBtnAccident: {
                        //Toast.makeText(ListingActivity.this, "사건", Toast.LENGTH_SHORT).show();
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

    // select
    public void select() {
        // 1) db의 데이터를 읽어와서, 2) 결과 저장, 3)해당 데이터를 꺼내 사용

        db = helper.getReadableDatabase(); // db객체를 얻어온다. 읽기 전용
        Cursor c = db.query("lifelog", null, null, null, null, null, null);

        /*
         * 위 결과는 select * from student 가 된다. Cursor는 DB결과를 저장한다. public Cursor
         * query (String table, String[] columns, String selection, String[]
         * selectionArgs, String groupBy, String having, String orderBy)
         */

        while (c.moveToNext()) {
            String Location = c.getString(c.getColumnIndex("Location"));
            String Action = c.getString(c.getColumnIndex("Action"));
            String Accident = c.getString(c.getColumnIndex("Accident"));

            Log.i("db", "위치 : " + Location + ", 하는 일 : " + Action + ", 사건 : " + Accident);
        }
    }

}
