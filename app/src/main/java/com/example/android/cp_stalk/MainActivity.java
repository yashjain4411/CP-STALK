package com.example.android.cp_stalk;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.cp_stalk.UserViewActivity;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        SharedPreferences sp = getSharedPreferences("key", Context.MODE_PRIVATE);
        if (!sp.contains("handle")) {
            Toast.makeText(this,"You are not registered . Please Login first",Toast.LENGTH_LONG).show();
            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(i);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        TextView name=navigationView.getHeaderView(0).findViewById(R.id.name);
        TextView email=navigationView.getHeaderView(0).findViewById(R.id.email);
        name.setText(sp.getString("name","0000"));
        email.setText(sp.getString("email","0000"));
        UserViewActivity f=new UserViewActivity();
        Bundle b=new Bundle();
        b.putString("handle",sp.getString("handle","Yash_Jain"));
        f.setArguments(b);
        getSupportFragmentManager().beginTransaction().add(R.id.mainframe,f).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.Logout) {
            getSharedPreferences("key",Context.MODE_PRIVATE).edit().clear().commit();
            Intent i=new Intent(MainActivity.this,LoginActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.useractivity) {
            UserViewActivity f=new UserViewActivity();
            Bundle b=new Bundle();
            b.putString("handle",getSharedPreferences("key",Context.MODE_PRIVATE).getString("handle","Yash_Jain"));
            f.setArguments(b);
            getSupportFragmentManager().beginTransaction().replace(R.id.mainframe,f).commit();



        }
        else if (id == R.id.friends) {
            getSupportFragmentManager().beginTransaction().replace(R.id.mainframe,new RatingActivity()).commit();


        } else if (id == R.id.contests) {
            getSupportFragmentManager().beginTransaction().replace(R.id.mainframe,new ContestActivity()).commit();


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
