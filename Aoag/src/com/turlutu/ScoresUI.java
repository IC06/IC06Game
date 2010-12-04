package com.turlutu;

import android.database.Cursor;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.widget.Toast;

import com.android.angle.AngleActivity;
import com.android.angle.AngleFont;
import com.android.angle.AngleObject;
import com.android.angle.AngleString;
import com.android.angle.AngleUI;

public class ScoresUI   extends AngleUI
{
	private AngleObject ogMenuTexts;
	private AngleString strExit, strScores, strNewScore;
	
	public ScoresUI(AngleActivity activity)
	{
		super(activity);
		ogMenuTexts = new AngleObject();
		
		addObject(ogMenuTexts);

		AngleFont fntCafe=new AngleFont(mActivity.mGLSurfaceView, 25, Typeface.createFromAsset(mActivity.getAssets(),"cafe.ttf"), 222, 0, 0, 30, 200, 255, 255);

		strNewScore = (AngleString) ogMenuTexts.addObject(new AngleString(fntCafe, "", 160, 30, AngleString.aCenter));
		strScores = (AngleString) ogMenuTexts.addObject(new AngleString(fntCafe, "", 160, 100, AngleString.aCenter));
		strExit = (AngleString) ogMenuTexts.addObject(new AngleString(fntCafe, "Retour", 160, 390, AngleString.aCenter));
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

			return true;
		}
		return false;
	}
	
	@Override
	public void onActivate()
	{
		strNewScore.set("Your Score : " + ((MainActivity)mActivity).mGame.mScore);
    	String scores = "";
		DBScores db = new DBScores(mActivity);
		db.open();
        Cursor c = db.getAllScores();
        if (c.moveToFirst())
        {
            do {          
                //DisplayScore(c);
            	scores +=  c.getString(0) + "    " + c.getString(1) + "\n";
            } while (c.moveToNext());
        }
        db.close();
        strScores.set(scores);
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
