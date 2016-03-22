package com.systex.hkspractice;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;


public class GoogleMapActivity extends ActionBarActivity {

    protected Fragment m_fragment = null;
    protected GoogleMap m_googleMap = null;
    protected TextView m_tvPageFiveTitle = null;
    protected ImageButton m_imgBtnPhone = null;
    protected String m_title = null;
    protected String m_address = null;
    protected String m_phoneNum = null;
    protected Double m_dLat=SDefinedData.LAT;
    protected Double m_dLon=SDefinedData.LON;
    protected LatLng storePosition = null;
    protected int m_nearByStorePosition = 0;
    protected int m_DiscountItemsPosition = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_google_map);

        init();

        m_nearByStorePosition = getIntent().getIntExtra(SDefinedData.NEARBYSTOREPOSITION, 0);
        m_DiscountItemsPosition = getIntent().getIntExtra(SDefinedData.DISCOUNTITEMSPOSITION, 0);

        try {
            m_tvPageFiveTitle.setText(m_title = SEncapsulationData.getInstance().getAlData().get(m_nearByStorePosition).getString(SDefinedData.NAME));
            this.setTitle(m_title);
            m_address = SEncapsulationData.getInstance().getAlData().get(m_nearByStorePosition).getString(SDefinedData.ADDR);
            m_dLat = Double.valueOf(SEncapsulationData.getInstance().getAlData().get(m_nearByStorePosition).getString(SDefinedData.STRLAT));
            m_dLon = Double.valueOf(SEncapsulationData.getInstance().getAlData().get(m_nearByStorePosition).getString(SDefinedData.STRLON));
            m_phoneNum= SEncapsulationData.getInstance().getAlData().get(m_nearByStorePosition).getJSONArray(SDefinedData.EVENT).getJSONObject(m_DiscountItemsPosition).getString(SDefinedData.PHONE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        storePosition = new LatLng(m_dLat, m_dLon);

        if(m_phoneNum.compareTo("")==0){
            m_phoneNum="0934XXXXXX";
        }

        m_imgBtnPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + m_phoneNum));
                startActivity(it);
            }
        });

        m_googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        m_googleMap.setMyLocationEnabled(true);

        Marker marker = m_googleMap.addMarker(new MarkerOptions().position(storePosition).title(m_title).snippet(m_address));
        marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.hksmapicon));
        marker.showInfoWindow();
        // m_googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(storePosition, 16));
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(storePosition)              // Sets the center of the map to ZINTUN
                .zoom(18)                   // Sets the zoom
                        // .bearing(90)                // Sets the orientation of the camera to east
                .tilt(90)                   // Sets the tilt of the camera to 90 degrees
                .build();                   // Creates a CameraPosition from the builder

        m_googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    public void init(){
        m_tvPageFiveTitle = (TextView) findViewById(R.id.tvPageFiveTitle);
        m_imgBtnPhone = (ImageButton) findViewById(R.id.imgBtnPhone);
        m_fragment = getFragmentManager().findFragmentById(R.id.pageFiveMap);
        m_googleMap = ((MapFragment) m_fragment).getMap();
    }
}
