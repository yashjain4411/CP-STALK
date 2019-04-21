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

public class ContestAdapter extends BaseAdapter{
    Context c;
    FragmentActivity e;
    ArrayList<String[]>list;

    ContestAdapter(FragmentActivity e, Context c, ArrayList<String[]>list){
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
        convertView= LayoutInflater.from(c).inflate(R.layout.contestactvity,null);
        TextView name=convertView.findViewById(R.id.contestname);
        TextView start=convertView.findViewById(R.id.starttime);
        TextView duration=convertView.findViewById(R.id.duration);
        String arr[]=list.get(position);
        name.setText(arr[0]);
        start.setText(arr[1]);
        duration.setText(arr[2]);
        return convertView;
    }
}
