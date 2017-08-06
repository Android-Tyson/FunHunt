package com.party.funhunt;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.party.funhunt.activities.FavSelectionActivity;
import com.party.funhunt.activities.LoginActivity;
import com.party.funhunt.adapter.ViewPagerAdapter;
import com.party.funhunt.fragments.ForYou;
import com.party.funhunt.fragments.UpComing;
import com.party.funhunt.utils.Pref;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static android.R.attr.name;

public class MainActivity extends AppCompatActivity {

    @InjectView(R.id.toolbar)
    Toolbar toolbar;

    @InjectView(R.id.tabs)
    TabLayout tabLayout;

    @InjectView(R.id.viewpager)
    ViewPager viewPager;

    Pref pref;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        pref = new Pref(this);

        //initializing toolbar
        initializeToolbar();
        //initializing viewpager
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

    }


    private void initializeToolbar() {
        toolbar.setTitleTextColor(getResources().getColor(R.color.main_bg));
        setSupportActionBar(toolbar);
        setTitle("Masti Nepal");
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new UpComing(), "Up Coming");
        adapter.addFragment(new ForYou(), "For You");
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_share) {
            Toast.makeText(this, "Share Clicked", Toast.LENGTH_SHORT).show();
        }
        if (id == R.id.action_fav) {
            if (pref.getPreference("userId").isEmpty()) {
                Toast.makeText(MainActivity.this, "Please Login to set your favourities", Toast.LENGTH_SHORT).show();
            } else {
                startActivity(new Intent(MainActivity.this, FavSelectionActivity.class));
            }
        }

        return super.onOptionsItemSelected(item);
    }

}
