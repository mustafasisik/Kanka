package com.turkcellgroup38.kanka.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;

import com.turkcellgroup38.kanka.R;
import com.turkcellgroup38.kanka.fragments.HomeFragment;
import com.turkcellgroup38.kanka.fragments.InformationsFragment;
import com.turkcellgroup38.kanka.fragments.ProfileFragment;

public class MainActivity extends AppCompatActivity {


    private AHBottomNavigation bottomNavigation;
    private FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setNavigation();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void setNavigation(){
        bottomNavigation = findViewById(R.id.xbottom_navigation);
        bottomNavigation.setDefaultBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        bottomNavigation.setAccentColor(getResources().getColor(R.color.colorAccent));
        bottomNavigation.setInactiveColor(Color.WHITE);

        // Create items
        AHBottomNavigationItem item0 = new AHBottomNavigationItem(R.string.information, R.drawable.ic_person_black_24dp, R.color.colorPrimary);
        AHBottomNavigationItem item1 = new AHBottomNavigationItem(R.string.home_page, R.drawable.ic_person_black_24dp, R.color.colorPrimary);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(R.string.profile, R.drawable.ic_person_black_24dp, R.color.colorPrimary);

        // Add items
        bottomNavigation.addItem(item0);
        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);
        bottomNavigation.setCurrentItem(1);


        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment, HomeFragment.newInstance("", "")).commit();
        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                switch (position) {
                    case 0:
                        fragmentTransaction.replace(R.id.fragment, InformationsFragment.newInstance("","")).commit();
                        break;
                    case 1:
                        fragmentTransaction.replace(R.id.fragment, HomeFragment.newInstance("", "")).commit();
                        break;
                    case 2:
                        fragmentTransaction.replace(R.id.fragment, ProfileFragment.newInstance("", "")).commit();
                        break;
                }
                return true;
            }
        });
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
