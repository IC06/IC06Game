package com.turlutu;

import android.view.MotionEvent;

import com.android.angle.AngleActivity;
import com.android.angle.AngleObject;
import com.android.angle.AngleSprite;
import com.android.angle.AngleSpriteLayout;
import com.android.angle.AngleString;
import com.android.angle.AngleUI;

public class MenuUI  extends AngleUI
{
	
	private AngleObject ogMenuTexts;
	
	private AngleString strPlay,  strHiScores, strHiScoresOnLine, strExit, strOptions, strInstructions;

	public MenuUI(AngleActivity activity)
	{
		super(activity);
		ogMenuTexts = new AngleObject();

		
		addObject(new AngleSprite(160, 240, new AngleSpriteLayout(mActivity.mGLSurfaceView, 320, 480,  com.turlutu.R.drawable.bg_menu)));
		
		addObject(ogMenuTexts);

		strPlay = (AngleString) ogMenuTexts.addObject(new AngleString(((MainActivity)mActivity).fntGlobal, "Play", 240, 120, AngleString.aLeft));
		strHiScores = (AngleString) ogMenuTexts.addObject(new AngleString(((MainActivity)mActivity).fntGlobal, "Hi Score", 210, 215, AngleString.aCenter));
		strHiScoresOnLine = (AngleString) ogMenuTexts.addObject(new AngleString(((MainActivity)mActivity).fntGlobal, "On line Score", 25, 40, AngleString.aLeft));
		strInstructions = (AngleString) ogMenuTexts.addObject(new AngleString(((MainActivity)mActivity).fntGlobal, "Instructions", 25, 100, AngleString.aLeft));
		strOptions = (AngleString) ogMenuTexts.addObject(new AngleString(((MainActivity)mActivity).fntGlobal, "Settings", 175, 295, AngleString.aCenter));
		strExit = (AngleString) ogMenuTexts.addObject(new AngleString(((MainActivity)mActivity).fntGlobal, "Exit", 250, 360, AngleString.aCenter));
		
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
			else if (strInstructions.test(eX, eY))
				((MainActivity) mActivity).setUI(((MainActivity) mActivity).mInstructions);
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
		((MainActivity)mActivity).dialog.dismiss();
	}
	
}