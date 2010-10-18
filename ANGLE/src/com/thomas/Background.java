package com.thomas;

import javax.microedition.khronos.opengles.GL10;

import com.android.angle.AngleObject;
import com.android.angle.AngleSprite;
import com.android.angle.AngleSpriteLayout;
import com.android.angle.AngleVector;

/** 
* @author Ivan Pajuelo
*/
public class Background extends AngleObject
{
	public AngleSprite mGround;

	//STEP 28:
	//This object should be simple and contain only the smileys,
	//but I have a little more complicated to show the versatility of the engine
	//In the constructor, we say that may contain up to 100 children.
	//The chid 0, will always be the bottom. So we fit 99 smileys
	//>PASO 28:
	//>Este objeto deber�a ser m�s simple y contener �nicamente los smileys,
	//>pero lo he complicado un poco m�s para mostrar la versatilidad del motor
	//>Para empezar, en el constructor, le decimos que podr� contener hasta 100 hijos.
	//>El hijo 0, siempre ser� el fondo. As� que nos caben 99 smileys
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
