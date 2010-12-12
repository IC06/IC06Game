package com.turlutu;

import javax.microedition.khronos.opengles.GL10;

import android.util.Log;

import com.android.angle.AnglePhysicObject;
import com.android.angle.AngleSound;
import com.android.angle.AngleSprite;

/**
 * extends AnglePhysicObject
 * 
 * @author Matthieu,Thomas
 *
 */
class Bonus extends AnglePhysicObject
{
	protected boolean mUsed;
	protected float mRadius;
	protected enum TypeBonus {	NONE, // 0
		ADDSCORE,// 1=> GameUI.setBonus(..)
		MOREJUMP, // 2 => Ball.jump()
		LESSJUMP, // 3 => Ball.jump()
		CHANGEPHYSICS, // 4 => MainActivity.onSensorChange() & GameUI.onTcouhEvent
		DISABLECHANGECOLOR, // 5 => Ball.changeColorLeft/Right()									
		ALLPLATEFORME}; // 6 => MyPhysicsEngine.kynetics()

	private GameUI mGame;
	private AngleSound sndTouch;
	private boolean mustdraw = true;
	private AngleSprite mSprite;
	private int mType = 0;
	
	static int radius = 16;
	static int nbtype = 15;
	static TypeBonus[] mapTypeBonus = {	TypeBonus.NONE, // 0
		TypeBonus.ADDSCORE, // 1
		TypeBonus.MOREJUMP, // 2
		TypeBonus.LESSJUMP, // 3
		TypeBonus.CHANGEPHYSICS, // 4
		TypeBonus.DISABLECHANGECOLOR, // 5
		TypeBonus.ALLPLATEFORME};// 6
	static float[] timesActionBonus = {	0, // 0
		0, // 1
		4, // 2
		4, // 3
		4, // 4
		4, // 5
		6}; // 6
	static int[] bonusOrder = { 0,
		1,
		3,
		2,
		5,
		5,
		1,
		4,
		6,
		4,
		3,
		6,
		5,
		6,
		2,
		6
	};
	
	public Bonus(GameUI game, int d) // d compris entre 0 et 100 (difficulté croissante)
	{
		super(0, 1);
		mGame = game;
		mUsed = false;
		int range = (int) ((float) d / 100.f * 2 * nbtype);
		if(range>nbtype) {
			range = nbtype;
		}
		mType = bonusOrder[(int) (Math.random() * range) + 1];
		Log.i("Strategie", "Bmax : "+ ( (float) d / 100.f * nbtype) + 1);
		sndTouch = mGame.sndBonus[mType];
		mSprite=new AngleSprite(mGame.mBonusLayout[mType]);
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
			//drawColliders(gl,1f,0f,0f);
		}
	}
	
	public BallCollider collider()
	{
		return (BallCollider) mCircleColliders[0];
	}

	protected void onDie()
	{
		if (mUsed)
		{
			Log.i("Bonus", "Bonus onDie debut");
			Log.i("Bonus", "TypeBonus : "+mapTypeBonus[mType]+" "+mType);
			mGame.setBonus(mapTypeBonus[mType],timesActionBonus[mType]);
			sndTouch.play(((MainActivity)mGame.mActivity).mOptions.mVolume / 100,false);
			Log.i("Bonus", "Bonus onDie fin");
		}
	}
	
};
