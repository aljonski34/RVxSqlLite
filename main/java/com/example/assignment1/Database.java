package com.example.assignment1;
// AJ DEV

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {


    final static String DATABASE_NAME = "Recordnotesss.db";

    private final static int DATABASE_VERSION = 2;

    private final static String TABLE_NOTES = "NotesRecords";
    private final static String COLUMN_ID  = "_id";
    private  final static String COLUMN_TITLE = "Title";
    private final static String COLUMN_NOTES = "Notes";

    private final static String COLUMN_DATE = "Date";

    private final static String COLUMN_TIME = "Time";





    private final static String Create_Table  = " CREATE TABLE " + TABLE_NOTES +"(" + COLUMN_ID + "" +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_TITLE + " TEXT," +
            COLUMN_NOTES +" TEXT, " +
            COLUMN_DATE + " TEXT, " +
            COLUMN_TIME + " TEXT " +")";




    public Database(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Create_Table);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(" DROP TABLE IF EXISTS " + TABLE_NOTES);
        onCreate(db);

    }

    public void AddRecord(String Title, String Notes,String Date,String Time){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, Title);
        values.put(COLUMN_NOTES, Notes);
        values.put(COLUMN_DATE, Date);
        values.put(COLUMN_TIME,Time);
        db.insert(TABLE_NOTES,null,values);
        db.close();

    }
    public void DeleteRecord(Long id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NOTES, COLUMN_ID + " = ? ", new String[]{String.valueOf(id)});
        db.close();

    }

    public int EditRecord(long id, String Title, String notes ,String Date,String Time){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, Title);
        values.put(COLUMN_NOTES,notes);
        values.put(COLUMN_DATE, Date);
        values.put(COLUMN_TIME,Time);
        return db.update(TABLE_NOTES, values, COLUMN_ID + " =?", new String[]{String.valueOf(id)} );


    }
    public Cursor getAllRecord(){


        SQLiteDatabase db = this.getReadableDatabase();
      return db.rawQuery(" SELECT * FROM " + TABLE_NOTES,null);

    }
}
