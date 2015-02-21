package com.matelau.junior.centsproject.Views.VisualizationFragments.SpendingBreakdown;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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

    @Override
    public void onBindViewHolder(SpendingBreakdownJobViewHolder holder, int position) {
        Log.d(LOG_TAG, "onBindView");
        //update the views
        holder._attribute.setText(_attributes.get(position));
        holder._attribute.setTextColor(_colors[position]);

        //only show two decimal places in values
        DecimalFormat df = new DecimalFormat("##.##");
        df.setRoundingMode(RoundingMode.DOWN);
        Float val =  _values.get(position) * _income;
//        String al = df.format(va);
        holder._value.setText(df.format(val));

    }


    @Override
    public int getItemCount() {
        return _attributes.size();
    }


    public class SpendingBreakdownJobViewHolder extends RecyclerView.ViewHolder {

        private String LOG_TAG = SpendingBreakdownJobViewHolder.class.getSimpleName();

        protected TextView _attribute;
        protected EditText _value;


        public SpendingBreakdownJobViewHolder(View itemView) {
            super(itemView);
            Log.v(LOG_TAG, "Constructor");
            //Pull Portions of CardView
            _attribute = (TextView) itemView.findViewById(R.id.attr_text);
            _value = (EditText) itemView.findViewById(R.id.editText1);
        }

    }
}
