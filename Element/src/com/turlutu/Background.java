package com.turlutu;


import com.android.angle.AngleObject;
import com.android.angle.AngleSprite;
import com.android.angle.AngleSpriteLayout;

/** 
* @author thomas
*/
public class Background extends AngleObject
{
	public AngleSprite mGround;

	public Background(AngleSpriteLayout layout)
	{
		super(1);

		mGround=(AngleSprite)addObject(new AngleSprite(0,0,layout));
		mGround.mPosition.mX = 256;
		mGround.mPosition.mY = 256f;
	}


}
