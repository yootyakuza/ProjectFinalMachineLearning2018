package com.example.qq.projectfinalmachinelearning2018;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    TextInputEditText txtUsername, txtPassword;
    Button butLogin,butRegister;
    BottomNavigationView NavigationView;
    Context context;
    private UserManage userManage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NavigationView = findViewById(R.id.navigation);
        context =this;
        userManage = new UserManage(context);
        loadFragment(new Fragment_match());

        NavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectFragment = null;
                switch (item.getItemId()){
                    case R.id.action1_football_match:
                        selectFragment = new Fragment_match();
                        break;
                    case R.id.action2_football_schedule:
                        selectFragment = new Fragment_schedule();
                        break;
                    case R.id.action3_additional:
                        selectFragment = new Fragment_other();
                        break;
                }
                return loadFragment(selectFragment);
            }
        });
    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        userManage.removeKey();
        finish();
    }
}

