package com.example.qq.projectfinalmachinelearning2018;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


public class Fragment_profile extends Fragment {

    private final String TAG = Fragment_profile.class.getSimpleName();
    ;
    private DatabaseReference databaseUser;
    private FirebaseDatabase database;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener AuthListener;
    private StorageReference storageRef;
    private UserManage userManage;
    private static int RESULT_LOAD_IMAGE = 1;
    private static int REQUEST_IMAGE_CAPTURE = 2;
    TextView txtProName, txtName, txtEmail, txtAge, tvMsg, tvAddPhoto, tvUpload, tvTakePhoto, tvCancel;
    CircleImageView circleImageView;
    Context context;
    private User uInfo;
    Uri file;
    String imgPath = "", uid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        txtProName = view.findViewById(R.id.textViewName);
        txtName = view.findViewById(R.id.textName);
        txtEmail = view.findViewById(R.id.textEmail);
        txtAge = view.findViewById(R.id.textAge);
        tvAddPhoto = view.findViewById(R.id.textAddPhoto);
        circleImageView = view.findViewById(R.id.img_HomeDetail);
        context = getActivity();
        uInfo = new User();
        userManage = new UserManage(context);

        database = FirebaseDatabase.getInstance();
        databaseUser = database.getReference("User");
        firebaseAuth = FirebaseAuth.getInstance();

        storageRef = FirebaseStorage.getInstance().getReference();

        AuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Boolean isSuccess = userManage.checkLoginValidate(user.getEmail());
                    if (isSuccess) {
                        ShowData(firebaseAuth);

                    } else {
                        Intent intent = new Intent(context, MainActivity.class);
                        startActivity(intent);
                    }
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // UserManage is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
        return view;
    }

    private void ShowData(@NonNull final FirebaseAuth firebaseAuth) {
        databaseUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    final FirebaseUser user = firebaseAuth.getCurrentUser();
                    uid = user.getUid();
                    if (user != null) {
                        if (ds.getKey().equals(uid)) {
                            uInfo = ds.getValue(User.class);
                            txtProName.setText(uInfo.get_email());
                            txtName.setText(uInfo.get_username());
                            txtEmail.setText(uInfo.get_email());
                            txtAge.setText(String.valueOf(uInfo.get_age()));
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        new LoadImage2(circleImageView).execute(uInfo.get_img());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).start();

                        }
                        Log.d(TAG, "Show data username: " + uInfo.get_username());
                        Log.d(TAG, "Show data email: " + uInfo.get_email());
                        Log.d(TAG, "Show data age: " + uInfo.get_age());
                    }
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
        tvAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogAddPhoto();
            }
        });
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

                if (i == 1) {
                    uInfo.set_username(etInput.getText().toString());
                    txtName.setText(etInput.getText().toString());
                    databaseUser.child(uid).child("_username").setValue(uInfo.get_username());
                } else if (i == 2) {
                    if (!EmailValidator(etInput.getText().toString())) {
                        txtEmail.setError("Please enter a valid email address !!");
                        txtEmail.requestFocus();
                    } else {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user != null) {
                            Boolean isSuccess = userManage.checkLoginValidate(user.getEmail());
                            user.updateEmail(etInput.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Boolean isSuccess = userManage.registerUser(etInput.getText().toString());
                                        if (isSuccess) {
                                            uInfo.set_email(etInput.getText().toString());
                                            databaseUser.child(uid).child("_email").setValue(uInfo.get_email());
                                            Log.d(TAG, "User email address updated");
                                        }
                                    }
                                }
                            });
                        } else {

                        }
                    }

                } else if (i == 3) {
                    uInfo.set_age(Integer.valueOf(etInput.getText().toString()));
                    txtAge.setText(etInput.getText().toString());
                    databaseUser.child(uid).child("_age").setValue(uInfo.get_age());
                }

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getActivity().finish();
                        startActivity(getActivity().getIntent());
                    }
                }, 2000);

                dialog.dismiss();
            }
        });
        dialog.show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        final CircleImageView viewPhoto = getView().findViewById(R.id.img_HomeDetail);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();

            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContext().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);

            //picturePath is the path to save in DB
            imgPath = cursor.getString(columnIndex);
            //upload photo to firebase storage
            uploadPhotoToFirebase(progressDialog, selectedImage);
            cursor.close();
            viewPhoto.setImageBitmap(BitmapFactory.decodeFile(imgPath));

        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            imgPath = file.getPath().toString();
            uploadPhotoToFirebase(progressDialog, file);
            viewPhoto.setImageBitmap(BitmapFactory.decodeFile(imgPath));
        }
    }

    private void uploadPhotoToFirebase(final ProgressDialog progressDialog, Uri selectedImage) {
        if (selectedImage != null) {
            StorageReference filepath = storageRef.child("Photos").child(selectedImage.getLastPathSegment());
            filepath.putFile(selectedImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                        }
                    }, 3000);
                    Toast.makeText(context, "Upload successfully", Toast.LENGTH_SHORT).show();
                    String upload = taskSnapshot.getDownloadUrl().toString();
                    databaseUser.child(uid).child("_img").setValue(upload);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage("Upload.... " + (int) progress + " %");
                    progressDialog.setProgress((int) progress);
                    progressDialog.show();
                }
            });
        } else {
            Toast.makeText(context, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

    public void openDialogAddPhoto() {

        final Dialog dialog = new Dialog(getActivity());
        dialog.setTitle("Profile pictures.");
        dialog.setContentView(R.layout.dialog_profile);
        tvUpload = dialog.findViewById(R.id.txtUploadImg);
        tvTakePhoto = dialog.findViewById(R.id.txtTakePhoto);
        tvCancel = dialog.findViewById(R.id.tvTakePhotoCancel);
        tvUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
                dialog.dismiss();
            }
        });
        tvTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                file = Uri.fromFile(getOutputMediaFile());
                intent.putExtra(MediaStore.EXTRA_OUTPUT, file);
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                dialog.dismiss();
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private static File getOutputMediaFile() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "ProjectAndroid2018");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_" + timeStamp + ".jpg");
    }

    public boolean EmailValidator(String email) {
        Pattern pattern;
        Matcher matcher;
        final String email_pattern = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(email_pattern);
        matcher = pattern.matcher(email);
        return matcher.matches();
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
