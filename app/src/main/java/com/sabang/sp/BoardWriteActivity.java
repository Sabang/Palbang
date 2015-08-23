package com.sabang.sp;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class BoardWriteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_write);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    public void onClick(View view){
        switch (view.getId())
        {
            case R.id.enroll:

                EditText edittitle = (EditText) findViewById(R.id.title);
                String title = edittitle.getText().toString();
                EditText editcontent = (EditText) findViewById(R.id.content);
                String content = editcontent.getText().toString();


                if (title.equals("")){
                    Toast toast = Toast.makeText(this, "제목을 입력해주세요.",Toast.LENGTH_SHORT);
                    toast.show();
                    break;
                }
                else if (content.equals("")){
                    Toast toast = Toast.makeText(this, "내용을 입력해주세요.",Toast.LENGTH_SHORT);
                    toast.show();
                    break;
                }
                else{
                    Intent intent = new Intent(this,Boards.class);
                    intent.putExtra("title", title);
                    intent.putExtra("content", content);
                    finish();
                    break;
                }

            case R.id.cancel:
                finish();
                break;
        }
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
