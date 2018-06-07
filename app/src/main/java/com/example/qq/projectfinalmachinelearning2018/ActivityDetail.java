package com.example.qq.projectfinalmachinelearning2018;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ActivityDetail extends AppCompatActivity {
    private final String TAG = ActivityDetail.class.getSimpleName();;
    private int pos = 0;
    private String home, away, imgHome, imgAway, time;
    private DatabaseReference databaseLastYearHome,databaseLastYearAway,databasePrediction;
    private FirebaseDatabase database;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener AuthListener;
    private FirebaseUser firebaseUser;
    Context context;
    TextView tvHWon, tvHDrawn, tvHLost,tvHAWon, tvHADrawn, tvHALost, tvAWon, tvADrawn, tvALost,tvAHWon, tvAHDrawn, tvAHLost, tvtime,homeName,awayName,result;
    ImageView imageViewH, imageViewA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        context = this;
        //home team
        tvHWon = findViewById(R.id.tvHWon);
        tvHDrawn = findViewById(R.id.tvHDrawn);
        tvHLost = findViewById(R.id.tvHLost);
        tvHAWon = findViewById(R.id.tvHAWon);
        tvHADrawn = findViewById(R.id.tvHADrawn);
        tvHALost = findViewById(R.id.tvHALost);
        //Away team
        tvAHWon = findViewById(R.id.tvAHWon);
        tvAHDrawn = findViewById(R.id.tvAHDrawn);
        tvAHLost = findViewById(R.id.tvAHLost);
        tvAWon = findViewById(R.id.tvAWon);
        tvADrawn = findViewById(R.id.tvADrawn);
        tvALost = findViewById(R.id.tvALost);

        //result
        homeName = findViewById(R.id.HomeName);
        awayName = findViewById(R.id.AwayName);
        result = findViewById(R.id.Result);

        tvtime = findViewById(R.id.tvTimeDetail);
        imageViewH = findViewById(R.id.img_HomeDetail);
        imageViewA = findViewById(R.id.img_AwayDetail);

        database = FirebaseDatabase.getInstance();
        databaseLastYearHome = database.getReference("LastYearHome");
        databaseLastYearAway = database.getReference("LastYearAway");
        databasePrediction = database.getReference("PredictionResult");
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        Intent intent = getIntent();
        pos = intent.getExtras().getInt("position");
        home = intent.getExtras().getString("homeTeamName");
        away = intent.getExtras().getString("awayTeamName");
        imgHome = intent.getExtras().getString("imgHome");
        imgAway = intent.getExtras().getString("imgAway");
        time = intent.getExtras().getString("date");

        new LoadImage(imageViewH).execute(imgHome);
        new LoadImage(imageViewA).execute(imgAway);
        tvtime.setText(time);

        databaseLastYearHome.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.child("Team").getValue().equals(home)) {
                        String won = String.valueOf(ds.child("Won").getValue());
                        String drawn = String.valueOf(ds.child("Drawn").getValue());
                        String lost = String.valueOf(ds.child("Lost").getValue());
                        tvHWon.setText("W: "+ won);
                        tvHDrawn.setText("D: " + drawn);
                        tvHLost.setText("L: "+ lost);

                    } else if (ds.child("Team").getValue().equals(away)) {
                        String won = String.valueOf(ds.child("Won").getValue());
                        String drawn = String.valueOf(ds.child("Drawn").getValue());
                        String lost = String.valueOf(ds.child("Lost").getValue());
                        tvAHWon.setText("W: "+ won);
                        tvAHDrawn.setText("D: " + drawn);
                        tvAHLost.setText("L: "+ lost);
                    }
                    Log.d(TAG, "Show data: " + ds.getValue());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });
        databaseLastYearAway.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.child("Team").getValue().equals(home)) {
                        String won = String.valueOf(ds.child("Won").getValue());
                        String drawn = String.valueOf(ds.child("Drawn").getValue());
                        String lost = String.valueOf(ds.child("Lost").getValue());
                        tvHAWon.setText("W: "+ won);
                        tvHADrawn.setText("D: " + drawn);
                        tvHALost.setText("L: "+ lost);

                    } else if (ds.child("Team").getValue().equals(away)) {
                        String won = String.valueOf(ds.child("Won").getValue());
                        String drawn = String.valueOf(ds.child("Drawn").getValue());
                        String lost = String.valueOf(ds.child("Lost").getValue());
                        tvAWon.setText("W: "+ won);
                        tvADrawn.setText("D: " + drawn);
                        tvALost.setText("L: "+ lost);
                    }
                    Log.d(TAG, "Show data: " + ds.getValue());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });

        databasePrediction.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.child("HomeTeam").getValue().equals(home) || ds.child("AwayTeam").equals(away)) {
                        String resultTeam = String.valueOf(ds.child("FTR").getValue());
                        homeName.setText(home);
                        awayName.setText(away);
                        if(resultTeam.equals("H")){
                            homeName.setTextColor(Color.parseColor("#009933"));
                            awayName.setTextColor(Color.parseColor("#e60000"));
                            result.setText("Home Team Win");
                            homeName.setTypeface(null, Typeface.BOLD);
                            result.setTextColor(Color.parseColor("#009933"));
                        }else if(resultTeam.equals("D")){
                            homeName.setTextColor(Color.parseColor("#ff9933"));
                            awayName.setTextColor(Color.parseColor("#ff9933"));
                            result.setText("Drawn");
                            result.setTextColor(Color.parseColor("#ff9933"));
                        }
                        else if(resultTeam.equals("A")){
                            homeName.setTextColor(Color.parseColor("#e60000"));
                            awayName.setTextColor(Color.parseColor("#009933"));
                            result.setText("Away Team Win");
                            awayName.setTypeface(null, Typeface.BOLD);
                            result.setTextColor(Color.parseColor("#009933"));
                        }
                    }
                    Log.d(TAG, "Show data: " + ds.getValue());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });
    }
}
