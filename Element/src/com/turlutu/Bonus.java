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
			mSprite.mPosition.set(mPosition);
			mSprite.draw(gl);
			drawColliders(gl,1f,0f,0f);
	}
	
	public boolean collide(Plateforme other)
	{
		if (mCircleColliders[0].test(other.collider()))
		{
			gererCollision((PlateformeCollider)other.collider());
			return true;
		}
		return false;
	}
	
	private void gererCollision(PlateformeCollider segmentCollider)
	{
		/*float	Ax = segmentCollider.getmObject().mPosition.mX + segmentCollider.getmA().mX,
					Bx = segmentCollider.getmObject().mPosition.mX + segmentCollider.getmB().mX,
					Ay = segmentCollider.getmObject().mPosition.mY + segmentCollider.getmA().mY,
					X = mObject.mPosition.mX + mCenter.mX,
					Y = mObject.mPosition.mY + mCenter.mY + mRadius - 20;
		
		if ((Ax < Bx && Ax < X && X < Bx)
			or(Ax < Bx && Bx <X && X < Ax))*/
		mPosition.mY = segmentCollider.getmObject().mPosition.mY + segmentCollider.getmA().mY - mRadius - 1;
	}
	
};
