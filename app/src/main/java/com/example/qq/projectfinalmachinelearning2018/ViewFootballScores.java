package com.example.qq.projectfinalmachinelearning2018;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by Sarayut on 13/5/2561.
 */
public class ViewFootballScores extends android.app.Fragment {

    private String TAG = ViewFootballScores.class.getSimpleName();
    ListView listView;
    ArrayList<HashMap<String, String>> footballScheduleList;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scores, container, false);
        footballScheduleList = new ArrayList<>();
        listView = view.findViewById(R.id.listviewSchedule);
        new GetSchedule().execute();

        return view;
    }

    private class GetSchedule extends AsyncTask<Void, Void, Void> {

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
            String url = "https://www.footballwebpages.co.uk/league-table.json?comp=1&range=20&show=pos,p,w,d,l,f,a,gd,pts&showHa=yes&sort=home&start=1";
            String jsonStr = sh.makeServiceCall(url);
            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    JSONObject getTeam = jsonObject.getJSONObject("leagueTable");
                    JSONArray jsonTeam = getTeam.getJSONArray("team");
                    //looping through All Contacts
                    for (int i = 0; i < jsonTeam.length(); i++) {
                        JSONObject c = jsonTeam.getJSONObject(i);
                        String position = c.getString("position");
                        String name = c.getString("name");
                        String played = c.getString("played");
                        String homeWon = c.getString("homeWon");
                        String awayWon = c.getString("awayWon");
                        int hWon = Integer.valueOf(homeWon);
                        int aWon = Integer.valueOf(awayWon);
                        String resultWon = String.valueOf(hWon + aWon);

                        String homeDrawn = c.getString("homeDrawn");
                        String awayDrawn = c.getString("awayDrawn");
                        int hDrawn = Integer.valueOf(homeDrawn);
                        int aDrawn = Integer.valueOf(awayDrawn);
                        String resultDrawn = String.valueOf(hDrawn + aDrawn);

                        String homeLost = c.getString("homeLost");
                        String awayLost = c.getString("awayLost");
                        int hLost = Integer.valueOf(homeLost);
                        int aLost = Integer.valueOf(awayLost);
                        String resultLost = String.valueOf(hLost + aLost);
                        //ยิงประตูได้กี่ลูก
                        String homeFor = c.getString("homeFor");
                        String awayFor = c.getString("awayFor");
                        int hFor = Integer.valueOf(homeFor);
                        int aFor = Integer.valueOf(awayFor);
                        String resultGF = String.valueOf(hFor + aFor);
                        //ถูกยิงกี่ลูก
                        String homeAgainst = c.getString("homeAgainst");
                        String awayAgainst = c.getString("awayAgainst");
                        int hAgainst = Integer.valueOf(homeAgainst);
                        int aAgainst = Integer.valueOf(awayAgainst);
                        String resultAgainst = String.valueOf(hAgainst + aAgainst);
                        String points = c.getString("points");

                        HashMap<String, String> schedule = new HashMap<>();
                        schedule.put("position", position);
                        schedule.put("name", name);
                        schedule.put("played", played);
                        schedule.put("won", resultWon);
                        schedule.put("drawn", resultDrawn);
                        schedule.put("lost", resultLost);
                        schedule.put("goalFor",resultGF);
                        schedule.put("against", resultAgainst);
                        schedule.put("points", points);
                        footballScheduleList.add(schedule);
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
                //lost to network
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

                ListAdapter adapter = new SimpleAdapter(getActivity(), footballScheduleList,
                        R.layout.item_scores, new String[]{"position", "name", "played", "won", "drawn", "lost","goalFor", "against", "points"}, new int[]{R.id.tvPosition, R.id.tvTeamName, R.id.tvPlay, R.id.tvWin, R.id.tvDraw, R.id.tvLost,R.id.tvGoalFor, R.id.tvGoalAgainst, R.id.tvPoints});
                listView.setAdapter(adapter);
            }
        }
    }
}
