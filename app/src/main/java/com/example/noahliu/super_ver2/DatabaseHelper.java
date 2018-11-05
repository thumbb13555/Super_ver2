package com.example.noahliu.super_ver2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "ShoppingCart.db";
    public static final String TABLE_NAME = "Cart";
    public static final String COL1 = "ID";
    public static final String COL2 = "Title";
    public static final String COL3 = "Sale";
    public static final String COL4 = "Amount";

    public DatabaseHelper(Context context){super(context,DATABASE_NAME,null,1);}
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                " TITLE TEXT, SALE TEXT, AMOUNT INT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP IF TABLE EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addData(String Title,String Sale){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2,Title);
        contentValues.put(COL3,Sale);



        long result = db.insert(TABLE_NAME,null,contentValues);

        if(result ==-1){
            return  false;
        }else{
            return  true;
        }
    }

    public Cursor getListContents(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return data;
    }

    public Integer deleteData(String id){
        Log.v("BT","herre");
        SQLiteDatabase db = this.getWritableDatabase();
        return  db.delete(TABLE_NAME,"ID = ",new String[] {id});


    }
}
