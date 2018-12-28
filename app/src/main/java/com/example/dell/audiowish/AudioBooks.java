package com.example.dell.audiowish;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class AudioBooks extends AppCompatActivity {
    HttpURLConnection books_json;
    JSONArray Books_Json_Array;
    JSONObject Audio;
    ListView Books_List;
    Book audioBook;
    ArrayList<Book> rack = new ArrayList<Book>();
    ArrayList<String> exampleList = new ArrayList<String>();
    CharSequence[] info = new CharSequence[3];
    ArrayAdapter listAdapter;
    AdapterView.OnItemClickListener itemClickListenener;
    Context _CONTEXT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_books);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Books_List = (ListView) findViewById(R.id.bookList);
        listAdapter = new ArrayAdapter<String>(this,R.layout.list_item_layout,R.id.label,exampleList);
        Books_List.setAdapter(listAdapter);
        _CONTEXT = getApplicationContext();
        Toast.makeText(_CONTEXT,"Refresh",Toast.LENGTH_LONG).show();
        itemClickListenener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                info[0] = "Name : "+rack.get(position).getName();
                info[1] = "Author :"+rack.get(position).getAuthor();
                info[2] = "Download Link :  " + rack.get(position).getDownloadLink();
                int i=0;
                String toastString = "";
                for(i=info.length-1;i>=0;i--){
                    toastString = info[i]+ "\n" +toastString;
                }
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AudioBooks.this);
                alertDialogBuilder.setTitle("Info");
                alertDialogBuilder.setMessage(toastString);
                alertDialogBuilder.setPositiveButton("Download", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(rack.get(position).getDownloadLink())));

                    }
                });
                alertDialogBuilder.setNegativeButton("Copy Link", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                        ClipData clipData = ClipData.newPlainText("Link",rack.get(position).getDownloadLink());
                        clipboardManager.setPrimaryClip(clipData);
                        Toast.makeText(_CONTEXT,"Link Copied",Toast.LENGTH_LONG).show();
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }
        };
        Books_List.setOnItemClickListener(itemClickListenener);








        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                exampleList.removeAll(exampleList);
                rack.removeAll(rack);
                fetchFirebaseData();

            }
        });
    }




    public void fetchFirebaseData() {
        try {
            new FirebaseFetcher(this.getApplicationContext()).execute(new URL("https://audiowish-c37e3.firebaseio.com/RACK_books/Books.json"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }


    public class FirebaseFetcher extends AsyncTask<URL,Integer,String> {
        private final Context context;
        public FirebaseFetcher(Context c){
            this.context = c;
        }


        protected void onPreExecute(){
            Toast.makeText(getApplicationContext(), "Loading...", Toast.LENGTH_SHORT).show();
        }


        protected void onPostExecute(){
            Toast.makeText(getApplicationContext(), "Over...", Toast.LENGTH_SHORT).show();
        }



        @Override
        protected String doInBackground(URL... params) {
            try {
                books_json = (HttpURLConnection) params[0].openConnection();
                books_json.setRequestMethod("GET");


                final int ResponseCode = books_json.getResponseCode();
                BufferedReader br = new BufferedReader(new InputStreamReader(books_json.getInputStream()));
                String line = "";
                final StringBuilder responseOutput = new StringBuilder();
                while((line = br.readLine()) != null ) {
                    responseOutput.append(line);
                }
                br.close();
                AudioBooks.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Books_Json_Array = new JSONArray(responseOutput.toString());
                            int i=0;
                            for(i=0;i<Books_Json_Array.length();i++){
                                Audio = Books_Json_Array.getJSONObject(i);
                                exampleList.add(Audio.getString("name"));
                                audioBook = new Book(Audio.getString("name"),Audio.getString("author"),Audio.getString("downloadLink"));
                                rack.add(audioBook);

                            }
                            listAdapter.notifyDataSetChanged();
                            Books_List.setAdapter(listAdapter);
                            Books_List.setOnItemClickListener(itemClickListenener);

                            
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
