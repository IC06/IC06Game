package com.thomas;

import javax.microedition.khronos.opengles.GL10;

import com.android.angle.AngleObject;
import com.android.angle.AngleSprite;
import com.android.angle.AngleSpriteLayout;
import com.android.angle.AngleVector;

/** 
* @author thomas
*/
public class Background extends AngleObject
{
	public AngleSprite mGround;

	public Background(AngleSpriteLayout layout)
	{
		super(1);

		mGround=(AngleSprite)addObject(new AngleSprite(160,240,layout));
		mGround.mPosition.mX = 200;
		mGround.mPosition.mY = 200;
	}

	@Override
	public void step(float secondsElapsed)
	{
		/*smileyTO-=secondsElapsed;
		if (smileyTO<0)
		{
			addObject(new Smiley(mGame));
			smileyTO=sSmileySpawn;
		}
		super.step(secondsElapsed);*/
	}

	public void moveTo(AngleVector mSight)
	{
		/*mGround.mPosition.set(160-mSight.mX*(256-160),240-mSight.mY*(256-240));
		for (int s=1;s<count();s++)
			((Scrollable)childAt(s)).place();*/
	}


	@Override
	public void draw(GL10 gl)
	{
		mGround.draw(gl);
	}

}
