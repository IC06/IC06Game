package com.alt90.angle2;

import android.util.Log;

/**
 * FPS displayed in LogCat
 * @author Ivan Pajuelo
 *
 */
public class AngleFPSCounter extends AngleObject
{
	private float lTime;
	private int lCountEvery;
	private int lFrameCount;
	public float fFPS;
	
	public AngleFPSCounter ()
	{
		lTime=0;
		fFPS=0;
		lCountEvery=100;
		lFrameCount=lCountEvery;
	}

	public AngleFPSCounter (int countEvery)
	{
		lTime=0;
		fFPS=0;
		lCountEvery=countEvery;
		lFrameCount=lCountEvery;
	}

	@Override
	public void step(float secondsElapsed)
	{
		lTime+=secondsElapsed;
		lFrameCount--;
		if (lFrameCount == 0)
		{
			lFrameCount=lCountEvery;
			fFPS=lCountEvery/lTime;
			lTime=0;
			Log.v("FPS","" + fFPS);
		}
	}
}
