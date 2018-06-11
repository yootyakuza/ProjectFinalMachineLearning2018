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
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
/**
 * Created by Sarayut on 20/5/2561.
 */
public class ActivityRegister extends AppCompatActivity {

    TextInputEditText txtUsername, txtPassword, txtConfirmpassword, txtEmail, txtAge;
    CircularProgressButton butRegister;
    private FirebaseAuth firebaseAuth;
    FirebaseDatabaseHandle databaseHandle;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        txtUsername = findViewById(R.id.TextRegisterUsername);
        txtPassword = findViewById(R.id.TextRegisterPassword);
        txtConfirmpassword = findViewById(R.id.TextRegisterConfirmPassword);
        txtEmail = findViewById(R.id.TextRegisterEmail);
        txtAge = findViewById(R.id.TextRegisterAge);
        butRegister = findViewById(R.id.butRegister2);
        context = this;
        databaseHandle = new FirebaseDatabaseHandle(context);
        firebaseAuth = FirebaseAuth.getInstance();

        butRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = txtUsername.getText().toString();
                String password = txtPassword.getText().toString();
                String confirm = txtConfirmpassword.getText().toString();
                final String email = txtEmail.getText().toString();
                final String age = txtAge.getText().toString();

                if (username.isEmpty()) {
                    txtUsername.setError("Please input username !!");
                    txtUsername.requestFocus();
                } else if (password.isEmpty()) {
                    txtPassword.setError("Please input password !!");
                    txtPassword.requestFocus();
                }else if(password.length() < 6){
                    txtPassword.setError("Passwords must be at least 6 characters or numbers. please try again !!");
                    txtPassword.requestFocus();
                } else if (confirm.isEmpty()) {
                    txtConfirmpassword.setError("Please confirm password !!");
                    txtConfirmpassword.requestFocus();
                } else if (email.isEmpty()) {
                    txtEmail.setError("Please input e-mail !!");
                    txtEmail.requestFocus();
                } else if (!EmailValidator(email)) {
                    txtEmail.setError("Please enter a valid email address !!");
                    txtEmail.requestFocus();
                } else if (age.isEmpty()) {
                    txtAge.setError("Please input age !!");
                    txtAge.requestFocus();
                }else if(!password.equals(confirm)) {
                    txtConfirmpassword.setError("Passwords do not match. please try again !!");
                    txtConfirmpassword.requestFocus();
                } else {
                    firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(ActivityRegister.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                //insert data to firebase
                                databaseHandle.CreateUserInFirebaseDatabase(username,email,Integer.valueOf(age));
                                AsyncTask<String, String, String> register = getAsyncTask();
                                butRegister.startAnimation();
                                register.execute();
                            }else{
                                Toast.makeText(context, "Could not register...Please try again !!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
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
                return "register";
            }

            @Override
            protected void onPostExecute(String s) {
                if (s.equals("register")) {
                    Toast.makeText(context, "Register successfully", Toast.LENGTH_SHORT).show();

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(context, MainActivity.class);
                            startActivity(intent);
                        }
                    }, 2000);
                    butRegister.doneLoadingAnimation(Color.parseColor("#e60000"), BitmapFactory.decodeResource(getResources(), R.drawable.icon_done));
                }
            }
        };
    }

    public boolean EmailValidator(String email) {
        Pattern pattern;//กลุ่มคำ
        Matcher matcher;//แมช
        final String email_pattern = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(email_pattern);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
