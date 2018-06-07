package com.example.qq.projectfinalmachinelearning2018;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

/**
 * Created by Sarayut on 30/5/2561.
 */
public class Fragment_match extends android.app.Fragment {

    private String TAG = Fragment_match.class.getSimpleName();
    ListView listView;
    ArrayList<HashMap<String, String>> footballMatchList;
    ProgressDialog progressDialog;
    CustomAdapter adapter;
    private DatabaseReference databaseUser;
    private FirebaseDatabase database;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener AuthListener;
    private UserManage userManage;
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_match, container, false);
        footballMatchList = new ArrayList<>();
        listView = view.findViewById(R.id.listviewMatch);
        context = getActivity();
        userManage = new UserManage(context);
        database = FirebaseDatabase.getInstance();
        databaseUser = database.getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        new GetMatch().execute();

        return view;
    }

    private class GetMatch extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Loading....");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler sh = new HttpHandler();
            // String url = "https://www.football-data.org/v1/fixtures";
            String url = "https://api.myjson.com/bins/fxp36";
            String jsonStr = sh.makeServiceCall(url);
            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonStr);

                    JSONArray jsonContacts = jsonObject.getJSONArray("fixtures");
                    //looping through All Contacts
                    for (int i = 0; i < jsonContacts.length(); i++) {
                        JSONObject c = jsonContacts.getJSONObject(i);
                        String homeTeamName = c.getString("homeTeamName");
                        String awayTeamName = c.getString("awayTeamName");
                        String dateEng = c.getString("date");
                        String dateA = dateEng.substring(0, 10) + " ";
                        int dateB = Integer.valueOf(dateEng.substring(11, 13)) + 7;
                        String dateC = dateEng.substring(13, 19);
                        String date = dateA + String.valueOf(dateB) + dateC;
                        JSONObject result = c.getJSONObject("result");
                        String goalsHomeTeam = result.getString("goalsHomeTeam");
                        String goalsAwayTeam = result.getString("goalsAwayTeam");

                        JSONObject linkImage = c.getJSONObject("_links");

                        String imgHome = linkImage.getString("crestUrlHome");
                        String imgAway = linkImage.getString("crestUrlAway");
                        HashMap<String, String> match = new HashMap<>();
                        match.put("homeTeamName", homeTeamName);
                        match.put("awayTeamName", awayTeamName);
                        match.put("date", date);
                        match.put("goalsHomeTeam", goalsHomeTeam);
                        match.put("goalsAwayTeam", goalsAwayTeam);
                        match.put("imgHome", imgHome);
                        match.put("imgAway", imgAway);
                        footballMatchList.add(match);
                    }

                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } else

            {
                Log.e(TAG, "Couldn't get json from server.");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(),
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
                final String[] homeTeamName = new String[footballMatchList.size()];
                final String[] awayTeamName = new String[footballMatchList.size()];
                final String[] date = new String[footballMatchList.size()];
                String[] goalsHomeTeam = new String[footballMatchList.size()];
                String[] goalsAwayTeam = new String[footballMatchList.size()];
                final String[] imgHome = new String[footballMatchList.size()];
                final String[] imgAway = new String[footballMatchList.size()];
                for (int i = 0; i < homeTeamName.length; ) {
                    for (HashMap<String, String> hashMap : footballMatchList) {
                        hashMap.keySet();
                        for (String key : hashMap.keySet()) {
                            if (key.equals("homeTeamName")) {
                                homeTeamName[i] = hashMap.get(key);
                            } else if (key.equals("awayTeamName")) {
                                awayTeamName[i] = hashMap.get(key);
                            } else if (key.equals("goalsHomeTeam")) {
                                goalsHomeTeam[i] = hashMap.get(key);
                            } else if (key.equals("goalsAwayTeam")) {
                                goalsAwayTeam[i] = hashMap.get(key);
                            } else if (key.equals("date")) {
                                date[i] = hashMap.get(key);
                            } else if (key.equals("imgHome")) {
                                imgHome[i] = hashMap.get(key);
                            } else if (key.equals("imgAway")) {
                                imgAway[i] = hashMap.get(key);
                            }
                        }
                        i++;
                    }
                }

                adapter = new CustomAdapter(context, homeTeamName, awayTeamName, date, goalsHomeTeam, goalsAwayTeam, imgHome, imgAway);
                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        final FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user != null) {

                            Boolean isSuccess = userManage.checkLoginValidate(user.getEmail());
                            if (isSuccess) {
                                Intent intent = new Intent(context, ActivityDetail.class);
                                intent.putExtra("position", position);
                                intent.putExtra("homeTeamName",homeTeamName[position]);
                                intent.putExtra("awayTeamName",awayTeamName[position]);
                                intent.putExtra("imgHome",imgHome[position]);
                                intent.putExtra("imgAway",imgAway[position]);
                                intent.putExtra("date",date[position]);
                                startActivity(intent);
                            } else {
                                Toast.makeText(context, "Please Login !!", Toast.LENGTH_SHORT).show();
                            }
                            Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                        } else {
                            // UserManage is signed out
                            Log.d(TAG, "onAuthStateChanged:signed_out");
                        }
                    }
                });
            }
        }
    }
}
