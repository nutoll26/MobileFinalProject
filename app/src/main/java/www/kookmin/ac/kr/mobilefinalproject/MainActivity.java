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

    String curTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edLocation = (EditText)findViewById(R.id.editLocation);
        edAction = (EditText)findViewById(R.id.editDoing);
        edAccident = (EditText)findViewById(R.id.editAccident);

        buttonSave = (Button)findViewById(R.id.btnSave);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gps = new GpsInfo(MainActivity.this);

                if (gps.isGetLocation()) {
                    gps.getLatitude();
                    gps.getLongitude();
                    //Toast.makeText(getApplicationContext(), "당신의 위치 - \n위도: " + latitude + "\n경도: " + longitude, Toast.LENGTH_LONG).show();

                    Calendar calendar = Calendar.getInstance();
                    java.util.Date date = calendar.getTime();
                    String cur = (new SimpleDateFormat("yyyy.MM.dd-HH:mm:ss").format(date));

                    Intent intent = new Intent(MainActivity.this, ListingActivity.class);
                    intent.putExtra("time", cur);
                    intent.putExtra("location", edLocation.getText().toString());
                    intent.putExtra("latlong", latitude + ", " + longitude);
                    intent.putExtra("doing", edAction.getText().toString());
                    intent.putExtra("accident", edAccident.getText().toString());

                    startActivity(intent);
                } else {
                    // GPS 를 사용할수 없으므로
                    gps.showSettingsAlert();
                }
            }
        });

        buttonMap = (Button)findViewById(R.id.btnMap);
        buttonMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
