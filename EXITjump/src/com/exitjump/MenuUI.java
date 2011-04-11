package com.exitjump;

import android.view.MotionEvent;

import com.android.angle.AngleActivity;
import com.android.angle.AngleObject;
import com.android.angle.AngleString;
import com.android.angle.AngleUI;

public class MenuUI  extends AngleUI
{
	
	private AngleObject ogMenuTexts;
	
	private AngleString strPlay,  strHiScores, strHiScoresOnLine, strExit, strOptions, strInstructions;

	public MenuUI(AngleActivity activity, Background mBackground)
	{
		super(activity);
		ogMenuTexts = new AngleObject();

		if (mBackground != null)
			addObject(mBackground);
		
		addObject(ogMenuTexts);

		strPlay = (AngleString) ogMenuTexts.addObject(new AngleString(((MainActivity)mActivity).fntGlobal, "Play", 240, 120, AngleString.aLeft));
		strHiScores = (AngleString) ogMenuTexts.addObject(new AngleString(((MainActivity)mActivity).fntGlobal, "Hi Score", 230, 210, AngleString.aCenter));
		//strHiScoresOnLine = (AngleString) ogMenuTexts.addObject(new AngleString(((MainActivity)mActivity).fntGlobal, "On line Score", 25, 40, AngleString.aLeft));
		strInstructions = (AngleString) ogMenuTexts.addObject(new AngleString(((MainActivity)mActivity).fntGlobal, "Instructions", 125, 290, AngleString.aLeft));
		strOptions = (AngleString) ogMenuTexts.addObject(new AngleString(((MainActivity)mActivity).fntGlobal, "Settings", 240, 355, AngleString.aCenter));
		strExit = (AngleString) ogMenuTexts.addObject(new AngleString(((MainActivity)mActivity).fntGlobal, "Exit", 265, 440, AngleString.aCenter));
		
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
			/*else if (strHiScoresOnLine.test(eX, eY))
				((MainActivity) mActivity).setUI(((MainActivity) mActivity).mScoresOnLine);*/
			else if (strInstructions.test(eX, eY))
				((MainActivity) mActivity).setUI(((MainActivity) mActivity).mInstructions1);
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