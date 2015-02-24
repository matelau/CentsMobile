package com.matelau.junior.centsproject.Views.VisualizationFragments.SpendingBreakdown;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.matelau.junior.centsproject.Controllers.CentsApplication;
import com.matelau.junior.centsproject.R;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by matelau on 2/21/15.
 */
public class SpendingBreakdownRecycleAdapter extends RecyclerView.Adapter<SpendingBreakdownRecycleAdapter.SpendingBreakdownJobViewHolder> {
    private String LOG_TAG = SpendingBreakdownRecycleAdapter.class.getSimpleName();
    private List<String> _attributes;
    private List<Float> _values;
    private Context _context;
    private float _income;
    private int[] _colors;

    public SpendingBreakdownRecycleAdapter(List<String> attributes, List<Float> values, Context context){
        _attributes = attributes;
        _values = values;
        _context = context;
        _colors = CentsApplication.get_colors();
        try{
            _income = Float.parseFloat(CentsApplication.get_occupationSalary())/12f;
        }catch(NumberFormatException e) { e.printStackTrace(); }

    }

    @Override
    public SpendingBreakdownJobViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Log.d(LOG_TAG, "onCreateView");
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.spending_breakdown_mod_element,viewGroup, false);
        _context = viewGroup.getContext();
        return new SpendingBreakdownJobViewHolder(itemView);
    }


    public void updateLists(){
        Log.d(LOG_TAG, "updateLists");
        _attributes = CentsApplication.get_sbLabels();
        _values = CentsApplication.get_sbPercents();

//        notifyItemInserted(_attributes.size());
//        notifyItemRangeChanged(0, _values.size());
//        notifyDataSetChanged();

    }
    @Override
    public void onBindViewHolder(SpendingBreakdownJobViewHolder holder, int position) {
        Log.d(LOG_TAG, "onBindView");
        //update the views
        holder._attribute.setText(_attributes.get(position));
        holder._attribute.setTextColor(_colors[position]);
        holder._pos = position;

        //only show two decimal places in values
        DecimalFormat df = new DecimalFormat("##.##");
        df.setRoundingMode(RoundingMode.DOWN);
        Float val =  _values.get(position) * _income;
        holder._value.setText(df.format(val));
        holder._value.addTextChangedListener(new TextWatcher() {
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
                Log.d(LOG_TAG, "value: "+value+" percent: "+percent+ " salary: "+CentsApplication.get_occupationSalary());

                //verify numeric value
//                boolean number = true;
//                for(int i = 0; i < value.length(); i++){
//                    char c = value.charAt(i);
//
//                    if(!Character.isDigit(c))
//                        number = false;
//
//                }
//                if(!number){
//                    Toast.makeText(_context, "Invalid Number - No special characters", Toast.LENGTH_SHORT).show();
//                    s.clear();
//                }
//                else{
//
//                }


            }
        });

    }






    @Override
    public int getItemCount() {
        return _attributes.size();
    }


    public class SpendingBreakdownJobViewHolder extends RecyclerView.ViewHolder {

        private String LOG_TAG = SpendingBreakdownJobViewHolder.class.getSimpleName();
        protected TextView _attribute;
        protected EditText _value;
        protected int _pos;


        public SpendingBreakdownJobViewHolder(View itemView) {
            super(itemView);
            Log.v(LOG_TAG, "Constructor");
            //Pull Portions of CardView
            _attribute = (TextView) itemView.findViewById(R.id.attr_text);
            _value = (EditText) itemView.findViewById(R.id.editText1);
        }

    }
}
