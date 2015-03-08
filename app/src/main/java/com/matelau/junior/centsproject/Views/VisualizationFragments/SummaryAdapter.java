package com.matelau.junior.centsproject.Views.VisualizationFragments;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.matelau.junior.centsproject.Controllers.CentsApplication;
import com.matelau.junior.centsproject.Models.VizModels.ColiResponse;
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
    private ColiResponse _colResponse;
    private Context _context;
    public SummaryAdapter(int type, Context context){
        //Set Type -  0 COL Sum, 1 Major Sum, 2 College Sum, 3 Career Sum
        _context = context;
        _type = type;
        switch(_type){
            case 0:
                //get ColResponse
                _colResponse = CentsApplication.get_colResponse();
                break;
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
            case 0:
                return 4;
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
            case 0:
                createColSumView(position, leftVal, rightVal, catTitle);
                break;
            case 1:
                createMajorSumView(position, leftVal, rightVal, catTitle);
                break;

            default:
                catTitle.setText("ToDO BUILDME");
                break;
        }
        return rowView;
    }

    private void createColSumView(int position, TextView leftVal, TextView rightVal, TextView catTitle){
        //get necessary data from col response

        //avg cost of living cli_i[0]-100
        if(position == 0){
            catTitle.setText("AVERAGE COST OF LIVING");
            List<Double> cli1 = _colResponse.getCli1();
            List<Double> cli2 = _colResponse.getCli2();
            Double overall = cli1.get(0) - 100;
            String avgCost = costString(overall);
            leftVal.setText(overall+"%"+avgCost);
            if(cli2.size() > 0){
                overall = cli2.get(0) - 100;
                avgCost = costString(overall);
                rightVal.setText(overall+"%"+avgCost);
            }
            else{
                rightVal.setVisibility(View.GONE);
            }
        }
        //avg income - labor_i[1]
        if(position == 1){
            catTitle.setText("AVERAGE INCOME");
            List<Double> labor1 = _colResponse.getLabor1();
            List<Double> labor2 = _colResponse.getLabor2();
            Double income = labor1.get(1);
            leftVal.setText("$"+income);
            if(labor2.size() > 0){
                income = labor1.get(1);
                rightVal.setText("$"+income);
            }
            else{
                rightVal.setVisibility(View.GONE);
            }
        }
        //income tax range taxes_i[1]-taxes_i[2] if 1 != 2
        if(position == 2){
            catTitle.setText("INCOME TAX RANGE");
            List<Double> taxes1 = _colResponse.getTaxes1();
            List<Double> taxes2 = _colResponse.getTaxes2();
            Double tax1 = taxes1.get(1);
            Double tax2 = taxes1.get(2);
            String value = taxValues(tax1,tax2);
            leftVal.setText(value);
            if(taxes2.size() > 0){
                tax1 = taxes2.get(1);
                tax2 = taxes2.get(2);
                value = taxValues(tax1, tax2);
                rightVal.setText(value);
            }
            else{
                rightVal.setVisibility(View.GONE);
            }
        }
        //avg temp range weatherlow_i[0]-weather_i[length]
        if(position == 3){
            catTitle.setText("AVERAGE TEMPERATURES");
            List<Double> weatherLow1 = _colResponse.getWeatherlow1();
            List<Double> weather1 = _colResponse.getWeather1();
            List<Double> weatherLow2 = _colResponse.getWeatherlow2();
            List<Double> weather2 = _colResponse.getWeather2();

            String value = weatherLow1.get(0)+"°- "+weather1.get(13);
            leftVal.setText(value);
            if(weather2.size() > 0){
                value = weatherLow2.get(0)+"°- "+weather2.get(13);
                rightVal.setText(value);
            }
            else{
                rightVal.setVisibility(View.GONE);
            }
        }


    }

    private String costString(Double overall){
        String avgCost = "";
        if(overall > 0){
            avgCost = " ABOVE NATIONAL AVERAGE";

        }
        else{
            avgCost = " BELOW NATIONAL AVERAGE";
        }

        return avgCost;
    }

    private String taxValues(Double tax1, Double tax2){
        String value = "";
        if(tax1.equals(tax2)){
            value = tax1+"%";
        }
        else{
            value = tax1+"%-"+tax2+"%";
        }

        return value;


    }



    private void createMajorSumView(int position, TextView leftVal, TextView rightVal, TextView catTitle){
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
    }
}
