package com.matelau.junior.centsproject.Views.VisualizationFragments.SpendingBreakdown;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
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
import com.matelau.junior.centsproject.Models.VizModels.SpendingBreakdownCategory;
import com.matelau.junior.centsproject.R;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SpendingBreakdownModDialogFragment extends DialogFragment {
    private String LOG_TAG = SpendingBreakdownModDialogFragment.class.getSimpleName();
    private RelativeLayout _rootLayout;
    private RelativeLayout _circle;
    private ImageButton _spending_plus;
    private ImageButton _lock;
    private ListView _sbAttributes;
    private LinearLayoutManager _sbLayoutManager;
    private SBArrayAdapter _rAdapter;
    private List<String> _sbAttr;
    private List<Float> _sbAttrVals;


    public SpendingBreakdownModDialogFragment() {
        // Required empty public constructor
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "OnCreateDialog");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        _rootLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_spending_breakdown_mod_dialog, null, false);
        _circle = (RelativeLayout) _rootLayout.findViewById(R.id.plus_spending_category);
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
        _sbAttr = CentsApplication.get_sbLabels();
        _sbAttrVals = CentsApplication.get_sbPercents();

        //setup dynamic listview
        _sbAttributes = (ListView) _rootLayout.findViewById(R.id.sb_attr_list);
        _rAdapter = new SBArrayAdapter();
        CentsApplication.set_rAdapter(_rAdapter);
        _sbAttributes.setAdapter(_rAdapter);


        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
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
//                _category.setHint("Category");
            }
        });

        builder.setView(_rootLayout);
        return builder.create();
    }




    public class SBArrayAdapter extends BaseAdapter{
//        List<String> _labels;
//        List<Float> _percents;
        List<SpendingBreakdownCategory> _values;

        public SBArrayAdapter(){
            Log.v(LOG_TAG, "SBARRAY - create");
//            _percents = CentsApplication.get_sbPercents();
//            _labels = CentsApplication.get_sbLabels();
            _values = CentsApplication.get_sbValues();

        }


        public void add(){
            notifyDataSetChanged();

        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            Log.v(LOG_TAG, "SBARRAY - getView");
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.spending_breakdown_mod_element, parent, false);
            //get subviews
            EditText et = (EditText) rowView.findViewById(R.id.editText1);
            TextView tv = (TextView) rowView.findViewById(R.id.attr_text);
            ImageButton lock = (ImageButton) rowView.findViewById(R.id.lock);
            ImageButton delete = (ImageButton) rowView.findViewById(R.id.delete);

            //modify subviews
            et.setText(CentsApplication.convPercentToDollar(_values.get(position)._percent));
            tv.setText(_values.get(position)._category);
            if(_values.get(position)._locked) {
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
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String value = s.toString();
                    Float percent = CentsApplication.convDollarToPercent(value);
                    _values.get(position)._percent = percent;
                    String file = CentsApplication.get_currentBreakdown()+".dat";
                    CentsApplication.saveSB(file, getActivity());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
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
                    notifyDataSetChanged();
                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(_values.get(position)._locked){
                        Toast.makeText(getActivity(),"You cannot delete locked items.", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        _values.remove(position);
                    }
                    notifyDataSetChanged();

                }
            });

            return rowView;
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
