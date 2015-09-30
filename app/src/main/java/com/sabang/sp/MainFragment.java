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
import com.sabang.sp.api.RoomModel;
import com.sabang.sp.api.RoomRequest;
import com.sabang.sp.common.SPLog;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 */

public class MainFragment extends Fragment implements FragmentDialogListener {
    private Activity activity;
    private SearchFilterData mFilterData;
    private List<RoomModel> mRooms = new ArrayList<>();
    private List<RoomModel> mFilteredRooms = new ArrayList<>();


    ListviewAdapter mAdapter;

    ListView listView;

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
                SPLog.d(model.rooms.get(2).detail);
                SPLog.d(model.rooms.get(2).term);

                mRooms.clear();

                for(int i = 0; i < model.rooms.size();i++){
                    mRooms.add(model.rooms.get(i));
                }
                filtering(mFilteredRooms, mRooms, mFilterData);
                //if(change is exist) -> notify~() run.
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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        //config listview   by cyc
        //listView = (ListView) activity.findViewById(R.id.listview_room);

        MainActivity mainActivity = (MainActivity) getActivity();
        mFilterData = mainActivity.getSearchFilterData();

        //listView.setAdapter(mAdapter);
        mAdapter = new ListviewAdapter(getActivity(), mFilteredRooms);
        listView = (ListView) activity.findViewById(R.id.listview_room);

        listView.setAdapter(mAdapter);


        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MapActivity.class);
                getActivity().startActivityForResult(intent, 10);
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

        MainActivity mainActivity = (MainActivity) getActivity();
        mFilterData = mainActivity.getSearchFilterData();

        filtering(mFilteredRooms, mRooms, mFilterData);

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

    //memory

    private void filtering(List<RoomModel> result, List<RoomModel> original, SearchFilterData filter){


        //in app
        result.clear();
        for (int i = 0; i < original.size(); i++) {
            RoomModel temp = original.get(i);
            if(isFiltered(temp, filter) == true) {
                RoomModel room = new RoomModel();
                //image

                room.area = temp.area;
                room.monthPrice = temp.monthPrice;
                room.securityDeposit = temp.securityDeposit;

                result.add(room);
            }
        }


        mAdapter.notifyDataSetChanged();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent myIntent = new Intent(getActivity(), RoomActivity.class);

                myIntent.putExtra("roomModel", mFilteredRooms.get(position));

                startActivity(myIntent);
            }
        });

    }

    //in option, return true
    private boolean isFiltered(RoomModel room, SearchFilterData filter){
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
                if(max < room.monthPrice){   pass = false;   }
            }
            if(filter.isMonthly_min == true){
                int min = Integer.parseInt(filter.montly_min);
                if(min > room.monthPrice)  {   pass = false;   }
            }
        }
        //check security
        if(pass == true){
            if(filter.isSecurity_max == true){
                int max = Integer.parseInt(filter.security_max);
                if(max < room.securityDeposit){    pass = false;   }
            }
            if(filter.isSecurity_min == true){
                int min = Integer.parseInt(filter.security_min);
                if(min > room.securityDeposit){    pass = false;   }
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


}