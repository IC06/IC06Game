package com.turlutu;

import javax.microedition.khronos.opengles.GL10;

import com.android.angle.AnglePhysicObject;
import com.android.angle.AngleSprite;
import com.android.angle.AngleSpriteLayout;
import com.android.angle.AngleVector;

/**
 * extends AnglePhysicObject
 * 
 * @author thomas
 *
 */
class Ball extends AnglePhysicObject
{
	private AngleSprite mSprite;
	private AngleSpriteLayout mTextureB, mTextureV, mTextureR;
	protected enum Color {BLEU, VERT, ROUGE, TOUTE};
	private Color mColors[];
	protected float mRadius;
	private GameUI mGame;
	protected AngleVector mAcceleration;
	
	/**
	 * 
	 * @param layout Img to use
	 * @param radius Radius of the ball
	 * @param mass Mass of the ball
	 * @param bounce Coefficient of restitution(1 return all the energy)
	 */
	public Ball(AngleSpriteLayout textureB, AngleSpriteLayout textureV, AngleSpriteLayout textureR, float radius, float mass, float bounce, GameUI game)
	{
		super(0, 1);
		mAcceleration = new AngleVector(0f,0f);
		mColors = new Color[3];
		mColors[0] = Color.BLEU;
		mColors[1] = Color.ROUGE;
		mColors[2] = Color.VERT;
		mSprite=new AngleSprite(textureR);
		mTextureB=textureB;
		mTextureV=textureV;
		mTextureR=textureR;
		addCircleCollider(new BallCollider(0, 0, radius));
		mRadius = radius;
		mMass = mass;
		mBounce = bounce; // Coefficient of restitution (1 return all the energy)
		mVelocity.mY = -5;
		mGame = game;
	}

	/**
	 * si on change de couleur en passant à travers le bord gauche de l'écran
	 * @author thomas
	 */
	public void changeColorLeft()
	{
		Color temp = mColors[0];
		mColors[0] = mColors[2];
		mColors[2] = mColors[1];
		mColors[1] = temp;
		setColor(mColors[1]);
	}
	/**
	 * si on change de couleur en passant à travers le bord droit de l'écran
	 * @author thomas
	 */
	public void changeColorRight()
	{
		Color temp = mColors[0];
		mColors[0] = mColors[1];
		mColors[1] = mColors[2];
		mColors[2] = temp;
		setColor(mColors[1]);
	}
	
	private void setColor(Color newColor)
	{
		if (newColor == Color.ROUGE)
		{
			mGame.mSpriteLeft.setLayout(mTextureB);
			mSprite.setLayout(mTextureR);
			mGame.mSpriteRight.setLayout(mTextureV);
		}
		else if(newColor == Color.VERT)
		{
			mGame.mSpriteLeft.setLayout(mTextureR);
			mSprite.setLayout(mTextureV);
			mGame.mSpriteRight.setLayout(mTextureB);
		}
		else
		{
			mGame.mSpriteLeft.setLayout(mTextureV);
			mSprite.setLayout(mTextureB);
			mGame.mSpriteRight.setLayout(mTextureR);
		}
	}
	
	public Color getColor()
	{
		return mColors[1];
	}
	
	/**
	 * I think this function does nothing important
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
			//drawColliders(gl);
	}
	

	
};