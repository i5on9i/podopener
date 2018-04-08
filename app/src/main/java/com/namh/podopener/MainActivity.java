package com.namh.podopener;


import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.namh.podopener.frag.Frag;
import com.namh.podopener.frag.FragEcoToday;
import com.namh.podopener.frag.FragGrabEco;
import com.namh.podopener.frag.FragWhatThisMoneyIs;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        OneFragment.OnFragmentInteractionListener,
        FragGrabEco.OnFragmentInteractionListener,
        FragEcoToday.OnFragmentInteractionListener,
        FragWhatThisMoneyIs.OnFragmentInteractionListener{

    private static String TAG = "MainActivity";

    ////////////////////////////////////////////////////////////
    //
    //
    //  Overrides
    //
    //
    ////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();

        _setDrawer();
        _isStoragePermissionGranted();



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


    ////////////////////////////////////////////////////////////
    //
    //
    //  methods
    //
    //
    ////////////////////////////////////////////////////////////

    private void _setDrawer() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // select first item when start

        onNavigationItemSelected(navigationView.getMenu().getItem(0));
        navigationView.setCheckedItem(R.id.nav_grabeco);

    }

    private void _isStoragePermissionGranted(){
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted");
            }else{
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        2);
            }
        }

    }

    private void replaceFragment(Frag fragment) {
        String fragmentName = "frag-grabeco";

        FragmentManager fragmentManager = this.getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.main_content, fragment)
                .addToBackStack(fragmentName)
                .commit();
    }


    ////////////////////////////////////////////////////////////
    //
    //
    //  Implements
    //
    //
    ////////////////////////////////////////////////////////////

    //-------------------------------------------- Frag.OnFragmentInteractionListener
    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onFragEcoTodayInteraction(Uri uri) {

    }

    @Override
    public void onFragGrabEcoInteraction(Uri uri) {

    }

    @Override
    public void onFragWhatThisMoneyIsInteraction(Uri uri) {

    }




    //-------------------------------------------- NavigationView.OnNavigationItemSelectedListener
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        try {
            if (id == R.id.nav_grabeco) {

                replaceFragment(FragGrabEco.class.newInstance());

            } else if (id == R.id.nav_ecotoday) {
                replaceFragment(FragEcoToday.class.newInstance());

            } else if (id == R.id.nav_whatthismoneyis) {
                replaceFragment(FragWhatThisMoneyIs.class.newInstance());
            } else if (id == R.id.nav_manage) {

            } else if (id == R.id.nav_share) {

            } else if (id == R.id.nav_send) {

            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            Log.v(TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
            //resume tasks needing this permission
        }else{
            System.exit(1);
        }
    }
}
