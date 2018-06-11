package com.example.qq.projectfinalmachinelearning2018;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

/**
 * Created by Sarayut on 20/5/2561.
 */
public class Activity_login extends AppCompatActivity {

    TextInputEditText txtEmail, txtPassword;
    Button butRegister;
    CircularProgressButton butLogin;
    private FirebaseAuth firebaseAuth;
    private UserManage userManage;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtEmail = findViewById(R.id.TextEmail);
        txtPassword = findViewById(R.id.TextPassword);
        butLogin = findViewById(R.id.butLogin);
        butRegister = findViewById(R.id.butRegister);
        firebaseAuth = FirebaseAuth.getInstance();
        context = this;
        userManage = new UserManage(context);

        butLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final String email = txtEmail.getText().toString();
                String password = txtPassword.getText().toString();

                if (email.isEmpty()) {
                    txtEmail.setError("Please input E-mail !!");
                    txtEmail.requestFocus();
                } else if (password.isEmpty()) {
                    txtPassword.setError("Please input password !!");
                    txtPassword.requestFocus();
                } else {

                    firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(Activity_login.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Boolean isSuccess = userManage.registerUser(email);
                            if (isSuccess) {
                                if (task.isSuccessful()) {
                                    AsyncTask<String, String, String> register = getAsyncTask();
                                    butLogin.startAnimation();
                                    register.execute();
                                } else {
                                    Toast.makeText(context, "Login failed !!!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });

                }
            }
        });
        butRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ActivityRegister.class);
                startActivity(intent);
            }
        });
    }

    @NonNull
    private AsyncTask<String, String, String> getAsyncTask() {
        return new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... strings) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return "login";
            }

            @Override
            protected void onPostExecute(String s) {
                if (s.equals("login")) {
                    Toast.makeText(context, "Login successfully", Toast.LENGTH_SHORT).show();

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                            Intent intent = new Intent(context, ActivityNavigator.class);
                            startActivity(intent);
                            finish();
                        }
                    }, 2000);
                    butLogin.doneLoadingAnimation(Color.parseColor("#a6a6a6"), BitmapFactory.decodeResource(getResources(), R.drawable.icon_done));
                }
            }
        };
    }
}
