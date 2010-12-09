package com.android.angle;

public class AngleRect
{
	public AngleVector fSize;
	public AngleVector fPosition;

	public AngleRect(int left, int top, int width, int height)
	{
		fPosition=new AngleVector(left, top);
		fSize=new AngleVector(width, height);
	}
}
