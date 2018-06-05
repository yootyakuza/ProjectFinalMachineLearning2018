package com.example.qq.projectfinalmachinelearning2018;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class ActivityDetail extends AppCompatActivity {
    private int pos = 0;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        context = this;
        Intent intent = getIntent();
        pos = intent.getExtras().getInt("position");

        Toast.makeText(context,"Position receive: " + pos,Toast.LENGTH_SHORT).show();
    }
}
