package com.example.thegumza.oilprice;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by TheGuMZa on 10/20/2015.
 */
public class OilListAdapter extends BaseAdapter {
    List<Oil> oil;
    Context context;

    public OilListAdapter(List<Oil> oil, Context context) {
        this.oil = oil;
        this.context = context;
    }

    @Override
    public int getCount() {
        return oil.size();
    }

    @Override
    public Object getItem(int position) {
        return oil.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.oil_item, parent, false);
            holder = new ViewHolder();
            holder.productName = (TextView)convertView.findViewById(R.id.productName);
            holder.price = (TextView)convertView.findViewById(R.id.price);

            holder.productName.setTypeface(Typeface.createFromAsset(context.getAssets(), "ZoodHardSell2.ttf"));
            holder.price.setTypeface(Typeface.createFromAsset(context.getAssets(), "ZoodHardSell2.ttf"));
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Oil oils = oil.get(position);
        holder.productName.setText(oils.getName());
        holder.price.setText(oils.getPrice() + " บาท");

        return convertView;
    }

    private class ViewHolder{
        TextView productName;
        TextView price;
    }
}
