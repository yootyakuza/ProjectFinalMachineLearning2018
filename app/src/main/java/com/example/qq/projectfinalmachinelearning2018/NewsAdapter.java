package com.example.qq.projectfinalmachinelearning2018;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Sarayut on 7/6/2561.
 */
public class NewsAdapter extends BaseAdapter {
    Context context;
    String[] title;
    String[] description;
    String[] image;
    String[] published;
    int[] id;

    public NewsAdapter(Context context, String[] title, String[] description, String[] image, String[] published) {
        this.context = context;
        this.title = title;
        this.description = description;
        this.image = image;
        this.published = published;
    }

    public NewsAdapter(int[] id) {
        this.id = id;
    }

    @Override
    public int getCount() {
        return title.length;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null)
            convertView = inflater.inflate(R.layout.item_news, parent, false);

        TextView titleNews = convertView.findViewById(R.id.tvTitle_News);
        TextView descriptionNews = convertView.findViewById(R.id.tvDes_News);
        TextView publishedNews = convertView.findViewById(R.id.tvPub_News);
        ImageView imageNews = convertView.findViewById(R.id.imageViewNews);

        titleNews.setText(title[position]);
        descriptionNews.setText(description[position]);
        publishedNews.setText(published[position]);
        new LoadImage(imageNews).execute(image[position]);

        return convertView;
    }
}
