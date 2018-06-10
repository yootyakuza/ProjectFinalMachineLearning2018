package com.example.qq.projectfinalmachinelearning2018;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Sarayut on 31/5/2561.
*/
 public class LoadImage2 extends AsyncTask<String, Integer, Bitmap> {
    private CircleImageView _imgv = null;


    public LoadImage2(CircleImageView _imgv) {
        this._imgv = _imgv;
    }

    @Override
    protected Bitmap doInBackground(String... param) {
        Bitmap bitmap = null;
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        try {
            URL url = new URL(param[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = connection.getInputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            int sum = 0;
            while ((len = inputStream.read(buffer)) > 0) {
                byteBuffer.write(buffer, 0, len);
                sum += len;
            }
            bitmap = BitmapFactory.decodeByteArray(byteBuffer.toByteArray(), 0, byteBuffer.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }


    @Override
    protected void onProgressUpdate(Integer... progress) {
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        _imgv.setImageBitmap(result);
    }
}

