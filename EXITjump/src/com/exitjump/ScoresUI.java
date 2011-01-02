package com.exitjump;

import android.database.Cursor;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

import com.android.angle.AngleActivity;
import com.android.angle.AngleObject;
import com.android.angle.AngleString;
import com.android.angle.AngleUI;

public class ScoresUI   extends AngleUI
{
	private AngleObject ogMenuTexts;
	private AngleString strExit, strScores, strNames, strNewScore, strOnline;
	
	public ScoresUI(AngleActivity activity, Background mBackGround)
	{
		super(activity);
		if(mBackGround != null)
			addObject(mBackGround);
		ogMenuTexts = new AngleObject();
		
		addObject(ogMenuTexts);

		strNewScore = (AngleString) ogMenuTexts.addObject(new AngleString(((MainActivity)mActivity).fntGlobal, "", 160, 30, AngleString.aCenter));
		strScores = (AngleString) ogMenuTexts.addObject(new AngleString(((MainActivity)mActivity).fntGlobal, "", 30, 100, AngleString.aLeft));
		strNames = (AngleString) ogMenuTexts.addObject(new AngleString(((MainActivity)mActivity).fntGlobal, "", 170, 100, AngleString.aLeft));
		strExit = (AngleString) ogMenuTexts.addObject(new AngleString(((MainActivity)mActivity).fntGlobal, "Back home", 160, 390, AngleString.aCenter));
		strOnline = (AngleString) ogMenuTexts.addObject(new AngleString(((MainActivity)mActivity).fntGlobal, "Online \n   Scores...", 230, 440, AngleString.aCenter));

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
			else if (strOnline.test(eX, eY))
				((MainActivity) mActivity).setUI(((MainActivity) mActivity).mScoresOnLine);

			return true;
		}
		return false;
	}
	
	@Override
	public void onActivate()
	{
		Log.i("ScoresUI", "ScoresUI onActivate debut "+((MainActivity) mActivity).mGame.mScore);
		
		showScores();
		
		if( ((MainActivity) mActivity).mGame.mScore != 0) 
		{
			Log.i("ScoresUI", "Dialog show");
			strNewScore.set("Last Score : " + ((MainActivity)mActivity).mGame.mScore);
		}
		Log.i("ScoresUI", "ScoresUI onActivate fin");
	}
	
	public void showScores() {
    	String scores = "";
    	String names = "";
		DBScores db = new DBScores(mActivity);
		db.open();
        Cursor c = db.getAllScores();
        if (c.moveToFirst())
        {
            do {          
                //DisplayScore(c);
            	scores +=  c.getString(1) + "\n";
            	names += c.getString(2) + "\n";
            } while (c.moveToNext());
        }
        db.close();
        strScores.set(scores);
        strNames.set(names);
	}

	public void DisplayScore(Cursor c)
    {
        Toast.makeText(mActivity, 
                "score : " + c.getString(0) + "\n" +
                "name: " + c.getString(1) ,
                Toast.LENGTH_LONG).show();
    } 
	
	@Override
	public void onDeactivate()
	{
		
	}
}
