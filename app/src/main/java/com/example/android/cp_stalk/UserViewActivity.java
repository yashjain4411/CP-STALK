package com.example.android.cp_stalk;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationManagerCompat;
import android.text.GetChars;
import android.text.format.DateUtils;
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
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

public class UserViewActivity extends Fragment {


    ImageView photo;
    TextView citytv, countrytv, ratingtv, maxratingtv, handletv, ranktv, solvedcounttv;
    WebView webview, webviewlang, webviewtag;

    public UserViewActivity() {
        // Required empty public constructor

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.userviewactivity, null);
        webview = v.findViewById(R.id.piechart1);
        webviewtag = v.findViewById(R.id.piechart3);
        photo = v.findViewById(R.id.userpic);
        citytv = v.findViewById(R.id.city);
        countrytv = v.findViewById(R.id.country);
        ratingtv = v.findViewById(R.id.rating);
        maxratingtv = v.findViewById(R.id.maxrating);
        handletv = v.findViewById(R.id.handle);
        ranktv = v.findViewById(R.id.rank);
        webviewlang = v.findViewById(R.id.piechart2);
        solvedcounttv = v.findViewById(R.id.solvedcount);
        String handle = getArguments().getString("handle");
        new GetData(getActivity().getApplicationContext()).execute(handle);
        return v;
    }


    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    private class GetData extends AsyncTask<String, Void, ArrayList<JSONArray>> {
        Context c;
        private NotificationManager notificationManager;
        GetData( Context c) {
             this.c=c;
            notificationManager = (NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);
        }

        @Override
        protected ArrayList<JSONArray> doInBackground(String... data) {
            ArrayList<JSONArray> list = new ArrayList<>();
            try {

                URL url = new URL("http://codeforces.com/api/user.info?handles=" + data[0]);
                URLConnection urlConnection = url.openConnection();
                URL urlforsubmisson = new URL("http://codeforces.com/api/user.status?handle=" + data[0]);

                URLConnection urlConnection1 = urlforsubmisson.openConnection();

                BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String out = "";
                String input = null;
                while ((input = br.readLine()) != null) {
                    out += input;
                }
                try {

                    JSONObject jo1 = new JSONObject(out);
                    JSONArray jsonArray1 = jo1.getJSONArray("result");
                    list.add(jsonArray1);
                    br = new BufferedReader(new InputStreamReader(urlConnection1.getInputStream()));
                    out = "";
                    input = null;
                    while ((input = br.readLine()) != null) {
                        out += input;
                    }
                    JSONObject jo2 = new JSONObject(out);
                    JSONArray jsonArray2 = jo2.getJSONArray("result");
                    list.add(jsonArray2);
                    url = new URL("http://codeforces.com/api/contest.list");
                    urlConnection = url.openConnection();


                    br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    out = "";
                    input = null;
                    while ((input = br.readLine()) != null) {
                        out += input;
                    }
                    jo1 = new JSONObject(out);
                    list.add(jo1.getJSONArray("result"));

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

            return list;
        }

        @Override
        protected void onPostExecute(ArrayList<JSONArray> jsonArrays) {

            JSONArray jsonArray1 = jsonArrays.get(0);

            String handle = null;
            try {
                handle = jsonArray1.getJSONObject(0).getString("handle");

                String country = "NOT_PRESENT";
                if (jsonArray1.getJSONObject(0).has("country"))
                    country = jsonArray1.getJSONObject(0).getString("country");
                String city = "NOT_PRESENT";
                if (jsonArray1.getJSONObject(0).has("city"))
                    city = jsonArray1.getJSONObject(0).getString("city");

                int rating = jsonArray1.getJSONObject(0).getInt("rating");
                int maxrating = jsonArray1.getJSONObject(0).getInt("maxRating");
                String image = jsonArray1.getJSONObject(0).getString("titlePhoto");
                String rank = jsonArray1.getJSONObject(0).getString("rank");

                new UserViewActivity.DownloadImageTask(photo)
                        .execute("https:" + image);
                HashMap<String, Integer> m = new HashMap<>();

                JSONArray jsonArray2 = jsonArrays.get(1);

                for (int i = 0; i < jsonArray2.length(); i++) {
                    if (m.containsKey(jsonArray2.getJSONObject(i).getString("verdict"))) {
                        Integer integer = m.get(jsonArray2.getJSONObject(i).getString("verdict"));
                        m.replace(jsonArray2.getJSONObject(i).getString("verdict"), integer, integer + 1);
                    } else {
                        m.put(jsonArray2.getJSONObject(i).getString("verdict"), 1);
                    }
                }
                handletv.setText(handle);
                countrytv.setText(country);
                ratingtv.setText(String.valueOf(rating));
                maxratingtv.setText(String.valueOf(maxrating));
                citytv.setText(city);
                ranktv.setText(rank);
                solvedcounttv.setText(m.get("OK").toString());
                String content = "<html>" + "  <head>"
                        + "    <script type=\"text/javascript\" src=\"https://www.gstatic.com/charts/loader.js\"></script>"
                        + "    <script type=\"text/javascript\">"
                        + "      google.load(\"visualization\", \"1\", {packages:[\"corechart\"]});"
                        + "      google.setOnLoadCallback(drawChart);"
                        + "      function drawChart() {"
                        + "var data = google.visualization.arrayToDataTable(["
                        + "['Verdict', 'Number'],";
                int cnt = 0;
                for (String key : m.keySet()) {
                    if (cnt == m.keySet().size() - 1)
                        content += "['" + key + "'," + m.get(key) + "]";
                    else
                        content += "['" + key + "'," + m.get(key) + "],";
                    cnt++;
                }
                content += "]);" +
                        "var options = {" + "title: 'Problems Verdicts'," + "is3D: true," + "};" +
                        "var chart = new google.visualization.PieChart(document.getElementById('piechart_3d'));" +
                        "chart.draw(data, options);" +
                        "}" + "</script> </head> <body> <div id=\"piechart_3d\" style=\"width: 930px; height: 450px;\"></div> </body> </html>";
                final String content1 = content;

                WebSettings webSettings = webview.getSettings();
                webSettings.setJavaScriptEnabled(true);
                webview.requestFocusFromTouch();
                webview.loadDataWithBaseURL("file:///android_asset/", content1, "text/html", "utf-8", null);


                HashMap<String, Integer> ml = new HashMap<String, Integer>();
                for (int i = 0; i < jsonArray2.length(); i++) {
                    if (ml.containsKey(jsonArray2.getJSONObject(i).getString("programmingLanguage"))) {
                        Integer integer = ml.get(jsonArray2.getJSONObject(i).getString("programmingLanguage"));
                        ml.replace(jsonArray2.getJSONObject(i).getString("programmingLanguage"), integer, integer + 1);
                    } else {
                        ml.put(jsonArray2.getJSONObject(i).getString("programmingLanguage"), 1);
                    }
                }
                content = "<html>" + "  <head>"
                        + "    <script type=\"text/javascript\" src=\"https://www.gstatic.com/charts/loader.js\"></script>"
                        + "    <script type=\"text/javascript\">"
                        + "      google.load(\"visualization\", \"1\", {packages:[\"corechart\"]});"
                        + "      google.setOnLoadCallback(drawChart);"
                        + "      function drawChart() {"
                        + "var data = google.visualization.arrayToDataTable(["
                        + "['Languages', 'Number'],";
                cnt = 0;
                for (String key : ml.keySet()) {
                    if (cnt == ml.keySet().size() - 1)
                        content += "['" + key + "'," + ml.get(key) + "]";
                    else
                        content += "['" + key + "'," + ml.get(key) + "],";
                    cnt++;
                }
                content += "]);" +
                        "var options = {" + "title: 'Languages'," + "is3D: true," + "};" +
                        "var chart = new google.visualization.PieChart(document.getElementById('piechart_3d'));" +
                        "chart.draw(data, options);" +
                        "}" + "</script> </head> <body> <div id=\"piechart_3d\" style=\"width: 930px; height: 450px;\"></div> </body> </html>";
                final String content2 = content;


                webSettings = webviewlang.getSettings();
                webSettings.setJavaScriptEnabled(true);
                webviewlang.requestFocusFromTouch();
                webviewlang.loadDataWithBaseURL("file:///android_asset/", content2, "text/html", "utf-8", null);

                HashMap<String, Integer> mtag = new HashMap<>();
                for (int i = 0; i < jsonArray2.length(); i++) {
                    JSONObject jsonObject = jsonArray2.getJSONObject(i);
                    if (jsonObject.getString("verdict").equals("OK")) {
                        JSONArray jsonArray3 = jsonObject.getJSONObject("problem").getJSONArray("tags");
                        for (int j = 0; j < jsonArray3.length(); j++) {
                            Log.d("tag", jsonArray3.getString(j));
                            if (mtag.containsKey(jsonArray3.getString(j))) {
                                Integer integer = mtag.get(jsonArray3.getString(j));
                                mtag.replace(jsonArray3.getString(j), integer, integer + 1);
                            } else {
                                mtag.put(jsonArray3.getString(j), 1);
                            }
                        }
                    }
                }
                content = "<html>" + "  <head>"
                        + "    <script type=\"text/javascript\" src=\"https://www.gstatic.com/charts/loader.js\"></script>"
                        + "    <script type=\"text/javascript\">"
                        + "      google.load(\"visualization\", \"1\", {packages:[\"corechart\"]});"
                        + "      google.setOnLoadCallback(drawChart);"
                        + "      function drawChart() {"
                        + "var data = google.visualization.arrayToDataTable(["
                        + "['Tags', 'Number'],";
                cnt = 0;
                for (String key : mtag.keySet()) {
                    if (cnt == mtag.keySet().size() - 1)
                        content += "['" + key + "'," + mtag.get(key) + "]";
                    else
                        content += "['" + key + "'," + mtag.get(key) + "],";
                    cnt++;
                }
                content += "]);" +
                        "var options = {" + "title: 'Tags'," + "pieHole: 0.4," + "};" +
                        "var chart = new google.visualization.PieChart(document.getElementById('piechart_3d'));" +
                        "chart.draw(data, options);" +
                        "}" + "</script> </head> <body> <div id=\"piechart_3d\" style=\"width: 930px; height: 450px;\"></div> </body> </html>";
                final String content3 = content;
                Log.d("content", content3);

                webSettings = webviewtag.getSettings();
                webSettings.setJavaScriptEnabled(true);
                webviewtag.requestFocusFromTouch();
                webviewtag.loadDataWithBaseURL("file:///android_asset/", content3, "text/html", "utf-8", null);
                jsonArray1=jsonArrays.get(2);
                ArrayList<String[]>list=new ArrayList<>();
                for(int i=0;i<jsonArray1.length();i++){
                    JSONObject jo1=jsonArray1.getJSONObject(i);
                    int unixSeconds = jo1.getInt("startTimeSeconds");

                    Date date = new java.util.Date(unixSeconds*1000L);

                    SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");

                    sdf.setTimeZone(TimeZone.getDefault());
                    String formattedDate = sdf.format(date);
                    Log.d("sizeeee",""+date.toString());
                    Log.d("sizeeee",""+ Calendar.getInstance().getTime().toString());
                    int totalSecs=jo1.getInt("durationSeconds");

                    int hours = totalSecs / 3600;
                    int minutes = (totalSecs % 3600) / 60;
                    int seconds = totalSecs % 60;

                    String timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds);

                    if(new SimpleDateFormat("yyyyMMDD").format(date).equals(new SimpleDateFormat("yyyyMMDD").format(
                    (Calendar.getInstance().getTime())))){
                        list.add(new String[]{jo1.getString("name"),formattedDate,timeString});
                    }

                }
                showNotification(list);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        public void showNotification( ArrayList<String[]> list){
            Log.d("Notification","hi");
            Notification.Builder builder = new Notification.Builder(c)
                    .setSmallIcon(android.R.drawable.stat_sys_download)
                    .setAutoCancel(true)
                    .setContentTitle("Today's Contest");
            if(!list.isEmpty())
                builder.setContentText(list.get(0)[0]+" "+list.get(0)[1]+list.get(0)[2]);
            else
                builder.setContentText("No Contest Today,Happy Coding");


            //Get current notification

            NotificationChannel channel = new NotificationChannel("channel_1",
                    "Channel human readable title", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
            builder.setChannelId("channel_1");

            //Show the notification
            notificationManager.notify(1, builder.build());

        }
    }
}
