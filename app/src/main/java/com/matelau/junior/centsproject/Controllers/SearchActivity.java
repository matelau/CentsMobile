package com.matelau.junior.centsproject.Controllers;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.matelau.junior.centsproject.Models.Design.Col;
import com.matelau.junior.centsproject.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    //State City processing
    private List<Col> _cols;
    private String[] _states;
    private String[] _supportedCities;
    private Set<String> _supportedStatesHash;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search);

        //Setup Toolbar
        _toolbar = (Toolbar) findViewById(R.id.toolbar);
        _toolbar.setTitle("Cents");
        //Todo add logo etc - after gathering view feedback
        setActionBar(_toolbar);
        loadLocations();

        //check if user is logged in or not
        loginStatus();

        //Setup Navigation Drawer
        configureDrawer(CentsApplication.is_loggedIN());

        if(savedInstanceState == null){
            selectItem(0);
        }

        //Attach Search Fragment
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_placeholder, new SearchFragment());
        ft.commit();
    }


    /**
     * Reads sharedPreferences to determine if a user is logged in or not and sets flag
     */
    private  void loginStatus(){
        SharedPreferences settings = this.getSharedPreferences("com.matelau.junior.centsproject", Context.MODE_PRIVATE);
        if (settings.contains("EMAIL") && settings.contains("PASSWORD")){
            HashMap<String, String> contents = (HashMap<String, String>) settings.getAll();
            Log.d(LOG_TAG, "Logged in as:"+contents.get("EMAIL"));
            CentsApplication.set_loggedIN(true);
            CentsApplication.set_user(contents.get("EMAIL"));
            CentsApplication.set_password(contents.get("PASSWORD"));
            if(CentsApplication.isDebug())
                Toast.makeText(this, "Logged in as: "+ CentsApplication.get_user(), Toast.LENGTH_SHORT).show();
        }
        else{
            CentsApplication.set_loggedIN(false);
            Log.d(LOG_TAG, "Not Logged in");
        }
    }

    /**
     * Configures Navigation Drawer
     */
    private void configureDrawer(Boolean loggedIn){
        _drawerLayout =  (DrawerLayout) findViewById(R.id.drawer_layout);
        _drawerLayout.setStatusBarBackgroundColor(getResources().getColor(R.color.primary_dark));
        //Todo set drawer shadow
//        _drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        _drawerLinear = (LinearLayout) findViewById(R.id.drawer_linear);

        _drawerList = (ListView) findViewById(R.id.left_drawer);
        //Determine which menu elements to use based on login status
        setNavElements(loggedIn);

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
    }

    /**
     * sets proper nav elements based on flag
     * @param loggedIn
     */
    private void setNavElements(Boolean loggedIn){
        //Determine which menu elements to use based on login status
        if(!loggedIn){
            _navElements = getResources().getStringArray(R.array.nav_array_logged_out);
        }
        else{
            _navElements = getResources().getStringArray(R.array.nav_array_logged_in);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
//       boolean drawerOpen = _drawerLayout.isDrawerOpen(_drawerLinear);

        Log.d(LOG_TAG, "onPrepareOptionsMenu");
        //Determine which menu elements to use based on login status
        setNavElements(CentsApplication.is_loggedIN());
        _drawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_nav_element, _navElements));
        _drawerList.invalidateViews();
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
                showHome();
                _drawerLayout.closeDrawers();
                break;
            case 1:
                if(CentsApplication.is_loggedIN()){
                    logout();
                }
                else {
                    showLoginDialog();
                }
                break;
            case 2:
                if(CentsApplication.is_loggedIN()){
                    showProfile();
                }
                else{
                    showRegistration();
                }
                break;
            case 3:
                //examples
                showExamples();
                break;
            case 4:
                //Launch Wizard Dialog
                showWizardDialog();
                break;
            case 5:
                showAbout();
                break;
            case 6:
                showHelp();
                break;
            default:
                if(CentsApplication.isDebug())
                    Toast.makeText(this, "Selected item:" + pos, Toast.LENGTH_SHORT).show();
        }
        _drawerLayout.closeDrawers();
    }

    /**
     * clears shared prefs and removes flag
     */
    private void logout(){
        SharedPreferences preferences = this.getSharedPreferences("com.matelau.junior.centsproject", Context.MODE_PRIVATE);
        preferences.edit().clear().commit();
        CentsApplication.set_loggedIN(false);
        CentsApplication.deleteSB(this);
        Log.d(LOG_TAG, "Logged out");
        if(CentsApplication.isDebug())
            Toast.makeText(this, "Logged Out", Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onResume() {
        //check if user is logged in or not modify nav drawer accordingly
        loginStatus();
        configureDrawer(CentsApplication.is_loggedIN());
        super.onResume();
    }

    /**
     * Loads the Wizard ontop of current view
     */
    private void showWizardDialog(){
        FragmentManager fm = getSupportFragmentManager();
        WizardDialogFragment wizard = new WizardDialogFragment();
        wizard.setTargetFragment(fm.findFragmentById(R.id.fragment_placeholder), 01);
        wizard.show(fm, "tag");
    }

    private void showProfile(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        _toolbar.setTitle("Profile");
        // Replace the container with the new fragment
        ft.replace(R.id.fragment_placeholder, new ProfileFragment());
        ft.addToBackStack("main-search");
        // Execute the changes specified
        ft.commit();
    }

    private void showHelp(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        _toolbar.setTitle("Help");
        ft.replace(R.id.fragment_placeholder, new HelpFragment());
        ft.addToBackStack("main-search");
        ft.commit();
    }

    private void showLoginDialog(){
        FragmentManager fm = getSupportFragmentManager();
        LoginDialogFragment login = new LoginDialogFragment();
        login.setTargetFragment(fm.findFragmentById(R.id.fragment_placeholder), 02);
        login.show(fm, "login");
    }

    /**
     * Set placeholder to main search view
     */
    private void showHome(){
        //Home
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        _toolbar.setTitle("Cents");
        //Attach Search Fragment
        // Replace the container with the new fragment
        ft.replace(R.id.fragment_placeholder, new SearchFragment());
        ft.addToBackStack("main-search");
        // Execute the changes specified
        ft.commit();
    }

    /**
     * Set placeholder to examples views
     */
    private void showExamples(){
        CentsApplication.set_selectedVis("Examples");
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_placeholder, new VisualizationPagerFragment());
        ft.addToBackStack("main-search");
        ft.commit();
    }

    /**
     * Set placeholder to Registration Views
     */
    private void showRegistration(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        _toolbar.setTitle("Register");
        //Attach Search Fragment
        // Replace the container with the new fragment
        ft.replace(R.id.fragment_placeholder, new RegistrationFragment());
        // Execute the changes specified
        ft.addToBackStack("main-search");
        ft.commit();
    }


    /**
     * Set placeholder to About view
     */
    private void showAbout(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        _toolbar.setTitle("About");
        ft.replace(R.id.fragment_placeholder, new AboutFragment());
        ft.addToBackStack("main-search");
        ft.commit();

    }

    /**
     * opens col.json and loads locations in an async task
     */
    private void loadLocations(){
        _states = new String[]{"Arizona", "California", "Colorado", "District of Columbia", "Florida", "Illinois", "Indiana",
                "Massachusetts", "Michigan", "Ohio", "North Carolina", "New York", "Pennsylvania", "Tennessee", "Texas", "Washington",
                "Wisconsin", "Utah"};
        CentsApplication.set_states(_states);
        _supportedStatesHash = new HashSet<String>(Arrays.asList(_states));
        FetchLocationsTask ft = new FetchLocationsTask();
        ft.execute();
    }

    /**
     * Process Col.json
     */
    protected class FetchLocationsTask extends AsyncTask<Void, Void, String[]> {

        private final String LOG_TAG = FetchLocationsTask.class.getSimpleName();
        private String[] states = null;

        @Override
        protected void onPostExecute(String[] strings) {
            if (strings != null) {
                Log.i(LOG_TAG, "onPostExecute - updating _supportedcities");
                _supportedCities = new String[strings.length];
                int index = 0;
                for (String s : strings) {
                    //Check that location is not a state
                    //edge case city is named after state
                    if (!_supportedStatesHash.contains(s) || (s.equals("Washington") && index < 15) || (s.equals("New York") && index == 23)) {
                        _supportedCities[index] = s;
                        index++;
                        Log.i(LOG_TAG, "Added: " + s + " To supportedCities");
                    }

                }

                CentsApplication.set_cities(_supportedCities);
            }
        }

        @Override
        protected String[] doInBackground(Void... params) {
            String[] results = null;
            Gson gson = new Gson();
            Log.i(LOG_TAG, "doInBackground - Loading col.json File");
            //load Json file
            try {
                Type tp = new TypeToken<Collection<Col>>() {
                }.getType();
                InputStream is = getAssets().open("col.json");
                BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                String json = sb.toString();
                is.close();
                Log.i(LOG_TAG, json);
                //Convert to objects
                _cols = gson.fromJson(json, tp);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (_cols != null) {
                Log.i(LOG_TAG, "doInBackground - Processing col.json");
                CentsApplication.set_cols(_cols);
                //pull locations add to result
                results = new String[_cols.size()];
                int i = 0;
                //Get Locations from cols
                for (Col c : _cols) {
                    results[i] = c.getLocation();
                    i++;
                }
            }

            return results;

        }
    }


}

