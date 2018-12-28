package com.example.dell.audiowish;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Feedback extends AppCompatActivity {

    HttpURLConnection postConnection;
    HttpURLConnection getConnection;
    TextView submitFeedback;
    EditText comment;
    String commentString;
    JSONObject feedbackJSON;
    int feedbackIndex;
    int successCode = 0 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        submitFeedback = (TextView) findViewById(R.id.submit_feedback);
        submitFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Sending...",Toast.LENGTH_LONG).show();
                fetchFirebaseData(comment.getText().toString());
            }
        });
        comment = (EditText) findViewById(R.id.editText);
    }

    public  void fetchFirebaseData(String comment){
        try {
            commentString = comment;
            new FirebaseFetcher(this.getApplicationContext()).execute(new URL("https://audiowish-c37e3.firebaseio.com/Feedback/size.json"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void sendFirebaseData(int index) {
        try {
            new FirebaseWriter(this.getApplicationContext()).execute(new URL("https://audiowish-c37e3.firebaseio.com/Feedback/queue/"+index+".json"),
                    new URL("https://audiowish-c37e3.firebaseio.com/Feedback/size.json"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }


    public class FirebaseWriter extends AsyncTask<URL,Integer,String> {
        private final Context context;
        public FirebaseWriter(Context c){
            this.context = c;
        }


        protected void onPreExecute(){

        }


        protected void onPostExecute(){

        }



        @Override
        protected String doInBackground(URL... params) {
            try {
                postConnection = (HttpURLConnection) params[0].openConnection();
                postConnection.setRequestMethod("PUT");
                postConnection.setDoOutput(true);
                DataOutputStream outputStream = new DataOutputStream(postConnection.getOutputStream());
                outputStream.writeBytes("{\"comment\":\"" + commentString + "\"}");
                outputStream.flush();
                outputStream.close();
                final int responseCode = postConnection.getResponseCode();
                successCode = successCode + responseCode ;
            } catch (IOException e) {
                e.printStackTrace();
            }


            try {
                postConnection = (HttpURLConnection) params[1].openConnection();
                postConnection.setRequestMethod("PUT");
                postConnection.setDoOutput(true);
                DataOutputStream outputStream = new DataOutputStream(postConnection.getOutputStream());
                outputStream.writeBytes("{ \"count\":" + (feedbackIndex + 1) + "}");
                outputStream.flush();
                outputStream.close();
                final int responseCode = postConnection.getResponseCode();
                successCode =  successCode + responseCode ;
                Feedback.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(successCode == 600){
                            Toast.makeText(getApplicationContext(), "Feedback Sent", Toast.LENGTH_LONG).show();
                        }

                    }
                });


            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    public class FirebaseFetcher extends AsyncTask<URL,Integer,String> {
        private final Context context;
        int responseCode;
        public FirebaseFetcher(Context c){
            this.context = c;
        }


        protected void onPreExecute(){

        }


        protected void onPostExecute(){

        }



        @Override
        protected String doInBackground(URL... params) {
            try {
                getConnection = (HttpURLConnection) params[0].openConnection();
                getConnection.setRequestMethod("GET");
                responseCode = getConnection.getResponseCode();
                successCode = successCode + responseCode;
                BufferedReader br = new BufferedReader(new InputStreamReader(getConnection.getInputStream()));
                String line = "";
                final StringBuilder responseOutput = new StringBuilder();
                while((line = br.readLine()) != null ) {
                    responseOutput.append(line);
                }
                br.close();
                Feedback.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            feedbackJSON = new JSONObject(responseOutput.toString());
                            feedbackIndex = feedbackJSON.getInt("count");
                            sendFirebaseData(feedbackIndex);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;

        }
    }

}
