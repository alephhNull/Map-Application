package com.example.mapapp;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;

import com.example.mapapp.ui.bookmark.BookmarkFragment;
import com.example.mapapp.ui.bookmark.BookmarkItem;
import com.example.mapapp.ui.map.Location;
import com.example.mapapp.ui.map.MapFragment;
import com.example.mapapp.ui.setting.SettingFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    public static ArrayList<BookmarkItem> locations = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);
        navView.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        FragmentManager fm = getSupportFragmentManager();
        switch (item.getItemId()) {
            case R.id.navigation_bookmark:
                if (fm.findFragmentByTag("bookmark") != null) {
                    fm.beginTransaction().show(fm.findFragmentByTag("bookmark")).commit();
                } else {
                    fm.beginTransaction().add(R.id.nav_host_fragment, BookmarkFragment.class, null, "bookmark").commit();
                }
                if (fm.findFragmentByTag("map") != null)
                    fm.beginTransaction().hide(fm.findFragmentByTag("map")).commit();
                if (fm.findFragmentByTag("setting") != null)
                    fm.beginTransaction().hide(fm.findFragmentByTag("setting")).commit();
                break;

            case R.id.navigation_map:
                if (fm.findFragmentByTag("map") != null) {
                    fm.beginTransaction().show(fm.findFragmentByTag("map")).commit();
                }
                if (fm.findFragmentByTag("bookmark") != null)
                    fm.beginTransaction().hide(fm.findFragmentByTag("bookmark")).commit();
                if (fm.findFragmentByTag("setting") != null)
                    fm.beginTransaction().hide(fm.findFragmentByTag("setting")).commit();
                break;

            case R.id.navigation_settings:
                if (fm.findFragmentByTag("setting") != null) {
                    fm.beginTransaction().show(fm.findFragmentByTag("setting")).commit();
                } else {
                    fm.beginTransaction().add(R.id.nav_host_fragment,  SettingFragment.class, null,"setting").commit();
                }
                if (fm.findFragmentByTag("map") != null)
                    fm.beginTransaction().hide(fm.findFragmentByTag("map")).commit();
                if (fm.findFragmentByTag("bookmark") != null)
                    fm.beginTransaction().hide(fm.findFragmentByTag("bookmark")).commit();
                break;

        }
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            findViewById(R.id.recycler).setVisibility(View.GONE);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}