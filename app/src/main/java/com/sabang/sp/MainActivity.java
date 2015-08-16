package com.sabang.sp;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Parcelable;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewPager viewpager;
    private ImageButton button_main;
    private ImageButton button_board;
    private ImageButton button_setting;
    private int nowButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initLayout();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_main:
                setCurrentItem(0);
                break;
            case R.id.button_board:
                setCurrentItem(1);
                break;
            case R.id.button_setting:
                setCurrentItem(2);
                break;
        }
    }

    private void setCurrentItem(int index) {
        if (index == 0) {
            viewpager.setCurrentItem(0);
            setButtonOn(0);
        }
        else if (index == 1) {
            viewpager.setCurrentItem(1);
            setButtonOn(1);
        }
        else {
            viewpager.setCurrentItem(2);
            setButtonOn(2);
        }
    }

    private void initLayout() {
        button_main = (ImageButton) findViewById(R.id.button_main);
        button_board = (ImageButton) findViewById(R.id.button_board);
        button_setting = (ImageButton) findViewById(R.id.button_setting);
        button_main.setOnClickListener(this);
        button_board.setOnClickListener(this);
        button_setting.setOnClickListener(this);
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        viewpager.setAdapter(new PagerAdapter(getSupportFragmentManager()));
        nowButton = 0;
        button_main.setBackgroundResource(R.drawable.main_on);
    }

    //change images : all button off -> index button on
    private void setButtonOn(int index){
        //turn off prev button
        switch(nowButton){
            case 0:
                button_main.setBackgroundResource(R.drawable.main_off);
                break;
            case 1:
                button_board.setBackgroundResource(R.drawable.board_off);
                break;
            case 2:
                button_setting.setBackgroundResource(R.drawable.setting_off);
                break;
        }

        //turn on new button
        switch(index) {
            case 0:
                button_main.setBackgroundResource(R.drawable.main_on);
                nowButton = 0;
                break;
            case 1:
                button_board.setBackgroundResource(R.drawable.board_on);
                nowButton = 1;
                break;
            case 2:
                button_setting.setBackgroundResource(R.drawable.setting_on);
                nowButton = 2;
                break;
        }
    }

    private class PagerAdapter extends FragmentPagerAdapter {

        public PagerAdapter(FragmentManager supportFragmentManager) {
            super(supportFragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0)
                return MainFragment.newInstance();
            else if (position == 1) {
                return BoardFragment.newInstance();
            }
            else{
                return SettingFragment.newInstance();
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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
