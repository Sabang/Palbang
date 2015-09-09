package com.sabang.sp;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity{


    private SearchFilterData searchFilterData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        searchFilterData = new SearchFilterData();

        initLayout();
    }

    private void initLayout() {

        ViewPager viewpager = (ViewPager) findViewById(R.id.viewpager);
        MyAdapter adapter = new MyAdapter(getSupportFragmentManager(),MainActivity.this);

        viewpager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.main_tablayout);

        tabLayout.setupWithViewPager(viewpager);

        tabLayout.getTabAt(0).setIcon(R.drawable.menu_main);
        tabLayout.getTabAt(1).setIcon(R.drawable.menu_board);
        tabLayout.getTabAt(2).setIcon(R.drawable.menu_setting);

    }

    //return search filter data
    public SearchFilterData getSearchFilterData() {
        return searchFilterData;
    }


    public class MyAdapter extends FragmentPagerAdapter
    {
        final int PAGE_COUNT = 3;
        private Context context;

        public MyAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;

            switch (position){
                case 0:
                    fragment = MainFragment.newInstance();
                    break;
                case 1:
                    fragment = BoardFragment.newInstance();
                    break;
                case 2:
                    fragment = SettingFragment.newInstance();
                    break;
                default:
                    break;
            }

            return fragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return super.getPageTitle(position);
        }
    }

}
