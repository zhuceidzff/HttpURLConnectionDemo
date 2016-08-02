package com.example.lsx.httpurlconnectiondemo;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.renderscript.ScriptGroup;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class MainActivity extends AppCompatActivity {
    private TextView mTextview;
    private Button mButton;
    private EditText mEditText;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextview = (TextView) findViewById(R.id.activity_main_text_view);
        mButton = (Button) findViewById(R.id.activity_main_button);
        mEditText = (EditText) findViewById(R.id.activity_main_edit_text);

    }

    public void myClickHandler(View view){

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(
                Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()){
            Toast.makeText(MainActivity.this, "connected", Toast.LENGTH_SHORT).show();
            new DownloadWebpageTask().execute(mEditText.getText().toString());
        }
        else{
            mTextview.setText("no network connect available");
        }

    }
    class DownloadWebpageTask extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... urls) {
            try {
                return downLoad(urls[0]);
            } catch (IOException e) {
                return "Unable to download the webpage.";
            }

        }

        @Override
        protected void onPostExecute(String s) {
            mTextview.setText(s);
        }
    }
    private String downLoad(String url) throws IOException {
        InputStream is = null;
        int length = 500;
        try {
            URL myUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) myUrl.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            Log.d(TAG, "The response is: " + response);
            is = conn.getInputStream();

            // Convert the InputStream into a string
            String contentAsString = readIt(is, length);
            return contentAsString;
        } finally {
            if (is != null) {
                is.close();
            }
        }


    }

    private String readIt(InputStream is, int length) {
        Reader reader = null;
        char[] buffer = new char[length];
        try {
            reader = new InputStreamReader(is, "UTF-8");

            reader.read(buffer);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(buffer);
    }


}
