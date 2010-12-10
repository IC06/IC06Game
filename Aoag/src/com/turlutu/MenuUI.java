package com.turlutu;

import android.graphics.Typeface;
import android.view.MotionEvent;

import com.android.angle.AngleActivity;
import com.android.angle.AngleFont;
import com.android.angle.AngleObject;
import com.android.angle.AngleSprite;
import com.android.angle.AngleSpriteLayout;
import com.android.angle.AngleString;
import com.android.angle.AngleUI;

public class MenuUI  extends AngleUI
{
	
	private AngleObject ogMenuTexts;
	
	private AngleString strPlay,  strHiScores, strHiScoresOnLine, strExit, strOptions;

	public MenuUI(AngleActivity activity)
	{
		super(activity);
		ogMenuTexts = new AngleObject();

		
		addObject(new AngleSprite(160, 240, new AngleSpriteLayout(mActivity.mGLSurfaceView, 320, 480,  com.turlutu.R.drawable.bg_menu)));
		
		addObject(ogMenuTexts);

		AngleFont fntCafe=new AngleFont(mActivity.mGLSurfaceView, 25, Typeface.createFromAsset(mActivity.getAssets(),"cafe.ttf"), 222, 0, 0, 0, 0, 0, 255);
		
		strPlay = (AngleString) ogMenuTexts.addObject(new AngleString(fntCafe, "Play", 160, 110, AngleString.aLeft));
		strHiScores = (AngleString) ogMenuTexts.addObject(new AngleString(fntCafe, "Hi Score", 160, 210, AngleString.aCenter));
		strHiScoresOnLine = (AngleString) ogMenuTexts.addObject(new AngleString(fntCafe, "On line Score", 50, 50, AngleString.aLeft));
		strOptions = (AngleString) ogMenuTexts.addObject(new AngleString(fntCafe, "Options", 175, 295, AngleString.aCenter));
		strExit = (AngleString) ogMenuTexts.addObject(new AngleString(fntCafe, "Exit", 250, 360, AngleString.aCenter));
		
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		if (event.getAction() == MotionEvent.ACTION_DOWN)
		{
			float eX = event.getX();
			float eY = event.getY();

			if (strPlay.test(eX, eY))
				((MainActivity) mActivity).setUI(((MainActivity) mActivity).mGame);
			else if (strOptions.test(eX, eY))
				((MainActivity) mActivity).setUI(((MainActivity) mActivity).mOptions);
			else if (strHiScores.test(eX, eY))
				((MainActivity) mActivity).setUI(((MainActivity) mActivity).mScores);
			else if (strHiScoresOnLine.test(eX, eY))
				((MainActivity) mActivity).setUI(((MainActivity) mActivity).mScoresOnLine);
			else if (strExit.test(eX, eY))
				mActivity.finish();

			return true;
		}
		return false;
	}

	@Override
	public void onActivate()
	{
		super.onActivate();
	}
	
}