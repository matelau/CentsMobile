package com.matelau.junior.centsproject.Controllers;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toolbar;

import com.matelau.junior.centsproject.R;

public class SearchActivity extends Activity {

    private Toolbar _toolbar;
    private DrawerLayout _drawerLayout;
    private ListView _drawerList;
    private String[] _navElements;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search);
        //Todo modify toolbar layout to hold navigation drawer
        _toolbar = (Toolbar) findViewById(R.id.toolbar);
        _toolbar.setTitle("Add Navigation Drawer");
        //Todo add logo etc - after gathering view feedback
        setActionBar(_toolbar);
        _drawerLayout =  (DrawerLayout) findViewById(R.id.my_drawer_layout);
        _drawerLayout.setStatusBarBackgroundColor(getResources().getColor(R.color.primary_dark));
        _drawerList = (ListView) findViewById(R.id.nav_list);


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
