package com.thomas;

import javax.microedition.khronos.opengles.GL10;

import com.android.angle.AngleCircleCollider;
import com.android.angle.AnglePhysicObject;
import com.android.angle.AngleSprite;
import com.android.angle.AngleSpriteLayout;

/**
 * extends AnglePhysicObject
 * 
 * @author thomas
 *
 */
class Ball extends AnglePhysicObject
{
	private AngleSprite mSpriteB, mSpriteV, mSpriteO;
	protected enum Color {BLEU, VERT, ORANGE};
	protected Color mColor;
	protected float mRadius;

	/**
	 * 
	 * @param layout Img to use
	 * @param radius Radius of the ball
	 * @param mass Mass of the ball
	 * @param bounce Coefficient of restitution(1 return all the energy)
	 */
	public Ball(AngleSpriteLayout layoutV, AngleSpriteLayout layoutB, AngleSpriteLayout layoutO, float radius, float mass, float bounce)
	{
		super(0, 1);
		mColor = Color.BLEU;
		mSpriteB=new AngleSprite(layoutB);
		mSpriteV=new AngleSprite(layoutV);
		mSpriteO=new AngleSprite(layoutO);
		addCircleCollider(new AngleCircleCollider(0, 0, radius));
		mRadius = radius;
		mMass = mass;
		mBounce = bounce; // Coefficient of restitution (1 return all the energy)
	}

	public void setColor(Color newColor)
	{
		mColor = newColor;
	}
	
	public void switchColor()
	{
		if (mColor == Color.ORANGE)
			mColor = Color.VERT;
		else if(mColor == Color.VERT)
			mColor = Color.BLEU;
		else
			mColor = Color.ORANGE;
	}
	
	/**
	 * I think this function does nothing important
	 * @return surface
	 */
	@Override
	public float getSurface()
	{
		return 29 * 2; // Radius * 2
	}

	/**
	 * Draw the sprite and/or colliders
	 * 
	 * @param gl
	 */
	@Override
	public void draw(GL10 gl)
	{
		if (mColor == Color.BLEU) {
			mSpriteB.mPosition.set(mPosition);
			mSpriteB.draw(gl);
		} else if (mColor == Color.VERT) {
			mSpriteV.mPosition.set(mPosition);
			mSpriteV.draw(gl);
		} else if (mColor == Color.ORANGE) {
			mSpriteO.mPosition.set(mPosition);
			mSpriteO.draw(gl);
		}
	}
	
	
};