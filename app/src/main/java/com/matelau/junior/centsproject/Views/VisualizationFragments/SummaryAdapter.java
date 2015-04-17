package com.matelau.junior.centsproject.Views.VisualizationFragments;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.matelau.junior.centsproject.Controllers.CentsApplication;
import com.matelau.junior.centsproject.Models.VizModels.CareerResponse;
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
    private CareerResponse _cResponse;
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
                _cResponse = CentsApplication.get_cResponse();
                break;
        }
    }

    @Override
    public int getCount() {
        switch(_type){
            case 0:
                return 4;
            case 1:
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
        switch(_type){
            case 0:
                createColSumView(position, leftVal, rightVal,catTitle);
                break;
            case 1:
                createMajorSumView(position, leftVal, rightVal, catTitle);
                break;
            case 2:
                createCollegeSumView(position, leftVal, rightVal, catTitle);
                break;
            default:
                createCareerSumView(position, leftVal, rightVal, catTitle);
                break;
        }
        return rowView;
    }

    private void createCareerSumView(int position, TextView leftVal, TextView rightVal, TextView catTitle) {
        //get necessary data from col response
        //if the user searched for only one career hide second view
        boolean secondCareer = false;
        List<CareerResponse.Element> elements = _cResponse.getElements();
        if(elements.size() == 1){
            rightVal.setVisibility(View.GONE);
        }
        else{
            secondCareer = true;
        }
        if(position == 0){
            catTitle.setText("2013 AVERAGE SALARY");
            if(elements.get(0).getCareerSalary().get(0) != null){
                int val = elements.get(0).getCareerSalary().get(0).intValue();
                leftVal.setText("$" + val);
            }
            else{
                leftVal.setText("UNKNOWN");
            }
            if(secondCareer){
                if(elements.get(1).getCareerSalary().get(0) != null){
                    int val2 = elements.get(1).getCareerSalary().get(0).intValue();
                    rightVal.setText("$" + val2);
                }
                else{
                    rightVal.setText("UNKNOWN");
                }

            }
        }
        else if(position == 1){
            //update cents rating
            catTitle.setText("CENTS JOB RATING");
            if(elements.get(0).getCareerRating() != null){
                leftVal.setText("" + elements.get(0).getCareerRating() + " OUT OF 5.0");
            }
            if(secondCareer){
                if(elements.get(1).getCareerRating() != null){
                    rightVal.setText("" + elements.get(1).getCareerRating() + " OUT OF 5.0");
                }

            }
        }
        else if(position == 2){
            catTitle.setText("PROJECTED JOB DEMAND");
            if(elements.get(0).getCareerDemand().get(0) != null){
                int jDemand = elements.get(0).getCareerDemand().get(0).intValue();
                leftVal.setText(jDemand + " JOBS");
            }
            else{
                leftVal.setText("UNKNOWN");
            }

            if(secondCareer){
                if(elements.get(1).getCareerDemand().get(0) != null){
                    int jDemand2 = elements.get(1).getCareerDemand().get(0).intValue();
                    rightVal.setText(jDemand2 + " JOBS");
                }
                else{
                    rightVal.setText("UNKNOWN");
                }
            }
        }
        else if(position == 3){
            catTitle.setText("2012 UNEMPLOYMENT");
            if(elements.get(0).getCareerUnemploy().get(1) != null){
                leftVal.setText(elements.get(0).getCareerUnemploy().get(1) + "%");
            }
            else{
                leftVal.setText("UNKNOWN");
            }

            if(secondCareer){
                if(elements.get(1).getCareerUnemploy().get(1) != null){
                    rightVal.setText(elements.get(1).getCareerUnemploy().get(1) + "%");
                }
                else{
                    rightVal.setText("UNKNOWN");
                }
            }
        }
        leftVal.setTextSize(14);
        rightVal.setTextSize(14);
    }


    private void createCollegeSumView(int position, TextView leftVal, TextView rightVal, TextView catTitle){
        List<SchoolResponse.Element> elements = _sResponse.getElements();
        List<Double> school1 = elements.get(0).getSchool();
        List<Double> school2 = null;
        boolean hasSecondSchool = true;
        //hide school2 if no data
        if(elements.size() == 1){
            rightVal.setVisibility(View.GONE);
            hasSecondSchool = false;
        }
        else{
            school2 = elements.get(1).getSchool();
        }

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

            if(hasSecondSchool){
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
            if(hasSecondSchool){
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
            if(hasSecondSchool){
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
            if(hasSecondSchool){
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
            if(hasSecondSchool){
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
            if(hasSecondSchool){
                userRating = school2.get(5);
                if(userRating != null){
                    rightVal.setText(userRating+" OUT OF 5.0");
                }
                else{
                    rightVal.setText("UNKNOWN");
                }
            }
        }
    }


    private void createColSumView(int position, TextView leftVal, TextView rightVal, TextView catTitle){
        //get necessary data from col response
        //avg cost of living cli_i[0]-100
        List<ColiResponse.Element> elements = _colResponse.getElements();
        boolean hasSecondCity = false;
        if(elements.size() > 1){
            hasSecondCity = true;
        }
        if(position == 0){
            catTitle.setText("AVERAGE COST OF LIVING");
            List<Double> cli1 = elements.get(0).getCli();
            List<Double> cli2 = null;
            if(hasSecondCity){
                cli2 = elements.get(1).getCli();
            }
            Double overall = cli1.get(0) - 100;
            String avgCost = costString(overall);
            leftVal.setText(avgCost);
            leftVal.setTextSize(14);
            if(hasSecondCity){
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
            catTitle.setText("AVERAGE INCOME");
            List<Double> labor1 = elements.get(0).getLabor();
            List<Double> labor2 = null;
            if(hasSecondCity){
                labor2 = elements.get(1).getLabor();
            }
            int income = labor1.get(1).intValue();
            leftVal.setText("$"+income);
            if(hasSecondCity){
                income = labor2.get(1).intValue();
                rightVal.setText("$"+income);
            }
            else{
                rightVal.setVisibility(View.GONE);
            }
        }
        //income tax range taxes_i[1]-taxes_i[2] if 1 != 2
        else if(position == 2){
            catTitle.setText("INCOME TAX RANGE");
            List<Double> taxes1 = elements.get(0).getTaxes();
            List<Double> taxes2 = null;
            if(hasSecondCity){
                taxes2 = elements.get(1).getTaxes();
            }
            Double tax1 = taxes1.get(1);
            Double tax2 = taxes1.get(2);
            String value = taxValues(tax1,tax2);
            leftVal.setText(value);
            if(hasSecondCity){
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
        else{//if(position == 3)
            catTitle.setText("AVERAGE TEMPERATURES");
            List<Double> weatherLow1 = elements.get(0).getWeatherlow();
            List<Double> weather1 = elements.get(0).getWeather();
            List<Double> weatherLow2 = null;
            List<Double> weather2 = null;
            if(hasSecondCity){
                weatherLow2 = elements.get(1).getWeatherlow();
                weather2 = elements.get(1).getWeather();
            }

            String value = weatherLow1.get(0)+"°- "+weather1.get(13)+"°";
            leftVal.setText(value);
            if(hasSecondCity){
                value = weatherLow2.get(0)+"°- "+weather2.get(13)+"°";
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
        List<MajorResponse.Element> elements = _mResponse.getElements();
        List<Float> vals1 = elements.get(0).getDegree();
        List<Float> vals2 = null;
        if(elements.size() > 1){
            vals2 = elements.get(0).getDegree();
        }

        if(vals2 == null){
            rightVal.setVisibility(View.GONE);
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


    }
}
