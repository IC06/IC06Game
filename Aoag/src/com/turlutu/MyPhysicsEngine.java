package com.turlutu;


import javax.microedition.khronos.opengles.GL10;

import android.util.Log;

import com.android.angle.AnglePhysicObject;
import com.android.angle.AnglePhysicsEngine;
import com.android.angle.AngleSprite;
import com.android.angle.AngleSurfaceView;
import com.android.angle.AngleVector;
import com.turlutu.Ball.Color;
import com.turlutu.Bonus.TypeBonus;

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
	final private float max_dy=130f;
	private float dy,new_y,current_max_dy;
	
	public MyPhysicsEngine(int maxObjects, float worldWidth, float worldHeight,AngleSurfaceView SurfaceView, GameUI gameUI)
	{
		super(maxObjects);
		mWorldWidth = worldWidth;
		mWorldHeight = worldHeight;
		mGLSurfaceView = SurfaceView;
		mGameUI = gameUI;
		dy = 30;
		current_max_dy = 60;
	}

	/*
	 *  TODO Faire bouger les plateformes (voir bonus (faire bouger aussi (avec ou non)))
	 */
	private void addPlateform(final float decalage)
	{
		new_y -= decalage;
		if (new_y < 0)
		{
			new_y = max_dy;
			new_y = dy + (float) (Math.random() * (current_max_dy - dy));
			float d = 5f/dy;
			if (dy < max_dy){dy+=((Math.random()*1.5)+1) * d;}
			if (current_max_dy < max_dy){current_max_dy+=((Math.random()*2)+1) * d;}
			Log.i("DY",""+dy+" "+current_max_dy);
			new Thread() 
			{
				@Override 
				public void run() 
				{
					float new_x = (float) (Math.random() * (320f - Plateforme.SIZE)) + Plateforme.SIZE/2;
					int couleur = (int) (Math.random() * 6);
					AngleSprite sprite;
					Color color;
					switch (couleur)
					{
						case 0:
							color = Color.JAUNE;
							sprite = mGameUI.mPlateformej;
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
					Plateforme newPlateforme = new Plateforme(sprite,color);
					newPlateforme.mPosition.set(new_x,-1);
					addObject(newPlateforme);
					if(Math.random()>0.6) { // 30% de chance d'avoir un bonus
						Bonus bonus = new Bonus(mGameUI, current_max_dy - 60);
						bonus.mPosition.set(new_x+(int) (Math.random() * (Plateforme.SIZE) - (Plateforme.SIZE / 2)),-22);
						addObject(bonus);
					}
				}
			}.start();
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
				Ball ball = (Ball) mChilds[o];

				ball.mAcceleration.mY =  ball.mMass*mGravity.mY - mViscosity * ball.mVelocity.mY;
				ball.mVelocity.mY += ball.mAcceleration.mY * secondsElapsed;
				
				ball.mDelta.mX = ball.mVelocity.mX * secondsElapsed;
				ball.mDelta.mY = ball.mVelocity.mY * secondsElapsed;
				ball.changeSens();
			}
			else if (mChilds[o] instanceof Plateforme)
			{
				Plateforme plateforme = (Plateforme) mChilds[o];
				
				plateforme.mDelta.mX = plateforme.mVelocity.mX * secondsElapsed;
			}
		}
	}


	@Override
	protected void kynetics(float secondsElapsed)
	{
		
		for (int o = 0; o < mChildsCount; o++)
		{
			if (mChilds[o] instanceof Plateforme)
			{
				Plateforme plateforme = (Plateforme) mChilds[o];
				
				plateforme.mPosition.mX += plateforme.mDelta.mX;
				if (plateforme.mPosition.mX < 0)
				{
					plateforme.mPosition.mX = 0;
					plateforme.mVelocity.mX = -plateforme.mVelocity.mX;
				}
				else if (plateforme.mPosition.mX > mWorldWidth)
				{
					plateforme.mPosition.mX = mWorldWidth;
					plateforme.mVelocity.mX = -plateforme.mVelocity.mX;
				}
			}
			else if (mChilds[o] instanceof Ball)
			{
				Ball ball = (Ball) mChilds[o];
				
				// perdu
				// TODO trouver où Matthieu a mis l'autre suppression de la balle dans le code (surement dans le code de ANGLE)
				// apparement c'est pas dans anglephysicengine ou angle physicobject
				if (ball.mPosition.mY > mWorldHeight)
				{
					init();
					mGameUI.backToMenu();
					return;
				}
				
				if ((ball.mDelta.mX != 0) || (ball.mDelta.mY != 0))
				{
					// Changement d'état
					ball.mPosition.mX += ball.mDelta.mX;
					ball.mPosition.mY += ball.mDelta.mY;
					if (ball.mPosition.mX > mWorldWidth)
					{
						ball.mPosition.mX = 0;
						ball.changeColorRight();
					}
					else if  (ball.mPosition.mX < 0)
					{
						ball.mPosition.mX = mWorldWidth;
						ball.changeColorLeft();
					}

					// translation + score
					if (mGameUI != null && ball.mPosition.mY < mWorldHeight/3)
					{
						mCounterScore = (int) (mWorldHeight/3 - ball.mPosition.mY);
						mGameUI.upScore(mCounterScore); // En mettant ça ici, on gagne 2 tests
						translateAll(new AngleVector(0,mWorldHeight/3 - ball.mPosition.mY));
					}
					
					// collisions
					for (int c = 0; c < mChildsCount; c++)
					{
						if (c != o)
						{
							if (mChilds[c] instanceof Plateforme // l'objet est de type plateforme
									&& ball.mVelocity.mY > 0)  // et il est entrain de descendre
							{
								Plateforme plateforme = (Plateforme) mChilds[c];
								if((mGameUI != null && mGameUI.mTypeBonus == TypeBonus.ALLPLATEFORME) || plateforme.mColor == Color.TOUTE || plateforme.mColor == ball.getColor()) // si l'objet est de la même couleure
								{
									if (ball.collide(plateforme))
									{
										ball.jump();
										break;
									}
								}
							} else if (mChilds[c] instanceof Bonus) {
								Bonus bonus = (Bonus) mChilds[c];
								if (ball.collide(bonus))
								{
									bonus.mUsed = true;
									removeObject(bonus);
								}
							}
							else if (!(mChilds[c] instanceof Plateforme) && mChilds[c] instanceof AnglePhysicObject)
							{
								AnglePhysicObject mChildC = (AnglePhysicObject) mChilds[c];
								if (ball.collide(mChildC))
								{
									ball.jump();
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
		if (secondsElapsed > 0.04)
			secondsElapsed = (float) 0.04;
		
		super.step(secondsElapsed);
	}

	/**
	 * @author matthieu, thomas
	 * 
	 */
	@Override
	public void draw(GL10 gl)
	{
		for (int t=1;t<mChildsCount;t++)
			mChilds[t].draw(gl);
		mChilds[0].draw(gl); // balle dessiné en dernier
	}
	
	public void init()
	{
		dy = 30;
		current_max_dy = 60;
	}
}
