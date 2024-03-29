package com.matelau.junior.centsproject.Controllers;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.matelau.junior.centsproject.Models.CentsAPIServices.UserService;
import com.matelau.junior.centsproject.Models.UserModels.Id;
import com.matelau.junior.centsproject.Models.UserModels.Login;
import com.matelau.junior.centsproject.R;

import java.util.regex.Pattern;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginDialogFragment extends DialogFragment {

    private String LOG_TAG = LoginDialogFragment.class.getSimpleName();
    private EditText _email;
    private EditText _password;
    private TextView _errorMsg;
    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";


    public LoginDialogFragment() {
        // Required empty public constructor
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreateDialog");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        LinearLayout _rootLayout = (LinearLayout) inflater.inflate(R.layout.fragment_login_dialog, null, false);
        //get buttons
        Button _submit = (Button) _rootLayout.findViewById(R.id.login_submit);
        Button _cancel = (Button) _rootLayout.findViewById(R.id.login_cancel);
        //add listeners
        _cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        _submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Validate submission
                validateLogin();
            }
        });

        //Get Text
        _email = (EditText) _rootLayout.findViewById(R.id.user_email);
        _password = (EditText) _rootLayout.findViewById(R.id.user_password);

        //Error
        _errorMsg = (TextView) _rootLayout.findViewById(R.id.login_error);

        builder.setView(_rootLayout);
        builder.setCancelable(true);
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
     * Validates login submissions
     */
    private boolean validateLogin(){
        //remove previous messages
        _errorMsg.setVisibility(View.GONE);
        String error = "Email or Password is Incorrect\n";
        boolean passValid = true;
        boolean emailValid = true;
        //prevalidate locally before calling api
        //password must be 6 chars long no white space
        String pass = _password.getText().toString();
        String email = _email.getText().toString();
        if(pass.length() < 6){
            passValid = false;
        }

        emailValid = validateEmail(email);

        if(!passValid || !emailValid){
            _errorMsg.setTextColor(getResources().getColor(R.color.red));
            _errorMsg.setText(error);
            _errorMsg.setVisibility(View.VISIBLE);
        }
        else{
            _errorMsg.setTextColor(getResources().getColor(R.color.green));
            _errorMsg.setText("Logging In...");
            _errorMsg.setVisibility(View.VISIBLE);
            Log.d(LOG_TAG, "Logging In: "+email);


            //call api
            UserService service = CentsApplication.get_centsRestAdapter().create(UserService.class);
            service.login(new Login(email, pass), new Callback<Id>() {
                @Override
                public void success(Id id, Response response) {
                    Log.d(LOG_TAG, "Login Success id: "+id.getId());
                    _errorMsg.setVisibility(View.GONE);
                    //hide keyboard after submit
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(_password.getWindowToken(), 0);
                    Toast.makeText(getActivity(), "Login Success", Toast.LENGTH_SHORT).show();
                    //Store Login information and update app state
                    CentsApplication.set_loggedIN(true);
                    SharedPreferences settings = getActivity().getSharedPreferences("com.matelau.junior.centsproject", Context.MODE_PRIVATE);
                    settings.edit().
                            putString("EMAIL", _email.getText().toString()).
                            putString("PASSWORD", _password.getText().toString()).
                            putInt("ID", id.getId()).
                            apply();
                    dismiss();

                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e(LOG_TAG, error.getMessage());
                    Toast.makeText(getActivity(), "Error Please Try Again", Toast.LENGTH_SHORT).show();
                    //Remove Login information and update app state
                    CentsApplication.set_loggedIN(false);
                    SharedPreferences settings = getActivity().getSharedPreferences("com.matelau.junior.centsproject", Context.MODE_PRIVATE);
                    settings.edit().remove("EMAIL").remove("PASSWORD").remove("ID").apply();
                    _errorMsg.setTextColor(getResources().getColor(R.color.red));
                    _errorMsg.setText("Authentication Failed");
                    _errorMsg.setVisibility(View.VISIBLE);

                }
            });
        }

        return passValid && emailValid;

    }

    /**
     * local email validation
     * @param hex
     * @return
     */
    private boolean validateEmail(final String hex) {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        return pattern.matcher(hex).matches();
    }
}
