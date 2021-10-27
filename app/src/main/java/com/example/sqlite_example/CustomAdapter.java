package com.example.sqlite_example;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder>{

    private ArrayList<MemoItem> mMemoList;
    private Context mContext;
    private DBHelper mDBHelper;

    public CustomAdapter(ArrayList<MemoItem> mMemoList, Context mContext) {
        this.mMemoList = mMemoList;
        this.mContext = mContext;
        mDBHelper = new DBHelper(mContext);
    }



    @NonNull
    @Override
    public CustomAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View holder = LayoutInflater.from(parent.getContext()).inflate(R.layout.memo_item,parent,false);    //리사이클러 문제 생기면 아마 여기일듯
        return new ViewHolder(holder);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapter.ViewHolder holder, int position) {

        holder.tv_title.setText(mMemoList.get(position).getTitle());
        holder.tv_content.setText(mMemoList.get(position).getContent());
        holder.tv_writtenDate.setText(mMemoList.get(position).getWrittenDate());

    }

    @Override
    public int getItemCount() {
        return mMemoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_title;
        private TextView tv_content;
        private TextView tv_writtenDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_title = itemView.findViewById(R.id.tv_title);
            tv_content = itemView.findViewById(R.id.tv_content);
            tv_writtenDate = itemView.findViewById(R.id.tv_writtenDate);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int curPos = getAdapterPosition();    //현재 리스트 아이템 위치
                    MemoItem memoItem = mMemoList.get(curPos);

                    String[] strChoiceItems = {"수정하기", "삭제하기"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("원하는 작업을 선택해주세요.");

                    builder.setItems(strChoiceItems, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialoginterface, int position) {

                        if(position == 0){
                            //수정하기
                            //팝업 창 띄우기
                            Dialog dialog = new Dialog(mContext, android.R.style.Theme_Material_Light_Dialog);
                            dialog.setContentView(R.layout.dialog_edit);
                            EditText et_title = dialog.findViewById(R.id.et_title);
                            EditText et_content = dialog.findViewById(R.id.et_content);
                            Button btn_ok = dialog.findViewById(R.id.btn_ok);
                            btn_ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    //update datebase

                                    String title = et_title.getText().toString();
                                    String content = et_content.getText().toString();
                                    String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format((new Date()));        //현재시간 연월일시분초 받아오기
                                    String beforeDate = memoItem.getWrittenDate();


                                    mDBHelper.UpdateMemo(et_title.getText().toString(), et_content.getText().toString(), currentTime, beforeDate);


                                    //update UI
                                    memoItem.setTitle(title);
                                    memoItem.setContent(content);
                                    memoItem.setWrittenDate(currentTime);
                                    notifyItemChanged(curPos,memoItem);
                                    dialog.dismiss();   //   <----------다이얼로그 창 닫히는 메소드
                                    Toast.makeText(mContext,"목록 수정이 완료되었습니다.", Toast.LENGTH_SHORT).show();


                                }
                            });
                            dialog.show();
                        }
                        else if(position == 1){
                            //delete table
                            String beforeTime = memoItem.getWrittenDate();
                            mDBHelper.DeleteMemo(beforeTime);
                            //delete UI
                            mMemoList.remove(curPos);
                            notifyItemChanged(curPos);
                            Toast.makeText(mContext, "목록이 제거되었습니다.", Toast.LENGTH_SHORT).show();



                            }
                        }
                    });
                    builder.show();


                }
            });

        }
    }
    //액티비티에서 호출되는 함수.    현재 어댑터로 새로운 게시글 아이템을 전달받아 추가하는 용도.
    public void addItem(MemoItem _item){
        mMemoList.add(0,_item);
        notifyItemInserted(0);
    }



}
