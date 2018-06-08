package com.example.qq.projectfinalmachinelearning2018;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ActivityNews extends AppCompatActivity {

    private String TAG = ActivityNews.class.getSimpleName();
    ListView listView;
    ArrayList<HashMap<String, String>> NewsList;
    ProgressDialog progressDialog;
    NewsAdapter newsAdapter;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        NewsList = new ArrayList<>();
        listView = findViewById(R.id.listviewNews);
        context = this;

        new GetNews().execute();

    }

    private class GetNews extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Loading....");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler sh = new HttpHandler();
            String url = "https://www.footballwebpages.co.uk/news.json?comp=1&max=10";
            //String url = "https://api.myjson.com/bins/koxfu";
            String jsonStr = sh.makeServiceCall(url);
            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    JSONObject getNews = jsonObject.getJSONObject("news");
                    JSONArray jsonItem = getNews.getJSONArray("item");
                    //looping through All Contacts
                    for (int i = 0; i < jsonItem.length(); i++) {
                        JSONObject c = jsonItem.getJSONObject(i);
                        String title = c.getString("title");
                        String description = c.getString("description");
                        String image = c.getString("image");
                        String published = c.getString("published");
                        String link = c.getString("link");

                        HashMap<String, String> news = new HashMap<>();
                        news.put("title", title);
                        news.put("description", description);
                        news.put("image", image);
                        news.put("published", published);
                        news.put("link", link);
                        NewsList.add(news);
                    }

                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    ActivityNews.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Toast.makeText(context, "Json parsing error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } else

            {
                Log.e(TAG, "Couldn't get json from server.");
                ActivityNews.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context,
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
                String[] title = new String[NewsList.size()];
                String[] description = new String[NewsList.size()];
                String[] image = new String[NewsList.size()];
                String[] published = new String[NewsList.size()];
                final String[] link = new String[NewsList.size()];
                for (int i = 0; i < title.length; ) {
                    for (HashMap<String, String> hashMap : NewsList) {
                        hashMap.keySet();
                        for (String key : hashMap.keySet()) {
                            if (key.equals("title")) {
                                title[i] = hashMap.get(key);
                            } else if (key.equals("description")) {
                                description[i] = hashMap.get(key);
                            } else if (key.equals("image")) {
                                image[i] = hashMap.get(key);
                            } else if (key.equals("published")) {
                                published[i] = hashMap.get(key);
                            } else if (key.equals("link")) {
                                link[i] = hashMap.get(key);
                            }
                        }
                        i++;
                    }
                }

                newsAdapter = new NewsAdapter(context, title, description, image, published);
                listView.setAdapter(newsAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        String linked = link[position];
                        Uri url = Uri.parse(linked);
                        Intent intent = new Intent(Intent.ACTION_VIEW, url);
                        startActivity(intent);
                    }
                });
            }
        }
    }
}
