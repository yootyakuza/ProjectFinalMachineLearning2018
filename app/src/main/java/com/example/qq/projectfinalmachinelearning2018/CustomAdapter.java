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
 * Created by Sarayut on 31/5/2561.
 */
public class CustomAdapter extends BaseAdapter{
    Context context;
    String[] homeTeamName;
    String[] awayTeamName;
    String[] date;
    String[] goalsHomeTeam;
    String[] goalsAwayTeam;
    String[] imgHome;
    String[] imgAway;
    int[] id;

    public CustomAdapter(Context context, String[] homeTeamName, String[] awayTeamName, String[] date, String[] goalsHomeTeam, String[] goalsAwayTeam, String[] imgHome, String[] imgAway) {
        this.context = context;
        this.homeTeamName = homeTeamName;
        this.awayTeamName = awayTeamName;
        this.date = date;
        this.goalsHomeTeam = goalsHomeTeam;
        this.goalsAwayTeam = goalsAwayTeam;
        this.imgHome = imgHome;
        this.imgAway = imgAway;
    }

    public CustomAdapter(int[] id) {
        this.id = id;
    }

    @Override
    public int getCount() {
        return homeTeamName.length;
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

        if(convertView == null)
            convertView = inflater.inflate(R.layout.item_detail,parent,false);

        TextView homeName = convertView.findViewById(R.id.homeTeamName);
        TextView awayName = convertView.findViewById(R.id.awayTeamName);
        TextView date1 = convertView.findViewById(R.id.Time);
        TextView goalsHome = convertView.findViewById(R.id.goalsHome);
        TextView goalsAway = convertView.findViewById(R.id.goalsAway);
        ImageView imageHome = convertView.findViewById(R.id.imgHome);
        ImageView imageAway = convertView.findViewById(R.id.imgAway);

        homeName.setText(homeTeamName[position]);
        awayName.setText(awayTeamName[position]);
        date1.setText(date[position]);
        goalsHome.setText(goalsHomeTeam[position]);
        goalsAway.setText(goalsAwayTeam[position]);
        new LoadImage(imageHome).execute(imgHome[position]);
        new LoadImage(imageAway).execute(imgAway[position]);

        return convertView;
    }
}
