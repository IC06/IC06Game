/**
 * 
 */
package com.thomas;

import com.android.angle.AnglePhysicObject;
import com.android.angle.AnglePhysicsEngine;
import com.android.angle.AngleVector;

/**
 * @author thomas
 *
 */
public class MyPhysicsEngine extends AnglePhysicsEngine
{
	float mWorldWidth, mWorldHeight;
	
	public MyPhysicsEngine(int maxObjects, float worldWidth, float worldHeight)
	{
		super(maxObjects);
		mWorldWidth = worldWidth;
		mWorldHeight = worldHeight;
	}

	private void translateAll(AngleVector t)
	{
		for (int o = 0; o < mChildsCount; o++)
		{
			if (mChilds[o] instanceof AnglePhysicObject)
			{
				AnglePhysicObject mChildO = (AnglePhysicObject) mChilds[o];
				mChildO.mPosition.add(t);
				if(mChildO.mPosition.mY > mWorldHeight) 
				{ if (mChilds[o] instanceof Box) {
					mChildO.mPosition.mY = -1; 
				} else {
					removeObject(mChildO);
				}
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
