package com.matelau.junior.centsproject.Controllers;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.matelau.junior.centsproject.R;

import java.util.regex.Pattern;

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
        // Inflate the layout for this fragment
        _rootLayout = (LinearLayout) inflater.inflate(R.layout.fragment_registration, container, false);
        _firstName = (EditText) _rootLayout.findViewById(R.id.first_name);
        _lastName = (EditText) _rootLayout.findViewById(R.id.last_name);
        _email = (EditText) _rootLayout.findViewById(R.id.user_email);
        _password = (EditText) _rootLayout.findViewById(R.id.user_password);
        _confirmPassword = (EditText) _rootLayout.findViewById(R.id.confirm_password);
        _submit = (Button) _rootLayout.findViewById(R.id.register_submit);
        _messages = (TextView) _rootLayout.findViewById(R.id.registration_msg);
        _submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO register
                if(validateSubmision().equals("")){
                    if(CentsApplication.isDebug())
                        Toast.makeText(getActivity(), "Registering - "+_email.getText().toString(), Toast.LENGTH_SHORT).show();
                    //TODO MAKE API CALL

                }
                else{
                    _messages.setText("Make Sure Passwords Match and Email is Valid");
                    _messages.setTextColor(getResources().getColor(R.color.red));
                }


            }
        });
        return _rootLayout;
    }


    private String validateSubmision(){
        //names must be letters only
        String message = "";
        String fname = _firstName.getText().toString();
        String lname = _lastName.getText().toString();
        boolean validNames = true;
        if(fname.length() < 1 || lname.length() < 1)
            validNames = false;
        if(!isAlpha(fname) || !isAlpha(lname))
            validNames = false;

        //password must be 6-70 chars
//        validateEmail(_email.getText().toString()) && (_password.equals(_confirmPassword));
        return message;
    }

    public boolean isAlpha(String name) {
        return name.matches("[a-zA-Z]+");
    }


    private boolean validateEmail(final String hex) {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        return pattern.matcher(hex).matches();
    }


}
