package com.matelau.junior.centsproject.Views.VisualizationFragments.SpendingBreakdown;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.matelau.junior.centsproject.Controllers.CentsApplication;
import com.matelau.junior.centsproject.Controllers.VisualizationPagerFragment;
import com.matelau.junior.centsproject.Models.CentsAPIServices.UserService;
import com.matelau.junior.centsproject.Models.VizModels.SpendingBreakdownCategory;
import com.matelau.junior.centsproject.R;

import java.util.HashMap;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class SpendingBreakdownModDialogFragment extends DialogFragment {
    private String LOG_TAG = SpendingBreakdownModDialogFragment.class.getSimpleName();
    private ImageButton _spending_plus;
    private int _sizeBeforeMod;


    public SpendingBreakdownModDialogFragment() {
        // Required empty public constructor
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "OnCreateDialog");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        RelativeLayout _rootLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_spending_breakdown_mod_dialog, null, false);
        RelativeLayout _circle = (RelativeLayout) _rootLayout.findViewById(R.id.plus_spending_category);
        _spending_plus = (ImageButton) _circle.findViewById(R.id.circle_btn);

        //add more cats
        _spending_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LOG_TAG, "Spending onClick");
                //TODO add spinning animation
                _spending_plus.startAnimation((AnimationUtils.loadAnimation(getActivity(), R.anim.rotate)));
                FragmentManager fm = getActivity().getSupportFragmentManager();
                SpendingBreakdownAttributeAdditionDialogFragment addition = new SpendingBreakdownAttributeAdditionDialogFragment();
                addition.setTargetFragment(fm.findFragmentById(R.id.fragment_placeholder), 01);
                addition.show(fm, "tag");
            }
        });

        //******  setup Attribute List ********************
        //get Attr
        List<String> _sbAttr = CentsApplication.get_sbLabels();
        List<Float> _sbAttrVals = CentsApplication.get_sbPercents();
        _sizeBeforeMod = CentsApplication.get_sbValues().size();

        //setup dynamic listview
        ListView _sbAttributes = (ListView) _rootLayout.findViewById(R.id.sb_attr_list);
        final SBArrayAdapter _rAdapter = new SBArrayAdapter();
        CentsApplication.set_rAdapter(_rAdapter);
        _sbAttributes.setAdapter(_rAdapter);



        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //save all changes via api on submit if logged in
                _rAdapter.save(true);
                //reload viz
                CentsApplication.set_selectedVis("Spending Breakdown");
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_placeholder, new VisualizationPagerFragment());
                ft.commit();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               //remove values added before cancel
                if(CentsApplication.get_sbValues().size() != _sizeBeforeMod ){
                    for(int i = _sizeBeforeMod - 1; i < CentsApplication.get_sbValues().size(); i++ ){
                        List<SpendingBreakdownCategory> vals = CentsApplication.get_sbValues();
                        vals.remove(i);
                        CentsApplication.set_sbValues(vals);
                    }

                }
            }
        });

        builder.setView(_rootLayout);
        return builder.create();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "Destroyed");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "Resumed");
    }


    /**
     * Adapter to handle the modification of spending breakdown elements
     */
    public class SBArrayAdapter extends BaseAdapter{
        List<SpendingBreakdownCategory> _values;

        public SBArrayAdapter(){
            _values = CentsApplication.get_sbValues();
        }

        /**
         * updates Application context with new List
         */
        public void add(){
            _values = CentsApplication.get_sbValues();
            notifyDataSetChanged();



        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
//            Log.v(LOG_TAG, "SBARRAY - getView");
            SpendingBreakdownCategory sbc = _values.get(position);
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.spending_breakdown_mod_element, parent, false);

            //get subviews
            EditText et = (EditText) rowView.findViewById(R.id.editText1);
            TextView tv = (TextView) rowView.findViewById(R.id.attr_text);
            ImageButton lock = (ImageButton) rowView.findViewById(R.id.lock);
            ImageButton delete = (ImageButton) rowView.findViewById(R.id.delete);
            if(sbc._locked){
                Log.d(LOG_TAG, "Locked position: "+position+" percent: "+sbc._percent+" category: "+sbc._category);
            }

            //modify subviews
            if(position == 0){
                //taxed value is different
                et.setText(CentsApplication.convPercentToDollar(sbc._percent, true));
            }
            else{
                et.setText(CentsApplication.convPercentToDollar(sbc._percent, false));
                if(_values.get(position)._locked){
                    Log.d(LOG_TAG, "Locked cat: "+sbc._category+" percent: "+sbc._percent);
                }

            }

            tv.setText(_values.get(position)._category);
            if(sbc._locked) {
                lock.setBackground(getResources().getDrawable(R.drawable.lock_color_small));
                delete.setVisibility(View.GONE);
                et.setEnabled(false);
            }
            else{
                et.setEnabled(true);
            }

            //add listeners
            et.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String value = s.toString();
                    Float percent = CentsApplication.convDollarToPercent(value, false);
                    if(position == 0){
                        percent = CentsApplication.convDollarToPercent(value, true);

                    }

                    _values.get(position)._percent = percent;
                    save(false);
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });

            lock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageButton lock = (ImageButton) v;
                    // flip lock state, flip image
                    if(_values.get(position)._locked){
                        _values.get(position)._locked = false;
                        lock.setBackground(getResources().getDrawable(R.drawable.unlock_gray_small));
                    }
                    else{
                        _values.get(position)._locked = true;
                        lock.setBackground(getResources().getDrawable(R.drawable.lock_color_small));
                    }
                    //save change
                    save(false);

                    notifyDataSetChanged();
                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(position == 0){
                        Toast.makeText(getActivity(),"Two things in life are certain death and taxes... You cannot delete Taxes.", Toast.LENGTH_SHORT).show();
                        _values.get(position)._locked = true;
                    }
                    else{
                        _values.remove(position);
                    }
                    //save change
                    save(true);
                    notifyDataSetChanged();

                }
            });

            return rowView;
        }


        /**
         * Stores values to local storage and db if logged in
         */
        public void save(boolean saveAPI){
            String filename = CentsApplication.get_currentBreakdown()+".dat";
            CentsApplication.saveSB(filename, getActivity());
            if(CentsApplication.is_loggedIN() && saveAPI){
                updateCompleted("Create Custom Spending");
                //store sb to db via api
                HashMap<String, String> elements = new HashMap<String,String>();
                for(SpendingBreakdownCategory current : _values){
                    //dont store taxes it will be calculated
                    if(!current._category.equals("TAXES")){
                        float percent = current._percent * 100f;
                        elements.put(current._category, ""+percent);
                    }
                }
                SharedPreferences settings = getActivity().getSharedPreferences("com.matelau.junior.centsproject", Context.MODE_PRIVATE);
                int _id = settings.getInt("ID", 0);

                HashMap<String, HashMap<String, String>> fields = new HashMap<String, HashMap<String,String>>();
                fields.put("fields", elements);
                UserService service = CentsApplication.get_centsRestAdapter().create(UserService.class);
                service.initSpendingFields(_id, CentsApplication.get_currentBreakdown(), fields, new Callback<Response>() {
                    @Override
                    public void success(Response response, Response response2) {
                        Log.d(LOG_TAG, "updated spending records for: " + CentsApplication.get_currentBreakdown());
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.e(LOG_TAG, error.getMessage());
                    }
                });

            }

        }

        /**
         * update the users completed section
         */
        private void updateCompleted(String completed){
            SharedPreferences settings = getActivity().getSharedPreferences("com.matelau.junior.centsproject", Context.MODE_PRIVATE);
            int id = settings.getInt("ID", 0);
            UserService service = CentsApplication.get_centsRestAdapter().create(UserService.class);
            HashMap<String,String> completedTask = new HashMap<String, String>();
            completedTask.put("section", completed);
            service.updateCompletedData(id, completedTask, new Callback<Response>() {
                @Override
                public void success(Response response, Response response2) {

                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e(LOG_TAG, error.getMessage());

                }
            });
        }


        @Override
        public int getCount() {
            return _values.size();
        }

        @Override
        public Object getItem(int position) {
            return _values.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }


    }
}
