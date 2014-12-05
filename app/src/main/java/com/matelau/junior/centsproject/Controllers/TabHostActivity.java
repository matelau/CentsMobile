package com.matelau.junior.centsproject.Controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.Menu;
import android.view.MenuItem;

import com.matelau.junior.centsproject.R;

public class TabHostActivity extends FragmentActivity {
    private FragmentTabHost _tabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_host);

        _tabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
        _tabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        _tabHost.addTab(_tabHost.newTabSpec("tab1").setIndicator("Job Listings"),
                JobListFragment.class, null);
        _tabHost.addTab(_tabHost.newTabSpec("tab2").setIndicator("Cost of Living"),
                CostOfLivingFragment.class, null);
        _tabHost.addTab(_tabHost.newTabSpec("tab3").setIndicator("Spending"),
                SpendingBreakdownFragment.class, null);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tab_host, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //always return to main
        Intent returnToSearch = new Intent(this, MainActivity.class);
        startActivity(returnToSearch);
        return true;

    }
}
