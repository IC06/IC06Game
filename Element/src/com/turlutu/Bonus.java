package com.turlutu;

import javax.microedition.khronos.opengles.GL10;

import com.android.angle.AnglePhysicObject;
import com.android.angle.AngleSound;
import com.android.angle.AngleSprite;
import com.android.angle.AngleSpriteLayout;
import com.android.angle.AngleVector;

/**
 * extends AnglePhysicObject
 * 
 * @author Matthieu
 *
 */
class Bonus extends AnglePhysicObject
{
	private AngleSprite mSprite;
	//protected enum Type {ADDSCORE, MOREJUMP, LESSJUMP, CHANGEPHYSICS, DISABLECHANGECOLOR, ALLPLATEFORME};
	//private Type mType[];
	private int mType;
	protected float mRadius;
	private GameUI mGame;
	private AngleSound sndTouch;
	private boolean mustdraw = true;
	
	static int radius = 8;
	static int nbtype = 6;
	
	public Bonus(GameUI game)
	{
		super(0, 1);
		mGame = game;
		mType = (int) (Math.random() * (nbtype));
		// Sound of type
		//if(mGame.sndBonus.length >= mType) {
		//	sndTouch = mGame.sndBonus[mType];
		//} else {
			sndTouch = mGame.sndBonusDefault;
		//}
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
	
	/*
	 * Le bonus est touché
	 */
	public void touch()
	{
		if(mustdraw) { // Sinon on a deja touché ce bonus
			mustdraw = false; // On ne dessine plus le bonus
			sndTouch.play(1,false); // On joue la music
		}
	}
	
	/*
	 * On arrete d'effectuer l'action du bonus
	 */
	public void end()
	{
		
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
	
};
