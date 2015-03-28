package com.matelau.junior.centsproject.Views.VisualizationFragments;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.matelau.junior.centsproject.Controllers.CentsApplication;
import com.matelau.junior.centsproject.Models.VizModels.ColiResponse;
import com.matelau.junior.centsproject.Models.VizModels.MajorResponse;
import com.matelau.junior.centsproject.Models.VizModels.SchoolResponse;
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
    private SchoolResponse _sResponse;
    private Context _context;
    public SummaryAdapter(int type, Context context){
        //Set Type -  0 COL Sum, 1 Major Sum, 2 College Sum, 3 Career Sum
        Log.d(LOG_TAG, "Type: "+type);
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
            case 2:
                //get schoolResponse
                _sResponse = CentsApplication.get_sApiResponse();
                break;
            default:
                //get careerResponse
                break;
        }
    }

    @Override
    public int getCount() {
        switch(_type){
            case 0:
                //+1 for add
                return 5;
            case 1:
                if( _mResponse != null)
                    return _mResponse.getMajor1().size();
                else
                    return 4;
            case 2:
                return 6;
            default:
                return 4;
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
        AdView ad = (AdView) rowView.findViewById(R.id.adView);
        switch(_type){
            case 0:
                createColSumView(position, leftVal, rightVal, ad, catTitle);
                break;
            case 1:
                createMajorSumView(position, leftVal, rightVal, catTitle);
                break;
            case 2:
                createCollegeSumView(position, leftVal, rightVal, catTitle);
                break;
            default:
                createCareerSumView(position, leftVal, rightVal, ad, catTitle);
                break;
        }
        return rowView;
    }

    private void createCareerSumView(int position, TextView leftVal, TextView rightVal, AdView ad, TextView catTitle) {
        //get necessary data from col response
        if(position == 0){
            ad.setVisibility(View.GONE);
            catTitle.setText("2013 AVERAGE SALARY");
            leftVal.setText("$88000");
            leftVal.setTextSize(14);
            rightVal.setText("$88000");
            rightVal.setTextSize(14);
//            if(cli2.size() > 0){
//                overall = cli2.get(0) - 100;
//                avgCost = costString(overall);
//                rightVal.setText(avgCost);
//                rightVal.setTextSize(14);
//            }
//            else{
//                rightVal.setVisibility(View.GONE);
//            }
        }
        else if(position == 1){
            ad.setVisibility(View.GONE);
            catTitle.setText("CENTS JOB RATING");
            leftVal.setText("4.8 OUT OF 5.0");
            leftVal.setTextSize(14);
            rightVal.setText("2.9 OUT OF 5.0");
            rightVal.setTextSize(14);
//            if(labor2.size() > 0){
//                income = labor1.get(1).intValue();
//                rightVal.setText("$"+income);
//            }
//            else{
//                rightVal.setVisibility(View.GONE);
//            }
        }
        else if(position == 2){
            ad.setVisibility(View.GONE);
            catTitle.setText("PROJECTED JOB DEMAND");
            leftVal.setText("353,200 JOBS");
            leftVal.setTextSize(14);
            rightVal.setText("35,000 JOBS");
            rightVal.setTextSize(14);
//            if(taxes2.size() > 0){
//                tax1 = taxes2.get(1);
//                tax2 = taxes2.get(2);
//                value = taxValues(tax1, tax2);
//                rightVal.setText(value);
//            }
//            else{
//                rightVal.setVisibility(View.GONE);
//            }
        }
        else if(position == 3){
            ad.setVisibility(View.GONE);
            catTitle.setText("2012 UNEMPLOYMENT");
            leftVal.setText("3.8%");
            leftVal.setTextSize(14);
            rightVal.setText("8.1%");
            rightVal.setTextSize(14);
//            if(weather2.size() > 0){
//                value = weatherLow2.get(0)+"°- "+weather2.get(13)+"°";
//                rightVal.setText(value);
//            }
//            else{
//                rightVal.setVisibility(View.GONE);
//            }
        }
//        else if(position == 4){
//            //show ad hide other views
//            catTitle.setText("SPONSORS");
//            AdRequest adRequest = new AdRequest.Builder().build();
//            if(CentsApplication.isDebug())
//                adRequest = new AdRequest.Builder().addTestDevice("84B46C4862CAF80187170C1A7901502C").build();
//            ad.loadAd(adRequest);
//            rightVal.setVisibility(View.GONE);
//            leftVal.setVisibility(View.GONE);
//        }

    }


    private void createCollegeSumView(int position, TextView leftVal, TextView rightVal, TextView catTitle){
        List<Double> school1 = _sResponse.getSchool1();
        List<Double> school2 = _sResponse.getSchool2();
        //all fields could possibly be null - must check
        //ntl ranking [4]
        if(position == 0){
            catTitle.setText("NATIONAL RANKING");
            Double dRank = school1.get(4);
            if(dRank != null){
                int rank = dRank.intValue();
                leftVal.setText(rank + " OUT OF 200");
            }
            else{
                leftVal.setText("UNRANKED");
            }

            if(school2.size() > 0){
                dRank = school2.get(4);
                if(dRank != null){
                    int rank = dRank.intValue();
                    rightVal.setText(rank + " OUT OF 200");
                }
                else{
                    rightVal.setText("UNRANKED");
                }
            }
        }
        //in-state tuition [0]
        else if(position == 1){
            catTitle.setText("IN STATE TUITION");
            Double tuition = school1.get(0);
            if(tuition != null){
                int t = tuition.intValue();
                leftVal.setText("$"+t);
            }
            else{
                leftVal.setText("UNKNOWN");
            }
            if(school2.size() > 0){
                tuition = school2.get(0);
                if(tuition != null){
                    int t = tuition.intValue();
                    rightVal.setText("$"+t);
                }
                else{
                    rightVal.setText("UNKNOWN");
                }
            }
        }
        //out-state tuition [1]
        else if(position == 2){
            catTitle.setText("OUT OF STATE TUITION");
            Double tuition = school1.get(1);
            if(tuition != null){
                int t = tuition.intValue();
                leftVal.setText("$"+t);
            }
            else{
                leftVal.setText("UNKNOWN");
            }
            if(school2.size() > 0){
                tuition = school2.get(1);
                if(tuition != null){
                    int t = tuition.intValue();
                    rightVal.setText("$"+t);
                }
                else{
                    rightVal.setText("UNKNOWN");
                }
            }
        }
        //grad rate [2]
        else if(position == 3){
            catTitle.setText("GRADUATION RATE");
            Double gRate = school1.get(2);
            if(gRate != null){
                int t = gRate.intValue();
                leftVal.setText(t+"%");
            }
            else{
                leftVal.setText("UNKNOWN");
            }
            if(school2.size() > 0){
                gRate = school2.get(2);
                if(gRate != null){
                    int t = gRate.intValue();
                    rightVal.setText(t+"%");
                }
                else{
                    rightVal.setText("UNKNOWN");
                }
            }
        }
        //undergrad enrollment [3]
        else if(position == 4){
            catTitle.setText("UNDERGRAD ENROLLMENT (# OF STUDENTS)");
            Double enrollment = school1.get(3);
            if(enrollment != null){
                int t = enrollment.intValue();
                leftVal.setText(""+t);
            }
            else{
                leftVal.setText("UNKNOWN");
            }
            if(school2.size() > 0){
                enrollment = school2.get(3);
                if(enrollment != null){
                    int t = enrollment.intValue();
                    rightVal.setText(""+t);
                }
                else{
                    rightVal.setText("UNKNOWN");
                }
            }
        }
        //cents user rating [5]
        else{
            catTitle.setText("CENTS USER RATING");
            Double userRating = school1.get(5);
            if(userRating != null){
                leftVal.setText(userRating+" OUT OF 5.0");
            }
            else{
                leftVal.setText("UNKNOWN");
            }
            if(school2.size() > 0){
                userRating = school2.get(5);
                if(userRating != null){
                    rightVal.setText(userRating+" OUT OF 5.0");
                }
                else{
                    rightVal.setText("UNKNOWN");
                }
            }
        }


        //hide school2 if no data
        if(school2.size() == 0){
            rightVal.setVisibility(View.GONE);
            leftVal.setGravity(Gravity.CENTER_HORIZONTAL);
        }

    }


    private void createColSumView(int position, TextView leftVal, TextView rightVal, AdView ad, TextView catTitle){
        //get necessary data from col response
        //avg cost of living cli_i[0]-100
        if(position == 0){
            ad.setVisibility(View.GONE);
            catTitle.setText("AVERAGE COST OF LIVING");
            List<Double> cli1 = _colResponse.getCli1();
            List<Double> cli2 = _colResponse.getCli2();
            Double overall = cli1.get(0) - 100;
            String avgCost = costString(overall);
            leftVal.setText(avgCost);
            leftVal.setTextSize(14);
            if(cli2.size() > 0){
                overall = cli2.get(0) - 100;
                avgCost = costString(overall);
                rightVal.setText(avgCost);
                rightVal.setTextSize(14);
            }
            else{
                rightVal.setVisibility(View.GONE);
            }
        }
        //avg income - labor_i[1]
        else if(position == 1){
            ad.setVisibility(View.GONE);
            catTitle.setText("AVERAGE INCOME");
            List<Double> labor1 = _colResponse.getLabor1();
            List<Double> labor2 = _colResponse.getLabor2();
            int income = labor1.get(1).intValue();
            leftVal.setText("$"+income);
            if(labor2.size() > 0){
                income = labor1.get(1).intValue();
                rightVal.setText("$"+income);
            }
            else{
                rightVal.setVisibility(View.GONE);
            }
        }
        //income tax range taxes_i[1]-taxes_i[2] if 1 != 2
        else if(position == 2){
            ad.setVisibility(View.GONE);
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
        else if(position == 3){
            ad.setVisibility(View.GONE);
            catTitle.setText("AVERAGE TEMPERATURES");
            List<Double> weatherLow1 = _colResponse.getWeatherlow1();
            List<Double> weather1 = _colResponse.getWeather1();
            List<Double> weatherLow2 = _colResponse.getWeatherlow2();
            List<Double> weather2 = _colResponse.getWeather2();

            String value = weatherLow1.get(0)+"°- "+weather1.get(13)+"°";
            leftVal.setText(value);
            if(weather2.size() > 0){
                value = weatherLow2.get(0)+"°- "+weather2.get(13)+"°";
                rightVal.setText(value);
            }
            else{
                rightVal.setVisibility(View.GONE);
            }
        }
        else if(position == 4){
            //show ad hide other views
            catTitle.setText("SPONSORS");
            AdRequest adRequest = new AdRequest.Builder().build();
            if(CentsApplication.isDebug())
                 adRequest = new AdRequest.Builder().addTestDevice("84B46C4862CAF80187170C1A7901502C").build();
            ad.loadAd(adRequest);
            rightVal.setVisibility(View.GONE);
            leftVal.setVisibility(View.GONE);
        }


    }

    private String costString(Double overall){
        String avgCost = "";
        if(overall > 0){
            avgCost = Math.abs(overall)+"% ABOVE NATIONAL AVERAGE";

        }
        else{
            avgCost = Math.abs(overall)+"% BELOW NATIONAL AVERAGE";
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
        }

        if(vals2 == null){
            rightVal.setVisibility(View.GONE);
            leftVal.setGravity(Gravity.CENTER_HORIZONTAL);
        }
    }
}
