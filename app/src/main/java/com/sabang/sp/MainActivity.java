package com.sabang.sp;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity{


    private SearchFilterData searchFilterData;
    Toolbar toolbar;
    TabLayout tabLayout;
    //bug
    // need to fix tabLayout.getTabAt(0).setIcon(R.drawable.main_on);
    boolean bug = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        searchFilterData = new SearchFilterData();

        initLayout();
    }

    private void initLayout() {

        final ViewPager viewpager = (ViewPager) findViewById(R.id.viewpager);
        MyAdapter adapter = new MyAdapter(getSupportFragmentManager(),MainActivity.this);

        viewpager.setAdapter(adapter);

        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        viewpager.setCurrentItem(0);
        toolbar.setTitle(R.string.title_fragment_main);
        tabLayout = (TabLayout) findViewById(R.id.main_tablayout);

        toolbar.inflateMenu(R.menu.actionbar_button_filter);

        //can't fix bug yet
        // set tab0 as menu_main, when run app first time, don't turn on
        tabLayout.setupWithViewPager(viewpager);
        tabLayout.getTabAt(0).setIcon(R.drawable.menu_main);
        tabLayout.getTabAt(1).setIcon(R.drawable.menu_board);
        tabLayout.getTabAt(2).setIcon(R.drawable.menu_setting);


        //change tab title, when fragment changed
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                if(bug){
                    tabLayout.getTabAt(0).setIcon(R.drawable.menu_main);
                    bug = false;
                }

                switch(position){
                    case 0:
                        toolbar.getMenu().clear();
                        toolbar.inflateMenu(R.menu.actionbar_button_filter);
                        toolbar.setTitle(R.string.title_fragment_main);
                        viewpager.setCurrentItem(0);
                        break;
                    case 1:
                        toolbar.getMenu().clear();
                        toolbar.inflateMenu(R.menu.actionbar_button_write);
                        toolbar.setTitle(R.string.title_fragment_board);
                        viewpager.setCurrentItem(1);
                        break;
                    case 2:
                        toolbar.getMenu().clear();
                        viewpager.setCurrentItem(2);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }





        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_filter:

                }
                return false;
            }
        });

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
                    toolbar.inflateMenu(R.menu.menu_main_toolbar);
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
