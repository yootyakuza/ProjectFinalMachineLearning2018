package com.example.qq.projectfinalmachinelearning2018;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Sarayut on 10/6/2561.
 */
public class CustomSelectComAdapter extends BaseAdapter {
    Context context;
    String[] strName;

    public CustomSelectComAdapter(Context context, String[] strName) {
        this.context = context;
        this.strName = strName;
    }

    @Override
    public int getCount() {
        return strName.length;
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
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.item_select, parent, false);
        TextView selectTeam = convertView.findViewById(R.id.tvSelectTeamName);
        selectTeam.setText(strName[position]);
        return convertView;
    }
}
