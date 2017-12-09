package com.geolstudio.apipometera;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    AdapterViewPager adapterViewPager;
    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initValue();

        setListener();

        adapterViewPager.addFragment(new MenuFragment());
        adapterViewPager.addFragment(new ProfilFragment());

        viewPager.setAdapter(adapterViewPager);
    }

    public void initValue(){
        adapterViewPager = new AdapterViewPager(getSupportFragmentManager());

        tabLayout = findViewById(R.id.bottombar);
        viewPager = findViewById(R.id.viewPagerMenu);
    }

    public void setListener(){
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                TabLayout.Tab tab = tabLayout.getTabAt(position);
                tab.select();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}
