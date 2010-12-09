package com.android.angle;

import javax.microedition.khronos.opengles.GL10;

public class AngleScreenEraser extends AngleObject
{
	private AngleColor lColor;
	
	public AngleScreenEraser()
	{
		super();
		lColor=AngleColor.cBlack;
	}

	public AngleScreenEraser(AngleColor color)
	{
		super();
		lColor=color;
	}

	@Override
	public void draw(GL10 gl)
	{
      gl.glClearColor(lColor.fRed,lColor.fGreen,lColor.fBlue,lColor.fAlpha);
      gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
	}

}
