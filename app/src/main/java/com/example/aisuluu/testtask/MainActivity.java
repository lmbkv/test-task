package com.example.aisuluu.testtask;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements RegionsFragment.OnFragmentInteractionListener, CitiesFragment.OnFragmentInteractionListener {

    protected FragmentManager fm = getSupportFragmentManager();;
    protected FragmentTransaction ft;
    RegionsFragment regionsFragment = new RegionsFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Load Regions Fragment
        ft = fm.beginTransaction();
        ft.replace(R.id.fragmentContainer, regionsFragment);
        ft.addToBackStack(null).commit();
    }

    // When an item in RegionsFragment is clicked, replace it with CitiesFragment
    @Override
    public void loadCityInRegion(int id) {
        ft = fm.beginTransaction();
        ft.replace(R.id.fragmentContainer, CitiesFragment.newInstance(String.valueOf(id))).addToBackStack(null);
        ft.commit();
    }
}
