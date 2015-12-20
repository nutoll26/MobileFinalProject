package www.kookmin.ac.kr.mobilefinalproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends AppCompatActivity {

    static LatLng whereMe[];
    private GoogleMap map;

    Intent intent;
    double lat[], lng[];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        intent = getIntent();
        int size = intent.getIntExtra("size", 0);
        lat = new double[size];
        lng = new double[size];

        for(int i=0; i<size; i++){
            lat[i] = Double.parseDouble(intent.getStringExtra("lat"+i));
            lng[i] = Double.parseDouble(intent.getStringExtra("lng"+i));

            Log.d("latlng2", lat[i] + ", " + lng[i]);
        }

        // 넘어온 위도,경도를 활용하여 지도 상에 마커를 찍는다.
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

        whereMe = new LatLng[size];

        for(int i=0; i<size; i++) {
            whereMe[i] = new LatLng(lat[i], lng[i]);
            Marker seoul = map.addMarker(new MarkerOptions().position(whereMe[i]).title("현재위치"+(i+1)));

        }
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(whereMe[0], 15));
        map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
    }
}
