package com.sabang.sp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.sabang.sp.api.BaseModel;
import com.sabang.sp.api.BoardModel;
import com.sabang.sp.api.BoardRequest;
import com.sabang.sp.api.UserRequest;
import com.sabang.sp.common.DisableEnableControler;
import com.sabang.sp.common.SPLog;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BoardFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BoardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BoardFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private Activity activity;
    ArrayList<BoardModel> boardDatas;
    ArrayList<BoardModel> searchedDatas;
    private SwipeRefreshLayout mSwipeRefresh;
    ListView listView;
    ListviewAdapter2 mAdapter;
    EditText searchEditText;


    private final int MY_WRITE = 0;
    private final int SEARCH = 1;



    public static BoardFragment newInstance() {
        BoardFragment fragment = new BoardFragment();
        return fragment;
    }

    public BoardFragment() {
        // Required empty public constructor aaaaaa
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!isAdded()) {
            return;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        BoardRequest.newInstance(new Response.Listener<BoardRequest.Model>() {
            @Override
            public void onResponse(BoardRequest.Model model) {

                boardDatas.clear();

                for (int i = 0; i < model.boards.size(); i++) {
                    SPLog.d(model.boards.get(i).toString());
                    boardDatas.add(model.boards.get(i));
                }
                setSearchedDatas(SEARCH);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                SPLog.e(error.toString());
            }
        }).send();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_board, container, false);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        listView = (ListView) activity.findViewById(R.id.listview_board);

        //for test, temporarily server
        final MainActivity mainActivity = (MainActivity) getActivity();
        boardDatas = mainActivity.getBoardData();


        searchedDatas = new ArrayList<>();
        for(int i=0;i<boardDatas.size();i++) {
            searchedDatas.add(boardDatas.get(i));
        }

        mAdapter = new ListviewAdapter2(getActivity(), searchedDatas);
        listView.setAdapter(mAdapter);

        searchEditText = (EditText)activity.findViewById(R.id.editText);
        searchEditText.getBackground().setColorFilter(Color.rgb(198,198,198), PorterDuff.Mode.SRC_ATOP);
        //검색버튼 눌렀을때
        Button searchButton = (Button) activity.findViewById(R.id.button_search);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSearchedDatas(SEARCH);
            }
        });
        Button myBoardButton = (Button) activity.findViewById(R.id.button_my_board);
        myBoardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.email.equals("")){
                    makeNaverDialog();
                }
                else {
                    setSearchedDatas(MY_WRITE);
                }
            }
        });


        //글쓴이,날짜,말머리 추가
        Intent intent = getActivity().getIntent();



        mSwipeRefresh = (SwipeRefreshLayout)activity.findViewById(R.id.fragmentBoard_layout);
        mSwipeRefresh.setOnRefreshListener(this);
        mSwipeRefresh.setColorSchemeColors(Color.RED, Color.RED, Color.RED, Color.RED);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String user = mainActivity.email;
                    Intent myIntent = new Intent(getActivity(), BoardContentActivity.class);

                    myIntent.putExtra("User",user);
                    myIntent.putExtra("BoardId", searchedDatas.get(position).id);

                    startActivity(myIntent);
            }
        });
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int topRowVerticalPosition =
                        (listView == null || listView.getChildCount() == 0) ?
                                0 : listView.getChildAt(0).getTop();
                mSwipeRefresh.setEnabled(firstVisibleItem == 0 && topRowVerticalPosition >= 0);
            }
        });


    }

    private void makeNaverDialog(){
        new AlertDialog.Builder(getActivity())
                .setTitle("내가쓴 글")
                .setMessage("네이버 아이디로 로그인 하시겠습니까?")
                .setIcon(R.drawable.write_grey)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        SPLog.d("네이버 로그인 확인 눌림");
                        MainActivity.mOAuthLoginInstance.startOauthLoginActivity(getActivity(), mOAuthLoginHandler);

                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }






    /*type -    MY_WRITE면 이메일로 내가 쓴 글 검색
                SEARCH면 제목, 제품명으로 글 검색
     */
    public void setSearchedDatas(final int type){
        BoardRequest.newInstance(new Response.Listener<BoardRequest.Model>() {
            @Override
            public void onResponse(BoardRequest.Model model) {

                boardDatas.clear();

                for (int i = 0; i < model.boards.size(); i++) {
                    SPLog.d(model.boards.get(i).toString());
                    boardDatas.add(model.boards.get(i));
                }
                searchedDatas.clear();
                if(type == MY_WRITE){
                    for(int i=0;i<boardDatas.size();i++){
                        BoardModel temp = boardDatas.get(i);
                        if (temp.user.equals(MainActivity.email)) {
                            searchedDatas.add(temp);
                        }
                    }
                }
                else if(type == SEARCH){
                    String text = searchEditText.getText().toString();
                    searchEditText.setText("");
                    for(int i=0;i<boardDatas.size();i++){
                        BoardModel temp = boardDatas.get(i);
                        if (temp.title.contains(text) || temp.item.contains(text)) {
                            searchedDatas.add(temp);
                        }
                    }
                }
                mAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                SPLog.e(error.toString());
            }
        }).send();

    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }


    @Override
    public void onRefresh() {

        setSearchedDatas(SEARCH);

        mSwipeRefresh.setRefreshing(false);
    }




    private OAuthLoginHandler mOAuthLoginHandler = new OAuthLoginHandler() {
        @Override
        public void run(boolean success) {
            if (success) {
                MainActivity.accessToken = MainActivity.mOAuthLoginInstance.getAccessToken(getActivity());
                MainActivity.tokenType = MainActivity.mOAuthLoginInstance.getTokenType(getActivity());

                new RequestApiTask().execute(); //로그인이 성공하면  네이버에 계정값들을 가져온다.


            } else {

                Toast.makeText(getActivity(), "로그인이 취소/실패 하였습니다.!",
                        Toast.LENGTH_SHORT).show();
            }
        }

        ;
    };

    public class RequestApiTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            DisableEnableControler.call(false, getView());
        }

        @Override
        protected Void doInBackground(Void... params) {
            String url = "https://openapi.naver.com/v1/nid/getUserProfile.xml";
            String at = MainActivity.mOAuthLoginInstance.getAccessToken(getActivity());
            MainActivity.Pasingversiondata(MainActivity.mOAuthLoginInstance.requestApi(getActivity(), at, url));

            return null;
        }

        protected void onPostExecute(Void content) {


            if (MainActivity.email.equals("")) {
            }
            //requestApi 잘 됐을 때
            else {

                callUserRequest();


                Intent broadCast = new Intent("LogOnState");
                getActivity().sendBroadcast(broadCast);


            }
            DisableEnableControler.call(true, getView());

        }

    }

    private void callUserRequest() {

        TelephonyManager tm = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
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





}
