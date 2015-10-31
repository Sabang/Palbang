package com.sabang.sp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.sabang.sp.api.BoardModel;


public class BoardContentActivity extends AppCompatActivity {

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_content);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        toolbar.setTitleTextColor(Color.WHITE);

        Intent intent = getIntent();
        BoardModel data = (BoardModel)intent.getSerializableExtra("BoardItem");

        TextView boardTitle = (TextView)findViewById(R.id.board_title);
        boardTitle.setText(data.title);
        TextView boardEmail = (TextView)findViewById(R.id.board_email);
        boardEmail.setText(data.user);
        TextView boardDate = (TextView)findViewById(R.id.board_date);
        boardDate.setText(data.date);
        TextView boardContents = (TextView)findViewById(R.id.board_content);
        boardContents.setText(data.content);
        TextView boardName = (TextView)findViewById(R.id.board_name);
        boardName.setText(data.item);
        TextView boardCost = (TextView)findViewById(R.id.board_cost);
        boardCost.setText(data.cost);

        /*  맨뒤 *로바꾸기
    String email = item.id;
    int length = email.length();
    if(length < 8){
        if(length == 3){
            int n0 = email.charAt(0)-'A';
            int n1 = email.charAt(1)-'A';
            int n2 = email.charAt(2)-'A';
            int a = (n0+n1+n2)%52;
            email += ('A'+a);
            length++;
        }

        while(length<8){
            email += "*";
            length++;
        }
    }

    String hide = email.substring(0, length-4)+"****";*/


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.actionbar_button_sold); // 판매자일 경우만 보여준다. (수정해야함)
        toolbar.setTitle(R.string.title_activity_board_content);
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
