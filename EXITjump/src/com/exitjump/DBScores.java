package com.exitjump;



import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.exitjump.R;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DBScores
{
    public static final String KEY_ID = "id";
    public static final String KEY_SCORE = "score";
    public static final String KEY_NAME = "name";
    public static final String KEY_HASH = "hash";
    private static final String TAG = "DBScore";
    
    private static final String DATABASE_NAME = "exitjump_score";
    private static final String DATABASE_TABLE = "scores";
    private static final int DATABASE_VERSION = 9;

    private static final String DATABASE_CREATE =
     "create table "+DATABASE_TABLE+" ("+KEY_ID+" integer primary key autoincrement, "
    + KEY_SCORE+" integer not null, "
    + KEY_NAME+" text not null, " 
    + KEY_HASH+" text not null);";
    
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
    public DBScores open() throws SQLException 
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
    
    public long insertScore(int score, String nom)
    {
    	Cursor c = getAllScores();
    	if (c.getCount() >= 8)
    	{
    		c.moveToLast();
    		if (c.getInt(1) < score)
    		{
    			return replace(c.getInt(0),score,nom);
    		}
    		else return -1;
    	}
    	else
    		return insert(score, nom);
    	
    }
    /*
     *  Renvoit le nombre de score enregistré dans la db
     *  @author Matthieu
     */
    public int nbScore()
    {
    	Cursor c = getAllScores();
    	return c.getCount();
    }
    
	public static String md5(String key) {
		byte[] uniqueKey = key.getBytes();
		byte[] hash = null;
		try {
			hash = MessageDigest.getInstance("MD5").digest(uniqueKey);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		StringBuffer hashString = new StringBuffer();
		for ( int i = 0; i < hash.length; ++i ) {
			String hex = Integer.toHexString(hash[i]);
			if ( hex.length() == 1 ) {
				hashString.append('0');
				hashString.append(hex.charAt(hex.length()-1));
			} else {
				hashString.append(hex.substring(hex.length()-2));
			}
		}
		return hashString.toString();
	}
    
    
    private long insert(int score, String nom)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_SCORE , score);
        initialValues.put(KEY_NAME, nom);
        Resources Res = context.getResources();
        String version =  Res.getString(R.string.app_version);
        String key =  Res.getString(R.string.key);
        String hash = md5(score+nom+key+version);
        initialValues.put(KEY_HASH, hash);
        long retour = -1;
        try {
        	retour = db.insertOrThrow(DATABASE_TABLE, null, initialValues);}
        catch (SQLException e) {
        	Log.e(TAG,"error insert ("+score+","+nom+")");
        	return -1;}
        if (retour < 0)
        {
        	Log.w(TAG,"insert echoue ("+score+","+nom+") retour : "+retour);
    		return -1;
        }
        else
        {
        	Log.i(TAG,"insert ("+score+","+nom+") to index : "+retour);
			return retour;
        }
    }

    private long replace(int id, int score, String nom)
    {
    	ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_ID , id);
        initialValues.put(KEY_SCORE , score);
        initialValues.put(KEY_NAME, nom);
        Resources Res = context.getResources();
        String version =  Res.getString(R.string.app_version);
        String key =  Res.getString(R.string.key);
        String hash = md5(score+nom+key+version);
        initialValues.put(KEY_HASH, hash);
        long retour = -1;
        try {
        	retour = db.replaceOrThrow(DATABASE_TABLE, null, initialValues);}
        catch (SQLException e) {
        	Log.e(TAG,"error replace ("+id+","+score+","+nom+")");
        	return -1;}
        if (retour < 0)
        {
        	Log.w(TAG,"replace echoue ("+id+","+score+","+nom+") retour : "+retour);
    		return -1;
        }
        else
        {
        	Log.i(TAG,"replace ("+id+","+score+","+nom+") to index : "+retour);
			return retour;
        }
    }
    
    //---deletes a particular title---
    public boolean deleteScore(int score) 
    {
        return db.delete(DATABASE_TABLE, KEY_SCORE + 
        		"=" + score, null) > 0;
    }

    //---retrieves all the scores---
    public Cursor getAllScores() 
    {
        return db.query(DATABASE_TABLE, new String[] {
        		KEY_ID,
        		KEY_SCORE,
        		KEY_NAME,
        		KEY_HASH}, 
                null, 
                null, 
                null, 
                null, 
                KEY_SCORE + " DESC");
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
    
    public int getWorstScore()
    {
    	Cursor c = getAllScores();
    	if (c.moveToLast())
    	{
    		int worstScore = c.getInt(1);
    		Log.i(TAG,"getWorstScore : "+worstScore);
    		return worstScore;
    	}
    	else
    	{
    		Log.w(TAG,"getWorstScore echoue");
    		return -1;
    	}
    }
    
}


