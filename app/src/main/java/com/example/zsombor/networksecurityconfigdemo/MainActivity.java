package com.example.zsombor.networksecurityconfigdemo;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
    private Button downloadHTTPSButton;
    private EditText editTextField;

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
        downloadButton = (Button) findViewById(R.id.button);
        downloadHTTPSButton = (Button) findViewById(R.id.button2);
        editTextField = (EditText) findViewById(R.id.editText);

        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                try {
                    editTextField.setText("Loading!");
                    AsyncTask<String,Integer,String> rt = new ReadTask();
                    //rt.execute("https://metacortex.hu/~zsombor/test.html");
                    rt.execute("http://mrgsrv1.mrg-effitas.com/keepalive/alive2.html");
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

        downloadHTTPSButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                try {
                    editTextField.setText("Loading!");
                    AsyncTask<String,Integer,String> rt = new ReadTask();
                    //rt.execute("https://metacortex.hu/~zsombor/test.html");
                    rt.execute("https://mrgsrv1.mrg-effitas.com:9998/keepalive/alive3.html");
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
