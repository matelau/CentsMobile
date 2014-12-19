package com.matelau.junior.centsproject.Controllers;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toolbar;

import com.matelau.junior.centsproject.R;

public class SearchActivity extends Activity {

    private Toolbar _toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search);
//        getLayoutInflater().inflate(R.id.toolbar, this);

        _toolbar =  new Toolbar(this);//(Toolbar) findViewById(R.id.toolbar);
        if(_toolbar != null){
            setActionBar(_toolbar);
            getActionBar().setTitle("");
            getActionBar().setDisplayUseLogoEnabled(true);

            //TODO change apptheme parent to be material.noActionbar so toolbar won't conflict
        }
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
