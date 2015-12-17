package www.kookmin.ac.kr.mobilefinalproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class ListingActivity extends AppCompatActivity {

    Intent intent;
    String str1;
    String str2;
    String str3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing);

        intent = getIntent(); //전달된 인텐트

        str1 = intent.getStringExtra("location");
        str2 = intent.getStringExtra("doing");
        str3 = intent.getStringExtra("accident");

        Toast.makeText(ListingActivity.this, str1 + " " + str2 + " " + str3, Toast.LENGTH_SHORT).show();
    }
}
