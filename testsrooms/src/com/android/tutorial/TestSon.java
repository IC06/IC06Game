package com.android.tutorial;

import android.os.Bundle;
import android.view.MotionEvent;

import com.android.angle.AngleActivity;
import com.android.angle.AngleSound;

public class TestSon extends AngleActivity
{

	private AngleSound sndMachineGun;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		sndMachineGun=new AngleSound(this,R.raw.jump);
		
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		try
		{
			Thread.sleep(16);
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		// -------------------------
		if (event.getAction()==MotionEvent.ACTION_DOWN)
		{
			sndMachineGun.play(1,false);
		}
		else if (event.getAction()==MotionEvent.ACTION_UP)
		{
			sndMachineGun.stop();
		}
		return true;
		//return super.onTouchEvent(event);
	}
}
