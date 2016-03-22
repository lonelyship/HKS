package com.systex.hkspractice;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class HomeActivity extends ActionBarActivity implements LocationListener {

    protected  Double m_dLat = SDefinedData.LAT;
    protected  Double m_dLon = SDefinedData.LON;
    protected ProgressDialog m_progressDialog = null;
    protected Context m_Context = null;
    protected ImageSwitcher m_imgSwitcher = null;
    protected Boolean m_bShowImg1 = true;
    protected LocationManager m_LocationMgr = null;
    protected Button m_btn01 = null;
    protected Button m_btn07 = null;
    protected TextView m_tvHomeAddress = null;
    protected List<Address> m_listAddresses = null;
    protected TextView m_tvTxtSearch = null;
    protected ImageView m_ivSearchIcon = null;
    protected String m_strSearchAddress = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        init();

        this.setTitle(R.string.homeActionBarTitle);

        showLocationDialog();

        m_imgSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                return new ImageView(HomeActivity.this);
            }
        });
        showCurrentImage();
        m_imgSwitcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_bShowImg1 = !m_bShowImg1;
                showCurrentImage();
            }
        });
        m_imgSwitcher.setInAnimation(AnimationUtils.loadAnimation(HomeActivity.this, android.R.anim.fade_in));
        m_imgSwitcher.setOutAnimation(AnimationUtils.loadAnimation(HomeActivity.this, android.R.anim.fade_out));

        m_btn01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (NearByStoreActivity.m_alData != null) {
//                    NearByStoreActivity.m_alData.clear();
//                }
                Intent it;
                it = new Intent(HomeActivity.this, NearByStoreActivity.class);
                it.putExtra(SDefinedData.MODE,SDefinedData.NUMONE);
                it.putExtra(SDefinedData.MODE,SDefinedData.NUMONE);
                it.putExtra(SDefinedData.MODE,SDefinedData.NUMONE);
                startActivity(it);
            }
        });
        m_btn07.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (NearByStoreActivity.m_alData != null) {
//                    NearByStoreActivity.m_alData.clear();
//                }
                Intent it;
                it = new Intent(HomeActivity.this, NearByStoreActivity.class);
                it.putExtra(SDefinedData.MODE,SDefinedData.NUMSEVEN);
                startActivity(it);
            }
        });
    }

    public void init() {
        m_Context = this;
        m_listAddresses = null;
        SEncapsulationData.getInstance().setDLat(m_dLat);
        SEncapsulationData.getInstance().setDlon(m_dLon);
        m_LocationMgr = (LocationManager) getSystemService(LOCATION_SERVICE);
        m_imgSwitcher = (ImageSwitcher) findViewById(R.id.imageSwitcher);
        m_btn01 = (Button) findViewById(R.id.btn01);
        m_btn07 = (Button) findViewById(R.id.btn07);
        m_tvHomeAddress = (TextView) findViewById(R.id.tvPageOneAddress);
        m_tvHomeAddress.setSelected(true);
    }

    private void showCurrentImage() {
        if (m_bShowImg1) {
            m_imgSwitcher.setImageResource(R.drawable.img1);
        } else {
            m_imgSwitcher.setImageResource(R.drawable.img2);
        }
    }

    private void enableLocationUpdate() {
        // 如果GPS功能有開啟，優先使用GPS定位，否則使用網路定位。
        if (m_LocationMgr.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            m_LocationMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER,SDefinedData.NUMFIVETHOUSANDS,SDefinedData.NUMFIVE, this);
            Toast.makeText(HomeActivity.this,R.string.useGPS, Toast.LENGTH_SHORT).show();
        } else if (m_LocationMgr.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            m_LocationMgr.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,SDefinedData.NUMFIVETHOUSANDS,SDefinedData.NUMFIVE, this);
            Toast.makeText(HomeActivity.this,R.string.useNetWork, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(HomeActivity.this,R.string.openPositionSetting, Toast.LENGTH_SHORT).show();
            m_progressDialog.cancel();
        }
    }

    private void disableLocationUpdate() {
        m_LocationMgr.removeUpdates(this);
//        Toast.makeText(HomeActivity.this, "定位功能已經停用", Toast.LENGTH_LONG)
//                .show();
    }

    @Override
    public void onLocationChanged(Location location) {
        m_dLat = location.getLatitude();
        m_dLon = location.getLongitude();

        SEncapsulationData.getInstance().setDLat(m_dLat);
        SEncapsulationData.getInstance().setDlon(m_dLon);

        Toast.makeText(getApplicationContext(),
                      ""+getString(R.string.latitude)+location.getLatitude()+getString(R.string.longitude)+location.getLongitude(),
                      Toast.LENGTH_SHORT).show();
        m_progressDialog.cancel();
        disableLocationUpdate();
        m_tvHomeAddress.setText(getString(R.string.searchPosition) + getAddressByLocation(location) + getString(R.string.nearby));
    }

    public String getAddressByLocation(Location location) {
        String returnAddress = "";
        try {
            if (location != null) {
                Double longitude = location.getLongitude();       //取得經度
                Double latitude = location.getLatitude();        //取得緯度

                Geocoder gc = new Geocoder(this, Locale.TRADITIONAL_CHINESE);        //地區:台灣
                //自經緯度取得地址
                List<Address> lstAddress = gc.getFromLocation(latitude,longitude,1);
//
//                if (!Geocoder.isPresent()) { //Since: API Level 9
//                   returnAddress = "Sorry! Geocoder service not Present.";
//                }
                returnAddress = lstAddress.get(0).getAddressLine(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnAddress;
    }

    public Location useSearchAddress(String address) {
        Toast.makeText(getApplicationContext(),getString(R.string.search) + address, Toast.LENGTH_SHORT).show();
        Location location = new Location(LocationManager.NETWORK_PROVIDER);
        Geocoder geoCoder = new Geocoder(this);
        try {
            m_listAddresses =geoCoder.getFromLocationName(address,1);

            if (m_listAddresses.size() > 0) {
                Double latitude = m_listAddresses.get(0).getLatitude();
                Double longitude = m_listAddresses.get(0).getLongitude();
                location.setLatitude(latitude);
                location.setLongitude(longitude);
                return location;
                //  Toast.makeText(this,"緯度:"+location.getLatitude()+"經度:"+location.getLongitude(),Toast.LENGTH_SHORT).show();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
//        String str = provider;
//        switch (status) {
//            case LocationProvider.OUT_OF_SERVICE:
//                str += "定位功能無法使用";
//                break;
//            case LocationProvider.TEMPORARILY_UNAVAILABLE:
//                str += "暫時無法定位";    // GPS正在定位中時會傳入這個值。
//                break;
//        }
//
//        Toast.makeText(HomeActivity.this, str, Toast.LENGTH_LONG)
//                .show();
    }

    @Override
    public void onProviderEnabled(String provider) {
//        Toast.makeText(HomeActivity.this, provider + "定位功能開啟", Toast.LENGTH_LONG)
//                .show();
//        enableLocationUpdate();
    }

    @Override
    public void onProviderDisabled(String provider) {
//        Toast.makeText(HomeActivity.this, provider + "定位功能已經被關閉", Toast.LENGTH_LONG).show();
    }

    public void showLocationDialog() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(R.string.choosePositionMode);
        alert.setPositiveButton(R.string.searchAddressName, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showSearchAddressDialog();
            }

        });
        alert.setNegativeButton(R.string.autoTakeLocation, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_progressDialog = ProgressDialog.show(m_Context, "",getString(R.string.gettingPositionPleaseWait), true);
                enableLocationUpdate();
            }
        });
        alert.show();
    }

    public void showSearchAddressDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog alertSearch = builder.create();
        alertSearch.setTitle(R.string.typeAddressKeyWords);

        final View searchView = getLayoutInflater().inflate(R.layout.home_search, null, false);
        m_tvTxtSearch = (EditText) searchView.findViewById(R.id.txt_search);
        m_tvTxtSearch.setText(m_strSearchAddress);
        m_ivSearchIcon = (ImageView) searchView.findViewById(R.id.ivSearchIcon);

        m_ivSearchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_strSearchAddress = m_tvTxtSearch.getText().toString();
                //TextUtils.isEmpty();
                if (TextUtils.isEmpty(m_strSearchAddress) == true){
                    Toast.makeText(getBaseContext(),R.string.typeKeyWords, Toast.LENGTH_SHORT).show();
                } else {
                    m_progressDialog = ProgressDialog.show(m_Context,"",getString(R.string.gettingPositionPleaseWait),true);
                    // Toast.makeText(m_Context,""+useSearchAddress().getLatitude(),Toast.LENGTH_SHORT).show();
                    Location location = useSearchAddress(m_strSearchAddress);
                    if (location == null) {
                        Toast.makeText(getApplicationContext(),R.string.findNoData, Toast.LENGTH_SHORT).show();
                        m_progressDialog.cancel();
                        showLocationDialog();
                        alertSearch.dismiss();
                    } else {
                        onLocationChanged(location);
                        alertSearch.dismiss();
                    }
                }
            }
        });
        alertSearch.setView(searchView);
        alertSearch.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (R.id.showDialogMenuItem == item.getItemId()) {
            showLocationDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //停止Google Map 內部定位功能
        disableLocationUpdate();
    }
}

