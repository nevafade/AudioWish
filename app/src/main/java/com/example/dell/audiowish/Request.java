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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Request extends AppCompatActivity {

    int requestIndex = 0 ;
    String nameString,authorString,emailString;
    EditText name,author,email;
    TextView submit;
    int successCode = 0 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        name = (EditText)findViewById(R.id.bookName);
        author = (EditText) findViewById(R.id.author);
        email = (EditText) findViewById(R.id.email);
        submit = (TextView)findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Sending...",Toast.LENGTH_LONG).show();
                fetchFirebaseData();
            }
        });
    }

    public void sendFirebaseData(int index) {
        try {
            requestIndex = index;
            new FirebaseWriter(this.getApplicationContext()).execute(new URL("https://audiowish-c37e3.firebaseio.com/Requests/queue/"+index+".json"),
                    new URL("https://audiowish-c37e3.firebaseio.com/Requests/size.json"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public  void fetchFirebaseData(){
        try {
            nameString = name.getText().toString();
            authorString = author.getText().toString();
            emailString = email.getText().toString();
            new FirebaseFetcher(this.getApplicationContext()).execute(new URL("https://audiowish-c37e3.firebaseio.com/Requests/size.json"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public class FirebaseWriter extends AsyncTask<URL,Integer,String> {
        private final Context context;
        HttpURLConnection postConnection;
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
                outputStream.writeBytes("{\"name\":\"" + nameString + "\" , "
                        + "\"author\":\"" + authorString + "\" ,"
                        + "\"email\":\"" + emailString + "\""
                        + "}");
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
                outputStream.writeBytes("{ \"count\":" + (requestIndex + 1) + "}");
                outputStream.flush();
                outputStream.close();
                final int responseCode = postConnection.getResponseCode();
                successCode = successCode + responseCode ;
                Request.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(successCode == 600){
                            Toast.makeText(getApplicationContext(), "Request Sent", Toast.LENGTH_LONG).show();
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
        HttpURLConnection getConnection;
        JSONObject requestJSON;
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
                successCode = successCode + responseCode ;
                BufferedReader br = new BufferedReader(new InputStreamReader(getConnection.getInputStream()));
                String line = "";
                final StringBuilder responseOutput = new StringBuilder();
                while((line = br.readLine()) != null ) {
                    responseOutput.append(line);
                }
                br.close();
                Request.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            requestJSON = new JSONObject(responseOutput.toString());
                            requestIndex = requestJSON.getInt("count");
                            sendFirebaseData(requestIndex);
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
