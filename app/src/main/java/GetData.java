import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.webkit.WebSettings;
import android.widget.ImageView;

import com.example.android.cp_stalk.UserViewActivity;

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
import java.util.ArrayList;
import java.util.HashMap;

public class GetData extends AsyncTask<String,Void,ArrayList<JSONArray>> {
    Context c;
    GetData(Context context){
        c=context;
    }

    @Override
    protected ArrayList<JSONArray> doInBackground(String... data) {
        ArrayList<JSONArray>list=new ArrayList<>();
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
        Intent intent = new Intent();
        intent.putExtra("JsonArray",jsonArrays);

        // broadcast the completion
        c.sendBroadcast(intent);
    }
}
