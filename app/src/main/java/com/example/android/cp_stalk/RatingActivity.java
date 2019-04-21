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
import java.util.ArrayList;
import java.util.HashMap;

public class RatingActivity extends Fragment {

    Button adduser;
    EditText addhandle;
    RelativeLayout rv;
    ListView lv;
    ArrayList<String> list;

    public RatingActivity() {
        // Required empty public constructor

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.ratingactivity, null);


        adduser = v.findViewById(R.id.adduser);

        lv = v.findViewById(R.id.lv);
        addhandle = v.findViewById(R.id.addhandle);
        final DBHelperUsers dbHelperUsers = new DBHelperUsers(getActivity().getApplicationContext());

        list = dbHelperUsers.fetchHandles();


        String handles = list.get(0);
        for (int index = 1; index < list.size(); index++) {
            handles += ";" + list.get(index);
        }


        adduser.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String handle = addhandle.getText().toString();
                if (handle.equals("")) {
                    AlertDialog.Builder ad = new AlertDialog.Builder(getActivity().getApplicationContext());
                    ad.setTitle("Enter the handle");
                    ad.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    ad.show();
                } else {

                    dbHelperUsers.insert(handle);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainframe, new RatingActivity()).commit();
                }
            }
        });
        new GetData().execute(handles);
        return v;
    }

    private class GetData extends AsyncTask<String, Void, JSONObject> {

        GetData() {

        }

        @Override
        protected JSONObject doInBackground(String... data) {
            JSONObject jo1 = new JSONObject();
            try {

                URL url = new URL("http://codeforces.com/api/user.info?handles=" + data[0]);
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
            ArrayList<String> details = new ArrayList<String>();
            try {

                for (int index = 0; index < list.size(); index++) {
                    String handle = jo.getJSONArray("result").getJSONObject(index).getString("handle");

                    int rating = jo.getJSONArray("result").getJSONObject(index).getInt("rating");

                    details.add(handle + " " + rating);
                }


                ListViewAdapterRatingView ad = new ListViewAdapterRatingView(getActivity(), getActivity().getApplicationContext(), details);
                lv.setAdapter(ad);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}
