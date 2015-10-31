package com.sabang.sp;

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
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.sabang.sp.api.BoardModel;
import com.sabang.sp.common.SPLog;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;


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



    //네이버부분 http://ondestroy.tistory.com/category참고

    private static String OAUTH_CLIENT_ID = "w9jtNkiVROYyQ8TbzKGo";
    private static String OAUTH_CLIENT_SECRET = "2uHCDckP2n";
    private static String OAUTH_CLIENT_NAME = "네이버 아이디로 로그인";

    private static OAuthLogin mOAuthLoginInstance;
    private static Context mContext;

    String email = "";
    /*String nickname = "";
    String enc_id = "";
    String profile_image = "";
    String age = "";
    String gender = "";
    String id = "";
    String name = "";
    String birthday = "";*/

    String accessToken = "";
    String tokenType;

    private OAuthLoginHandler mOAuthLoginHandler = new OAuthLoginHandler() {
        @Override
        public void run(boolean success) {
            if (success) {
                accessToken = mOAuthLoginInstance.getAccessToken(mContext);
                tokenType = mOAuthLoginInstance.getTokenType(mContext);

                new RequestApiTask().execute(); //로그인이 성공하면  네이버에 계정값들을 가져온다.

            } else {

                Toast.makeText(MainActivity.this, "로그인이 취소/실패 하였습니다.!",
                        Toast.LENGTH_SHORT).show();
            }
        };
    };


    @Override
    protected void onResume() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        super.onResume();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mContext = getApplicationContext();
        mOAuthLoginInstance = OAuthLogin.getInstance();
        mOAuthLoginInstance.init(mContext, OAUTH_CLIENT_ID,
                OAUTH_CLIENT_SECRET, OAUTH_CLIENT_NAME);


        searchFilterData = new SearchFilterData();
        boardDatas = new ArrayList<>();

        initLayout();


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
                        //if (OAuthLoginState.NEED_INIT.equals(OAuthLogin.getInstance().getState(mContext))) {
                            new AlertDialog.Builder(MainActivity.this)
                                    .setTitle("로그인")
                                    .setMessage("네이버 아이디로 로그인 하시겠습니까?")
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                        public void onClick(DialogInterface dialog, int whichButton) {
                                            SPLog.d("네이버 로그인 확인 눌림");

                                            mOAuthLoginInstance.startOauthLoginActivity(MainActivity.this, mOAuthLoginHandler);


                                        }
                                    })
                        .setNegativeButton(android.R.string.no, null).show();

                        //}


                        return true;
                }
                return false;
            }
        });

    }


    private class RequestApiTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {


        }

        @Override
        protected Void doInBackground(Void... params) {
            String url = "https://openapi.naver.com/v1/nid/getUserProfile.xml";
            String at = mOAuthLoginInstance.getAccessToken(mContext);
            Pasingversiondata(mOAuthLoginInstance.requestApi(mContext, at, url));

            return null;
        }

        protected void onPostExecute(Void content) {

            if (email == null) {
                Toast.makeText(MainActivity.this,
                        "로그인 실패하였습니다.  잠시후 다시 시도해 주세요!!",                 Toast.LENGTH_SHORT)
                        .show();
            } else {

                Intent intent = new Intent(mContext,BoardWriteActivity.class);
                intent.putExtra("email", email);

                startActivity(intent);

            }

        }

        private void Pasingversiondata(String data) { // xml 파싱
            String f_array[] = new String[9];

            try {
                XmlPullParserFactory parserCreator = XmlPullParserFactory
                        .newInstance();
                XmlPullParser parser = parserCreator.newPullParser();
                InputStream input = new ByteArrayInputStream(
                        data.getBytes("UTF-8"));
                parser.setInput(input, "UTF-8");

                int parserEvent = parser.getEventType();
                String tag;
                boolean inText = false;

                int colIdx = 0;

                while (parserEvent != XmlPullParser.END_DOCUMENT) {
                    switch (parserEvent) {
                        case XmlPullParser.START_TAG:
                            tag = parser.getName();
                            if (tag.compareTo("xml") == 0) {
                                inText = false;
                            } else if (tag.compareTo("data") == 0) {
                                inText = false;
                            } else if (tag.compareTo("result") == 0) {
                                inText = false;
                            } else if (tag.compareTo("resultcode") == 0) {
                                inText = false;
                            } else if (tag.compareTo("message") == 0) {
                                inText = false;
                            } else if (tag.compareTo("response") == 0) {
                                inText = false;
                            } else {
                                inText = true;

                            }
                            break;
                        case XmlPullParser.TEXT:
                            if (inText) {
                                if (parser.getText() == null) {
                                    f_array[colIdx] = "";
                                } else {
                                    f_array[colIdx] = parser.getText().trim();
                                }

                                colIdx++;
                            }
                            inText = false;
                            break;
                        case XmlPullParser.END_TAG:
                            inText = false;
                            break;

                    }

                    parserEvent = parser.next();
                }
            } catch (Exception e) {
                Log.e("dd", "Error in network call", e);
            }

            //이메일
            String temp = f_array[0];
            email = temp.substring(0,temp.length()-10);

            /*email = f_array[0];
            nickname = f_array[1];
            enc_id = f_array[2];
            profile_image = f_array[3];
            age = f_array[4];
            gender = f_array[5];
            id = f_array[6];
            name = f_array[7];
            birthday = f_array[8];*/

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






//네이버 getProfile parsing하는 소스
            /*
            try {
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
            }*/