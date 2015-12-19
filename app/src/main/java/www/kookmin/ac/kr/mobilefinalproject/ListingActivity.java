package www.kookmin.ac.kr.mobilefinalproject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Vector;

public class ListingActivity extends AppCompatActivity{

    Intent intent;
    String location, doing, accident, curTime;

    Button buttonBack;
    Button buttonPhone, buttonServer;
    RadioGroup rg;

    SQLiteDatabase db;
    MySQLiteOpenHelper helper;

    ListView listview;
    ArrayList<String> arrLog = new ArrayList<String>();
    ArrayAdapter<String> Adapter;

    private final String urlPath = "http://ec2-52-192-246-36.ap-northeast-1.compute.amazonaws.com/lifelogging.php";
    String slocation, saction, saccident, stime;
    String whichWork = "send";
    String curStatus = "phone";

    Vector<String> vc_time = new Vector<String>();

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
        curTime = intent.getStringExtra("time");
        location = intent.getStringExtra("location");
        doing = intent.getStringExtra("doing");
        accident = intent.getStringExtra("accident");

        listview = (ListView)findViewById(R.id.listv);
        //listview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        Adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, arrLog);
        listview.setOnItemClickListener(mItemClickListener);

        insert(curTime, location, doing, accident);

        selectAll();

        buttonBack = (Button)findViewById(R.id.btnBack);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(ListingActivity.this, location + " " + doing + " " + accident, Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        buttonPhone = (Button)findViewById(R.id.btnMyPhone);
        buttonPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                curStatus = "phone";
                selectAll();
            }
        });

        buttonServer = (Button)findViewById(R.id.btnServer);
        buttonServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                curStatus = "server";
                selectAll();
            }
        });


        rg = (RadioGroup)findViewById(R.id.rgroup);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioBtnAll: {
                        selectAll();

                        break;
                    }
                    case R.id.radioBtnLocation:{
                        AlertDialog.Builder alert = new AlertDialog.Builder(ListingActivity.this);
                        alert.setTitle("장소 검색");
                        alert.setMessage("검색어 입력");

                        // et an EditText view to get user input
                        final EditText input = new EditText(ListingActivity.this);
                        alert.setView(input);

                        alert.setPositiveButton("검색", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                String value = input.getText().toString();
                                value.toString();
                                slocation = value;
                                Log.d("result", value);
                                select(value, 0);
                            }
                        });
                        alert.show();

                        break;
                    }
                    case R.id.radioBtnAction: {
                        //Toast.makeText(ListingActivity.this, "행동", Toast.LENGTH_SHORT).show();

                        AlertDialog.Builder alert = new AlertDialog.Builder(ListingActivity.this);
                        alert.setTitle("행동 검색");
                        alert.setMessage("검색어 입력");

                        // Set an EditText view to get user input
                        final EditText input = new EditText(ListingActivity.this);
                        alert.setView(input);

                        alert.setPositiveButton("검색", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                String value = input.getText().toString();
                                value.toString();
                                saction = value;
                                Log.d("result", value);
                                select(value, 1);
                            }
                        });
                        alert.show();

                        break;
                    }
                    case R.id.radioBtnAccident: {
                        //Toast.makeText(ListingActivity.this, "사건", Toast.LENGTH_SHORT).show();
                        AlertDialog.Builder alert = new AlertDialog.Builder(ListingActivity.this);
                        alert.setTitle("사건 검색");
                        alert.setMessage("검색어 입력");

                        // Set an EditText view to get user input
                        final EditText input = new EditText(ListingActivity.this);
                        alert.setView(input);

                        alert.setPositiveButton("검색", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                String value = input.getText().toString();
                                value.toString();
                                saccident = value;
                                Log.d("result", value);
                                select(value, 2);
                            }
                        });
                        alert.show();
                        break;
                    }
                }
            }
        });
    }

    class HttpTask extends AsyncTask<Void, Void, String> {

        ProgressDialog asyncDialog = new ProgressDialog(ListingActivity.this);

        protected void onPreExecute(){
        }

        @Override
        protected String doInBackground(Void... voids) {

            try{
                HttpPost request = new HttpPost(urlPath);
                Vector<NameValuePair> nameValue = new Vector<NameValuePair>();

                switch(whichWork) {
                    case "send":{
                        nameValue.add(new BasicNameValuePair("CASE", whichWork));

                        nameValue.add(new BasicNameValuePair("TIME", stime));
                        nameValue.add(new BasicNameValuePair("LOCATION", slocation));
                        nameValue.add(new BasicNameValuePair("ACTION", saction));
                        nameValue.add(new BasicNameValuePair("ACCIDENT", saccident));

                        Log.d("item", whichWork+","+stime+","+slocation+","+saction+","+saccident);
                        break;
                    }
                    case "requestAll": {
                        nameValue.add(new BasicNameValuePair("CASE", whichWork));

                        break;
                    }
                    case "requestLocation": {
                        nameValue.add(new BasicNameValuePair("CASE", whichWork));
                        nameValue.add(new BasicNameValuePair("LOCATION", slocation));

                        Log.d("server", whichWork + ", " + slocation);
                        break;
                    }
                    case "requestAction": {
                        nameValue.add(new BasicNameValuePair("CASE", whichWork));
                        nameValue.add(new BasicNameValuePair("ACTION", saction));

                        break;
                    }
                    case "requestAccident": {
                        nameValue.add(new BasicNameValuePair("CASE", whichWork));
                        nameValue.add(new BasicNameValuePair("ACCIDENT", saccident));

                        break;
                    }
                }

                //웹 접속 - utf-8 방식으로
                HttpEntity enty = new UrlEncodedFormEntity(nameValue, "UTF-8");
                request.setEntity(enty);

                HttpClient client = new DefaultHttpClient();
                HttpResponse res = client.execute(request);

                //웹 서버에서 값받기
                HttpEntity entityResponse = res.getEntity();
                InputStream im = entityResponse.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(im, "UTF-8"));

                String total = "";
                String tmp = "";

                //버퍼에있는거 전부 더해주기
                //readLine -> 파일내용을 줄 단위로 읽기
                while ((tmp = reader.readLine()) != null) {
                    if (tmp != null) {
                        total += tmp;
                    }
                }

                im.close();

                return total;
            }catch(UnsupportedEncodingException e){
                e.printStackTrace();
            }catch(IOException e){
                e.printStackTrace();
            }
            //오류시 null 반환
            return null;
        }

        //asyncTask 3번째 인자와 일치 매개변수값 -> doInBackground 리턴값이 전달됨
        //AsynoTask 는 preExcute - doInBackground - postExecute 순으로 자동으로 실행됩니다.
        //ui는 여기서 변경
        protected void onPostExecute(String value){
            asyncDialog.dismiss();
            super.onPostExecute(value);
        }
    }









    private AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long l_position) {
//            // parent는 AdapterView의 속성의 모두 사용 할 수 있다.
            if(curStatus == "phone") {
                final String selectItem = (String) parent.getAdapter().getItem(position);
                final String subSelectItem = selectItem.substring(0, 19);

                Log.d("sub", subSelectItem);

                AlertDialog.Builder alert = new AlertDialog.Builder(ListingActivity.this);
                alert.setTitle("확인 팝업");
                alert.setMessage("어떠한 일을 수행하나요?");
                alert.setPositiveButton("서버 저장", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(getApplicationContext(), subSelectItem, Toast.LENGTH_SHORT).show();
                        select(subSelectItem, 10);

                        stime = subSelectItem;
                        try {
                            whichWork = "send";
                            String result = new HttpTask().execute().get();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        dialog.dismiss();     //닫기
                    }
                });
                alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();     //닫기
                    }
                });
                alert.show();
            }
        }
    };

    void getResultFromDB(String result){
        try{
            JSONArray jArray = new JSONArray(result);

            String[] jsonName = {"TIME", "LOCATION", "ACTION", "ACCIDENT"};
            String[][] parsedData = new String[jArray.length()][jsonName.length];
            JSONObject json = null;

            arrLog.clear();
            for(int i=0; i<jArray.length();i++){
                json = jArray.getJSONObject(i);
                if(json != null){
                    for(int j=0; j< jsonName.length; j++){
                        parsedData[i][j] = json.getString(jsonName[j]);
                    }
                }
            }

            for(int i=0; i<parsedData.length; i++){
                String content = parsedData[i][0] + "\n" + parsedData[i][1] + ", " + parsedData[i][2] + ", " + parsedData[i][3];
                arrLog.add(content);
            }
        }catch(JSONException e){
            e.printStackTrace();
        }
    }











    // insert
    public void insert(String time, String location, String doing, String accident) {
        db = helper.getWritableDatabase(); // db 객체를 얻어온다. 쓰기 가능

        ContentValues values = new ContentValues();
        // db.insert의 매개변수인 values가 ContentValues 변수이므로 그에 맞춤
        // 데이터의 삽입은 put을 이용한다.
        values.put("Time", time);
        values.put("Location", location);
        values.put("Action", doing);
        values.put("Accident", accident);

        db.insert("lifelog", null, values);
    }

    public void select(String searchWord, int index) {
        db = helper.getReadableDatabase();
        String content = "";

        arrLog.clear();
        if(index == 0) { // 위치 기반 검색
            if(curStatus.compareTo("phone") == 0) {
                Cursor cursor = db.rawQuery("select * from lifelog where Location = '" + searchWord + "'", null);
                if (cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        content = cursor.getString(1) + "\n" + cursor.getString(2) + ", " + cursor.getString(3) + ", " + cursor.getString(4);
                        arrLog.add(content);

                        //Log.d("db", content);
                    }
                }
                listview.setAdapter(Adapter);
            }else if(curStatus.compareTo("server") == 0){
                whichWork = "requestLocation";
                try {
                    String result = new HttpTask().execute().get();
                    getResultFromDB(result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                listview.setAdapter(Adapter);

            }
        }else if(index == 1){ // 행동 기반 검색
            if(curStatus.compareTo("phone") == 0) {
                Cursor cursor = db.rawQuery("select * from lifelog where Action = '" + searchWord + "'", null);
                if (cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        content = cursor.getString(1) + "\n" + cursor.getString(2) + ", " + cursor.getString(3) + ", " + cursor.getString(4);
                        arrLog.add(content);
                        Log.d("db", content);
                    }
                }
                listview.setAdapter(Adapter);
            }else{
                whichWork = "requestAction";
                try {
                    String result = new HttpTask().execute().get();
                    getResultFromDB(result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                listview.setAdapter(Adapter);
            }
        }else if(index == 2){ // 사건 기반 검색
            if(curStatus.compareTo("phone") == 0) {
                Cursor cursor = db.rawQuery("select * from lifelog where Accident = '" + searchWord + "'", null);
                if (cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        content = cursor.getString(1) + "\n" + cursor.getString(2) + ", " + cursor.getString(3) + ", " + cursor.getString(4);
                        arrLog.add(content);
                        Log.d("db", content);
                    }
                }
                listview.setAdapter(Adapter);
            }else{
                whichWork = "requestAccident";
                try {
                    String result = new HttpTask().execute().get();
                    getResultFromDB(result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                listview.setAdapter(Adapter);
            }
        }else if(index == 10){ // 서버에 저장할 데이터 선택
            Cursor cursor = db.rawQuery("select * from lifelog where Time = '" + searchWord + "'", null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    slocation = cursor.getString(2);
                    saction = cursor.getString(3);
                    saccident = cursor.getString(4);

                    Log.d("db", slocation + ", " + saction + ", " + saccident);
                }
            }
        }
    }
    // select
    public void selectAll() {

        if(curStatus.compareTo("phone") == 0) {
            db = helper.getReadableDatabase(); // db객체를 얻어온다. 읽기 전용
            Cursor c = db.query("lifelog", null, null, null, null, null, null);

            arrLog.clear();
            if (c.getCount() > 0) {
                while (c.moveToNext()) {
                    String Time = c.getString(c.getColumnIndex("Time"));
                    String Location = c.getString(c.getColumnIndex("Location"));
                    String Action = c.getString(c.getColumnIndex("Action"));
                    String Accident = c.getString(c.getColumnIndex("Accident"));

                    String content = Time + "\n" + Location + ", " + Action + ", " + Accident;
                    arrLog.add(content);

                    Log.i("db", "시간 : " + Time + ", 위치 : " + Location + ", 하는 일 : " + Action + ", 사건 : " + Accident);
                }
            }
            listview.setAdapter(Adapter);
        }else if(curStatus.compareTo("server") == 0){
            whichWork = "requestAll";
            try {
                String result = new HttpTask().execute().get();
                getResultFromDB(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
            listview.setAdapter(Adapter);
        }
    }
}