package com.matelau.junior.centsproject.Controllers;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.matelau.junior.centsproject.Models.CentsAPIModels.Login;
import com.matelau.junior.centsproject.Models.CentsAPIModels.LoginService;
import com.matelau.junior.centsproject.Models.Design.IndeedAPIModels.IndeedQueryResults;
import com.matelau.junior.centsproject.Models.Design.IndeedAPIModels.IndeedService;
import com.matelau.junior.centsproject.R;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.regex.Pattern;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginDialogFragment extends DialogFragment {

    private String LOG_TAG = LoginDialogFragment.class.getSimpleName();
    private LinearLayout _rootLayout;
    private Button _submit;
    private Button _cancel;
    private EditText _email;
    private EditText _password;
    private TextView _errorMsg;
    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";


    public LoginDialogFragment() {
        // Required empty public constructor
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreateDialog" );
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        _rootLayout = (LinearLayout) inflater.inflate(R.layout.fragment_login_dialog, null, false);
        //get buttons
        _submit = (Button) _rootLayout.findViewById(R.id.login_submit);
        _cancel = (Button) _rootLayout.findViewById(R.id.login_cancel);
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
//        builder.setCancelable(true);
        return builder.create();
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
        if(CentsApplication.isDebug()){
            email = "fake@xkcd.com";
            pass = "correcthorsebatterystaple";
        }

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
            LoginService service = CentsApplication.get_centsRestAdapter().create(LoginService.class);
            service.login(new Login(email, pass), new Callback<Response>() {
                @Override
                public void success(Response response, Response response2) {
                    Log.d(LOG_TAG, "Login Success ");
                    _errorMsg.setVisibility(View.GONE);
//                    String sResponse = response.().toString();
                    if(CentsApplication.isDebug())
                        Toast.makeText(getActivity(), "Login Success", Toast.LENGTH_SHORT).show();
                    //Store Login information and update app state
                    CentsApplication.set_loggedIN(true);
                    SharedPreferences settings = getActivity().getSharedPreferences("com.matelau.junior.centsproject", Context.MODE_PRIVATE);
                    settings.edit().
                            putString("EMAIL", _email.getText().toString()).
                            putString("PASSWORD", _password.getText().toString()).
                            commit();

                    //TODO update Navigation Drawer

                }

                @Override
                public void failure(RetrofitError error) {
                    try{
                        throw error.getCause();
                    }
                    catch(UnknownHostException e){
                        e.printStackTrace();
//                        e.getLocalizedMessage()
                        Log.e("Failure", e.getLocalizedMessage());

                    }
                     catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                    Log.e(LOG_TAG, error.getMessage());
                    //Remove Login information and update app state
                    CentsApplication.set_loggedIN(false);
                    SharedPreferences settings = getActivity().getSharedPreferences("com.matelau.junior.centsproject", Context.MODE_PRIVATE);
                    settings.edit().remove("EMAIL").remove("Password").commit();
                    _errorMsg.setTextColor(getResources().getColor(R.color.red));
                    _errorMsg.setText("Authentication Failed");
                    _errorMsg.setVisibility(View.VISIBLE);
                }
            });
        }



        return passValid && emailValid;

    }

    private boolean validateEmail(final String hex) {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        return pattern.matcher(hex).matches();
    }
}
