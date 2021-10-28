package com.example.sqlite_example;

import static com.example.sqlite_example.MainActivity.add_item;
import static com.example.sqlite_example.MainActivity.modify_item;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class writing_page extends AppCompatActivity {


    private Spinner sp_folder;
    private Spinner sp_categori;
    private ImageButton back_button;
    private Button save_button;
    private EditText et_name;
    private EditText et_content;
    private Button image_button;
    private DBHelper mDBHelper;
    private CustomAdapter mAdapter;
    private RecyclerView mRv_memo;


    int iv_profile;
    String tv_name;
    String tv_content;


    ArrayList<String> folder_list;        //폴더 스피너 용도
    ArrayAdapter<String> arrayAdapter;

    ArrayList<String> categori_list;     //카테고리 스피너 용도
    ArrayAdapter<String> ct_arrayAdapter;

    int Modi_or_New = 0;

    int itsId = 0;

//    private void saveAction(){
//
//
//        if(Modi_or_New == 1){
//
//
//            String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format((new Date()));        //현재시간 연월일시분초 받아오기
//            mDBHelper.InsertMemo(et_name.getText().toString(),et_content.getText().toString(),currentTime);
//        }else{
//
//            String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format((new Date()));        //현재시간 연월일시분초 받아오기
//            mDBHelper.UpdateMemo(et_name.getText().toString(),et_content.getText().toString(),currentTime,itsId);
//        }
//    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writing_page);






        folder_list = new ArrayList<>();  //폴더 스피너용 리스트
        folder_list.add("폴더1");
        folder_list.add("폴더2");
        folder_list.add("폴더3");

        arrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, folder_list);

        sp_folder = (Spinner)findViewById(R.id.sp_folder);

        sp_folder.setAdapter(arrayAdapter);

        sp_folder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(),folder_list.get(i)+"가 선택되었습니다.", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            } });





            categori_list = new ArrayList<>();  //카테고리 스피너용 리스트
            categori_list.add("업무용");
            categori_list.add("맛집");
            categori_list.add("낙서");

            ct_arrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, categori_list);

            sp_categori = (Spinner)findViewById(R.id.sp_categori);

            sp_categori.setAdapter(ct_arrayAdapter);
                sp_categori.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    Toast.makeText(getApplicationContext(),categori_list.get(i)+"가 선택되었습니다.", Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                } });




        image_button = findViewById(R.id.image_button);

        image_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("content://media/internal/images/media"));
                startActivity(mIntent);


            }
        });


        et_name = findViewById(R.id.et_name);
        et_content = findViewById(R.id.et_content);
        save_button = findViewById(R.id.save_button);


        Intent receive = getIntent();
        Modi_or_New = receive.getIntExtra("or",0);

        mDBHelper = new DBHelper(this);
        mRv_memo = findViewById(R.id.rv_memo);


        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//                DataBase에 입력
                        String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format((new Date()));        //현재시간 연월일시분초 받아오기
                        String a = et_name.getText().toString();
                        String b = et_content.getText().toString();
                        mDBHelper.InsertMemo(a, b, currentTime);


                        //입력된 내용을 리사이클러뷰에 연동
                        MemoItem item = new MemoItem();
                        item.setTitle(et_name.getText().toString());
                        item.setContent(et_content.getText().toString());
                        item.setWrittenDate((currentTime));

                        mAdapter.addItem(item);

                        mRv_memo.smoothScrollToPosition(0);

                        Toast.makeText(writing_page.this, "메모가 추가되었습니다.",Toast.LENGTH_SHORT).show();
                          finish();

            }
        });


        back_button = findViewById(R.id.back_button);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });


    }













}