package com.exitjump;

import java.util.HashMap;

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
	protected enum TypeBonus {	
		MOREJUMP, // 0 => Ball.jump()
		LESSJUMP, // 1 => Ball.jump()
		DISABLECHANGECOLOR, // 2 => Ball.changeColorLeft/Right()	
		ALLPLATEFORME, //3 => MyPhysicsEngine.kynetics()
		CHANGEPHYSICS, // 4 => MainActivity.onSensorChange() & GameUI.onTcouhEvent
		ADDSCORE,// 5=> GameUI.setBonus(..)
		BONUSX, //6  INACTIF
		BONUSINTERO, //7 INACTIF
		LIFE,// 8 => addlife
		NONE}; // 9 => none

	private GameUI mGame;
	private AngleSound sndTouch;
	private boolean mustdraw = true;
	private AngleSprite mSprite;
	private int mType = 0;
	
	static HashMap<TypeBonus,Integer> TypeBonusToInt;
	static {
		TypeBonusToInt = new HashMap<TypeBonus,Integer>();
		TypeBonusToInt.put(TypeBonus.MOREJUMP,			0);
		TypeBonusToInt.put(TypeBonus.LESSJUMP,			1);
		TypeBonusToInt.put(TypeBonus.DISABLECHANGECOLOR,2);
		TypeBonusToInt.put(TypeBonus.ALLPLATEFORME,		3);
		TypeBonusToInt.put(TypeBonus.CHANGEPHYSICS,		4);
		TypeBonusToInt.put(TypeBonus.ADDSCORE,			5);
		TypeBonusToInt.put(TypeBonus.BONUSX,			6);
		TypeBonusToInt.put(TypeBonus.BONUSINTERO,		7);
		TypeBonusToInt.put(TypeBonus.LIFE,				8);
		TypeBonusToInt.put(TypeBonus.NONE,				9);
	}
	
	static int radius = 16;
	static int nbtype = 49;
	static TypeBonus[] mapTypeBonus = {	
		TypeBonus.MOREJUMP, // 0
		TypeBonus.LESSJUMP, // 1
		TypeBonus.DISABLECHANGECOLOR, // 2
		TypeBonus.ALLPLATEFORME, // 3
		TypeBonus.CHANGEPHYSICS, // 4
		TypeBonus.ADDSCORE, // 5
		TypeBonus.BONUSX, // 6
		TypeBonus.BONUSINTERO, // 7
		TypeBonus.LIFE, //8
		TypeBonus.NONE};// 9
	static float[] timesActionBonus = {	4, // 0 MORE JUMP
		4, // LESSJUMP
		4, // DISABLE CHANGE COLOR
		6, // ALL PLATEFORME
		6, // CHANGE PHYSICS
		0, // ADDSCORE
		0, // INACTIF
		0, // INACTIF
		0, // ADDLIFE
		0}; // NONE 
	static int[] bonusOrder = { 
		0, 						// MOREJUMP
		/// LESS JUMP
			1,					// LESSJUMP
		0,	
			1,
		
		/// ADD SCORE
				5,				// ADDSCORE
		0,
				5,
			1,
				5,
		
		/// DISABLE CHANGE COLOR
					2, 			// DISABLE CHANGE COLOR
		0,
					2,
			1,
					2,
				5,
					2,
					
		/// ALL PALTEFORME
						3,		// ALL PLATEFORME
		0,
						3,
			1,
						3,
				5,
						3,
					2,
						3,
						
		/// CHANGE PHYSICS
							4,	//CHANGEPHYSICS
		0,
							4,
			1,
							4,
				5,
							4,
					2,
							4,
						3,
							4,
		
		//// LIFE
								8, //LIFE
		0,
								8,
			1,
								8,
				5,
								8,
					2,
								8,
						3,
								8,
							4,
								8
	};
	
	public Bonus(GameUI game, int d) // d compris entre 0 et 100 (difficulté croissante)
	{
		super(0, 1);
		mGame = game;
		mUsed = false;
		int range = (int) ((float) d / 100.f * nbtype);
		if(range>nbtype) {
			range = nbtype;
		}
		mType = bonusOrder[(int) (Math.random() * range)];
		Log.i("Strategie", "Bmax : "+ ( (float) d / 100.f * nbtype));
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
			sndTouch.play(((MainActivity)mGame.mActivity).mOptions.mVolume / 100,false);
			if(mapTypeBonus[mType] == TypeBonus.LIFE) {
				// Ajout d'une vie
				mGame.mLife.add();
			} else if (mapTypeBonus[mType] == TypeBonus.ADDSCORE) {
				// Ajout de score
				mGame.mScore += 600;
			} else {
				mGame.setBonus(mapTypeBonus[mType],timesActionBonus[mType]);
			}
			Log.i("Bonus", "Bonus onDie fin");
		}
	}
	
};
