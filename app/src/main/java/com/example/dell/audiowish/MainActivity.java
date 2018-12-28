package com.example.dell.audiowish;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.Display;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    Context _CONTEXT;
    FloatingActionButton available,request,feedback,search;
    final int RECORD_REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.INTERNET},
                    RECORD_REQUEST_CODE);
        }
        _CONTEXT = this.getBaseContext();
        if (permission == PackageManager.PERMISSION_GRANTED) {
            available = (FloatingActionButton) findViewById(R.id.view);
            available.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent _AUD = new Intent(getApplicationContext(), AudioBooks.class);
                    startActivity(_AUD);
                }
            });
            request = (FloatingActionButton) findViewById(R.id.view3);
            request.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent _REQ = new Intent(getApplicationContext(), Request.class);
                    startActivity(_REQ);

                }
            });
            feedback = (FloatingActionButton) findViewById(R.id.view4);
            feedback.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent _FB = new Intent(getApplicationContext(), Feedback.class);
                    startActivity(_FB);
                }
            });
            search = (FloatingActionButton) findViewById(R.id.view2);
            search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent _SER = new Intent(getApplicationContext(), Search.class);
                    startActivity(_SER);
                }
            });
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RECORD_REQUEST_CODE: {

                if (grantResults.length == 0
                        || grantResults[0] !=
                        PackageManager.PERMISSION_GRANTED) {

                   Toast.makeText(getApplicationContext(),"Internet access permission required.",Toast.LENGTH_LONG).show();
                } else {

                    available = (FloatingActionButton) findViewById(R.id.view);
                    available.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent _AUD = new Intent(getApplicationContext(), AudioBooks.class);
                            startActivity(_AUD);
                        }
                    });
                    request = (FloatingActionButton) findViewById(R.id.view3);
                    request.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent _REQ = new Intent(getApplicationContext(), Request.class);
                            startActivity(_REQ);

                        }
                    });
                    feedback = (FloatingActionButton) findViewById(R.id.view4);
                    feedback.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent _FB = new Intent(getApplicationContext(), Feedback.class);
                            startActivity(_FB);
                        }
                    });
                    search = (FloatingActionButton) findViewById(R.id.view2);
                    search.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent _SER = new Intent(getApplicationContext(), Search.class);
                            startActivity(_SER);
                        }
                    });

                }
                return;
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camara) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
