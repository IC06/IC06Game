package com.turlutu;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DBScores
{
    public static final String KEY_ROWID = "_id";
    public static final String KEY_SCORE = "score";
    public static final String KEY_NAME = "name";
    private static final String TAG = "DBScore";
    
    private static final String DATABASE_NAME = "exitjump";
    private static final String DATABASE_TABLE = "scores";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE =
    "create table scores ("
    + "score integer primary key, "
    + "name text not null);";
    
    private final Context context; 
    
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DBScores(Context ctx) 
    {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }
        
    private static class DatabaseHelper extends SQLiteOpenHelper 
    {
        DatabaseHelper(Context context) 
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) 
        {
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, 
        int newVersion) 
        {
            Log.w(TAG, "Upgrading database from version " + oldVersion 
                    + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS titles");
            onCreate(db);
        }
    }    
    
    //---opens the database---
    public DBScores open() throws SQLException 
    {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    //---closes the database---    
    public void close() 
    {
        DBHelper.close();
    }
    
    //---insert a title into the database---
    public long insertScore(int score, String nom)
    {
    	Cursor c = getAllScores();
    	if (c.getCount() >= 8)
    	{
    		c.moveToLast();
    		if (c.getInt(0) < score)
    		{
    			if (!deleteScore(c.getInt(0)))
    				return -1;
    		}
    		else return -1;
    	}
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_SCORE , score);
        initialValues.put(KEY_NAME, nom);
        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    //---deletes a particular title---
    public boolean deleteScore(int score) 
    {
        return db.delete(DATABASE_TABLE, KEY_SCORE + 
        		"=" + score, null) > 0;
    }

    //---retrieves all the titles---
    public Cursor getAllScores() 
    {
        return db.query(DATABASE_TABLE, new String[] {
        		KEY_SCORE,
        		KEY_NAME}, 
                null, 
                null, 
                null, 
                null, 
                KEY_SCORE + " DESC");
    }

    
}


