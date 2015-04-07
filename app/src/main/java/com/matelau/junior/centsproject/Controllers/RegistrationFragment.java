package com.matelau.junior.centsproject.Controllers;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.matelau.junior.centsproject.Models.CentsAPIServices.RegisterService;
import com.matelau.junior.centsproject.Models.UserModels.User;
import com.matelau.junior.centsproject.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegistrationFragment extends Fragment {

    private String LOG_TAG = RegistrationFragment.class.getSimpleName();
    private LinearLayout _rootLayout;
    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    private EditText _firstName;
    private EditText _lastName;
    private EditText _email;
    private EditText _password;
    private EditText _confirmPassword;
    private Button _submit;
    private TextView _messages;


    public RegistrationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(LOG_TAG, "onCreateView");
        //Get all the views that will need to be validated or updated
        _rootLayout = (LinearLayout) inflater.inflate(R.layout.fragment_registration, container, false);
        _firstName = (EditText) _rootLayout.findViewById(R.id.first_name);
        _lastName = (EditText) _rootLayout.findViewById(R.id.last_name);
        _email = (EditText) _rootLayout.findViewById(R.id.user_email);
        _password = (EditText) _rootLayout.findViewById(R.id.user_password);
        _confirmPassword = (EditText) _rootLayout.findViewById(R.id.confirm_password);
        _submit = (Button) _rootLayout.findViewById(R.id.register_submit);
        _messages = (TextView) _rootLayout.findViewById(R.id.registration_msg);
        _messages.setVisibility(View.VISIBLE);
        //TODO add email type to registration
        //on Submit
        _submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //register
                String message = validateSubmision();
                _messages.setText("Registering..");
                _messages.setTextColor(getResources().getColor(R.color.green));
                _messages.invalidate();
                if(message.equals("")){
                    if(CentsApplication.isDebug())
                        Toast.makeText(getActivity(), "Registering - "+_email.getText().toString(), Toast.LENGTH_SHORT).show();
                    RegisterService service = CentsApplication.get_centsRestAdapter().create(RegisterService.class);
                    //added trim - white space is getting added by view on the end of text
                    String fname = _firstName.getText().toString().trim();
                    String lname = _lastName.getText().toString().trim();
                    String pass = _password.getText().toString();
                    String confirm = _confirmPassword.getText().toString();
                    service.register(new User(fname, lname,_email.getText().toString(), pass, confirm, "HTML"), new Callback<Response>() {
                        @Override
                        public void success(Response response, Response response2) {
                            //Store User information
                            Log.d(LOG_TAG, "Register Success");
                            CentsApplication.set_loggedIN(true);
                            SharedPreferences settings = getActivity().getSharedPreferences("com.matelau.junior.centsproject", Context.MODE_PRIVATE);
                            settings.edit().
                                    putString("EMAIL", _email.getText().toString()).
                                    putString("PASSWORD", _password.getText().toString()).
                                    commit();

                            //return to searchFrag
                            showSearch();
                            if(CentsApplication.isDebug())
                                Toast.makeText(getActivity(), "Registered", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Log.e(LOG_TAG, error.getMessage());
                            //set error message by parsing response body
                            BufferedReader reader = null;
                            StringBuilder sb = new StringBuilder();
                            try {
                                reader = new BufferedReader(new InputStreamReader(error.getResponse().getBody().in()));
                                String line;
                                try {
                                    while ((line = reader.readLine()) != null) {
                                        sb.append(line);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            String rsp = sb.toString();
                            Log.e(LOG_TAG, rsp);
                            _messages.setText("Registration Error - Try Again");
                            _messages.setTextColor(getResources().getColor(R.color.red));
                            if(CentsApplication.isDebug())
                                Toast.makeText(getActivity(), rsp, Toast.LENGTH_SHORT).show();
                        }
                    });

                }
                else{
                    _messages.setText(message);
                    _messages.setTextColor(getResources().getColor(R.color.red));
                    _messages.invalidate();
                }
            }
        });
        return _rootLayout;
    }

    /*
     * Replaces registration with the searchFragment
     */
    private void showSearch(){
        //update title
        getActivity().getActionBar().setTitle("Cents");
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_placeholder, new SearchFragment()).addToBackStack("registration").commit();
        showRegistrationEmailNotice();


    }

    /**
     * Loads the Wizard ontop of current view
     */
    private void showRegistrationEmailNotice(){
        FragmentManager fm = getActivity().getSupportFragmentManager();
        RegistrationConfirmationDialogFragment confirmation = new RegistrationConfirmationDialogFragment();
        confirmation.setTargetFragment(fm.findFragmentById(R.id.fragment_placeholder), 01);
        confirmation.show(fm, "tag");
    }

    /**
     * Returns Error message if invalid empty string otherwise
     * @return
     */
    private String validateSubmision(){
        //names must be letters only
        String message = "";
        String fname = _firstName.getText().toString();
        String lname = _lastName.getText().toString();
        String pass = _password.getText().toString();
        String confirm = _confirmPassword.getText().toString();
        boolean validNames = true;
        if(fname.length() < 1 || lname.length() < 1)
            validNames = false;
        if(!isAlpha(fname) || !isAlpha(lname))
            validNames = false;

        boolean validPass = true;
        //password must be 6-70 chars
        if(pass.length() < 6 || pass.length() > 70 || !pass.equals(confirm))
            validPass = false;

        if(!validNames)
            message += "First name or Last name is invalid \n";
        if(!validPass)
            message += "Invalid Password \n";
        if(!validateEmail(_email.getText().toString()))
            message += "Invalid email \n";

//        validateEmail() && (_password.equals(_confirmPassword));
        return message;
    }

    /**
     * returns true if string only contains alpha chars
     * @param name
     * @return
     */
    private boolean isAlpha(String name) {
        boolean isAlpha = name.matches("^.*[^a-zA-Z].*$");
        return isAlpha;
    }

    /**
     * checks against regex for valid email
     * @param hex
     * @return
     */
    private boolean validateEmail(final String hex) {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        return pattern.matcher(hex).matches();
    }

}
