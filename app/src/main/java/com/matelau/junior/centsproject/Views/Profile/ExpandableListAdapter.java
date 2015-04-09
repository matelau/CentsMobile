package com.matelau.junior.centsproject.Views.Profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.matelau.junior.centsproject.Controllers.CentsApplication;
import com.matelau.junior.centsproject.Models.CentsAPIServices.CareerService;
import com.matelau.junior.centsproject.Models.CentsAPIServices.MajorService;
import com.matelau.junior.centsproject.Models.CentsAPIServices.SchoolService;
import com.matelau.junior.centsproject.Models.CentsAPIServices.UserService;
import com.matelau.junior.centsproject.Models.UserModels.Field;
import com.matelau.junior.centsproject.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by matelau on 3/25/15.
 */
public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private String LOG_TAG = ExpandableListAdapter.class.getSimpleName();
    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;
    private String[] _ratings;

    public ExpandableListAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<String>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        String childText = (String) getChild(groupPosition, childPosition);
        SharedPreferences settings = _context.getSharedPreferences("com.matelau.junior.centsproject", Context.MODE_PRIVATE);
        final int userId = settings.getInt("ID", 0);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.expandable_list_cat_child, null);
        }

        Spinner spinnerRating = (Spinner) convertView.findViewById(R.id.spinner_rating);
        spinnerRating.setVisibility(View.GONE);
        ProgressBar pb = (ProgressBar) convertView.findViewById(R.id.user_progress);
        pb.setVisibility(View.GONE);

        if (childPosition == getChildrenCount(groupPosition) - 1) {
            convertView.setPadding(0, 0, 0, 10);
        } else
            convertView.setPadding(0, 0, 0, 0);

        //ratings section 2,3,4
        if(groupPosition >= 2 && groupPosition < 5){
            final int ratingVal = Integer.parseInt(childText.substring(childText.indexOf(':') + 1, childText.length()).trim());
            final String element = childText.substring(0, childText.indexOf(':')).trim();
            final HashMap<String, Integer> user = new HashMap<String, Integer>();
            user.put("user", userId);
            childText = element;
            _ratings = _context.getResources().getStringArray(R.array.rating_values);
            ArrayAdapter<String> ratingAdapter = new ArrayAdapter<String>(_context, android.R.layout.simple_spinner_dropdown_item, _ratings);
            spinnerRating.setAdapter(ratingAdapter);
            spinnerRating.setSelection(ratingVal);
            spinnerRating.setVisibility(View.VISIBLE);
            if(groupPosition == 2){
                //major
                final String level = childText.substring(childText.indexOf(",")+1, childText.length()).trim();
                final String major = childText.substring(0, childText.indexOf(",")).trim();
                Log.d(LOG_TAG, "level: "+level+", Major: "+ major);
                spinnerRating.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
                        MajorService service = CentsApplication.get_centsRestAdapter().create(MajorService.class);
                        service.rateMajor(level, major, position, user, new Callback<Response>() {
                            @Override
                            public void success(Response response, Response response2) {
                                Log.d(LOG_TAG, "Success");
                                //update list
                                //_listDataChild.get(_listDataChild.get(_listDataHeader.get(2)).set(childPosition, ""+position));

                            }

                            @Override
                            public void failure(RetrofitError error) {
                                Log.e(LOG_TAG, error.getMessage());
                                Toast.makeText(_context, "Error - Please try again later", Toast.LENGTH_SHORT).show();

                            }
                        });
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

            }
            else if(groupPosition == 3){
                //update university ratings
                spinnerRating.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
                        SchoolService service = CentsApplication.get_centsRestAdapter().create(SchoolService.class);
                        service.rateSchool(element, position, user, new Callback<Response>() {
                            @Override
                            public void success(Response response, Response response2) {
                                Log.d(LOG_TAG, "Success");
                                 //update list
                                //_listDataChild.get(_listDataChild.get(_listDataHeader.get(3)).set(childPosition, ""+position));
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                Log.e(LOG_TAG, error.getMessage());
                                Toast.makeText(_context, "Error - Please try again later", Toast.LENGTH_SHORT).show();

                            }
                        });
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        //do nothing
                    }
                });
            }
            else{
                //update career rating
                spinnerRating.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view,final int position, long id) {

                        CareerService service = CentsApplication.get_centsRestAdapter().create(CareerService.class);
                        service.rateCareer(element, position, user, new Callback<Response>() {
                            @Override
                            public void success(Response response, Response response2) {
                                Log.d(LOG_TAG, "Success");
                                //update list
                                //_listDataChild.get(_listDataChild.get(_listDataHeader.get(4)).set(childPosition, ""+position));

                            }

                            @Override
                            public void failure(RetrofitError error) {
                                Log.e(LOG_TAG, error.getMessage());
                                Toast.makeText(_context, "Error - Please try again later", Toast.LENGTH_SHORT).show();

                            }
                        });
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

            }
        }
        //completed sections
        if(groupPosition == 6){
            //show progressbar
            if(childText.contains("Progress :")){
                int numberOfComps = Integer.parseInt(childText.substring(childText.indexOf(":")+1,childText.indexOf("/")).trim());
                float progress = (numberOfComps /13f)*100f;
                pb.setProgress((int) progress);
                pb.setVisibility(View.VISIBLE);
                childText = numberOfComps+"/13";
            }


        }

        //preferences
        if(groupPosition == 5){
            //hide default layout
            convertView.findViewById(R.id.profile_data).setVisibility(View.GONE);
            convertView.findViewById(R.id.profile_preferences).setVisibility(View.VISIBLE);
            Switch acSwitch = (Switch) convertView.findViewById(R.id.auto_complete_switch);
            boolean checked = settings.getBoolean("Autocomplete", true);
            acSwitch.setChecked(checked);
            acSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    SharedPreferences settings = _context.getSharedPreferences("com.matelau.junior.centsproject", Context.MODE_PRIVATE);
                    settings.edit().
                        putBoolean("Autocomplete", isChecked).
                        apply();
                    //update db
                    UserService service = CentsApplication.get_centsRestAdapter().create(UserService.class);
                    Field f = new Field();
                    f.setName("prefers_autocomplete");
                    f.setValue("" + isChecked);
                    List<Field> fs = new ArrayList<Field>();
                    fs.add(f);
                    HashMap<String, List<Field>> fields = new HashMap<String, List<Field>>();
                    fields.put("fields", fs);
                    service.updateFields(userId, fields, new Callback<Response>() {
                        @Override
                        public void success(Response response, Response response2) {
                            Log.d(LOG_TAG, "update prefers autocomplete success");

                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Log.e(LOG_TAG, error.getMessage());
                        }
                    });
                }
            });

        }
        else{
            //profile info
            convertView.findViewById(R.id.profile_data).setVisibility(View.VISIBLE);
            convertView.findViewById(R.id.profile_preferences).setVisibility(View.GONE);
            TextView txtListChild = (TextView) convertView
                    .findViewById(R.id.lblListItem);
            txtListChild.setText(childText);
        }

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.expandable_list_cat_header, null);
        }
        if (isExpanded)
            convertView.setPadding(0, 0, 0, 0);
        else
            convertView.setPadding(0, 0, 0, 10);

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
