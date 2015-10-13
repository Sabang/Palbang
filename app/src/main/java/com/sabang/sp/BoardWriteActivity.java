package com.sabang.sp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sabang.sp.common.SPLog;

public class BoardWriteActivity extends AppCompatActivity {



    Bundle extra;
    Intent intent;

    Activity activity;
    TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_write);

        activity = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        toolbar.setTitleTextColor(Color.WHITE);

        extra = new Bundle();
        intent = getIntent();

        mTextView = (TextView)findViewById(R.id.email);
        String email = intent.getStringExtra("email");

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

        String hide = email.substring(0, length-4)+"****";
        mTextView.setText(hide);
        Button bt = (Button)findViewById(R.id.enroll);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText edittitle = (EditText) findViewById(R.id.board_title);
                String title = edittitle.getText().toString();
                EditText editcontent = (EditText) findViewById(R.id.content);
                String content = editcontent.getText().toString();
                EditText editName = (EditText) findViewById(R.id.name);
                String name = editName.getText().toString();
                EditText editCost = (EditText)findViewById(R.id.cost);
                String cost = editCost.getText().toString();

                if (title.equals("")){
                    Toast toast = Toast.makeText(activity, "제목을 입력해주세요.",Toast.LENGTH_SHORT);
                    toast.show();
                }
                else if (content.equals("")){
                    Toast toast = Toast.makeText(activity, "내용을 입력해주세요.",Toast.LENGTH_SHORT);
                    toast.show();
                }
                else if (name.equals("")){
                    Toast toast = Toast.makeText(activity, "물품명을 입력해주세요.",Toast.LENGTH_SHORT);
                    toast.show();
                }
                else if (cost.equals("")){
                    Toast toast = Toast.makeText(activity, "가격을 입력해주세요.",Toast.LENGTH_SHORT);
                    toast.show();
                }

                else{

                    SPLog.d("c");
                    //bf.boardDatas.add(new BoardData(2, 0, 2015, 9,8,2, R.drawable.board3, "그릇", 10,"카메라 싸게판다 zgzgz"));
                    String email = mTextView.getText().toString();
                    BoardData bd = new BoardData(1, email, "", R.drawable.board3, name, cost,title,content);

                    extra.putSerializable("data", bd);
                    intent.putExtras(extra);

                    activity.setResult(RESULT_OK, intent); // 성공했다는 결과값을 보내면서 데이터 꾸러미를 지고 있는 intent를 함께 전달한다.

                    finish();
                }

            }
        });
    }

    /*
    public void onClick(View view){
        switch (view.getId())
        {
            case R.id.enroll:


        }



    }*/




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
