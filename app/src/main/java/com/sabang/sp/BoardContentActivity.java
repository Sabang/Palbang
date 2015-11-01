package com.sabang.sp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.sabang.sp.api.BaseModel;
import com.sabang.sp.api.BoardDetailRequest;
import com.sabang.sp.api.BoardModel;
import com.sabang.sp.api.CommentModel;
import com.sabang.sp.api.CommentRequest;
import com.sabang.sp.api.UserRequest;
import com.sabang.sp.api.VolleySingleton;
import com.sabang.sp.common.DisableEnableControler;
import com.sabang.sp.common.SPLog;

import java.util.ArrayList;


public class BoardContentActivity extends AppCompatActivity {

    Toolbar toolbar;
    int id;
    String user;
    Context mContext;
    EditText et;
    CommentAdapter commentAdapter;
    ArrayList<CommentModel> comments;
    ListView commentLV;

    @Override
    protected void onStart() {
        super.onStart();


        id = getIntent().getIntExtra("BoardId", 0);
        user = getIntent().getStringExtra("User");

        BoardDetailRequest.newInstance(id, new Response.Listener<BoardDetailRequest.Model>() {
            @Override
            public void onResponse(BoardDetailRequest.Model response) {
                BoardModel board = response.boardDetail;

                TextView boardTitle = (TextView) findViewById(R.id.board_title);
                boardTitle.setText(board.title);
                TextView boardEmail = (TextView) findViewById(R.id.board_email);

                //이메일 뒤에 가려서 출력
                boardEmail.setText(Util.hideEmailBack(board.user));
                TextView boardDate = (TextView) findViewById(R.id.board_date);
                boardDate.setText(board.date);
                TextView boardContents = (TextView) findViewById(R.id.board_content);
                boardContents.setText(board.content);
                TextView boardName = (TextView) findViewById(R.id.board_name);
                boardName.setText(board.item);
                TextView boardCost = (TextView) findViewById(R.id.board_cost);
                boardCost.setText(board.cost);
                NetworkImageView imageView = (NetworkImageView) findViewById(R.id.boardContent_image);
                imageView.setImageUrl(board.images[0], VolleySingleton.getInstance().getImageLoader());

                ArrayList<CommentModel> temp = board.comments;

                comments.clear();
                for (int i = 0; i < temp.size(); i++) {
                    comments.add(temp.get(i));
                }
                height_lv(commentLV);
                commentAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                SPLog.e(error.toString());
            }
        }).send();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_content);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("물품 정보");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        toolbar.setTitleTextColor(Color.WHITE);

        mContext = this;

        et = (EditText) findViewById(R.id.board_comment);

        Button enroll = (Button) findViewById(R.id.board_commentEnroll);
        enroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = et.getText().toString();
                //내용 비어있을때
                if (comment.equals("")){
                    Toast.makeText(BoardContentActivity.this, "댓글내용을 입력해주세요", Toast.LENGTH_SHORT).show();
                }
                //로그인 안되있으면 시키기
                else if (user.equals("")) {

                    new AlertDialog.Builder(BoardContentActivity.this)
                            .setTitle("댓글")
                            .setMessage("네이버 아이디로 로그인 하시겠습니까?")
                            .setIcon(R.drawable.write_grey)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {
                                    SPLog.d("네이버 로그인 확인 눌림");

                                    MainActivity.mOAuthLoginInstance.startOauthLoginActivity(BoardContentActivity.this, mOAuthLoginHandler);

                                }
                            })
                            .setNegativeButton(android.R.string.no, null).show();
                }
                //댓글내용 서버로 보내기
                else {

                    //새로운 댓글 넣기
                    DisableEnableControler.call(false, getWindow());
                    CommentRequest.newInstance(user, id, comment, new Response.Listener<BaseModel>() {
                        @Override
                        public void onResponse(BaseModel response) {

                            DisableEnableControler.call(true, getWindow());
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            SPLog.e(error.toString());
                            DisableEnableControler.call(true, getWindow());
                        }
                    }).send();

                    //새로운 댓글 다시 가져오기(date가 app에는 없어서 그냥 서버에서 다시 가져옴)
                    BoardDetailRequest.newInstance(id, new Response.Listener<BoardDetailRequest.Model>() {
                        @Override
                        public void onResponse(BoardDetailRequest.Model response) {
                            ArrayList<CommentModel> temp = response.boardDetail.comments;

                            comments.clear();
                            for (int i = 0; i < temp.size(); i++) {
                                comments.add(temp.get(i));
                            }
                            height_lv(commentLV);
                            commentAdapter.notifyDataSetChanged();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            SPLog.e(error.toString());
                        }
                    }).send();


                    Toast.makeText(BoardContentActivity.this, "댓글이 등록되었습니다.", Toast.LENGTH_SHORT).show();
                    et.setText("");


                }
            }
        });


        comments = new ArrayList<>();

        commentLV = (ListView) findViewById(R.id.board_commentListview);
        commentAdapter = new CommentAdapter(mContext, comments);
        commentLV.setAdapter(commentAdapter);
        //commentLV.setScrollContainer(false);


    }


    public void height_lv(ListView lv) {
        ListAdapter adapter = lv.getAdapter();
        if (adapter == null) {
            return;
        }

        ViewGroup vg = lv;
        int totalHeight = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, vg);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams par = lv.getLayoutParams();
        par.height = totalHeight + (lv.getDividerHeight() * (adapter.getCount() - 1));
        lv.setLayoutParams(par);
        lv.requestLayout();
    }


    private OAuthLoginHandler mOAuthLoginHandler = new OAuthLoginHandler() {
        @Override
        public void run(boolean success) {
            if (success) {
                MainActivity.accessToken = MainActivity.mOAuthLoginInstance.getAccessToken(BoardContentActivity.this);
                MainActivity.tokenType = MainActivity.mOAuthLoginInstance.getTokenType(BoardContentActivity.this);

                new RequestApiTask().execute(); //로그인이 성공하면  네이버에 계정값들을 가져온다.


            } else {

                Toast.makeText(BoardContentActivity.this, "로그인이 취소/실패 하였습니다.!",
                        Toast.LENGTH_SHORT).show();
            }
        }

        ;
    };

    public class RequestApiTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            DisableEnableControler.call(false, getWindow());
        }

        @Override
        protected Void doInBackground(Void... params) {
            String url = "https://openapi.naver.com/v1/nid/getUserProfile.xml";
            String at = MainActivity.mOAuthLoginInstance.getAccessToken(BoardContentActivity.this);
            MainActivity.Pasingversiondata(MainActivity.mOAuthLoginInstance.requestApi(BoardContentActivity.this, at, url));

            return null;
        }

        protected void onPostExecute(Void content) {


            if (MainActivity.email.equals("")) {
                Toast.makeText(BoardContentActivity.this,
                        "로그인 실패하였습니다.  잠시후 다시 시도해 주세요!!", Toast.LENGTH_SHORT)
                        .show();
            }
            //requestApi 잘 됐을 때
            else {

                callUserRequest();


                Intent broadCast = new Intent("LogOnState");
                sendBroadcast(broadCast);
                user = MainActivity.email;


            }
            DisableEnableControler.call(true, getWindow());

        }

    }


    private void callUserRequest() {

        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = tm.getDeviceId();

        UserRequest.newInstance(MainActivity.email, deviceId, new Response.Listener<BaseModel>() {
            @Override
            public void onResponse(BaseModel response) {
                SPLog.d("success");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                SPLog.e(error.toString());
            }
        }).send();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.actionbar_button_sold); // 판매자일 경우만 보여준다. (수정해야함)
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
