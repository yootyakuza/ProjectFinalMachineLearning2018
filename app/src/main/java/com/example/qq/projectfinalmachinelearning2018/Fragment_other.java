package com.example.qq.projectfinalmachinelearning2018;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by Sarayut on 13/5/2561.
 */
public class Fragment_other extends android.app.Fragment {
    private final String TAG = Fragment_other.class.getSimpleName();
    ;
    CircleImageView imageView;
    LinearLayout layoutLogin, layoutProfile;
    Button butLogin;
    TextView txtnoti, txtProfile, txtNews;
    private DatabaseReference databaseUser;
    private FirebaseDatabase database;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener AuthListener;
    private UserManage userManage;
    Context context;
    private User uInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_other, container, false);
        imageView = view.findViewById(R.id.photoProfile);
        txtProfile = view.findViewById(R.id.txtProfile);
        txtnoti = view.findViewById(R.id.txtnoti);
        txtNews = view.findViewById(R.id.txtNews);
        butLogin = view.findViewById(R.id.butLogin);
        layoutLogin = view.findViewById(R.id.layoutlogin);
        layoutProfile = view.findViewById(R.id.layoutprofile);
        context = getActivity();
        userManage = new UserManage(context);

        database = FirebaseDatabase.getInstance();
        databaseUser = database.getReference("User");
        firebaseAuth = FirebaseAuth.getInstance();

        uInfo = new User();

        layoutLogin.setVisibility(View.VISIBLE);
        layoutProfile.setVisibility(View.GONE);

        AuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                    Boolean isSuccess = userManage.checkLoginValidate(user.getEmail());
                    if (isSuccess) {
                        layoutLogin.setVisibility(View.GONE);
                        layoutProfile.setVisibility(View.VISIBLE);
                        getData(user.getUid());
                    } else {
                        layoutLogin.setVisibility(View.VISIBLE);
                        layoutProfile.setVisibility(View.GONE);
                    }
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // UserManage is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

        butLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Activity_login.class);
                startActivity(intent);
            }
        });
        layoutProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ActivityNavigator.class);
                startActivity(intent);
            }
        });
        txtnoti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        txtNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ActivityNews.class);
                startActivity(intent);
            }
        });
        return view;
    }

    private void getData(final String id) {
        databaseUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.getKey().equals(id)) {
                        uInfo = ds.getValue(User.class);
                        txtProfile.setText(uInfo.get_email());
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    new LoadImage2(imageView).execute(uInfo.get_img());
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }
                    Log.d(TAG, "Show data email: " + uInfo.get_email());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(AuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (AuthListener != null) {
            firebaseAuth.removeAuthStateListener(AuthListener);
        }
    }
}
