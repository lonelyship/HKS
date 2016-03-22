package com.systex.hkspractice;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.JSONException;


public class SDiscountItemsListViewAdapter extends BaseAdapter {
    private Context m_context = null;
    private int m_position = 0;

    SDiscountItemsListViewAdapter(Context context, int position) {
        this.m_context = context;
        this.m_position = position;
    }

    @Override
    public int getCount() {
        try {
            return SEncapsulationData.getInstance().getAlData().get(m_position).getJSONArray(SDefinedData.EVENT).length();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = ((Activity) m_context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.discount_items_listview_row, null, false);
            holder.tv1 = (TextView) convertView.findViewById(R.id.textViewPageThree);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        try {
            String strEventTitle = SEncapsulationData.getInstance().getAlData().get(m_position).getJSONArray(SDefinedData.EVENT).getJSONObject(position).getString(SDefinedData.TITLE);
            holder.tv1.setText(strEventTitle);
            holder.tv1.setTextSize(20);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        convertView.setBackgroundColor(Color.WHITE);
        return convertView;
    }

    class ViewHolder {
        TextView tv1;
    }
}
