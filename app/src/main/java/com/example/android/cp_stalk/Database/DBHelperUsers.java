package com.example.android.cp_stalk.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class DBHelperUsers {
    private static final String DATABASE_NAME="IA";
    private static final String TABLE_NAME="user";
    private static final String handle="handle";
    private static final String dbcreate="Create table if not exists "+TABLE_NAME+" ("+handle+" text primary key )";
    private static final int version=1;
    private DBAdapter dba;

    public DBHelperUsers(Context c){
        dba=new DBAdapter(c);

    }
    public ArrayList<String> fetchHandles(){
        ArrayList<String>list=new ArrayList<>();
        SQLiteDatabase db=dba.getWritableDatabase();

        Cursor c=db.query(TABLE_NAME,new String[]{handle},null,null,null,null,null);
        if(c.moveToFirst()){
            do {
                list.add(new String(c.getString(0)));
            }
            while(c.moveToNext());
        }

        return list;
    }

    public void insert(String handletoinsert){
        ContentValues cv=new ContentValues();
        cv.put(handle,handletoinsert);
        SQLiteDatabase db=dba.getWritableDatabase();
        db.insert(TABLE_NAME,null,cv);
    }

    private class DBAdapter extends SQLiteOpenHelper{

        DBAdapter(Context c){
            super(c,DATABASE_NAME,null,version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(dbcreate);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
           db.execSQL("Drop table if exists "+TABLE_NAME);
           onCreate(db);
        }
    }
}
