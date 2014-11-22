package com.matelau.junior.centsproject.Controllers;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.matelau.junior.centsproject.R;


public class MainActivity extends Activity {
//    Hex Color #884412
    private EditText _editText;
    private String classLogTag = "MainActivity";
    private String _city;
    private String _state;
    private String _occupation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_search);
        ImageButton search_submit = (ImageButton) findViewById(R.id.search_button);
        _editText = (EditText) findViewById(R.id.editText1);
        _editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        actionId == EditorInfo.IME_ACTION_DONE ||
                        event.getAction() == KeyEvent.ACTION_DOWN &&
                                event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                        handleSubmit();
                        return true;
                }
                    return false;
            }
        });
        search_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSubmit();
            }
        });
    }


    private void handleSubmit(){
        String searchText = _editText.getText().toString();
        Log.v(classLogTag, "handleSubmit: "+ searchText);
        if(validSubmission(searchText)){
            CharSequence cs = "Seaching for: "+ searchText;
            Toast.makeText(getApplicationContext(), cs  , Toast.LENGTH_SHORT).show();
            //TODO call glassdoor/jobs api
        }
        else{
            Toast.makeText(getApplicationContext(), "Invalid Search", Toast.LENGTH_SHORT).show();
        }


    }

    private boolean validSubmission(String searchText) {
        //insure searchText is of the form City, State, Occupation
        String value = searchText.replace(',', ' ');
        String[] tokens = value.split("\\s");
        //remove extra white spaces
        String[] cleansedTokens = new String[3];
        for(int i = 0; i < tokens.length; i++){
            if(!tokens[i].equals(' '))
                cleansedTokens[i] = tokens[i];
        }

        _city = cleansedTokens[0];
        _state = cleansedTokens[1];
        _occupation = cleansedTokens[2];
        if((_city != null) && (_city != "") && (_state != null) && (_state != "") && (_occupation != null) && (_occupation != "")  ) {
            Log.v(classLogTag, "validSubmission - city: "+ _city+" state: "+ _state+" occupation: "+_occupation );
            return true;
        }
        Log.v(classLogTag, "invalidSubmission");
       return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
