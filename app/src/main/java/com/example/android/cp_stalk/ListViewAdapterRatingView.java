package com.example.android.cp_stalk;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ListViewAdapterRatingView extends BaseAdapter{
    Context c;
    FragmentActivity e;
    ArrayList<String>list;

    ListViewAdapterRatingView(FragmentActivity e, Context c, ArrayList<String>list){
        this.c=c;
        this.e=e;
        this.list=list;
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView= LayoutInflater.from(c).inflate(R.layout.viewdetails,null);
        final TextView handle=convertView.findViewById(R.id.handle);
        TextView rating=convertView.findViewById(R.id.rating);
        final String [] arr=list.get(position).split(" ");
        handle.setText(arr[0]);
        rating.setText(arr[1]);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserViewActivity f=new UserViewActivity();
                Bundle b=new Bundle();
                b.putString("handle",arr[0]);
                f.setArguments(b);
                e.getSupportFragmentManager().beginTransaction().replace(R.id.mainframe,f).commit();
            }
        });
        return convertView;
    }
}
