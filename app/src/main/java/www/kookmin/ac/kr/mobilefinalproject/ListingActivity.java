package www.kookmin.ac.kr.mobilefinalproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

public class ListingActivity extends AppCompatActivity{

    Intent intent;
    String str1, str2, str3;

    Button buttonBack;

    RadioGroup rg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing);

        rg = (RadioGroup)findViewById(R.id.rgroup);

        // 전달된 인텐드와 내용
        intent = getIntent();
        str1 = intent.getStringExtra("location");
        str2 = intent.getStringExtra("doing");
        str3 = intent.getStringExtra("accident");

        buttonBack = (Button)findViewById(R.id.btnBack);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ListingActivity.this, str1 + " " + str2 + " " + str3, Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.radioBtnAction: {
                        Toast.makeText(ListingActivity.this, rg.getCheckedRadioButtonId() + " ", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case R.id.radioBtnAccident: {
                        Toast.makeText(ListingActivity.this, rg.getCheckedRadioButtonId() + " ", Toast.LENGTH_SHORT).show();

                        break;
                    }
                }
            }
        });
    }
}
