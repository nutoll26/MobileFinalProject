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

    static LatLng whereMe;
    private GoogleMap map;

    Intent intent;
    double lat, lng;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        intent = getIntent();
        lat = intent.getDoubleExtra("lat", 0);
        lng = intent.getDoubleExtra("lng", 0);

        Log.d("latlng2", lat + ", " + lng);

        whereMe = new LatLng(lat, lng);
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        Marker seoul = map.addMarker(new MarkerOptions().position(whereMe).title("현재위치"));

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(whereMe, 15));

        map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
    }
}
