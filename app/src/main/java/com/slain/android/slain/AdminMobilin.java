package com.slain.android.slain;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class AdminMobilin extends AppCompatActivity {
    private AdminMobilin.SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_mobilin);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarMobilin);
        setSupportActionBar(toolbar);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = new AdminMobilinFrag1();
            switch (position) {
                case 0:
                    fragment = new AdminMobilinFrag1();
                    break;

                case 1:
                    fragment = new AdminMobilinFrag2();
                    break;
            } return fragment;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, MainActivityAdmin.class);
        startActivity(i);
        finish();
    }
}
