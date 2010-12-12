package com.turlutu;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DBOptions {
    public static final String KEY_ID = "id";
    public static final String KEY_SENSIBILITY = "score";
    public static final String KEY_VOLUME = "volume";
    public static final String KEY_VIBRATION = "vibration";
    public static final String KEY_NAME = "name";
    private static final String TAG = "DBOptions";
    
    private static final String DATABASE_NAME = "exitjump_option";
    private static final String DATABASE_TABLE = "options";
    private static final int DATABASE_VERSION = 8;

    private static final String DATABASE_CREATE =
    "create table "+DATABASE_TABLE+" ("+KEY_ID+" integer primary key autoincrement, "
    + KEY_SENSIBILITY+" integer not null, "
    + KEY_VOLUME+" integer not null, "
    + KEY_VIBRATION+" integer not null, "
    + KEY_NAME+" text not null);";
    
    private final Context context; 
    
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DBOptions(Context ctx)
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
        	Log.i(TAG,"creation de la database");
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, 
        int newVersion) 
        {
            Log.w(TAG, "Upgrading database from version " + oldVersion 
                    + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
            onCreate(db);
        }
    }    
    
    //---opens the database---
    public DBOptions open() throws SQLException 
    {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    //---closes the database---    
    public void close() 
    {
        DBHelper.close();
        db.close();
    }

    public long replace(int id, int sensibility, int volume, int vibration, String nom)
    {
    	ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_ID , id);
        initialValues.put(KEY_SENSIBILITY , sensibility);
        initialValues.put(KEY_VOLUME , volume);
        initialValues.put(KEY_VIBRATION , vibration);
        initialValues.put(KEY_NAME, nom);
        long retour = -1;
        try {
        	retour = db.replaceOrThrow(DATABASE_TABLE, null, initialValues);}
        catch (SQLException e) {
        	Log.e(TAG,"error replace ("+id+","+sensibility+","+volume+","+vibration+","+nom+")");
        	return -1;}
        if (retour < 0)
        {
        	Log.w(TAG,"replace echoue ("+id+","+sensibility+","+volume+","+vibration+","+nom+") retour : "+retour);
    		return -1;
        }
        else
        {
        	Log.i(TAG,"replace ("+id+","+sensibility+","+volume+","+vibration+","+nom+") to index : "+retour);
			return retour;
        }
    }
    
    public long insert(int sensibility, int volume, int vibration, String nom)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_SENSIBILITY , sensibility);
        initialValues.put(KEY_VOLUME , volume);
        initialValues.put(KEY_VIBRATION , vibration);
        initialValues.put(KEY_NAME, nom);
        long retour = -1;
        try {
        	retour = db.insertOrThrow(DATABASE_TABLE, null, initialValues);}
        catch (SQLException e) {
        	Log.e(TAG,"error insert ("+sensibility+","+volume+","+vibration+","+nom+")");
        	return -1;}
        if (retour < 0)
        {
        	Log.w(TAG,"insert echoue ("+sensibility+","+volume+","+vibration+","+nom+") retour : "+retour);
    		return -1;
        }
        else
        {
        	Log.i(TAG,"insert ("+sensibility+","+volume+","+vibration+","+nom+") to index : "+retour);
			return retour;
        }
    }

    //---retrieves all the scores---
    public Cursor getOptions() 
    {
        return db.query(DATABASE_TABLE, new String[] {
        		KEY_ID,
        		KEY_SENSIBILITY,
        		KEY_VOLUME,
        		KEY_VIBRATION,
        		KEY_NAME}, 
                null, 
                null, 
                null, 
                null, 
                null);
    }

    public boolean reset()
    {
    	long retour = -1;
    	
    	try {
    		retour = db.delete(DATABASE_TABLE,null,null); }
    	catch (SQLException e) {
    		Log.e(TAG,"error reset : "+e.getMessage());
    		return false;
    	}
    	if (retour >= 0)
    	{
    		Log.i(TAG,"reset ok");
    		return true;
    	}
    	else
    	{
    		Log.w(TAG,"reset echoue");
    		return false;
    	}
    }
    
    
}



