package com.matelau.junior.centsproject.Views.VisualizationFragments;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.matelau.junior.centsproject.Controllers.CentsApplication;
import com.matelau.junior.centsproject.Models.VizModels.MajorResponse;
import com.matelau.junior.centsproject.R;

import java.util.List;

/**
 * Created by matelau on 3/5/15.
 */
public class SummaryAdapter extends BaseAdapter {
    private String LOG_TAG = SummaryAdapter.class.getSimpleName();
    private int _type = -1;
    private MajorResponse _mResponse;
    private Context _context;
    public SummaryAdapter(int type, Context context){
        //Set Type -  0 COL Sum, 1 Major Sum, 2 College Sum, 3 Career Sum
        _context = context;
        _type = type;
        switch(_type){
            case 1:
                //get MajorResponse
                _mResponse = CentsApplication.get_mResponse();
                break;
            default:
                break;
        }
    }

    @Override
    public int getCount() {
        switch(_type){
            case 1:
                return _mResponse.getMajor1().size();
            default:
                return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.v(LOG_TAG, "SummaryAdapter - getView: "+position);
        LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.summary_element, parent, false);
        //summary view has left right and cat title
        TextView catTitle = (TextView) rowView.findViewById(R.id.element_category);
        TextView leftVal = (TextView) rowView.findViewById(R.id.cat1_value);
        TextView rightVal = (TextView) rowView.findViewById(R.id.cat2_value);
        switch(_type){
            case 1:
                List<Float> vals1 = _mResponse.getMajor1();
                List<Float> vals2 = _mResponse.getMajor2();
                if(vals2.size() == 0){
                    vals2 = null;
                }

                //major sum consists of avg sal, major rec, maj sat, cents major rating
                if(position == 0){
                    catTitle.setText("AVERAGE SALARY");
                    if(vals1.get(position) != null){
                        leftVal.setText("$"+vals1.get(position).intValue());
                    }
                    else{
                        leftVal.setText("Unknown");
                    }
                    if(vals2 != null){
                        if(vals2.get(position) != null)
                            rightVal.setText("$"+vals2.get(position).intValue());
                        else
                            rightVal.setText("Unknown");

                    }
                    else{
                        rightVal.setVisibility(View.GONE);
                    }
                }
                else if(position == 1){
                    catTitle.setText("MAJOR RECOMMENDATION");
                    if(vals1.get(position) != null){
                        leftVal.setText(vals1.get(position).intValue()+" OUT OF 100");
                    }
                    else{
                        leftVal.setText("Unknown");
                    }
                    if(vals2 != null){
                        if(vals2.get(position) != null)
                            rightVal.setText(vals2.get(position).intValue()+ " OUT OF 100");
                        else
                            rightVal.setText("Unknown");
                    }
                    else{
                        rightVal.setVisibility(View.GONE);
                    }
                }
                else if(position == 2){
                    catTitle.setText("MAJOR SATISFACTION");
                    if(vals1.get(position) != null){
                        leftVal.setText(vals1.get(position).intValue()+" OUT OF 100");
                    }
                    else{
                        leftVal.setText("Unknown");
                    }
                    if(vals2 != null){
                        if(vals2.get(position) != null)
                            rightVal.setText(vals2.get(position).intValue()+ " OUT OF 100");
                        else
                            rightVal.setText("Unknown");
                    }
                    else{
                        rightVal.setVisibility(View.GONE);
                    }
                }
                else if(position == 3){
                    catTitle.setText("CENTS MAJOR RATING");
                    if(vals1.get(position) != null){
                        leftVal.setText(vals1.get(position)+" OUT OF 5.0");
                    }
                    else{
                        leftVal.setText("Unknown");
                    }
                    if(vals2 != null){
                        if(vals2.get(position) != null)
                            rightVal.setText(vals2.get(position)+ " OUT OF 5.0");
                        else
                            rightVal.setText("Unknown");

                    }
                    else{
                        rightVal.setVisibility(View.GONE);
                    }
                }
                break;

            default:
                catTitle.setText("ToDO BUILDME");
                break;
        }
        return rowView;
    }
}
