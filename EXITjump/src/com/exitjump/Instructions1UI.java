package com.exitjump;

import android.util.Log;
import android.view.MotionEvent;

import com.android.angle.AngleActivity;
import com.android.angle.AngleObject;
import com.android.angle.AngleString;
import com.android.angle.AngleUI;



public class Instructions1UI extends AngleUI
{
	
	private AngleObject ogTexts;
	private AngleString strExit;
	private Background mBackgroud;

	public Instructions1UI(AngleActivity activity, Background mBackGround)
	{
		super(activity);
		Log.i("InstructionsUI", "constructor debut");
		ogTexts = new AngleObject();
		addObject(ogTexts);

		ogTexts.addObject(new AngleString(((MainActivity)mActivity).fntGlobal, "Instructions", 160, 20, AngleString.aCenter));
		
		String instruct = "Use your phone's accelerometer\n" +
				"to control your character.\n" +
				"You have to climb\n" +
				"as high as possible.\n" +
				"Warning some bonus will help you\n" +
				"and some other will not ...\n" +
				"\n " +
				"Remember you must go across\n" +
				"the screen sides to change color\n" +
				"and be able to bounce\n" +
				"on the platforms of your color!";
		
		ogTexts.addObject(new AngleString(((MainActivity)mActivity).fntGlobal1, instruct, 160, 140, AngleString.aCenter));
		strExit = (AngleString) ogTexts.addObject(new AngleString(((MainActivity)mActivity).fntGlobal, "Go back", 160, 390, AngleString.aCenter));
		Log.i("InstructionsUI", "constructor fin");
	}
	
	private void showInstructions1()
	{
		
	}
	
	private void showInstructions2()
	{
		
	}
	
	@Override
	public void onActivate()
	{
		Log.i("InstructionsUI", "onActivate debut");
		Log.i("InstructionsUI", "onActivate fin");
	}
	
	@Override
	public void onDeactivate()
	{
		Log.i("InstructionsUI", "onDeactivate debut");
		Log.i("InstructionsUI", "onDeactivate fin");
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
	
}