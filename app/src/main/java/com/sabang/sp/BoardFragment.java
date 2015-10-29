package com.sabang.sp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
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

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.sabang.sp.api.BoardModel;
import com.sabang.sp.api.BoardRequest;
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

        final EditText searchEditText = (EditText)activity.findViewById(R.id.editText);
        searchEditText.getBackground().setColorFilter(Color.rgb(198,198,198), PorterDuff.Mode.SRC_ATOP);
        //검색버튼 눌렀을때
        Button searchButton = (Button) activity.findViewById(R.id.button_search);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String thing = searchEditText.getText().toString();
                searchEditText.setText("");
                searchedDatas.clear();
                for(int i=0;i<boardDatas.size();i++){
                    BoardModel temp = boardDatas.get(i);
                    if (temp.title.contains(thing) || temp.item.contains(thing)) {
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
                //검색하기 수정해야함***************************************************
                for(int i=0;i<boardDatas.size();i++){
                    BoardModel temp = boardDatas.get(i);
                    if (temp.users.user.equals("kbjb7535")) {
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


    @Override
    public void onRefresh() {

        mAdapter.notifyDataSetChanged();


        mSwipeRefresh.setRefreshing(false);
    }







}
