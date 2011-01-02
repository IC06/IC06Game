package com.exitjump;

import android.util.Log;
import android.view.MotionEvent;

import com.android.angle.AngleActivity;
import com.android.angle.AngleObject;
import com.android.angle.AngleString;
import com.android.angle.AngleUI;



public class Instructions2UI extends AngleUI
{
	private AngleString strExit;

	public Instructions2UI(AngleActivity activity, Background background)
	{
		super(activity);
		Log.i("InstructionsUI", "constructor debut");
		
		if (background != null)
			addObject(background);
		
		AngleObject ogTexts = new AngleObject();
		addObject(ogTexts);
		
		ogTexts.addObject(new AngleString(((MainActivity)mActivity).fntGlobal, "Instructions", 160, 20, AngleString.aCenter));
		
		
		
		String instruct = "Remember you must go across\n" +
				"the screen sides to change color\n" +
				"and be able to bounce off\n" +
				"on colored plateforms!";
		
		ogTexts.addObject(new AngleString(((MainActivity)mActivity).fntGlobalSmall, instruct, 160, 220, AngleString.aCenter));
		
		instruct = "You are going to \n" +
				"discover bonus\n" +
				"Be warned some of them \n" +
				"are malus...";

		ogTexts.addObject(new AngleString(((MainActivity)mActivity).fntGlobalSmall, instruct, 190, 320, AngleString.aCenter));
		
		strExit = (AngleString) ogTexts.addObject(new AngleString(((MainActivity)mActivity).fntGlobal, 
													"Back", 
													270, 450, 
													AngleString.aCenter));
		
		Log.i("InstructionsUI", "constructor fin");
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
