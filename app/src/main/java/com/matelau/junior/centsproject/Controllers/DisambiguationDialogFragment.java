package com.matelau.junior.centsproject.Controllers;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.matelau.junior.centsproject.Models.AmbigElement;
import com.matelau.junior.centsproject.Models.VizModels.CareerResponse;
import com.matelau.junior.centsproject.Models.VizModels.ColiResponse;
import com.matelau.junior.centsproject.Models.VizModels.MajorResponse;
import com.matelau.junior.centsproject.Models.VizModels.SchoolResponse;
import com.matelau.junior.centsproject.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class DisambiguationDialogFragment extends DialogFragment {
    private final String LOG_TAG = DisambiguationDialogFragment.class.getSimpleName();
    private AmbigAdapter _adapter;

    private int _type;
    private ArrayList<String> _options;

    public DisambiguationDialogFragment() {
        // Required empty public constructor
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        //get values
        Bundle mArgs = getArguments();
        _type = mArgs.getInt("type");
        _options = mArgs.getStringArrayList("elements");
        ArrayList<AmbigElement> elements = new ArrayList<AmbigElement>();
        for(String s: _options){
            AmbigElement element = new AmbigElement();
            element.setName(s);
            element.setIsChecked(false);
            elements.add(element);
        }

        RelativeLayout _rootLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_disambiguation_dialog, null, false);
        ListView _ambiguousList = (ListView) _rootLayout.findViewById(R.id.sb_attr_list);
        _adapter = new AmbigAdapter(getActivity(), R.layout.ambigous_element, elements);
        _ambiguousList.setAdapter(_adapter);

        builder.setTitle("Select one or two options");
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showHome();
                dismiss();
            }
        });

        builder.setPositiveButton("submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch(_type){
                    case 0:
                        majorSelection();
                        break;
                    case 1:
                        careerSelection();
                        break;
                    case 2:
                        citySelection();
                        break;
                    case 3:
                        schoolSelection();
                        break;
                }
            }
        });

        builder.setView(_rootLayout);
        return builder.create();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "resumed");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "destroyed");
    }

    /**
     * handle school selection
     */
    private void schoolSelection(){
        //get response
        SchoolResponse response = CentsApplication.get_sApiResponse();
        //get elements
        List<SchoolResponse.Element> elements = response.getElements();
        //get selections
        ArrayList<AmbigElement> selections = _adapter.elements;
        //update response
        ArrayList<SchoolResponse.Element> selectedElements = new ArrayList<SchoolResponse.Element>();
        for(int i = 0; i <  selections.size(); i++){
            if(selections.get(i).isChecked()){
                selectedElements.add(elements.get(i));
            }
        }
        response.setElements(selectedElements);
        CentsApplication.set_sApiResponse(response);
        CentsApplication.set_selectedVis("College Comparison");
        switchToVizPager();

    }

    /**
     * handle city selection
     */
    private void citySelection(){
        //get response
        ColiResponse response = CentsApplication.get_colResponse();
        //get elements
        List<ColiResponse.Element> elements = response.getElements();
        //get selections
        ArrayList<AmbigElement> selections = _adapter.elements;
        //update response
        ArrayList<ColiResponse.Element> selectedElements = new ArrayList<ColiResponse.Element>();
        for(int i = 0; i <  selections.size(); i++){
            if(selections.get(i).isChecked()){
                selectedElements.add(elements.get(i));
            }
        }
        response.setElements(selectedElements);
        CentsApplication.set_colResponse(response);
        CentsApplication.set_selectedVis("COL Comparison");
        switchToVizPager();

    }

    /**
     * handle career selection
     */
    private void careerSelection(){
        //get response
        CareerResponse response = CentsApplication.get_cResponse();
        //get elements
        List<CareerResponse.Element> elements = response.getElements();
        //get selections
        ArrayList<AmbigElement> selections = _adapter.elements;
        //update response
        ArrayList<CareerResponse.Element> selectedElements = new ArrayList<CareerResponse.Element>();
        for(int i = 0; i <  selections.size(); i++){
            if(selections.get(i).isChecked()){
                selectedElements.add(elements.get(i));
            }
        }
        response.setElements(selectedElements);
        CentsApplication.set_cResponse(response);
        CentsApplication.set_selectedVis("Career Comparison");
        switchToVizPager();

    }

    /**
     * handle major selection
     */
    private void majorSelection(){
        //get response
        MajorResponse mResponse = CentsApplication.get_mResponse();
        //get elements
        List<MajorResponse.Element> elements = mResponse.getElements();
        //get selections
        ArrayList<AmbigElement> selections = _adapter.elements;
        //update response
        ArrayList<MajorResponse.Element> selectedElements = new ArrayList<MajorResponse.Element>();
        for(int i = 0; i <  selections.size(); i++){
            if(selections.get(i).isChecked()){
                selectedElements.add(elements.get(i));
            }
        }
        mResponse.setElements(selectedElements);
        CentsApplication.set_mResponse(mResponse);
        CentsApplication.set_selectedVis("Major Comparison");
        switchToVizPager();
    }


    /**
     * Switch to appropriate vis
     */
    private void switchToVizPager(){
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_placeholder, new VisualizationPagerFragment());
        ft.addToBackStack("main-search");
        ft.commit();
    }

    /**
     * Set placeholder to main search view
     */
    private void showHome(){
        //Home
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        getActivity().getActionBar().setTitle("Cents");
        //Attach Search Fragment
        // Replace the container with the new fragment
        ft.replace(R.id.fragment_placeholder, new SearchFragment());
        ft.addToBackStack("main-search");
        // Execute the changes specified
        ft.commit();
    }

    /**
     * Adapter used to display disambiguation options within a listview
     */
    private class AmbigAdapter extends ArrayAdapter<AmbigElement> {
        public ArrayList<AmbigElement> elements;
        private Context _context;

        public AmbigAdapter(Context context, int textViewResourceId, ArrayList<AmbigElement> objects) {
            super(context, textViewResourceId, objects);
            elements = new ArrayList<AmbigElement>();
            elements.addAll(objects);
            this._context = context;

        }

        private class ViewHolder{
            CheckBox element;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;

            if(convertView == null){
                LayoutInflater vi = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView =  vi.inflate(R.layout.ambigous_element, null);
                holder = new ViewHolder();
                holder.element = (CheckBox) convertView.findViewById(R.id.checkBox1);
                convertView.setTag(holder);
                holder.element.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v;
                        //todo make sure only two elements are checked
                        AmbigElement element = (AmbigElement) cb.getTag();
                        //unselected element
                        if(!cb.isChecked()){
                            element.setIsChecked(false);
                        }
                        //either first or second selection
                        else if(!twoSelected()){
                            element.setIsChecked(cb.isChecked());
                        }
                        else{
                            Toast.makeText(_context, "You May Only Compare Two Options", Toast.LENGTH_SHORT).show();
                            cb.setChecked(false);
                        }

                    }
                });
            }
            else{
                holder = (ViewHolder) convertView.getTag();
            }

            AmbigElement element = elements.get(position);
            holder.element.setText(element.getName());
            holder.element.setChecked(element.isChecked());
            holder.element.setTag(element);

            return convertView;
        }

        /**
         * checks if two values have already been selected
         * @return
         */
        private boolean twoSelected(){
            int count = 0;
            for(AmbigElement curr: elements){
                if(curr.isChecked()){
                    count++;
                }
                if(count == 2){
                    return true;
                }
            }

            return false;

        }
    }
}
