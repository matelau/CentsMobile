package com.matelau.junior.centsproject.Views.Profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.matelau.junior.centsproject.Controllers.CentsApplication;
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
        EditText rating = (EditText) convertView.findViewById(R.id.edit_rating);
        rating.setVisibility(View.GONE);

        String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.expandable_list_cat_child, null);
        }

        if (childPosition == getChildrenCount(groupPosition) - 1) {
            convertView.setPadding(0, 0, 0, 20);
        } else
            convertView.setPadding(0, 0, 0, 0);

        if(groupPosition > 2 && groupPosition != 5){
            String ratingVal = childText.substring(childText.indexOf(':'), childText.length()).trim();
            rating.setText(ratingVal);
            childText = childText.substring(0,childText.indexOf(':')).trim();
            rating.setVisibility(View.VISIBLE);
            if(groupPosition == 2){
                //major

            }
            else if(groupPosition == 3){
                //university

            }
            else{
                //career

            }
        }
        //preferences
        if(groupPosition == 5){
            //hide default layout
            convertView.findViewById(R.id.profile_data).setVisibility(View.GONE);
            convertView.findViewById(R.id.profile_preferences).setVisibility(View.VISIBLE);
            Switch acSwitch = (Switch) convertView.findViewById(R.id.auto_complete_switch);
            SharedPreferences settings = _context.getSharedPreferences("com.matelau.junior.centsproject", Context.MODE_PRIVATE);
            final int id = settings.getInt("ID", 0);
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
                    service.updateFields(id, fields, new Callback<Response>() {
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
            convertView.setPadding(0, 0, 0, 20);

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
