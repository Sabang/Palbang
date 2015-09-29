package com.sabang.sp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BoardFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BoardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BoardFragment extends Fragment {
    private Activity activity;
    private ArrayList<BoardData> boardDatas;
    private SearchFilterData filterData;


    private OnFragmentInteractionListener mListener;


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


        ListView listView = (ListView) activity.findViewById(R.id.listview_board);

        final ArrayList<BoardListviewitem> listviewitems = new ArrayList<>();

        //for test, temporarily server
        boardDatas = new ArrayList<BoardData>();
        makeDummyData(boardDatas);


        MainActivity mainActivity = (MainActivity) getActivity();
        filterData = mainActivity.getSearchFilterData();

        setListview(listviewitems, boardDatas, filterData);


        ListviewAdapter2 adapter = new ListviewAdapter2(getActivity(), listviewitems);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent myIntent = new Intent(getActivity(), BoardContentActivity.class);

                myIntent.putExtra("BoardItem", (Serializable) listviewitems.get(position));

                startActivity(myIntent);
            }
        });




        //글쓴이,날짜,말머리 추가
        Intent intent = getActivity().getIntent();



    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    private void setListview(ArrayList<BoardListviewitem> data, ArrayList<BoardData> BoardDatas, SearchFilterData filter) {


        //in app
        for (int i = 0; i < BoardDatas.size(); i++) {
            BoardData temp = BoardDatas.get(i);

            BoardListviewitem board = new BoardListviewitem();
            board.icon = temp.icon;
            board.year = temp.year;
            board.month = temp.month;
            board.day = temp.day;
            board.name = temp.name;
            board.price = temp.price;
            board.state = temp.state;
            data.add(board);

        }


    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    public void makeDummyData(ArrayList<BoardData> BoardDatas) {

        //in server
        //id, area(0~5), security, monthly, image
        BoardDatas.add(new BoardData(0, 0, 2015, 10,8,1, R.drawable.board1, "카메라", 25));
        BoardDatas.add(new BoardData(1, 0, 2015, 10,12,1, R.drawable.board2, "이어폰", 7));
        BoardDatas.add(new BoardData(2, 0, 2015, 9,8,2, R.drawable.board3, "그릇", 10));
        BoardDatas.add(new BoardData(3, 0, 2015, 12,8,3, R.drawable.board4, "건조대", 0));
        BoardDatas.add(new BoardData(4, 0, 2015, 10,8,1, R.drawable.board5, "재사용 봉투", 1));
        BoardDatas.add(new BoardData(5, 0, 2015, 10,38,1, R.drawable.board1, "카메라", 25));
        BoardDatas.add(new BoardData(6, 0, 2015, 7,22,1, R.drawable.board2,"이어폰", 10));
        BoardDatas.add(new BoardData(7, 0, 2015, 4,9,1, R.drawable.board3,"그릇", 10));
        BoardDatas.add(new BoardData(8, 0, 2015, 4,21,1, R.drawable.board1, "카메라", 25));

    }

}
