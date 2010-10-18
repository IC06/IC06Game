/**
 * 
 */
package com.thomas;

import javax.microedition.khronos.opengles.GL10;

import com.android.angle.AnglePhysicObject;
import com.android.angle.AnglePhysicsEngine;
import com.android.angle.AngleSurfaceView;
import com.android.angle.AngleVector;
import com.thomas.Ball.Color;

/**
 * L'objet ajouté en premier à cet objet sera dessiné en dernier
 * @author thomas
 *
 */
public class MyPhysicsEngine extends AnglePhysicsEngine
{
	float mWorldWidth, mWorldHeight;
	int toNewPlateform=10;
	AngleSurfaceView mGLSurfaceView;
	
	public MyPhysicsEngine(int maxObjects, float worldWidth, float worldHeight,AngleSurfaceView SurfaceView)
	{
		super(maxObjects);
		mWorldWidth = worldWidth;
		mWorldHeight = worldHeight;
		mGLSurfaceView = SurfaceView; 
	}

	private void addPlateform(float decalage)
	{
		// TODO : cette fonction est pas térrible pour l'instant
		float size, posX;
		int couleur;
		size = 85.f;
		toNewPlateform-=(int)decalage;
		if(toNewPlateform<0) {
		    toNewPlateform = (int) (Math.random() * (100-25) + 25);
			posX = (float) (Math.random() * (mWorldWidth - size)) + size / 2;
			couleur = (int) (Math.random() * 5);
			Plateforme newPlateforme = new Plateforme(mGLSurfaceView, size, 1,couleur);
			newPlateforme.mPosition.set(posX,-1);
			addObject(newPlateforme);
		}
	}
	
	private void translateAll(AngleVector t)
	{
		addPlateform(t.mY);
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
		for (int o = 0; o < mChildsCount; o++)
		{
			if (mChilds[o] instanceof Ball)
			{
				Ball mChildO = (Ball) mChilds[o];
				// Gravity
				//mChildO.mVelocity.mX += mChildO.mMass * mGravity.mX * secondsElapsed;
				mChildO.mVelocity.mY += mChildO.mMass * mGravity.mY * secondsElapsed;
				/*if ((mChildO.mVelocity.mX != 0) || (mChildO.mVelocity.mY != 0))
				{
					// Air viscosity
					if (mViscosity > 0)
					{
						float surface = mChildO.getSurface();
						if (surface > 0)
						{
							float decay = surface * mViscosity * secondsElapsed;
							if (mChildO.mVelocity.mX > decay)
								mChildO.mVelocity.mX -= decay;
							else if (mChildO.mVelocity.mX < -decay)
								mChildO.mVelocity.mX += decay;
							else
								mChildO.mVelocity.mX = 0;
							if (mChildO.mVelocity.mY > decay)
								mChildO.mVelocity.mY -= decay;
							else if (mChildO.mVelocity.mY < -decay)
								mChildO.mVelocity.mY += decay;
							else
								mChildO.mVelocity.mY = 0;
						}
					}
				}*/
				// Velocity
				mChildO.mDelta.mX = mChildO.mVelocity.mX * secondsElapsed;
				mChildO.mDelta.mY = mChildO.mVelocity.mY * secondsElapsed;
			}
		}
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
						mChildO.changeColorRight();
					}
					else if  (mChildO.mPosition.mX < 0)
					{
						mChildO.mPosition.mX = mWorldWidth;
						mChildO.changeColorLeft();
					}
					
					// TODO : meilleur gestion du défilement de l'écran, il faut enfait que la derniere plateforme touchée se retrouve en bas,
					// donc il faut faire par rapport aux plateformes et non par rapport à la balle
					if (mChildO.mPosition.mY < mWorldHeight/2)
						translateAll(new AngleVector(0,mWorldHeight/2 - mChildO.mPosition.mY));
					
					for (int c = 0; c < mChildsCount; c++)
					{
						if (c != o)
						{
							if (mChilds[c] instanceof Plateforme // l'objet est de type plateforme
									&& mChildO.mVelocity.mY > 0)  // et il est entrain de monter
							{
								Plateforme mChildC = (Plateforme) mChilds[c];
								if(mChildC.mColor == mChildO.getColor() || mChildC.mColor == Color.TOUTE) // si l'objet est de la même couleure
								{
									if (mChildO.collide(mChildC))
									{
										mChildO.mPosition.mX -= mChildO.mDelta.mX;
										mChildO.mVelocity.mY = - 15 * mChildO.mRadius; // la balle rebondit toujours de la même hauteur (simule un saut)
										mChildC.mDelta.mX = mChildC.mVelocity.mX * secondsElapsed;
										mChildC.mDelta.mY = mChildC.mVelocity.mY * secondsElapsed;
										break;
									}
								}
							}
							/*else if (!(mChilds[c] instanceof Plateforme) && mChilds[c] instanceof AnglePhysicObject)
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
							}*/
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

	/**
	 * @author thomas
	 */
	@Override
	public void draw(GL10 gl)
	{
		for (int t=1;t<mChildsCount;t++)
			mChilds[t].draw(gl);
		mChilds[0].draw(gl); // balle dessiné en dernier
	}
	
}
