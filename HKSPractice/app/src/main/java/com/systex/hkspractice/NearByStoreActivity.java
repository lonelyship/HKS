package com.systex.hkspractice;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class NearByStoreActivity extends ActionBarActivity {

    protected String m_strSearch = null;
    protected ArrayList<JSONObject> m_alData = new ArrayList<>();
    protected ArrayList<JSONObject> m_alDataSearch = new ArrayList<>();
    protected ArrayList<Integer> m_alDataSearchIndex = new ArrayList<>();
    protected int m_iMode = 0;
    protected String m_strMode = null;
    protected ProgressDialog m_progressDialog = null;
    protected int m_iSearchNum = 0;
    protected ListView m_lvNearByStore = null;
    protected ImageSwitcher m_imgSwitcher = null;
    protected Boolean m_bShowImg1 = true;
    protected Boolean m_bReadyLoading = true;
    protected Boolean m_bNoMoreData = false;
    protected Boolean m_bIsSearchPosition=false;
    protected SNearByStoreListViewAdapter m_nearByStoreAdapter = null;
    protected SNearByStoreSearchListViewAdapter m_nearByStoreSearchAdapter = null;
    protected Context m_context = null;
    protected ImageView m_ivSearchIcon = null;
    protected Double m_dLat = SDefinedData.LAT;
    protected Double m_dLon = SDefinedData.LON;
    protected android.support.v7.app.ActionBar m_actionBar = null;
    protected HandlerThread m_thread = null;
    protected  int m_iIndex = 0;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            m_nearByStoreAdapter.notifyDataSetChanged();
            m_bReadyLoading = true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_near_by_store);

        init();

        SEncapsulationData.getInstance().getAlData().clear();
        SEncapsulationData.getInstance().setStrSearch("");

        m_iMode = getIntent().getIntExtra(SDefinedData.MODE, 1);
        if (m_iMode == SDefinedData.NUMONE) {
            m_strMode = SDefinedData.MODE01;
        } else {
            m_strMode = SDefinedData.MODE07;
        }

        Toast.makeText(getApplicationContext(), R.string.loading, Toast.LENGTH_LONG).show();
        m_progressDialog = ProgressDialog.show(m_context, "", getString(R.string.loadingDataPleaseWait), true);
        m_progressDialog.setCancelable(true);

        getData(m_context, m_iIndex, m_dLat, m_dLon, handler, m_strMode);


        m_imgSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                return new ImageView(NearByStoreActivity.this);
            }
        });
        m_imgSwitcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_bShowImg1 = !m_bShowImg1;
                showCurrentImage();
            }
        });
        m_imgSwitcher.setInAnimation(AnimationUtils.loadAnimation(NearByStoreActivity.this, android.R.anim.fade_in));
        m_imgSwitcher.setOutAnimation(AnimationUtils.loadAnimation(NearByStoreActivity.this, android.R.anim.fade_out));
        showCurrentImage();

        m_nearByStoreAdapter = new SNearByStoreListViewAdapter(this);
        m_lvNearByStore.setAdapter(m_nearByStoreAdapter);

        m_lvNearByStore.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if ((firstVisibleItem + visibleItemCount) == (totalItemCount - 20) && m_bNoMoreData == false) {
                    if (m_bReadyLoading) {
                        m_bReadyLoading = false;
                        m_iIndex += 40;
                        getData(m_context, m_iIndex, m_dLat, m_dLon, handler, m_strMode);
                        Toast.makeText(getApplicationContext(), R.string.loading, Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        m_lvNearByStore.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent it;
                try {
                    //先判斷進來的position為搜尋的或一般的
                    if(m_bIsSearchPosition == true) {
                        position=m_alDataSearchIndex.get(position);
                        //Toast.makeText(m_context,position+"",Toast.LENGTH_SHORT).show();
                    }
                        if (1 == m_alData.get(position).getJSONArray(SDefinedData.EVENT).length()) {
                            it = new Intent(NearByStoreActivity.this, DiscountDetailActivity.class);
                            it.putExtra(SDefinedData.NEARBYSTOREPOSITION, position);
                            it.putExtra(SDefinedData.DISCOUNTITEMSPOSITION, 0);
                            startActivity(it);
                        } else {
                            it = new Intent(NearByStoreActivity.this, DiscountItemsActivity.class);
                            it.putExtra(SDefinedData.NEARBYSTOREPOSITION, position);
                            startActivity(it);
                        }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void init() {
        m_context = this;
        m_strSearch = "";
        m_actionBar = getSupportActionBar();
        //m_actionBar.setBackgroundDrawable(new ColorDrawable(Color.RED));
        m_dLat = SEncapsulationData.getInstance().getDLat();
        m_dLon = SEncapsulationData.getInstance().getDlon();
        m_imgSwitcher = (ImageSwitcher) findViewById(R.id.imageSwitcher2);
        m_lvNearByStore = (ListView) findViewById(R.id.listView);
    }

    private void showCurrentImage() {
        if (m_bShowImg1) {
            m_imgSwitcher.setImageResource(R.drawable.img1);
        } else {
            m_imgSwitcher.setImageResource(R.drawable.img2);
        }
    }

    public void getData(final Context c, final int index, double lat, double lon, final Handler h, String mode) {
        m_thread = new HandlerThread("thread");
        m_thread.start();
        final Handler handler = new Handler(m_thread.getLooper());
        final String URL_01 = "http://test1.hokhang.com/hksCloudService/getEventService.php?appId=119871&dataGroupCode=" + mode + "&primaryCategory=null&secondaryCategory=null&distance=1000&index=" + index + "&limit=40&lat=" + lat + "&lon=" + lon;
        //"http://test1.hokhang.com/hksCloudService/getEventService.php?appId=119871&dataGroupCode=00&primaryCategory=null&secondaryCategory=null&distance=1000&index=0&limit=20&lat=24.159816&lon=120.648203";
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    //Log.i("url", URL_01);
                    HttpPost post = new HttpPost(URL_01);
                    HttpResponse response = new DefaultHttpClient().execute(post);
                    StringBuilder stringBuffer = new StringBuilder();

                    if (response.getStatusLine().getStatusCode() == 200) {
                        HttpEntity entity = response.getEntity();
                        if (entity.getContentLength() > Integer.MAX_VALUE) {
                            Log.i("TOO LARGE", "" + entity.getContentLength());
                        }

                        InputStream is = entity.getContent();
                        BufferedReader br = new BufferedReader(new InputStreamReader(is, HTTP.UTF_8), 8);
                        String temp;
                        while ((temp = br.readLine()) != null) {
                            stringBuffer.append(temp);
                        }
                        Log.i("ALL DATA", stringBuffer.toString());

                        is.close();
                        br.close();
                        String json = stringBuffer.toString();

                        try {
                            JSONObject jsonObj = new JSONObject(json);
                            JSONArray jsonArray = jsonObj.getJSONArray(SDefinedData.BRANCH);
                            JSONObject obj;

                            for (int i = 0; i < jsonArray.length(); i++) {
                                obj = jsonArray.getJSONObject(i);
                                m_alData.add(obj);
                            }

                            SEncapsulationData.getInstance().setAlData(m_alData);

                            if (jsonArray.length() < 40) {
                                m_bNoMoreData=true;
                            }

                            Toast.makeText(c, R.string.loadingFinished, Toast.LENGTH_SHORT).show();
                            m_progressDialog.cancel();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            if (m_alData.size() == 0) {
                                Toast.makeText(c, R.string.noDataNearby, Toast.LENGTH_SHORT).show();
                                m_progressDialog.cancel();
                                NearByStoreActivity.this.finish();
                            } else {
                                Toast.makeText(c, R.string.noMoreData, Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        Toast.makeText(c, "Not Connect OK", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                h.sendMessage(handler.obtainMessage());
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.menu_near_by_store, menu);

        m_actionBar.setCustomView(R.layout.home_search);
        final EditText search = (EditText) m_actionBar.getCustomView().findViewById(R.id.txt_search);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Toast.makeText(getBaseContext(), "Search : " + s, Toast.LENGTH_SHORT).show();
                m_strSearch = s.toString();
                SEncapsulationData.getInstance().setStrSearch(m_strSearch);
                m_nearByStoreAdapter.notifyDataSetChanged();

                if(TextUtils.isEmpty(m_strSearch) == true){
                    m_bIsSearchPosition=false;
                    m_lvNearByStore.setAdapter(m_nearByStoreAdapter);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        m_ivSearchIcon = (ImageView) m_actionBar.getCustomView().findViewById(R.id.ivSearchIcon);
        m_ivSearchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_iSearchNum = 0;
                m_alDataSearch.clear();
                m_alDataSearchIndex.clear();
                m_bIsSearchPosition=true;

                for (int i = 0; i < m_alData.size(); i++) {
                    try {
                        if (m_alData.get(i).getString(SDefinedData.NAME).toUpperCase().contains(m_strSearch.toUpperCase())) {
                            m_iSearchNum++;
                            m_alDataSearch.add(m_alData.get(i));
                            m_alDataSearchIndex.add(i);
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

               // Log.i("@@@@@",m_alDataSearchIndex.toString());

                if(TextUtils.isEmpty(m_strSearch) == true) {
                    Toast.makeText(getBaseContext(), R.string.typeKeyWords, Toast.LENGTH_SHORT).show();
                    m_bIsSearchPosition = false;
                } else if (0 == m_iSearchNum) {
                    Toast.makeText(getBaseContext(), R.string.findNoData, Toast.LENGTH_SHORT).show();
                    m_nearByStoreSearchAdapter = new SNearByStoreSearchListViewAdapter(m_context,m_alDataSearch);
                    m_lvNearByStore.setAdapter(m_nearByStoreSearchAdapter);
                    m_bIsSearchPosition = false;
                } else {
                    Toast.makeText(getBaseContext(), getString(R.string.totalFind) + m_iSearchNum + getString(R.string.item), Toast.LENGTH_SHORT).show();
                    m_nearByStoreSearchAdapter = new SNearByStoreSearchListViewAdapter(m_context,m_alDataSearch);
                    m_lvNearByStore.setAdapter(m_nearByStoreSearchAdapter);
                }
            }
        });

        m_actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
                | ActionBar.DISPLAY_SHOW_HOME);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        m_thread.quit();
        //SEncapsulationData.getInstance().getAlData().clear();
        //m_nearByStoreAdapter.notifyDataSetChanged();
       // m_iIndex = 0;
        finish();
    }
}
