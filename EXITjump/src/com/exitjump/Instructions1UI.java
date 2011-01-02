package com.exitjump;

import android.util.Log;
import android.view.MotionEvent;

import com.android.angle.AngleActivity;
import com.android.angle.AngleObject;
import com.android.angle.AngleString;
import com.android.angle.AngleUI;



public class Instructions1UI extends AngleUI
{
	private AngleString strExit;

	public Instructions1UI(AngleActivity activity, Background background)
	{
		super(activity);
		Log.i("InstructionsUI", "constructor debut");
		
		if (background != null)
			addObject(background);
		
		AngleObject ogTexts = new AngleObject();
		addObject(ogTexts);
		
		ogTexts.addObject(new AngleString(((MainActivity)mActivity).fntGlobal, "Instructions", 160, 20, AngleString.aCenter));
		
		ogTexts.addObject(new AngleString(((MainActivity)mActivity).fntGlobalSmall,
										"Use your phone's\n"+
										"accelerometer to\n" +
										"control your character.",
										110, 110,
										AngleString.aLeft));
		ogTexts.addObject(new AngleString(((MainActivity)mActivity).fntGlobalSmall,
							"Jump is\n" +
							"automatic.",
							210, 290,
							AngleString.aLeft));
		ogTexts.addObject(new AngleString(((MainActivity)mActivity).fntGlobalSmall,
				"You have to climb\n" +
				"as high as possible.\n",
				80, 385,
				AngleString.aLeft));
		
		strExit = (AngleString) ogTexts.addObject(new AngleString(((MainActivity)mActivity).fntGlobal, 
													"Next", 
													160, 460, 
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
				((MainActivity) mActivity).setUI(((MainActivity) mActivity).mInstructions2);

			return true;
		}
		return false;
	}
	
}
