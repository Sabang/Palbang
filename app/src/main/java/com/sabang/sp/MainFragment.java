package com.sabang.sp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.sabang.sp.api.RoomRequest;
import com.sabang.sp.common.SPLog;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 */

public class MainFragment extends Fragment implements FragmentDialogListener {
    private Activity activity;
    private ArrayList<RoomData> roomDatas;
    private SearchFilterData filterData;

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    public MainFragment() {
        // Required empty public constructor
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
        return inflater.inflate(R.layout.fragment_main, container, false);
    }


    @Override
    public void onStart() {
        super.onStart();

        RoomRequest.newInstance(new Response.Listener<RoomRequest.Model>() {
            @Override
            public void onResponse(RoomRequest.Model model) {
                SPLog.d(model.rooms.get(0).detail);
                SPLog.d(model.rooms.get(0).term);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                SPLog.e(error.toString());
            }
        }).send();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        //config listview   by cyc
        ListView listView = (ListView) activity.findViewById(R.id.listview_room);

        final ArrayList<RoomListviewitem> listviewitems = new ArrayList<>();

        //for test, temporarily server
        roomDatas = new ArrayList<RoomData>();
        makeDummyData(roomDatas);


        MainActivity mainActivity = (MainActivity) getActivity();
        filterData = mainActivity.getSearchFilterData();

        setListview(listviewitems, roomDatas, filterData);


        ListviewAdapter adapter = new ListviewAdapter(getActivity(), listviewitems);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent myIntent = new Intent(getActivity(), RoomActivity.class);

                myIntent.putExtra("roomItem", (Serializable) listviewitems.get(position));

                startActivity(myIntent);
            }
        });


        

        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //Intent intent = new Intent(getActivity(), MapActivity.class);
                //naver test
                Intent intent = new Intent(getActivity(), navertest.class);
                startActivity(intent);
            }
        });

        Button button_search_filter = (Button) getActivity().findViewById(R.id.button_search_filter);

        //make dialog to search filter
        button_search_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();

            }
        });






    }

    private void showDialog() {

        SearchFilterDialog dialog = new SearchFilterDialog();
        MainActivity mainActivity = (MainActivity) getActivity();
        dialog.setSearchFilterData(mainActivity.getSearchFilterData());
        dialog.setTargetFragment(this, 0);
        dialog.show(getFragmentManager(), "dialog");
    }

    @Override
    public void onYesClick() {

        ListView listView = (ListView) activity.findViewById(R.id.listview_room);

        ArrayList<RoomListviewitem> listviewitems = new ArrayList<>();

        MainActivity mainActivity = (MainActivity) getActivity();
        filterData = mainActivity.getSearchFilterData();

        setListview(listviewitems, roomDatas, filterData);

        ListviewAdapter adapter = new ListviewAdapter(getActivity(), listviewitems);
        listView.setAdapter(adapter);

    }

    @Override
    public void onNoClick() {

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


    private void setListview(ArrayList<RoomListviewitem> data, ArrayList<RoomData> roomDatas, SearchFilterData filter){


        //in app
        for (int i = 0; i < roomDatas.size(); i++) {
            RoomData temp = roomDatas.get(i);
            if(isFiltered(temp, filter) == true) {
                RoomListviewitem room = new RoomListviewitem();
                room.icon = temp.icon;
                room.area = temp.area;
                room.monthly = temp.monthly;
                room.security = temp.security;

                data.add(room);
            }
        }
    }

    //in option, return true
    private boolean isFiltered(RoomData room, SearchFilterData filter){
        boolean pass = true;

        //check by area
        switch(room.area){
            case 0:
                if(!filter.check_A){    pass = false;   }
                break;
            case 1:
                if(!filter.check_B) {   pass = false;   }
                break;
            case 2:
                if(!filter.check_C) {   pass = false;   }
                break;
            case 3:
                if(!filter.check_D) {   pass = false;   }
                break;
            case 4:
                if(!filter.check_E) {   pass = false;   }
                break;
            case 5:
                if(!filter.check_F) {   pass = false;   }
                break;
        }   //end switch


        //check monthly
        if(pass == true){
            if(filter.isMonthly_max == true){
                int max = Integer.parseInt(filter.montly_max);
                if(max < room.monthly){   pass = false;   }
            }
            if(filter.isMonthly_min == true){
                int min = Integer.parseInt(filter.montly_min);
                if(min > room.monthly)  {   pass = false;   }
            }
        }
        //check security
        if(pass == true){
            if(filter.isSecurity_max == true){
                int max = Integer.parseInt(filter.security_max);
                if(max < room.security){    pass = false;   }
            }
            if(filter.isSecurity_min == true){
                int min = Integer.parseInt(filter.security_min);
                if(min > room.security){    pass = false;   }
            }
        }


        return pass;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */

    //result of SearchFilterDialog
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }


    public void makeDummyData(ArrayList<RoomData> roomDatas) {

        //in server
        //id, area(0~5), security, monthly, image
        roomDatas.add(new RoomData(0, 0, 100, 10, R.drawable.room1));
        roomDatas.add(new RoomData(1, 1, 200, 20, R.drawable.room2));
        roomDatas.add(new RoomData(2, 2, 300, 30, R.drawable.room3));
        roomDatas.add(new RoomData(3, 3, 400, 40, R.drawable.room4));
        roomDatas.add(new RoomData(4, 4, 500, 50, R.drawable.room2));
        roomDatas.add(new RoomData(5, 5, 230, 20, R.drawable.room3));
        roomDatas.add(new RoomData(6, 5, 1000, 100, R.drawable.room1));
        roomDatas.add(new RoomData(7, 5, 100, 10, R.drawable.room1));
        roomDatas.add(new RoomData(8, 5, 220, 20, R.drawable.room4));
        roomDatas.add(new RoomData(9, 1, 30, 30, R.drawable.room4));
        roomDatas.add(new RoomData(10, 2, 300, 30, R.drawable.room1));
        roomDatas.add(new RoomData(11, 3, 50, 10, R.drawable.room3));
        roomDatas.add(new RoomData(12, 4, 650, 60, R.drawable.room1));
        roomDatas.add(new RoomData(13, 0, 640, 60, R.drawable.room2));
        roomDatas.add(new RoomData(14, 2, 320, 70, R.drawable.room3));
        roomDatas.add(new RoomData(15, 3, 120, 50, R.drawable.room4));
        roomDatas.add(new RoomData(16, 0, 340, 40, R.drawable.room3));
        roomDatas.add(new RoomData(17, 1, 50, 30, R.drawable.room3));
        roomDatas.add(new RoomData(18, 0, 70, 20, R.drawable.room1));



    }
}