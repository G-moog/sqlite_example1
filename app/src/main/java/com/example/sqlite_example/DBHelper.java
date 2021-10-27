package com.example.sqlite_example;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "MEMO.db";


    public DBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //데이터 베이스가 생성이 될 때 호출
        //데이터베이스 -> 테이블 -> 컬럼 -> 값
        db.execSQL("CREATE TABLE IF NOT EXISTS MemoList (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT NOT NULL, content TEXT NOT NULL, writtenDate TEXT NOT NULL)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        onCreate(db);

    }

    // SELECT문 (할일 목록들을 조회)
    public ArrayList<MemoItem>getMemoList(){
        ArrayList<MemoItem>MemoArray = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM MemoList ORDER BY writtenDate DESC", null);
        if(cursor.getCount() != 0){
            // 조회해온 데이터가 있을경우(0이 아닌 경우) 아래 코드 수행
            while(cursor.moveToNext()){

                int id = cursor.getInt((cursor.getColumnIndex("id")));
                String title = cursor.getString((cursor.getColumnIndex("title")));
                String content = cursor.getString((cursor.getColumnIndex("content")));
                String writtenDate = cursor.getString((cursor.getColumnIndex("writtenDate")));

                MemoItem memoItem = new MemoItem();
                memoItem.setId(id);
                memoItem.setTitle(title);
                memoItem.setContent(content);
                memoItem.setWrittenDate(writtenDate);
                MemoArray.add(memoItem);

            }
        }
        cursor.close();

        return MemoArray;
    }



    //INSERT문 (DB에 메모를 입력.)
    public void InsertMemo(String title, String content, String writtenDate){

        SQLiteDatabase db = getWritableDatabase();

        db.execSQL("INSERT INTO MemoList (title, content,writtenDate) VALUE('" + title + "','" + content + "','" + writtenDate + "');");
//                  INSERT INTO MemoList (title, content,writtenDate) VALUE('" + title + "','" + content + "','" + writtenDate + "');    ----------> SQL 쿼리문

    }

//    UPDATE문(저장된 메모를 수정.)
    public void UpdateMemo(String title, String content, String writtenDate, String beforeDate){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE MemoList SET title = '" + title + "', '" + content + "', '" + writtenDate + "' WHERE writtenDate = '" + beforeDate + "' ");
    }




//    DELETE문(저장된 메모를 삭제.)
    public void DeleteMemo(String beforeDate){
    SQLiteDatabase db = getWritableDatabase();
    db.execSQL("DELETE FROM MemoList WHERE writtenDate = '" + beforeDate + "'");
}
}
