package com.example.android.cp_stalk;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.android.cp_stalk.Database.DBHelperUsers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

public class ContestActivity extends Fragment{


    ListView lv;


    public ContestActivity() {


    }
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.contestactivity,null);



        lv=v.findViewById(R.id.lv);

        new GetData().execute();
        return v;
    }

    private class GetData extends AsyncTask<Void,Void,JSONObject> {

        GetData(){

        }


        @Override
        protected JSONObject doInBackground(Void ...voids) {
            JSONObject jo1=new JSONObject();
            try{

                URL url = new URL("http://codeforces.com/api/contest.list");
                URLConnection urlConnection = url.openConnection();


                BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String out = "";
                String input = null;
                while ((input = br.readLine()) != null) {
                    out += input;
                }
                try {

                    jo1 = new JSONObject(out);



                } catch (JSONException e) {
                    Log.d("error", "1");
                    e.printStackTrace();
                }

            } catch (MalformedURLException e) {
                Log.d("error", "2");
                e.printStackTrace();
            } catch (IOException e) {
                Log.d("error", "3");
                e.printStackTrace();
            }

            return jo1;
        }

        @Override
        protected void onPostExecute(JSONObject jo) {
            ArrayList<String[]> details=new ArrayList<String[]>();
            try {

                JSONArray jsonArray=jo.getJSONArray("result");
                ArrayList<String[]>list=new ArrayList<>();
                Log.d("sizeeee",""+jsonArray.length());
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject jo1=jsonArray.getJSONObject(i);
                    int unixSeconds = jo1.getInt("startTimeSeconds");

                    Date date = new java.util.Date(unixSeconds*1000L);

                    SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");

                    sdf.setTimeZone(TimeZone.getDefault());
                    String formattedDate = sdf.format(date);
                    Log.d("sizeeee",""+date.toString());
                    Log.d("sizeeee",""+Calendar.getInstance().getTime().toString());
                    int totalSecs=jo1.getInt("durationSeconds");

                    int hours = totalSecs / 3600;
                    int minutes = (totalSecs % 3600) / 60;
                    int seconds = totalSecs % 60;

                    String timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds);

                    if(date.after(Calendar.getInstance().getTime())){
                        list.add(new String[]{jo1.getString("name"),formattedDate,timeString});
                    }
                }
                ContestAdapter ad=new ContestAdapter(getActivity(),getActivity().getApplicationContext(),list);
                lv.setAdapter(ad);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}
