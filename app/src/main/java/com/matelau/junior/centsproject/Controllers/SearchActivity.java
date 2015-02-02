package com.matelau.junior.centsproject.Controllers;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.matelau.junior.centsproject.R;

public class SearchActivity extends FragmentActivity {

    private String LOG_TAG = SearchActivity.class.getSimpleName();
    private Toolbar _toolbar;
    //Nav Drawer Properties
    private DrawerLayout _drawerLayout;
    private ListView _drawerList;
    private String[] _navElements;
    private ActionBarDrawerToggle _drawerToggle;
    private LinearLayout _drawerLinear;
    private boolean _isDrawerOpen;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search);

        //Setup Toolbar
        _toolbar = (Toolbar) findViewById(R.id.toolbar);
        _toolbar.setTitle("Cents");
        //Todo add logo etc - after gathering view feedback
        setActionBar(_toolbar);


        //Setup Navigation Drawer
        _drawerLayout =  (DrawerLayout) findViewById(R.id.drawer_layout);
        _drawerLayout.setStatusBarBackgroundColor(getResources().getColor(R.color.primary_dark));
        //Todo set drawer shadow
//        _drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        _drawerLinear = (LinearLayout) findViewById(R.id.drawer_linear);
        _drawerList = (ListView) findViewById(R.id.left_drawer);
        _navElements = getResources().getStringArray(R.array.nav_array);
        _drawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_nav_element, _navElements));
        _drawerList.setOnItemClickListener(new DrawerItemClickListener());

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setDisplayShowHomeEnabled(true);

        _drawerToggle = new ActionBarDrawerToggle(this,_drawerLayout, R.string.drawer_open, R.string.drawer_closed) {
            /**
             * Called when a drawer has settled in a completely closed state.
             */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                Log.d(LOG_TAG, "Drawer Closed");
//                getActionBar().setTitle("Closed");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /**
             * Called when a drawer has settled in a completely open state.
             */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                Log.d(LOG_TAG, "Drawer Opened");
//                getActionBar().setTitle("Open");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }


        };
        _drawerLayout.setDrawerListener(_drawerToggle);

        if(savedInstanceState == null){
            selectItem(0);
        }

        //Attach Search Fragment
        // Begin the transaction
       FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        // Replace the container with the new fragment
        ft.replace(R.id.fragment_placeholder, new SearchFragment());
        // Execute the changes specified
        ft.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
//       boolean drawerOpen = _drawerLayout.isDrawerOpen(_drawerLinear);
        Log.d(LOG_TAG, "onPrepareOptionsMenu");
//        menu.findItem(R.id.action_new).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        _drawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(_drawerToggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        _drawerToggle.onConfigurationChanged(newConfig);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        private String LOG_TAG = DrawerItemClickListener.class.getSimpleName();

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //TODO add animation for onClick Selections
            selectItem(position);
        }



        public void setTitle(CharSequence title){
            getActionBar().setTitle(title);
        }
    }

    /**
     * Switch out fragment based on Nav Drawer element clicked
     * @param pos
     */
    private void selectItem(int pos){
        Log.d(LOG_TAG, "Item Selected: "+pos);

        //launch and attach fragment based on clicked item
        //TODO add drawer open/closed state, click response - http://developer.android.com/training/implementing-navigation/nav-drawer.html
        switch (pos) {
            case 0:
                //Home
                _toolbar.setTitle("Cents");
                //Attach Search Fragment
                // Begin the transaction
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                // Replace the container with the new fragment
                ft.replace(R.id.fragment_placeholder, new SearchFragment());
                // or ft.add(R.id.your_placeholder, new FooFragment());
                // Execute the changes specified
                ft.commit();
                _drawerLayout.closeDrawers();
                break;
            case 4:
                //Launch Wizard Dialog
                showWizardDialog();
                _drawerLayout.closeDrawers();
                break;
            default:
                Toast.makeText(this, "Selected item:" + pos, Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    protected void onResume() {

        super.onResume();
    }

    private void showWizardDialog(){
        FragmentManager fm = getSupportFragmentManager();
        WizardDialogFragment wizard = new WizardDialogFragment();
        wizard.setTargetFragment(fm.findFragmentById(R.id.fragment_placeholder), 01);
//        secondCity.setTargetFragment(this, 01);
        wizard.show(fm, "tag");

    }


}

