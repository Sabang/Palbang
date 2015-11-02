package com.sabang.sp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.sabang.sp.api.BaseModel;
import com.sabang.sp.api.UserRequest;
import com.sabang.sp.common.DisableEnableControler;
import com.sabang.sp.common.SPLog;


//
//mOAuthLoginInstance.logout(mContext);

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SettingFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SettingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    View rootView;
    MainActivity mainActivity;




    public static SettingFragment newInstance() {
        SettingFragment fragment = new SettingFragment();
        return fragment;
    }

    public SettingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();


        IntentFilter intentFilter = new IntentFilter("LogOnState");
        getActivity().registerReceiver(this.broadcastReceiver, intentFilter);
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            setLoginStateTV();

        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_setting, container, false);
        mainActivity = (MainActivity) getActivity();

        setLoginStateTV();

        LinearLayout naver_state = (LinearLayout)rootView.findViewById(R.id.setting_loginState);
        naver_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainActivity.email.equals("")) {
                    makeNaverDialog();

                }
                else{
                    makeLogoutDialog();

                }
            }
        });

        Switch pushSwitch = (Switch)rootView.findViewById(R.id.setting_RoomPushSW);
        pushSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainActivity.email.equals("")) {
                    makeNaverDialog();

                }
                else{
                }
            }
        });
        TextView settingRoom = (TextView) rootView.findViewById(R.id.setting_pushSettingTV);
        settingRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainActivity.email.equals("")) {
                    makeNaverDialog();

                }
                else{
                    mainActivity.showDialog();
                }
            }
        });



        return rootView;
    }


    CompoundButton.OnCheckedChangeListener myListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            /*Switch
            switch (buttonView){
                case buttonView==
            }
            if (isChecked) {

                //서버로 true 쏴주면됨
            }


            else {
            }*/
        }
    };

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }




    private void makeNaverDialog(){
        new AlertDialog.Builder(getActivity())
                .setTitle("네이버")
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

    private void makeLogoutDialog(){
        new AlertDialog.Builder(getActivity())
                .setTitle("네이버")
                .setMessage("로그아웃 하시겠습니까?")
                .setIcon(R.drawable.write_grey)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        MainActivity.mOAuthLoginInstance.logout(getActivity());
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }
    private void setLoginStateTV(){
        String email = mainActivity.email;
        if(!email.equals("")){
            TextView tv = (TextView) rootView.findViewById(R.id.setting_loginStateTV);
            tv.setText(email+"@naver.com");
        }
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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
