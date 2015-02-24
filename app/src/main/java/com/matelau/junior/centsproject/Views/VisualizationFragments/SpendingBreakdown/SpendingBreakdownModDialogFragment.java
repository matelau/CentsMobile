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

import com.matelau.junior.centsproject.Controllers.CentsApplication;
import com.matelau.junior.centsproject.Controllers.VisualizationPagerFragment;
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
    private ListView _sbAttributes;
    private LinearLayoutManager _sbLayoutManager;
    private SBArrayAdapter _rAdapter;
    private List<String> _sbAttr;
    private List<Float> _sbAttrVals;


    public SpendingBreakdownModDialogFragment() {
        // Required empty public constructor
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//
//        _rootLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_spending_breakdown_mod_dialog, null, false);
//        _circle = (RelativeLayout) _rootLayout.findViewById(R.id.plus_spending_category);
//        _spending_plus = (ImageButton) _circle.findViewById(R.id.circle_btn);
//        //add more cats
//        _spending_plus.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d(LOG_TAG, "Spending onClick");
//                //TODO add spinning animation
//                _spending_plus.startAnimation((AnimationUtils.loadAnimation(getActivity(), R.anim.rotate)));
//                FragmentManager fm = getActivity().getSupportFragmentManager();
//                SpendingBreakdownAttributeAdditionDialogFragment addition = new SpendingBreakdownAttributeAdditionDialogFragment();
//                addition.setTargetFragment(fm.findFragmentById(R.id.fragment_placeholder), 01);
//                addition.show(fm, "tag");
//            }
//        });
//
//        //******  setup Attribute List ********************
//        //get Attr
//        _sbAttr = CentsApplication.get_sbLabels();
//        _sbAttrVals = CentsApplication.get_sbPercents();
//        //setup dynamic listview
//        _sbAttributes = (RecyclerView) _rootLayout.findViewById(R.id.sb_attr_list);
//        _sbAttributes.setHasFixedSize(false);
//        _sbLayoutManager = new LinearLayoutManager(getActivity());
//        _sbLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        _sbAttributes.setLayoutManager(_sbLayoutManager);
//        _rAdapter = new SpendingBreakdownRecycleAdapter(_sbAttr, _sbAttrVals, getActivity());
//        _sbAttributes.setAdapter(_rAdapter);
//
//        return _rootLayout;
//    }

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

//        _sbAttributes = (android.support.v7.widget.RecyclerView) _rootLayout.findViewById(R.id.sb_attr_list);
//        _sbAttributes.setHasFixedSize(false);
//        _sbLayoutManager = new LinearLayoutManager(getActivity());
//        _sbLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

//        _sbAttributes.setLayoutManager(_sbLayoutManager);
//        _rAdapter = new SpendingBreakdownRecycleAdapter(_sbAttr, _sbAttrVals, getActivity());
//        CentsApplication.set_rAdapter(_rAdapter);
//        _sbAttributes.setHasFixedSize(true);
//        _sbAttributes.setAdapter(_rAdapter);
//        CentsApplication.set_sbAttributes(_sbAttributes);


        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //validate values



//                ------ normalize values to 1




                // add values to array
//                ArrayList<String> labels = (ArrayList<String>) CentsApplication.get_sbLabels();
//
//                String dollarAmt = _value.getText().toString();
//                Float monthPercent = CentsApplication.convDollarToPercent(dollarAmt);
//                CentsApplication.get_sbPercents().add(monthPercent);
//                Log.d(LOG_TAG, "amt= "+monthPercent);
//                Log.d(LOG_TAG, "onClick Switch to ViewPager");
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
        List<String> _labels;
        List<Float> _percents;

        public SBArrayAdapter(){
            Log.v(LOG_TAG, "SBARRAY - create");
            _percents = CentsApplication.get_sbPercents();
            _labels = CentsApplication.get_sbLabels();

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
            //modify subviews
            et.setText(CentsApplication.convPercentToDollar(_percents.get(position)));
            tv.setText(_labels.get(position));

            //add listeners
            et.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String value = s.toString();
                    Float percent = CentsApplication.convDollarToPercent(value);
                    _percents.remove(position);
                    _percents.add(position, percent);
                    notifyDataSetChanged();

                }
            });
            return rowView;
        }

        @Override
        public int getCount() {
            return _labels.size();
        }

        @Override
        public Object getItem(int position) {
            return _labels.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }


    }
}
