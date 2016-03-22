package com.systex.hkspractice;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;

import java.util.ArrayList;


public class DiscountDetailActivity extends ActionBarActivity {



    protected Context m_context = null;
    protected TextView m_tvPageFourTitle = null;
    protected TextView m_tvPageFourDetail = null;
    protected TextView m_tvPageFourDate = null;
    protected TextView m_tvPageFourNote = null;
    protected TextView m_tvPageFourLink = null;
    protected TextView m_tvPageFourMap = null;
    protected TextView m_tvCouPonTitle = null;
    protected TextView m_tvCouPon = null;
    protected ImageButton m_imgBtnDiscountDetailMapLink = null;
    protected LinearLayout m_llPageFourCoupon = null;
    protected  int m_iCouponNum = 0;
    protected  int m_nearByStorePosition = 0;
    protected  int m_DiscountItemsPosition = 0;
    protected final int iNOTCOUPON = 0;
    protected final int iISCOUPON = 1;
    protected String m_url = null;
    protected String m_strFirstCouponUrl = null;
    protected String m_strCouponUrl = null;
    protected String m_title = null;
    protected ArrayList<String> m_alCouponTitle = null;
    protected ArrayList<String> m_alCouponUrl = null;
    protected MenuItem nextMenuItem=null;
    protected MenuItem backMenuItem=null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_discount_detail);

        init();


            m_nearByStorePosition = getIntent().getIntExtra(SDefinedData.NEARBYSTOREPOSITION, 0);
            m_DiscountItemsPosition = getIntent().getIntExtra(SDefinedData.DISCOUNTITEMSPOSITION, 0);
            //Toast.makeText(this, "!", Toast.LENGTH_SHORT).show();


            setData();


        m_imgBtnDiscountDetailMapLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it;
                it = new Intent(DiscountDetailActivity.this, GoogleMapActivity.class);
                it.putExtra(SDefinedData.NEARBYSTOREPOSITION, m_nearByStorePosition);
                it.putExtra(SDefinedData.DISCOUNTITEMSPOSITION, m_DiscountItemsPosition);
                startActivity(it);
            }
        });

        m_tvPageFourLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadWebView(m_title, m_url, iNOTCOUPON, v);
            }
        });
    }

    public void init() {
        m_context = this;
        m_iCouponNum = 0;
        m_tvPageFourTitle = (TextView) findViewById(R.id.tvPageFourTitle);
        m_tvPageFourDetail = (TextView) findViewById(R.id.tvPageFourDetail);
        m_tvPageFourDate = (TextView) findViewById(R.id.tvPageFourDate);
        m_tvPageFourNote = (TextView) findViewById(R.id.tvPageFourNote);
        m_tvPageFourLink = (TextView) findViewById(R.id.tvPageFourLink);
        m_tvPageFourMap = (TextView) findViewById(R.id.tvPageFourMap);
        m_tvCouPonTitle = (TextView) findViewById(R.id.tvCouPonTitle);
        m_llPageFourCoupon = (LinearLayout) findViewById(R.id.llPageFourCoupon);
        m_imgBtnDiscountDetailMapLink = (ImageButton) findViewById(R.id.imgBtnPageFourMapLink);

    }

    public void setData(){
        m_iCouponNum = 0;
        m_llPageFourCoupon.removeAllViews();
        m_llPageFourCoupon.addView(m_tvCouPonTitle);
        try {
            m_tvPageFourMap.setText(SEncapsulationData.getInstance().getAlData().get(m_nearByStorePosition).getString(SDefinedData.NAME));
            this.setTitle(SEncapsulationData.getInstance().getAlData().get(m_nearByStorePosition).getString(SDefinedData.NAME));
            m_tvPageFourTitle.setText(m_title = SEncapsulationData.getInstance().getAlData().get(m_nearByStorePosition).getJSONArray(SDefinedData.EVENT).getJSONObject(m_DiscountItemsPosition).getString(SDefinedData.TITLE));
            m_tvPageFourDetail.setText(SEncapsulationData.getInstance().getAlData().get(m_nearByStorePosition).getJSONArray(SDefinedData.EVENT).getJSONObject(m_DiscountItemsPosition).getString(SDefinedData.DETAIL));
            m_tvPageFourDate.setText(SEncapsulationData.getInstance().getAlData().get(m_nearByStorePosition).getJSONArray(SDefinedData.EVENT).getJSONObject(m_DiscountItemsPosition).getString(SDefinedData.STARTDATE) + "~" +
                    SEncapsulationData.getInstance().getAlData().get(m_nearByStorePosition).getJSONArray(SDefinedData.EVENT).getJSONObject(m_DiscountItemsPosition).getString(SDefinedData.ENDDATE));
            m_tvPageFourDate.setSelected(true);
            //tvPageFourCoupon.setText(m_couponTitle=SEncapsulationData.getInstance().getAlData()..get(m_discountItemsPosition).getJSONArray("event").getJSONObject(m_DiscountItemsPosition).getJSONArray("coupon").getJSONObject(0).getString("name"));
            m_tvPageFourNote.setText(SEncapsulationData.getInstance().getAlData().get(m_nearByStorePosition).getJSONArray(SDefinedData.EVENT).getJSONObject(m_DiscountItemsPosition).getString(SDefinedData.NOTE));
            m_url = SEncapsulationData.getInstance().getAlData().get(m_nearByStorePosition).getJSONArray(SDefinedData.EVENT).getJSONObject(m_DiscountItemsPosition).getString(SDefinedData.WEBSITE);
            m_strFirstCouponUrl = SEncapsulationData.getInstance().getAlData().get(m_nearByStorePosition).getJSONArray(SDefinedData.EVENT).getJSONObject(m_DiscountItemsPosition).getJSONArray(SDefinedData.COUPON).getJSONObject(0).getString(SDefinedData.URL);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            if (m_strFirstCouponUrl.compareTo("") != 0) {
                m_iCouponNum = SEncapsulationData.getInstance().getAlData().get(m_nearByStorePosition).getJSONArray(SDefinedData.EVENT).getJSONObject(m_DiscountItemsPosition).getJSONArray(SDefinedData.COUPON).length();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        m_alCouponUrl = new ArrayList<>();
        m_alCouponTitle = new ArrayList<>();
        //    View blackLineView=getLayoutInflater().inflate(R.layout.black_line, null, false);
        for (int i = 0; i < m_iCouponNum; i++) {
            View couponView = getLayoutInflater().inflate(R.layout.coupon, null, false);
            m_tvCouPon = (TextView) couponView.findViewById(R.id.tvCouPon);
            couponView.setTag(i);
            try {
                m_tvCouPon.setText(SEncapsulationData.getInstance().getAlData().get(m_nearByStorePosition).getJSONArray(SDefinedData.EVENT).getJSONObject(m_DiscountItemsPosition).getJSONArray(SDefinedData.COUPON).getJSONObject(i).getString(SDefinedData.NAME));
                m_alCouponTitle.add(SEncapsulationData.getInstance().getAlData().get(m_nearByStorePosition).getJSONArray(SDefinedData.EVENT).getJSONObject(m_DiscountItemsPosition).getJSONArray(SDefinedData.COUPON).getJSONObject(i).getString(SDefinedData.NAME));
                m_strCouponUrl = SEncapsulationData.getInstance().getAlData().get(m_nearByStorePosition).getJSONArray(SDefinedData.EVENT).getJSONObject(m_DiscountItemsPosition).getJSONArray(SDefinedData.COUPON).getJSONObject(i).getString(SDefinedData.URL);
                m_alCouponUrl.add(m_strCouponUrl);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            couponView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    loadWebView(m_alCouponTitle.get((Integer) v.getTag()), m_strCouponUrl, iISCOUPON, v);
                }
            });
            m_llPageFourCoupon.addView(couponView);
//            if(i < m_iCouponNum-1){
//              m_llPageFourCoupon.addView(blackLineView);
//            }
        }

        if (TextUtils.isEmpty(m_url) == true) {
            m_tvPageFourLink.setVisibility(View.GONE);
        } else {
            m_tvPageFourLink.setVisibility(View.VISIBLE);
        }

        if (m_iCouponNum == 0) {
            m_llPageFourCoupon.setVisibility(View.GONE);
        } else{
            m_llPageFourCoupon.setVisibility(View.VISIBLE);
        }

    }

    public void loadWebView(String getTitle, String getUrl, int getTag, View getView) {
        ProgressDialog dialog = new ProgressDialog(m_context);
        dialog.setMessage(getString(R.string.loadingWebsitePleaseWait));

        AlertDialog.Builder alert = new AlertDialog.Builder(m_context);
        alert.setTitle(getTitle);

        WebView wv = new WebView(m_context);
        if (getTag == 0) {
            wv.loadUrl(getUrl);
        } else {
            String strCouponUrl = m_alCouponUrl.get((Integer) getView.getTag());
            String[] strTotal = strCouponUrl.split("coupon_");

            String str1 = strTotal[0];
            String str2 = strTotal[1];
            wv.loadDataWithBaseURL(str1, "<img src=coupon_" + str2 + " />", "text/html", "utf8", null);
        }
        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setBuiltInZoomControls(true);
        wv.setInitialScale(100);
        wv.setWebViewClient(new SMyWebViewClient(m_context, dialog) {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        alert.setView(wv);
        alert.setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        alert.show();
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_discount_detail, menu);

         nextMenuItem=menu.findItem(R.id.next);
         backMenuItem=menu.findItem(R.id.back);

        if(m_nearByStorePosition == 0 && m_DiscountItemsPosition == 0){
            backMenuItem.setVisible(false);
        }

        try {
            if(m_DiscountItemsPosition == SEncapsulationData.getInstance().getAlData().get(m_nearByStorePosition).getJSONArray(SDefinedData.EVENT).length() - 1 &&
               m_nearByStorePosition == SEncapsulationData.getInstance().getAlData().size() - 1){
               nextMenuItem.setVisible(false);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        backMenuItem.setVisible(true);
        nextMenuItem.setVisible(true);



        try {
            if (R.id.next == item.getItemId()) {

                if (m_DiscountItemsPosition < SEncapsulationData.getInstance().getAlData().get(m_nearByStorePosition).getJSONArray(SDefinedData.EVENT).length() - 1) {
                    m_DiscountItemsPosition++;
                    //Toast.makeText(this,""+m_DiscountItemsPosition,Toast.LENGTH_SHORT).show();
                    //isFirstTime = false;
                    //DiscountDetailActivity.this.recreate();
                    setData();
                } else if (m_nearByStorePosition < SEncapsulationData.getInstance().getAlData().size() - 1) {
                    m_DiscountItemsPosition = 0;
                    m_nearByStorePosition++;
                    //isFirstTime = false;
                    //DiscountDetailActivity.this.recreate();
                    setData();
                }
            }

            if (R.id.back == item.getItemId()) {

                if (m_DiscountItemsPosition > 0) {
                    m_DiscountItemsPosition--;
                    //Toast.makeText(this,""+m_DiscountItemsPosition,Toast.LENGTH_SHORT).show();
                    //isFirstTime = false;
                    //DiscountDetailActivity.this.recreate();
                    setData();
                } else if (m_nearByStorePosition > 0) {
                    m_nearByStorePosition--;
                    m_DiscountItemsPosition = SEncapsulationData.getInstance().getAlData().get(m_nearByStorePosition).getJSONArray(SDefinedData.EVENT).length() - 1;
                    setData();
                    //isFirstTime = false;
                   // DiscountDetailActivity.this.recreate();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(m_nearByStorePosition == 0 && m_DiscountItemsPosition == 0){
            backMenuItem.setVisible(false);
        }

        try {
            if(m_DiscountItemsPosition == SEncapsulationData.getInstance().getAlData().get(m_nearByStorePosition).getJSONArray(SDefinedData.EVENT).length() - 1 &&
                    m_nearByStorePosition == SEncapsulationData.getInstance().getAlData().size() - 1){
                nextMenuItem.setVisible(false);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return super.onOptionsItemSelected(item);
    }
}



