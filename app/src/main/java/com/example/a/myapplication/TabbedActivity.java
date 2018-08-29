package com.example.a.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

;

public class TabbedActivity extends AppCompatActivity {
    private SectionsPageAdapter mSectionsPageAdapter;
    private static final String TAG = "TabbedActivity";

    private ViewPager mViewPager;

    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed);
        Log.d(TAG, "onCreate: Starting.");

        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("ASN Driver");
    }

    private void setupViewPager(ViewPager viewPager) {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());

        adapter.addFragment(new DriverMapActivity(), "Driving");
        adapter.addFragment(new ObdActivity(), "Obd");
        adapter.addFragment(new MainHomeActivity(), "Home");
        adapter.addFragment(new SocialProfileActivity(), "Profile");

       /* adapter.addFragment(new Tab2Fragment(), "TAB2");
        adapter.addFragment(new Tab3Fragment(), "TAB3");*/
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu: ");
        getMenuInflater().inflate(R.menu.map_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.map_logout) {
            Log.d(TAG, "onOptionsItemSelected: ");
            LogoutUtils mLogoutUtils = LogoutUtils.getInstance(this);
            mLogoutUtils.logout();
            startActivity(new Intent(this, MainActivity.class));

            return true;
        } else if (id == R.id.map_settings){

            Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivity(intent);
            return true;
        }/*else if(id==R.id.map_OBD){
            startActivity(new Intent(this,ObdActivity.class));
            return true;

        }*/
        else if(id==R.id.map_profile){
            startActivity(new Intent(this,Profile2Activity.class));
            return true;

        }
        return super.onOptionsItemSelected(item);
    }

}
