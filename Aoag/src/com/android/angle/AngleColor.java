package com.android.angle;

public class AngleColor
{
	public static final AngleColor cBlack=new AngleColor(0,0,0,1);
	public static final AngleColor cRed=new AngleColor(1,0,0,1);
	public static final AngleColor cLime=new AngleColor(0,1,0,1);
	public static final AngleColor cBlue=new AngleColor(0,0,1,1);
	public static final AngleColor cMaroon=new AngleColor(0.5f,0,0,1);
	public static final AngleColor cGreen=new AngleColor(0,0.5f,0,1);
	public static final AngleColor cNavy=new AngleColor(0,0,0.5f,1);
	public static final AngleColor cWhite=new AngleColor(1,1,1,1);
	public float fRed; // Red tint (0 - 1)
	public float fGreen; // Green tint (0 - 1)
	public float fBlue; // Blue tint (0 - 1)
	public float fAlpha; // Alpha channel (0 - 1)
	
	AngleColor (float r, float g, float b, float a)
	{
		fRed=r;
		fGreen=g;
		fBlue=b;
		fAlpha=a;
	}
}
