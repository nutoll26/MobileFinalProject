package www.kookmin.ac.kr.mobilefinalproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button buttonSave;
    Button buttonMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
}
