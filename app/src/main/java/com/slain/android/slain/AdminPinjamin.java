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

public class AdminPinjamin extends AppCompatActivity {
    private AdminPinjamin.SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_pinjamin);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarPinjamin);
        setSupportActionBar(toolbar);

        mSectionsPagerAdapter = new AdminPinjamin.SectionsPagerAdapter(getSupportFragmentManager());

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
            Fragment fragment = new AdminPinjaminFrag1();
            switch (position) {
                case 0:
                    fragment = new AdminPinjaminFrag1();
                    break;

                case 1:
                    fragment = new AdminPinjaminFrag2();
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
