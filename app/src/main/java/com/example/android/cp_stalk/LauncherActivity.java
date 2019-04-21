package com.example.android.cp_stalk;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.example.android.cp_stalk.Database.DBHelperUsers;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class LauncherActivity extends AppCompatActivity{
    Button gotoratingactivity;
    DBHelperUsers dbHelperUsers;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launcheractivity);
        gotoratingactivity=findViewById(R.id.GoToRatingActivity);
        gotoratingactivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            dbHelperUsers=new DBHelperUsers(LauncherActivity.this);
                            ArrayList<String>list=dbHelperUsers.fetchHandles();
                            Intent i=new Intent(LauncherActivity.this,RatingActivity.class);

                            ArrayList<String> details=new ArrayList<String>();

                            String handles=list.get(0);
                            for(int index=1;index<list.size();index++){
                                handles+=";"+list.get(index);
                            }

                            URL url = new URL("http://codeforces.com/api/user.info?handles="+handles);
                            URLConnection urlConnection = url.openConnection();
                            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                            String out = "";
                            String input = null;
                            while ((input = br.readLine()) != null) {
                                out += input;
                            }
                            try {

                                JSONObject jo = new JSONObject(out);
                            for(int index=0;index<list.size();index++) {
                                String handle = jo.getJSONArray("result").getJSONObject(index).getString("handle");

                                int rating = jo.getJSONArray("result").getJSONObject(index).getInt("rating");

                                details.add(handle+" "+rating);
                            }


                            } catch (JSONException e) {
                                Log.d("error", "1");
                                e.printStackTrace();
                            }
                            i.putStringArrayListExtra("List",details);
                            startActivity(i);

                        } catch (MalformedURLException e) {
                            Log.d("error", "2");
                            e.printStackTrace();
                        } catch (IOException e) {
                            Log.d("error", "3");
                            e.printStackTrace();
                        }
                    }
                }).start();






            }
        });
    }
}
