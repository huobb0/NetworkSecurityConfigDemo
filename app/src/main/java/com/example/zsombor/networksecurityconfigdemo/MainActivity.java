package com.example.zsombor.networksecurityconfigdemo;

import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity {

    private Button downloadButton;
    private EditText editTextField;
    private Integer select;
    private RadioButton selfSignedButton;
    private RadioButton letsEncryptButton;
    private RadioButton customCAButton;
    private RadioButton plainTextButton;

    private class ReadTask extends AsyncTask<String, Integer, String>
    {
        @Override
        protected String doInBackground(String... params)
        {
            return getResponseFromUrl(params[0]);
        }

        private String getResponseFromUrl(String param) {
            try {
                URL url = new URL(param);
                URLConnection urlConnection = url.openConnection();
                InputStream in = urlConnection.getInputStream();
                BufferedReader r = new BufferedReader(new InputStreamReader(in));
                StringBuilder total = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    total.append(line);
                }
                Log.d("x", total.toString());
                return total.toString();
            }catch (MalformedURLException e) {
                e.printStackTrace();
                return "M";
            } catch (IOException e) {
                e.printStackTrace();
                return e.getMessage();
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        select = new Integer(1);
        downloadButton = (Button) findViewById(R.id.button);
        editTextField = (EditText) findViewById(R.id.editText);

        letsEncryptButton = (RadioButton)findViewById(R.id.radio_ninjas);
        customCAButton = (RadioButton) findViewById(R.id.radio_ninjas2);
        plainTextButton = (RadioButton) findViewById(R.id.radio_ninjas3);

        letsEncryptButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0){
                editTextField.setText("Proper cert at https://zs.labs.defdev.eu:443");
                select = 3;
            }
        });

        customCAButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0){
                editTextField.setText("Custom CA pinned at https://zs.labs.defdev.eu:444");
                select = 2;
            }
        });

        plainTextButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0){
                editTextField.setText("Plain text on http://zs.labs.defdev.eu");
                select = 1;
            }
        });
        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                try {
                    AsyncTask<String,Integer,String> rt = new ReadTask();
                    if(select == 1){
                        rt.execute("http://zs.labs.defdev.eu/success.html");
                    } else if (select == 2){
                        rt.execute("https://zs.labs.defdev.eu:444/success.html");
                    } else if (select == 3){
                        rt.execute("https://zs.labs.defdev.eu/success.html");
                    }
                    String back = rt.get();
                    editTextField.setText(back);
                }catch(Exception e){
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    e.printStackTrace(pw);
                    editTextField.setText(sw.toString());
                }
                }

            });
    }
}
