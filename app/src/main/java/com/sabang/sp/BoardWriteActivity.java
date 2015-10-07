package com.sabang.sp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.nhn.android.naverlogin.OAuthLogin;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class BoardWriteActivity extends AppCompatActivity {



    Bundle extra;
    Intent intent;

    EditText mEditText;
    Context mContext;
    private static OAuthLogin mOAuthLoginInstance;
    private class RequestApiTask extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
        }
        @Override
        protected String doInBackground(Void... params) {
            // String url = "https://apis.naver.com/nidlogin/nid/getHashId_v2.xml";

            String url="";
            String at="";
            try {
                url = "https://openapi.naver.com/v1/nid/getUserProfile.xml";
                at = mOAuthLoginInstance.getAccessToken(mContext);
            }
            catch (Exception e){
                Intent intent = new Intent(mContext,LoginActivity.class);
                startActivity(intent);
            }
            return mOAuthLoginInstance.requestApi(mContext, at, url);
        }
        protected void onPostExecute(String content) {
            //mApiResultText.setText((String) content);
            try {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document document = builder.parse(new InputSource(new StringReader(content)));
                //텍스트가 한번더 감싸져잇어서 getFirstChild로 한번더 벗겨줌
                mEditText.setText(document.getElementsByTagName("email").item(0).getFirstChild().getNodeValue());
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_write);
        mContext = this;

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

        mEditText = (EditText)findViewById(R.id.email);

        mOAuthLoginInstance = navertest.getLoginInstance();
        new RequestApiTask().execute();

        extra = new Bundle();
        intent = new Intent(); //초기화 깜빡 했다간 NullPointerException이라는 짜증나는 놈이랑 대면하게 된다.
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

                    //bf.boardDatas.add(new BoardData(2, 0, 2015, 9,8,2, R.drawable.board3, "그릇", 10,"카메라 싸게판다 zgzgz"));
                    String email = mEditText.getText().toString();

                    BoardData bd = new BoardData(2, email, 2015,9,8,2, R.drawable.board3, "그릇", 10,"카메라 싸게판다 zgzgz");
                    extra.putSerializable("data", bd);
                    intent.putExtras(extra);

                    this.setResult(RESULT_OK, intent); // 성공했다는 결과값을 보내면서 데이터 꾸러미를 지고 있는 intent를 함께 전달한다.

                    this.finish();
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
