package com.turlutu;


import javax.microedition.khronos.opengles.GL10;

import com.android.angle.AnglePhysicObject;
import com.android.angle.AnglePhysicsEngine;
import com.android.angle.AngleSpriteLayout;
import com.android.angle.AngleSprite;
import com.android.angle.AngleSurfaceView;
import com.android.angle.AngleVector;
import com.turlutu.Ball.Color;

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
	GameUI mGameUI;
	private int mCounterScore;
	
	public MyPhysicsEngine(int maxObjects, float worldWidth, float worldHeight,AngleSurfaceView SurfaceView, GameUI gameUI)
	{
		super(maxObjects);
		mWorldWidth = worldWidth;
		mWorldHeight = worldHeight;
		mGLSurfaceView = SurfaceView;
		mGameUI = gameUI;
	}

	private void addPlateform(final float decalage)
	{
		// TODO : cette fonction est pas terrible pour l'instant
		new Thread() {
			@Override public void run() {
				float posX;
				int couleur;
				Color color;
				AngleSprite sprite;
				toNewPlateform-=(int)decalage;
				if(toNewPlateform<0) {
				    toNewPlateform = (int) (Math.random() * (80-15) + 15);
					posX = (float) (Math.random() * (mWorldWidth - Plateforme.SIZE)) + Plateforme.SIZE / 2;
					couleur = (int) (Math.random() * 5);
					// TODO les layouts
					switch (couleur)
					{
						case 0:
							color = Color.BLEU;
							sprite = mGameUI.mPlateformeb;
							break;
							
						case 1:
							color = Color.ROUGE;
							sprite = mGameUI.mPlateformer;
							break;
							
						case 2:
							color = Color.VERT;
							sprite = mGameUI.mPlateformev;
							break;
							
						case 3:
						default:
							color = Color.TOUTE;
							sprite = mGameUI.mPlateformew;
							break;
					}
					if(Math.random()>0.7) { // 30% de chance d'avoir un bonus
						Bonus bonus = new Bonus(mGameUI);
						bonus.mPosition.set(posX+(int) (Math.random() * (Plateforme.SIZE) - (Plateforme.SIZE / 2)),-15);
						addObject(bonus);
					}
					Plateforme newPlateforme = new Plateforme(sprite,color);
					newPlateforme.mPosition.set(posX,-1);
					addObject(newPlateforme);
				}
			}
		}.start();
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
					if(mChildO instanceof Bonus)
					{
						Bonus mybonus = (Bonus) mChildO;
						mybonus.end();
					}
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
				Ball ball = (Ball) mChilds[o];
				ball.mAcceleration.mY =  ball.mMass*mGravity.mY - mViscosity * ball.mVelocity.mY;
				ball.mVelocity.mY += ball.mAcceleration.mY * secondsElapsed;
				
				ball.mDelta.mX = ball.mVelocity.mX * secondsElapsed;
				ball.mDelta.mY = ball.mVelocity.mY * secondsElapsed;
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
				
				// perdu
				// TODO trouver où Matthieu a mis l'autre suppression de la balle dans le code (surement dans le code de ANGLE)
				// apparement c'est pas dans anglephysicengine ou angle physicobject
				if (mChildO.mPosition.mY > mWorldHeight)
				{
					mGameUI.backToMenu();
					return;
				}
				
				if ((mChildO.mDelta.mX != 0) || (mChildO.mDelta.mY != 0))
				{
					// Changement d'état
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

					// translation + score
					if (mChildO.mPosition.mY < mWorldHeight/3)
					{
						mCounterScore = (int) (mWorldHeight/3 - mChildO.mPosition.mY);
						mGameUI.upScore(mCounterScore); // En mettant ça ici, on gagne 2 tests
						translateAll(new AngleVector(0,mWorldHeight/3 - mChildO.mPosition.mY));
					}
					
					// collisions
					for (int c = 0; c < mChildsCount; c++)
					{
						if (c != o)
						{
							if (mChilds[c] instanceof Plateforme // l'objet est de type plateforme
									&& mChildO.mVelocity.mY > 0)  // et il est entrain de descendre
							{
								Plateforme mChildC = (Plateforme) mChilds[c];
								if(mChildC.mColor == Color.TOUTE || mChildC.mColor == mChildO.getColor()) // si l'objet est de la même couleure
								{
									if (mChildO.collide(mChildC))
									{
										mChildO.jump();
										break;
									}
								}
							} else if (mChilds[c] instanceof Bonus) {
								Bonus mChildC = (Bonus) mChilds[c];
								if (mChildO.collide(mChildC))
								{
									mChildC.touch();
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
		if (secondsElapsed > 0.04)
			secondsElapsed = (float) 0.04;
		
		super.step(secondsElapsed);
	}

	/**
	 * @author thomas
	 * 
	 */
	@Override
	public void draw(GL10 gl)
	{
		for (int t=1;t<mChildsCount;t++)
			mChilds[t].draw(gl);
		mChilds[0].draw(gl); // balle dessiné en dernier
	}
	
}