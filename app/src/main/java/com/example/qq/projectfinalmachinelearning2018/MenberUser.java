package com.example.qq.projectfinalmachinelearning2018;

import android.content.Context;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Sarayut on 23/5/2561.
 */
public class MenberUser {

    private DatabaseReference databaseUser;
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth firebaseAuth;
    Context _con;

    public MenberUser(Context context) {
        firebaseAuth = FirebaseAuth.getInstance();
        _con = context;
    }

    public void CreateUserInFirebaseDatabase(String username, String email, int age) {

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        User user = new User();
        user._username = username;
        user._email = email;
        user._age = age;
        databaseUser = database.getReference("User");
        databaseUser.child(firebaseUser.getUid()).setValue(user);
    }
}
