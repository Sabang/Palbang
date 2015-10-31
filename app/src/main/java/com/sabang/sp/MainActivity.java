package com.sabang.sp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.nhn.android.naverlogin.data.OAuthLoginState;
import com.sabang.sp.api.BaseModel;
import com.sabang.sp.api.BoardModel;
import com.sabang.sp.api.UserRequest;
import com.sabang.sp.common.DisableEnableControler;
import com.sabang.sp.common.SPLog;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public class MainActivity extends AppCompatActivity{





    private SearchFilterData searchFilterData;
    private ArrayList<BoardModel> boardDatas;
    private static final int BOARD_WRITE = 0;


    Toolbar toolbar;
    TabLayout tabLayout;
    MyAdapter adapter;
    ViewPager viewpager;



    //bug
    // need to fix tabLayout.getTabAt(0).setIcon(R.drawable.main_on);
    boolean bug = true;



    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    private static final String TAG = "OAuthSampleActivity";
    private static String OAUTH_CLIENT_ID = "w9jtNkiVROYyQ8TbzKGo";
    private static String OAUTH_CLIENT_SECRET = "2uHCDckP2n";
    private static String OAUTH_CLIENT_NAME = "네이버 아이디로 로그인";

    private static OAuthLogin mOAuthLoginInstance;
    private String email;
    private static Context mContext;

    /** UI 요소들 */

    private void initNaver() {
        mOAuthLoginInstance = OAuthLogin.getInstance();
        mOAuthLoginInstance.init(mContext, OAUTH_CLIENT_ID, OAUTH_CLIENT_SECRET, OAUTH_CLIENT_NAME);
    }
    @Override
    protected void onResume() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        super.onResume();

    }


    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mContext = this;

        email = "";

        searchFilterData = new SearchFilterData();
        boardDatas = new ArrayList<>();
        initLayout();
        initNaver();


        View settingFragment = (View) getLayoutInflater().
                inflate(R.layout.fragment_setting, null);

        /*if(mOAuthLoginButton == null)
            SPLog.d("");
        else
            SPLog.d("");*/
    }

    private void initLayout() {

        viewpager = (ViewPager) findViewById(R.id.viewpager);
        adapter = new MyAdapter(getSupportFragmentManager(),MainActivity.this);

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
        tabLayout.getTabAt(0).setIcon(R.drawable.main_on);
        tabLayout.getTabAt(1).setIcon(R.drawable.menu_board);
        tabLayout.getTabAt(2).setIcon(R.drawable.menu_setting);


        //change tab title, when fragment changed
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                if (bug) {
                    tabLayout.getTabAt(0).setIcon(R.drawable.menu_main);
                    bug = false;
                }

                switch (position) {
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
                        showDialog();
                        return true;

                    case R.id.action_write:
                        SPLog.d("Board Write Button 눌림");

                        // access token 이 없는 상태로 로그인 보여 줌
                        if (OAuthLoginState.NEED_INIT.equals(OAuthLogin.getInstance().getState(mContext))) {
                            new AlertDialog.Builder(mContext)
                                    .setTitle("로그인")
                                    .setMessage("네이버 아이디로 로그인 하시겠습니까?")
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                        public void onClick(DialogInterface dialog, int whichButton) {
                                            SPLog.d("네이버 로그인 확인 눌림");
                                            mOAuthLoginInstance.startOauthLoginActivity((Activity) mContext, mOAuthLoginHandler);
                                        }
                                    })
                                    .setNegativeButton(android.R.string.no, null).show();
                        }

                        //로그인 되어잇는상태
                        new RequestApiTask().execute();

                        Intent intent = new Intent(mContext,BoardWriteActivity.class);
                        intent.putExtra("email",email);

                        startActivity(intent);


                        return true;
                }
                return false;
            }
        });

    }

    public void setEmail(String email){
        this.email = email;
    }


    final OAuthLoginHandler mOAuthLoginHandler =new OAuthLoginHandler() {
        @Override
        public void run(boolean success) {

            if (success) {
                SPLog.d("handler successsssssssss");

            } else {
                String errorCode = mOAuthLoginInstance.getLastErrorCode(mContext).getCode();
                String errorDesc = mOAuthLoginInstance.getLastErrorDesc(mContext);
                Toast.makeText(mContext, "errorCode:" + errorCode
                        + ", errorDesc:" + errorDesc, Toast.LENGTH_SHORT).show();
            }


        };
    };
    private static class RequestApiTask extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            DisableEnableControler.call(false,((Activity)mContext).getWindow());
        }
        @Override
        protected String doInBackground(Void... params) {
            // String url = "https://apis.naver.com/nidlogin/nid/getHashId_v2.xml";

            String url="";
            String at="";
            try {
                url = "https://openapi.naver.com/v1/nid/getUserProfile.xml";
                at = mOAuthLoginInstance.getAccessToken(mContext);

                return mOAuthLoginInstance.requestApi(mContext, at, url);
            }
            catch (Exception e){
            }

            return "";
        }
        protected void onPostExecute(String content) {
            //mApiResultText.setText((String) content);
            try {
                SPLog.d("네이버 PostExecute");
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document document = builder.parse(new InputSource(new StringReader(content)));
                //텍스트가 한번더 감싸져잇어서 getFirstChild로 한번더 벗겨줌
                String email = (document.getElementsByTagName("email").item(0).getFirstChild().getNodeValue());
                email = email.substring(0,email.length()-10);

                ((MainActivity)mContext).setEmail(email);
                SPLog.d(email);

                UserRequest.newInstance(email, new Response.Listener<BaseModel>() {
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
            catch(Exception e){
                e.printStackTrace();
            }


            DisableEnableControler.call(true,((Activity)mContext).getWindow());

        }
    }

    public void showDialog(){
        SearchFilterDialog dialog = new SearchFilterDialog(this, getSearchFilterData(), new SearchFilterDialog.ICustomDialogEventListener() {
            @Override
            public void customDialogEvent() {
                Intent intent = new Intent("Dialog");
                sendBroadcast(intent);
            }
        });
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        dialog.show();


    }

    //return search filter data
    public SearchFilterData getSearchFilterData() {
        return searchFilterData;
    }
    public ArrayList<BoardModel> getBoardData(){
        return boardDatas;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if(resultCode == RESULT_OK) {
            switch (requestCode) {
                case 10:
                    int area = intent.getExtras().getInt("area");

                    //init to all false, and set selected area true
                    searchFilterData.check_A = false;
                    searchFilterData.check_B = false;
                    searchFilterData.check_C = false;
                    searchFilterData.check_D = false;
                    searchFilterData.check_E = false;
                    searchFilterData.check_F = false;
                    switch (area) {
                        case 1:
                            searchFilterData.check_A = true;
                            break;
                        case 2:
                            searchFilterData.check_B = true;
                            break;
                        case 3:
                            searchFilterData.check_C = true;
                            break;
                        case 4:
                            searchFilterData.check_D = true;
                            break;
                        case 5:
                            searchFilterData.check_E = true;
                            break;
                        case 6:
                            searchFilterData.check_F = true;
                            break;
                        default:
                            break;
                    }
                    break;

                case BOARD_WRITE:
                    SPLog.d("값가져옴!@#$");
                    BoardModel newOne = (BoardModel)intent.getExtras().getSerializable("data");

                    boardDatas.add(newOne);
                    SPLog.d(boardDatas.get(boardDatas.size()-1).title);
                default:
                    break;
            }
        }
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
