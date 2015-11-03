package com.sabang.sp;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.sabang.sp.api.BaseModel;
import com.sabang.sp.api.BoardWriteRequest;
import com.sabang.sp.common.DisableEnableControler;
import com.sabang.sp.common.SPLog;

import java.io.ByteArrayOutputStream;

public class BoardWriteActivity extends AppCompatActivity {


    Bundle extra;
    Intent intent;

    Activity activity;
    TextView mTextView;

    final int REQ_CODE_SELECT_IMAGE = 100;

    byte[] image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_write);


        //기본이미지 ***********************수정해야함**********************
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.no_pic);
        image = bitmapToByteArray(bitmap);


        activity = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        toolbar.setTitleTextColor(Color.WHITE);

        extra = new Bundle();
        intent = getIntent();

        mTextView = (TextView) findViewById(R.id.email);
        final String email = intent.getStringExtra("email");


        mTextView.setText(email);

        Button imageButton = (Button) findViewById(R.id.boardWriteImage);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);
            }
        });


        Button bt = (Button) findViewById(R.id.enroll);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override


            //등록 버튼 눌렀을때
            public void onClick(View v) {


                EditText edittitle = (EditText) findViewById(R.id.board_title);
                String title = edittitle.getText().toString();
                EditText editcontent = (EditText) findViewById(R.id.board_content);
                String content = editcontent.getText().toString();
                EditText editName = (EditText) findViewById(R.id.board_name);
                String name = editName.getText().toString();
                EditText editCost = (EditText) findViewById(R.id.board_cost);
                String cost = editCost.getText().toString();

                if (title.equals("")) {
                    Toast toast = Toast.makeText(activity, "제목을 입력해주세요.", Toast.LENGTH_SHORT);
                    toast.show();
                } else if (content.equals("")) {
                    Toast toast = Toast.makeText(activity, "내용을 입력해주세요.", Toast.LENGTH_SHORT);
                    toast.show();
                } else if (name.equals("")) {
                    Toast toast = Toast.makeText(activity, "물품명을 입력해주세요.", Toast.LENGTH_SHORT);
                    toast.show();
                } else if (cost.equals("")) {
                    Toast toast = Toast.makeText(activity, "가격을 입력해주세요.", Toast.LENGTH_SHORT);
                    toast.show();
                }

                //칸들 다 채워져잇을때
                else {
                    DisableEnableControler.call(false, getWindow());
                    BoardWriteRequest.newInstance(image, email, title, content, cost, name, new Response.Listener<BaseModel>() {
                        @Override
                        public void onResponse(BaseModel response) {
                            SPLog.d("success");
                            DisableEnableControler.call(true, getWindow());
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            SPLog.e(error.toString());
                            DisableEnableControler.call(true, getWindow());
                        }
                    }).send();
                    finish();
                }








                /*EditText edittitle = (EditText) findViewById(R.id.board_title);
                String title = edittitle.getText().toString();
                EditText editcontent = (EditText) findViewById(R.id.content);
                String content = editcontent.getText().toString();
                EditText editName = (EditText) findViewById(R.id.name);
                String name = editName.getText().toString();
                EditText editCost = (EditText)findViewById(R.id.cost);
                String cost = editCost.getText().toString();

                if (title.equals("")){
                    Toast toast = Toast.makeText(activity, "제목을 입력해주세요.",Toast.LENGTH_SHORT);
                    toast.show();
                }
                else if (content.equals("")){
                    Toast toast = Toast.makeText(activity, "내용을 입력해주세요.",Toast.LENGTH_SHORT);
                    toast.show();
                }
                else if (name.equals("")){
                    Toast toast = Toast.makeText(activity, "물품명을 입력해주세요.",Toast.LENGTH_SHORT);
                    toast.show();
                }
                else if (cost.equals("")){
                    Toast toast = Toast.makeText(activity, "가격을 입력해주세요.",Toast.LENGTH_SHORT);
                    toast.show();
                }

                else{

                    SPLog.d("c");
                    //bf.boardDatas.add(new BoardData(2, 0, 2015, 9,8,2, R.drawable.board3, "그릇", 10,"카메라 싸게판다 zgzgz"));
                    String email = mTextView.getText().toString();

                    // 현재 시간
                    Date date = new Date(System.currentTimeMillis());
                    SimpleDateFormat CurDateFormat = new SimpleDateFormat("yyyy.MM.dd");
                    String sDate = CurDateFormat.format(date);

                    BoardData bd = new BoardData(1, email, sDate, R.drawable.board3, name, cost,title,content);

                    extra.putSerializable("data", bd);
                    intent.putExtras(extra);

                    activity.setResult(RESULT_OK, intent); // 성공했다는 결과값을 보내면서 데이터 꾸러미를 지고 있는 intent를 함께 전달한다.

                    finish();
                }*/

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_CODE_SELECT_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    //Uri에서 이미지이름을 가져오기
                    String name_Str = getImageNameToUri(data.getData());

                    TextView tv = (TextView) findViewById(R.id.boardWrite_imageName);
                    tv.setText(name_Str);


                    //이미지 데이터를 비트맵으로 가져오기
                    Bitmap image_bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());

                    image = bitmapToByteArray(image_bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }


    public byte[] bitmapToByteArray(Bitmap $bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        $bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    public String getImageNameToUri(Uri data) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(data, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();

        String imgPath = cursor.getString(column_index);
        String imgName = imgPath.substring(imgPath.lastIndexOf("/") + 1);

        return imgName;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
