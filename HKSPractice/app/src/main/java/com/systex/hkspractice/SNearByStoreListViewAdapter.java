package com.systex.hkspractice;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;

public class SNearByStoreListViewAdapter extends BaseAdapter {
    protected Context m_context = null;
    LayoutInflater inflater = null;

    SNearByStoreListViewAdapter(Context context) {
        this.m_context = context;
        inflater = ((Activity) m_context).getLayoutInflater();
    }

    @Override
    public int getCount() {
        return SEncapsulationData.getInstance().getAlData().size();
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
        Boolean isSearched = false;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.nearby_store_listview_row, null, false);
            holder.ivPageTwo = (ImageView) convertView.findViewById(R.id.ivPageTwo);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            holder.tvContent = (TextView) convertView.findViewById(R.id.tvContent);
            holder.tvDistance = (TextView) convertView.findViewById(R.id.tvDistance);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        try {
            holder.tvTitle.setText(SEncapsulationData.getInstance().getAlData().get(position).getString(SDefinedData.NAME));
            if(SEncapsulationData.getInstance().getAlData().get(position).getJSONArray(SDefinedData.EVENT).length()>1) {
                holder.tvContent.setText(SEncapsulationData.getInstance().getAlData().get(position).getJSONArray(SDefinedData.EVENT).getJSONObject(0).getString(SDefinedData.TITLE)+" | "+
                                         SEncapsulationData.getInstance().getAlData().get(position).getJSONArray(SDefinedData.EVENT).getJSONObject(1).getString(SDefinedData.TITLE));

            } else{
                holder.tvContent.setText(SEncapsulationData.getInstance().getAlData().get(position).getJSONArray(SDefinedData.EVENT).getJSONObject(0).getString(SDefinedData.TITLE));
            }
            holder.tvDistance.setText(SEncapsulationData.getInstance().getAlData().get(position).getString(SDefinedData.DISTANCE) + "KM");

            if (SEncapsulationData.getInstance().getStrSearch() != null && SEncapsulationData.getInstance().getStrSearch().compareTo("") != 0) {
                if ((SEncapsulationData.getInstance().getAlData().get(position).getString(SDefinedData.NAME).toUpperCase()).contains(SEncapsulationData.getInstance().getStrSearch().toUpperCase())) {
                    isSearched = true;
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (isSearched) {
            convertView.setBackgroundColor(0xFFDDFF77);
        } else {
            convertView.setBackgroundColor(Color.WHITE);
        }
        return convertView;
    }

    class ViewHolder {
        ImageView ivPageTwo;
        TextView tvTitle;
        TextView tvContent;
        TextView tvDistance;
    }
}
