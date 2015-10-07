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
import android.widget.AdapterView;
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
    private SearchFilterData filterData;
    ArrayList<BoardData> boardDatas;
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
        MainActivity mainActivity = (MainActivity) getActivity();
        boardDatas = mainActivity.getBoardData();
        makeDummyData(boardDatas);


        filterData = mainActivity.getSearchFilterData();

        mAdapter = new ListviewAdapter2(getActivity(), boardDatas);
        listView.setAdapter(mAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent myIntent = new Intent(getActivity(), BoardContentActivity.class);

                myIntent.putExtra("BoardItem", boardDatas.get(position));

                startActivity(myIntent);
            }
        });
       /* listView.setOnScrollListener(new AbsListView.OnScrollListener() {
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
        });*/



        //글쓴이,날짜,말머리 추가
        Intent intent = getActivity().getIntent();



        mSwipeRefresh = (SwipeRefreshLayout)activity.findViewById(R.id.fragmentBoard_layout);
        mSwipeRefresh.setOnRefreshListener(this);
        mSwipeRefresh.setColorSchemeColors(Color.RED, Color.RED, Color.RED, Color.RED);

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
        BoardDatas.add(new BoardData(0, "a@a.com", 2015, 10,8,1, R.drawable.board1, "카메라", 25,"카메라 싸게판다"));
        BoardDatas.add(new BoardData(1, "a@a.com", 2015, 10,12,1, R.drawable.board2, "이어폰", 7,"카메라 싸게판다"));
        BoardDatas.add(new BoardData(2, "a@a.com", 2015, 9,8,2, R.drawable.board3, "그릇", 10,"카메라 싸게판다"));
        BoardDatas.add(new BoardData(3, "a@a.com", 2015, 12,8,3, R.drawable.board4, "건조대", 0,"카메라 싸게판다"));
        BoardDatas.add(new BoardData(4, "a@a.com", 2015, 10,8,1, R.drawable.board5, "재사용 봉투", 1,"카메라 싸게판다"));
        BoardDatas.add(new BoardData(5, "a@a.com", 2015, 10,38,1, R.drawable.board1, "카메라", 25,"카메라 싸게판다"));
        BoardDatas.add(new BoardData(6, "a@a.com", 2015, 7,22,1, R.drawable.board2,"이어폰", 10,"카메라 싸게판다"));
        BoardDatas.add(new BoardData(7, "a@a.com", 2015, 4,9,1, R.drawable.board3,"그릇", 10,"카메라 싸게판다"));
        BoardDatas.add(new BoardData(8, "a@a.com", 2015, 4,21,1, R.drawable.board1, "카메라", 25,"카메라 싸게판다"));

    }



    @Override
    public void onRefresh() {

        mAdapter.notifyDataSetChanged();


        mSwipeRefresh.setRefreshing(false);
    }

}
