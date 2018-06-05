package com.example.qq.projectfinalmachinelearning2018;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import de.hdodenhof.circleimageview.CircleImageView;


public class Fragment_profile extends Fragment {

    private final String TAG = "YUT";
    private DatabaseReference databaseUser;
    private FirebaseDatabase database;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    UserManage userManage;
    TextView txtProName, txtName, txtEmail, txtAge, tvMsg;
    CircleImageView circleImageView;
    Context context;
    User uInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile,container,false);

        txtProName = view.findViewById(R.id.textViewName);
        txtName = view.findViewById(R.id.textName);
        txtEmail = view.findViewById(R.id.textEmail);
        txtAge = view.findViewById(R.id.textAge);
        context = getActivity();
        userManage = new UserManage(context);
        uInfo = new User();
        database = FirebaseDatabase.getInstance();
        databaseUser = database.getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        databaseUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    uInfo.set_username(ds.child(firebaseUser.getUid()).getValue(User.class).get_username());
                    uInfo.set_email(ds.child(firebaseUser.getUid()).getValue(User.class).get_email());
                    uInfo.set_age(ds.child(firebaseUser.getUid()).getValue(User.class).get_age());

                    txtProName.setText(uInfo.get_email());
                    txtName.setText(uInfo.get_username());
                    txtEmail.setText(uInfo.get_email());
                    txtAge.setText(String.valueOf(uInfo.get_age()));

                    Log.d(TAG, "Show data username: " + uInfo.get_username());
                    Log.d(TAG, "Show data email: " + uInfo.get_email());
                    Log.d(TAG, "Show data age: " + uInfo.get_age());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });
        txtName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog(uInfo.get_username(), 1);
            }
        });
        txtEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog(uInfo.get_email(), 2);
            }
        });
        txtAge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog(String.valueOf(uInfo.get_age()), 3);
            }
        });
        return view;
    }


    public void openDialog(String oldData, final int i) {
        final Dialog dialog = new Dialog(getActivity());
        String title = "", Text = "";
        if (i == 1) {
            title = "Change display name.";
            Text = "Rename";
        } else if (i == 2) {
            title = "Change display email.";
            Text = "New email";
        } else if (i == 3) {
            title = "Change display age.";
            Text = "New age";
        }
        dialog.setTitle(title);
        dialog.setContentView(R.layout.input_box_name);
        tvMsg = dialog.findViewById(R.id.tvMsg);
        tvMsg.setText(Text);
        final EditText etInput = dialog.findViewById(R.id.etInput);
        etInput.setText(oldData);
        TextView tvUpdate = dialog.findViewById(R.id.tvUpdate);
        TextView tvCancel = dialog.findViewById(R.id.tvCancel);

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        tvUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("Updating....");
                progressDialog.setMax(50);
                progressDialog.show();

//                int[] _id = new int[contacts.size()];
//                for (int i = pos; i < _id.length; i++) {
//                    _id[i] = contacts.get(i)._id;
//                }
//
//                final CustomAdapter adapter = new CustomAdapter(_id);
//                final int posID = adapter.id[pos];
//                Contact update = new Contact();
//
//                if (i == 1) {
//                    update._name = etInput.getText().toString();
//                    update._email = tvShowEmail.getText().toString();
//                    update._phone_number = tvShowPhone.getText().toString();
//                    update._id = posID;
//                    tvShowName.setText(etInput.getText().toString());
//                } else if (i == 2) {
//                    update._name = tvShowName.getText().toString();
//                    update._email = etInput.getText().toString();
//                    update._phone_number = tvShowPhone.getText().toString();
//                    update._id = posID;
//                    tvShowEmail.setText(etInput.getText().toString());
//                } else if (i == 3) {
//                    update._name = tvShowName.getText().toString();
//                    update._email = tvShowEmail.getText().toString();
//                    update._phone_number = etInput.getText().toString();
//                    update._id = posID;
//                    tvShowPhone.setText(etInput.getText().toString());
//                }

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getActivity().finish();
                        startActivity(getActivity().getIntent());
                    }
                }, 2000);

                //db.updateContact(update);
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
