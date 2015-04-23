package com.matelau.junior.centsproject.Controllers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
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

import com.matelau.junior.centsproject.Models.CentsAPIServices.UserService;
import com.matelau.junior.centsproject.R;

import java.util.HashMap;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SearchActivity extends FragmentActivity {

    private String LOG_TAG = SearchActivity.class.getSimpleName();
    private Toolbar _toolbar;
    //Nav Drawer Properties
    private DrawerLayout _drawerLayout;
    private ListView _drawerList;
    private String[] _navElements;
    private ActionBarDrawerToggle _drawerToggle;
    private LinearLayout _drawerLinear;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search);

        //Setup Toolbar
        _toolbar = (Toolbar) findViewById(R.id.toolbar);
        _toolbar.setTitle("Cents");
        setActionBar(_toolbar);

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
                //allow user to select email client and send feedback
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", "admin@trycents.com", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "FEEDBACK");
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
                updateCompleted("Submit Feedback");
                break;

        }
        _drawerLayout.closeDrawers();
    }

    /**
     * update the users completed section
     */
    private void updateCompleted(String completed){
        SharedPreferences settings = this.getSharedPreferences("com.matelau.junior.centsproject", Context.MODE_PRIVATE);
        int id = settings.getInt("ID", 0);
        UserService service = CentsApplication.get_centsRestAdapter().create(UserService.class);
        HashMap<String,String> useMobile = new HashMap<String, String>();
        useMobile.put("section", completed);
        service.updateCompletedData(id, useMobile, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {

            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(LOG_TAG, error.getMessage());

            }
        });
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
        //force return to home on logout
        showHome();
    }


    @Override
    protected void onResume() {
        //check if user is logged in or not modify nav drawer accordingly
        loginStatus();
        configureDrawer(CentsApplication.is_loggedIN());
        super.onResume();
        Log.d(LOG_TAG, "resumed");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "destroyed");
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

}

