package com.example.qq.projectfinalmachinelearning2018;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;

public class ActivitySelectCompetition extends AppCompatActivity {
    public static final String TAG = "NotifyMessage";
    ListView listViewSelect;
    Context context;
    CustomSelectComAdapter adapter;
    TextView tvNotiCancel;
    Button butSub, butUnsub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_competition);

        listViewSelect = findViewById(R.id.listviewSelectTeam);
        context = this;

        final String[] listTeam = {"Liverpool", "Burnley", "CrystalPalace", "Everton", "ManCity", "Southampton", "Arsenal", "Bournemouth", "Chelsea", "ManUnited", "Stoke", "Swansea", "Tottenham", "Watford", "WestBrom", "WestHam", "Brighton", "Newcastle", "Huddersfield", "Leicester"};

        adapter = new CustomSelectComAdapter(context, listTeam);
        listViewSelect.setAdapter(adapter);
        listViewSelect.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openDialog(listTeam, position);
            }
        });
    }

    public void openDialog(final String[] listTeam, final int position) {
        final Dialog dialog = new Dialog(this);
        dialog.setTitle("Setting notification");
        dialog.setContentView(R.layout.dialog_notification);
        tvNotiCancel = dialog.findViewById(R.id.tvNotiCancel);
        butSub = dialog.findViewById(R.id.butSub);
        butUnsub = dialog.findViewById(R.id.butUnsub);

        tvNotiCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        butSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog = new ProgressDialog(ActivitySelectCompetition.this);
                progressDialog.setMessage("Subscribe....");
                progressDialog.setMax(50);
                progressDialog.show();

                String topic = listTeam[position];
                FirebaseMessaging.getInstance().subscribeToTopic(topic);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                        startActivity(getIntent());
                    }
                }, 2000);
                Toast.makeText(context, "Topic Subscribed: " + topic, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        butUnsub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog = new ProgressDialog(ActivitySelectCompetition.this);
                progressDialog.setMessage("Unsubscribe....");
                progressDialog.setMax(50);
                progressDialog.show();

                String topic = listTeam[position];
                FirebaseMessaging.getInstance().unsubscribeFromTopic(topic);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                        startActivity(getIntent());
                    }
                }, 2000);
                Toast.makeText(context, "Topic Unsubscribe From: " + topic, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
