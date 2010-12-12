package com.turlutu;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.database.Cursor;
import android.os.Looper;
import android.util.Log;
import android.view.MotionEvent;

import android.widget.Toast;

import com.android.angle.AngleActivity;
import com.android.angle.AngleObject;
import com.android.angle.AngleString;
import com.android.angle.AngleUI;

public class OnLineScoresUI   extends AngleUI
{
	private AngleObject ogMenuTexts;
	private AngleString strExit, strScores, strNames, strPushScore;
	
	public OnLineScoresUI(AngleActivity activity, Background mBackGround)
	{
		super(activity);
		if(mBackGround != null)
			addObject(mBackGround);
		ogMenuTexts = new AngleObject();
		
		addObject(ogMenuTexts);

		strPushScore = (AngleString) ogMenuTexts.addObject(new AngleString(((MainActivity)mActivity).fntGlobal, "Send my score", 160, 30, AngleString.aCenter));
		strScores = (AngleString) ogMenuTexts.addObject(new AngleString(((MainActivity)mActivity).fntGlobal, "", 30, 100, AngleString.aLeft));
		strNames = (AngleString) ogMenuTexts.addObject(new AngleString(((MainActivity)mActivity).fntGlobal, "", 170, 100, AngleString.aLeft));
		strExit = (AngleString) ogMenuTexts.addObject(new AngleString(((MainActivity)mActivity).fntGlobal, "Back home", 160, 390, AngleString.aCenter));

	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		if (event.getAction() == MotionEvent.ACTION_DOWN)
		{
			float eX = event.getX();
			float eY = event.getY();

			if (strExit.test(eX, eY))
				((MainActivity) mActivity).setUI(((MainActivity) mActivity).mMenu);
			else if (strPushScore.test(eX, eY)) {
				Toast.makeText(mActivity, 
		                "You're score are being send please wait !" ,
		                Toast.LENGTH_SHORT).show();
				UploadMyScore();
			}

			return true;
		}
		return false;
	}
	
	@Override
	public void onActivate()
	{
		Log.i("ScoresUI", "OnlineScoresUI onActivate debut "+((MainActivity) mActivity).mGame.mScore);
		new Thread() 
		{
			@Override 
			public void run() 
			{
				Looper.prepare();
				getScores();
				checkVersion();
		        Looper.loop();
			}
		}.start();
		Log.i("ScoresUI", "OnlineScoresUI onActivate fin");
	}
	
	public void UploadMyScore() {
		new Thread() 
		{
			@Override 
			public void run() 
			{
				Looper.prepare();
				DBScores db = new DBScores(mActivity);
				db.open();
		        Cursor c = db.getAllScores();
		        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		        String version =  mActivity.getString(R.string.app_version);
				pairs.add(new BasicNameValuePair("version", version));
		        if (c.moveToFirst())
		        {
		        	int i = 0;
		            do {
		        		pairs.add(new BasicNameValuePair("name"+i, c.getString(2)));
		        		pairs.add(new BasicNameValuePair("score"+i, c.getString(1)));
		        		i++;
		            } while (c.moveToNext());
		        }
		        db.close();
				postData(pairs);
		        getScores();
		        Looper.loop();
			}
		}.start();
	}
	
	
	public static String md5(String key) throws NoSuchAlgorithmException {
		byte[] uniqueKey = key.getBytes();
		byte[] hash = null;
		hash = MessageDigest.getInstance("MD5").digest(uniqueKey);
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
	
	
	public void postData(List<NameValuePair> pairs) {
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost("http://wwwetu.utc.fr/~mguffroy/Score.php?page=add");
		
		try {
			pairs.add(new BasicNameValuePair("sign", md5(pairs.toString()+"I am the best")));
		} catch (NoSuchAlgorithmException e1) {
			Log.e("POST", "Error");
		}
		try {
			post.setEntity(new UrlEncodedFormEntity(pairs));
		} catch (UnsupportedEncodingException e) {
			Log.e("POST", "UnsupportedEncodingException");
		}
		try {
			ResponseHandler<String> responseHandler=new BasicResponseHandler();
			String responseBody=client.execute(post, responseHandler);
			Log.e("http",responseBody );
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

    }
	
	
	public String getData(String info) {
		DefaultHttpClient client = new DefaultHttpClient();
		String version =  mActivity.getString(R.string.app_version);
		HttpGet getMethod=new HttpGet("http://wwwetu.utc.fr/~mguffroy/Score.php?page=get&info="+info+"&version="+version);

		try {
			ResponseHandler<String> responseHandler=new BasicResponseHandler();
			String responseBody=client.execute(getMethod, responseHandler);
			return responseBody;
		}
		catch (Throwable t) {
	        Toast.makeText(mActivity, 
	                "An error was catched are you sure to be connected ?" ,
	                Toast.LENGTH_SHORT).show();
			Log.e("Exitjump","Exception in getData()", t);
			return "error";
		}
	}
	
	public void checkVersion() {
		DefaultHttpClient client = new DefaultHttpClient();
		String version =  mActivity.getString(R.string.app_version);
		HttpGet getMethod=new HttpGet("http://wwwetu.utc.fr/~mguffroy/Score.php?page=check&version="+version);

		try {
			ResponseHandler<String> responseHandler=new BasicResponseHandler();
			String responseBody=client.execute(getMethod, responseHandler);
			if(responseBody.length() > 5) {
				Toast.makeText(mActivity, 
	                responseBody ,
	                Toast.LENGTH_LONG).show();
			}
		}
		catch (Throwable t) {
			Log.e("Exitjump","Exception in getData()", t);
		}
	}
	
	public void getScores() {
		Toast.makeText(mActivity, 
                "Retrieving the score list on server..." ,
                Toast.LENGTH_SHORT).show();
    	String scores = getData("score");
    	String names = getData("name");
        strScores.set(scores);
        strNames.set(names);
	}
	
	
	@Override
	public void onDeactivate()
	{
		
	}
}
