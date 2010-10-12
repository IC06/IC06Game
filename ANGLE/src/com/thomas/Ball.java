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
	private AngleSprite mSprite;
	private AngleSpriteLayout mTextureB, mTextureV, mTextureO;
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
	public Ball(AngleSpriteLayout textureB, AngleSpriteLayout textureV, AngleSpriteLayout textureO, float radius, float mass, float bounce)
	{
		super(0, 1);
		mColor = Color.ORANGE;
		mSprite=new AngleSprite(textureO);
		mTextureB=textureB;
		mTextureV=textureV;
		mTextureO=textureO;
		addCircleCollider(new AngleCircleCollider(0, 0, radius));
		mRadius = radius;
		mMass = mass;
		mBounce = bounce; // Coefficient of restitution (1 return all the energy)
	}

	public void setColor(Color newColor)
	{
		mColor = newColor;
		if (newColor == Color.ORANGE)
			mSprite.setLayout(mTextureO);
		else if(mColor == Color.VERT)
			mSprite.setLayout(mTextureV);
		else
			mSprite.setLayout(mTextureB);
	}
	
	public void switchColor()
	{
		if (mColor == Color.ORANGE)
			setColor(Color.VERT);
		else if(mColor == Color.VERT)
			setColor(Color.BLEU);
		else
			setColor(Color.ORANGE);
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
			mSprite.mPosition.set(mPosition);
			mSprite.draw(gl);
	}
	

	@Override
	public void delete()
	{
		super.delete();
		// TODO GAME OVER !!!
	}
	
	
};