package com.example.sqlite_example;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRv_memo;
    private FloatingActionButton mBtn_write;
    private ArrayList<MemoItem> mMemoList;
    private DBHelper mDBHelper;
    private CustomAdapter mAdapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setInit();





    }

    private void setInit() {

        mDBHelper = new DBHelper(this);
        mRv_memo = findViewById(R.id.rv_memo);
        mBtn_write = findViewById(R.id.btn_add);
        mMemoList = new ArrayList<>();

        loadRecentDB();


        mBtn_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //팝업 창 띄우기
                Dialog dialog = new Dialog(MainActivity.this, android.R.style.Theme_Material_Light_Dialog);
                dialog.setContentView(R.layout.dialog_edit);
                EditText et_title = dialog.findViewById(R.id.et_title);
                EditText et_content = dialog.findViewById(R.id.et_content);
                Button btn_ok = dialog.findViewById(R.id.btn_ok);
                btn_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //DataBase에 입력
                        String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format((new Date()));        //현재시간 연월일시분초 받아오기

                        mDBHelper.InsertMemo(et_title.getText().toString(), et_content.getText().toString(), currentTime);


                        //입력된 내용을 리사이클러뷰에 연동
                        MemoItem item = new MemoItem();
                        item.setTitle(et_title.getText().toString());
                        item.setContent(et_content.getText().toString());
                        item.setWrittenDate((currentTime));

                        mAdapter.addItem(item);

                        mRv_memo.smoothScrollToPosition(0);

                        dialog.dismiss();
                        Toast.makeText(MainActivity.this, "메모가 추가되었습니다.",Toast.LENGTH_SHORT).show();


                    }
                });

                dialog.show();

            }
        });




    }

    private void loadRecentDB(){
        //저장된 DB를 가져온다.

        mMemoList = mDBHelper.getMemoList();
        if(mAdapter ==null){
            mAdapter = new CustomAdapter(mMemoList, this);
            mRv_memo.setHasFixedSize(true);
            mRv_memo.setAdapter(mAdapter);
        }
    }

}