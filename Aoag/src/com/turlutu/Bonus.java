package com.turlutu;

import javax.microedition.khronos.opengles.GL10;

import android.util.Log;

import com.android.angle.AnglePhysicObject;
import com.android.angle.AngleSound;
import com.android.angle.AngleSprite;

/**
 * extends AnglePhysicObject
 * 
 * @author Matthieu
 *
 */
class Bonus extends AnglePhysicObject
{
	protected float mRadius;
	protected enum TypeBonus {NONE,ADDSCORE, MOREJUMP, LESSJUMP, CHANGEPHYSICS, DISABLECHANGECOLOR, ALLPLATEFORME};

	private GameUI mGame;
	private AngleSound sndTouch;
	private boolean mustdraw = true;
	private AngleSprite mSprite;
	private int mType;
	
	static int radius = 8;
	static int nbtype = 6;
	static TypeBonus[] mapTypeBonus = {TypeBonus.NONE,TypeBonus.ADDSCORE, TypeBonus.MOREJUMP, TypeBonus.LESSJUMP, TypeBonus.CHANGEPHYSICS, TypeBonus.DISABLECHANGECOLOR, TypeBonus.ALLPLATEFORME};
	
	public Bonus(GameUI game)
	{
		super(0, 1);
		mGame = game;
		mType = (int) (Math.random() * (nbtype));

		sndTouch = mGame.sndBonusDefault;
		
		if(mType==0)
			mSprite=new AngleSprite(mGame.bonusTexture0);
		else if(mType==1)
			mSprite=new AngleSprite(mGame.bonusTexture1);
		else if(mType==2)
			mSprite=new AngleSprite(mGame.bonusTexture2);
		else if(mType==3)
			mSprite=new AngleSprite(mGame.bonusTexture3);
		else if(mType==4)
			mSprite=new AngleSprite(mGame.bonusTexture4);
		else if(mType==5)
			mSprite=new AngleSprite(mGame.bonusTexture5);
		addCircleCollider(new BallCollider(0, 0, radius));

	}
	
	
	/**
	 * @return surface
	 */
	@Override
	public float getSurface()
	{
		return (float) (mRadius * mRadius * 3.14);
	}

	/**
	 * Draw the sprite and/or colliders
	 * 
	 * @param gl
	 */
	@Override
	public void draw(GL10 gl)
	{
		if(mustdraw) {
			mSprite.mPosition.set(mPosition);
			mSprite.draw(gl);
			drawColliders(gl,1f,0f,0f);
		}
	}
	
	public BallCollider collider()
	{
		return (BallCollider) mCircleColliders[0];
	}

	protected void onDie()
	{
		Log.i("Bonus", "Bonus onDie debut");
		Log.i("Bonus", "TypeBonus : "+mapTypeBonus[mType]);
		mGame.setBonus(mapTypeBonus[mType]);
		sndTouch.play(1,false);
		Log.i("Bonus", "Bonus onDie fin");
	}
	
};
