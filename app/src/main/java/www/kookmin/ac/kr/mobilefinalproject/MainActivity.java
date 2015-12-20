package www.kookmin.ac.kr.mobilefinalproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    Button buttonSave;
    Button buttonMap;

    EditText edLocation;
    EditText edAction;
    EditText edAccident;

    // GPSTracker class
    private GpsInfo gps;

    double latitude;
    double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 현재 자신의 일상을 적는 에디트텍스
        edLocation = (EditText)findViewById(R.id.editLocation);
        edAction = (EditText)findViewById(R.id.editDoing);
        edAccident = (EditText)findViewById(R.id.editAccident);

        // 일상 기록을 내 폰에 저장한다.
        buttonSave = (Button)findViewById(R.id.btnSave);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gps = new GpsInfo(MainActivity.this);

                if (gps.isGetLocation()) { // 현재 위치의 위도, 경도를 받아온다.
                    latitude = gps.getLatitude();
                    longitude = gps.getLongitude();

                    // 디비의 주키가 될 시간
                    Calendar calendar = Calendar.getInstance();
                    java.util.Date date = calendar.getTime();
                    String cur = (new SimpleDateFormat("yyyy.MM.dd-HH:mm:ss").format(date));

                    Intent intent = new Intent(MainActivity.this, ListingActivity.class);
                    intent.putExtra("time", cur);
                    intent.putExtra("location", edLocation.getText().toString());
                    intent.putExtra("doing", edAction.getText().toString());
                    intent.putExtra("accident", edAccident.getText().toString());

                    intent.putExtra("lat", latitude);
                    intent.putExtra("lng", longitude);

                    startActivity(intent);
                } else {
                    // GPS 를 사용할수 없으므로
                    gps.showSettingsAlert();
                }
            }
        });

        // 현재 위치를 지도상에서 확인한다.
        buttonMap = (Button)findViewById(R.id.btnMap);
        buttonMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gps = new GpsInfo(MainActivity.this);

                if (gps.isGetLocation()) { // 현재 위도, 경도
                    latitude = gps.getLatitude();
                    longitude = gps.getLongitude();

                    Intent intent = new Intent(MainActivity.this, MapActivity.class);
                    intent.putExtra("size", 1);
                    intent.putExtra("lat0", latitude+"");
                    intent.putExtra("lng0", longitude+"");

                    startActivity(intent);
                } else {
                    // GPS 를 사용할수 없으므로
                    gps.showSettingsAlert();
                }
            }
        });
    }
}
