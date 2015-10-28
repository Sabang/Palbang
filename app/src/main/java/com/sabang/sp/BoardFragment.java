package com.sabang.sp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

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
    ArrayList<BoardData> boardDatas;
    ArrayList<BoardData> searchedDatas;
    private SwipeRefreshLayout mSwipeRefresh;
    ListView listView;
    ListviewAdapter2 mAdapter;




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
        makeDummyData(boardDatas);


        searchedDatas = new ArrayList<>();
        for(int i=0;i<boardDatas.size();i++) {
            searchedDatas.add(boardDatas.get(i));
        }

        mAdapter = new ListviewAdapter2(getActivity(), searchedDatas);
        listView.setAdapter(mAdapter);

        //검색버튼 눌렀을때
        Button searchButton = (Button) activity.findViewById(R.id.button_search);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText searchTextView = (EditText)activity.findViewById(R.id.editText);
                String thing = searchTextView.getText().toString();
                searchTextView.setText("");
                searchedDatas.clear();
                for(int i=0;i<boardDatas.size();i++){
                    BoardData temp = boardDatas.get(i);
                    if (temp.title.contains(thing) || temp.name.contains(thing)) {
                        searchedDatas.add(temp);
                    }
                    else if(thing.equals("")){
                        searchedDatas.add(temp);
                    }
                }
                mAdapter.notifyDataSetChanged();
            }
        });
        Button myButton = (Button) activity.findViewById(R.id.button_my_board);
        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchedDatas.clear();
                for(int i=0;i<boardDatas.size();i++){
                    BoardData temp = boardDatas.get(i);
                    if (temp.email.equals("kbjb7535")) {
                        searchedDatas.add(temp);
                    }
                }
                mAdapter.notifyDataSetChanged();
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
                Intent myIntent = new Intent(getActivity(), BoardContentActivity.class);

                myIntent.putExtra("BoardItem", searchedDatas.get(position));

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

    public void makeDummyData(ArrayList<BoardData> BoardDatas) {

        //in server
        //id, area(0~5), security, monthly, image
        BoardDatas.add(new BoardData(0, "kjwspecial", "2015.10.12", R.drawable.board1, "카메라", "25","카메라 싸게판다","카메라 팜. 한번도 안 씀."));
        BoardDatas.add(new BoardData(1, "cjaspecial", "2015.10.12", R.drawable.board2, "이어폰", "7","저 귀지없음","클린 귀, 클린 이어폰"));
        BoardDatas.add(new BoardData(2, "asdfxabc","2015.10.12", R.drawable.board3, "그릇", "10","반짞빤짞","반짝반짝 작은그릇"));
        BoardDatas.add(new BoardData(3, "kailabc", "2015.10.14", R.drawable.board4, "건조대", "0","햇볕 쩅쨍","튼튼해요! 정말좋아요!"));
        BoardDatas.add(new BoardData(4, "qqqwww11", "2015.10.11", R.drawable.board5, "재사용 봉투", "1","ㅆㄺㅆㄺ","버릴려다 아까워서 싸게 넘김."));
        BoardDatas.add(new BoardData(5, "awdrfy", "2015.10.11", R.drawable.board1, "카메라", "25","카메라 더 싸게판다","1G메모리 같이 팔아요"));
        BoardDatas.add(new BoardData(6, "aczz123", "2015.10.10", R.drawable.board2,"이어폰", "10","최고급 이어폰 급처","칼국수 선이라 잘 안꼬임"));
        BoardDatas.add(new BoardData(7, "erh", "2015.10.11", R.drawable.board3,"그릇", "10","그릇 3개 팝니다","집에서 가져온 그릇 싸게 팜"));
        BoardDatas.add(new BoardData(8, "4n43tn", "2015.10.13", R.drawable.board1, "카메라", "25","카메라 급처","제가 제일 싸게 팜 빨리 구매하세요@@@@@@@@@@@@@@"));
        BoardDatas.add(new BoardData(9, "kbjb7535", "2015.10.13", R.drawable.board1, "abc", "25","abcabc","asdffqwe"));

    }



    @Override
    public void onRefresh() {

        mAdapter.notifyDataSetChanged();


        mSwipeRefresh.setRefreshing(false);
    }







}
