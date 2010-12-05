package com.turlutu;


import com.android.angle.AngleSprite;
import com.android.angle.AngleSpriteLayout;

/** 
* @author thomas
*/
public class Background extends AngleSprite
{
	public AngleSprite mGround;

	public Background(AngleSpriteLayout layout)
	{
		super(layout);

		mPosition.mX = 160;
		mPosition.mY = 240;
	}


}
