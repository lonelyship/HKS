package com.systex.hkspractice;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import org.json.JSONException;

public class DiscountItemsActivity extends ActionBarActivity {

    protected Context m_context  = null;
    protected SDiscountItemsListViewAdapter m_pageThreeAdapter = null;
    protected ListView m_lvPageThree = null;
    protected TextView m_tvPageThree = null;
    protected int m_discountItemsPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_discount_items);

        init();

        m_discountItemsPosition = getIntent().getIntExtra(SDefinedData.NEARBYSTOREPOSITION, 0);

        try {
            m_tvPageThree.setText(SEncapsulationData.getInstance().getAlData().get(m_discountItemsPosition).getString(SDefinedData.NAME));
            this.setTitle(SEncapsulationData.getInstance().getAlData().get(m_discountItemsPosition).getString(SDefinedData.NAME));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        m_pageThreeAdapter = new SDiscountItemsListViewAdapter(this, m_discountItemsPosition);
        m_lvPageThree.setAdapter(m_pageThreeAdapter);

        m_lvPageThree.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent it;
                it = new Intent(DiscountItemsActivity.this, DiscountDetailActivity.class);
                it.putExtra(SDefinedData.NEARBYSTOREPOSITION, m_discountItemsPosition);
                it.putExtra(SDefinedData.DISCOUNTITEMSPOSITION, position);
                startActivity(it);
            }
        });
    }

    public void init(){
        m_context = this;
        m_lvPageThree = (ListView) findViewById(R.id.listView2);
        m_tvPageThree = (TextView) findViewById(R.id.tvPageThreeTitle);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
