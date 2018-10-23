package com.example.noahliu.super_ver2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.provider.BaseColumns._ID;


public class DBHelper extends SQLiteOpenHelper{
    private final static String DATABASE_NAME="demo.db";  //資料庫檔案名稱
    private final static int DATABASE_VERSION=1;   //資料庫版本
    public static final String TABLE_NAME="friends";
    public static final String NAME="name";
    public static final String TEL="tel";
    public static final String EMAIL="email";
    public DBHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db){
        final String INIT_TABLE="create table "+TABLE_NAME+"("+_ID+" integer primary key autoincrement,"+NAME+" char,"+TEL+" char,"+EMAIL+" char)";
        db.execSQL(INIT_TABLE);
    }
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){
        final String DROP_TABLE="drop tabel if exists "+TABLE_NAME;
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }

}
