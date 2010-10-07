/**
 * 
 */
package com.thomas;

import com.android.angle.AnglePhysicObject;
import com.android.angle.AnglePhysicsEngine;
import com.android.angle.AngleSurfaceView;
import com.android.angle.AngleVector;

/**
 * @author thomas
 *
 */
public class MyPhysicsEngine extends AnglePhysicsEngine
{
	float mWorldWidth, mWorldHeight;
	AngleSurfaceView mGLSurfaceView;
	int ToNewPlateforme = 5;
	
	public MyPhysicsEngine(int maxObjects, float worldWidth, float worldHeight,AngleSurfaceView SurfaceView)
	{
		super(maxObjects);
		mWorldWidth = worldWidth;
		mWorldHeight = worldHeight;
		mGLSurfaceView = SurfaceView; 
	}

	private int randomNew()
	{
		int size, posX, next, now;
		now = (int) (Math.random() * 100);
		next = (int) (Math.random() * (150 - 15)) + 15;
		if(now > 60)
		{
			size = (int) (Math.random() * (mWorldWidth/2 - 50)) + 50;
			posX = (int) (Math.random() * (mWorldWidth - size)) + size / 2;
			Plateforme newPlateforme = new Plateforme(mGLSurfaceView, size, 1);
			newPlateforme.mPosition.set(posX,-1);
			addObject(newPlateforme);
		}
		return next;
	}
	
	private void translateAll(AngleVector t)
	{
		ToNewPlateforme--;
		if(ToNewPlateforme<=0) {
			ToNewPlateforme = randomNew();
		}
		for (int o = 0; o < mChildsCount; o++)
		{
			if (mChilds[o] instanceof AnglePhysicObject)
			{
				AnglePhysicObject mChildO = (AnglePhysicObject) mChilds[o];
				mChildO.mPosition.add(t);
				if(mChildO.mPosition.mY > mWorldHeight) 
				{
					removeObject(mChildO);
				}
			}
		}
	}
	
	
	@Override
	protected void physics(float secondsElapsed)
	{
		super.physics(secondsElapsed);
	}

	@Override
	protected void kynetics(float secondsElapsed)
	{
		for (int o = 0; o < mChildsCount; o++)
		{
			if (mChilds[o] instanceof Ball)
			{
				Ball mChildO = (Ball) mChilds[o];
				if ((mChildO.mDelta.mX != 0) || (mChildO.mDelta.mY != 0))
				{
					// Collision
					mChildO.mPosition.mX += mChildO.mDelta.mX;
					mChildO.mPosition.mY += mChildO.mDelta.mY;
					if (mChildO.mPosition.mX > mWorldWidth)
					{
						mChildO.mPosition.mX = 0;
						mChildO.switchColor();
					}
					else if  (mChildO.mPosition.mX < 0)
					{
						mChildO.mPosition.mX = mWorldWidth;
						mChildO.switchColor();
					}
					if (mChildO.mPosition.mY < mWorldHeight/4)
					{
						translateAll(new AngleVector(0,1));
					}
					
					for (int c = 0; c < mChildsCount; c++)
					{
						if (c != o)
						{
							if (mChilds[c] instanceof Plateforme && mChildO.mVelocity.mY > 0) // l'objet est de type plateforme
							{
								Plateforme mChildC = (Plateforme) mChilds[c];
								if (mChildO.collide(mChildC))
								{
									mChildO.mPosition.mX -= mChildO.mDelta.mX;
									mChildO.mPosition.mY -= mChildO.mDelta.mY;
									mChildO.mVelocity.mY = -3 * mChildO.mRadius * 2; // la balle rebondit toujours de la même hauteur (simule un saut)
									mChildC.mDelta.mX = mChildC.mVelocity.mX * secondsElapsed;
									mChildC.mDelta.mY = mChildC.mVelocity.mY * secondsElapsed;
									break;
								}
							}
							else if (!(mChilds[c] instanceof Plateforme) && mChilds[c] instanceof AnglePhysicObject)
							{
								AnglePhysicObject mChildC = (AnglePhysicObject) mChilds[c];
								if (mChildO.collide(mChildC))
								{
									mChildO.mPosition.mX -= mChildO.mDelta.mX;
									mChildO.mPosition.mY -= mChildO.mDelta.mY;
									mChildC.mDelta.mX = mChildC.mVelocity.mX * secondsElapsed;
									mChildC.mDelta.mY = mChildC.mVelocity.mY * secondsElapsed;
									break;
								}
							}
						}
					}
				}
			}
		}
	}
	
	@Override
	public void step(float secondsElapsed)
	{
		super.step(secondsElapsed);
	}
}
