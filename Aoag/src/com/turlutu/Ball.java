package com.turlutu;

import javax.microedition.khronos.opengles.GL10;

import com.android.angle.AnglePhysicObject;
import com.android.angle.AngleSound;
import com.android.angle.AngleSprite;
import com.android.angle.AngleSpriteLayout;
import com.android.angle.AngleVector;
import com.turlutu.Bonus.TypeBonus;

/**
 * extends AnglePhysicObject
 * 
 * @author thomas
 *
 */
class Ball extends AnglePhysicObject
{
	protected enum Color {JAUNE, VERT, ROUGE, TOUTE};
	protected float mRadius;
	protected AngleVector mAcceleration;
	public int sens = 1;
	
	private MainActivity mActivity;
	private AngleSound sndJump;
	private Color mColors[];
	private AngleSprite mSprite;
	private AngleSpriteLayout mTexture[];
	
	
	public Ball(MainActivity activity, AngleSpriteLayout texture[], float radius, float mass, float bounce, AngleSound soundJump)
	{
		super(0, 1);
		mActivity = activity;
		sndJump = soundJump;
		mAcceleration = new AngleVector(0f,0f);
		mColors = new Color[3];
		mColors[0] = Color.ROUGE;
		mColors[1] = Color.VERT;
		mColors[2] = Color.JAUNE;
		mSprite=new AngleSprite(texture[0]);
		mTexture=texture;
		addCircleCollider(new BallCollider(0, 0, radius));
		mRadius = radius;
		mMass = mass;
		mBounce = bounce; // Coefficient of restitution (1 return all the energy)
		mVelocity.mY = -5;
	}

	/**
	 * si on change de couleur en passant à travers le bord gauche de l'écran
	 * @author matthieu, thomas
	 */
	public void changeColorLeft()
	{
		if (mActivity.mGame.mTypeBonus != TypeBonus.DISABLECHANGECOLOR)
		{
			Color temp = mColors[0];
			mColors[0] = mColors[2];
			mColors[2] = mColors[1];
			mColors[1] = temp;
			setColor(mColors[1]);
		}
	}
	
	/**
	 * si on change de couleur en passant à travers le bord droit de l'écran
	 * @author matthieu, thomas
	 */
	public void changeColorRight()
	{
		if (mActivity.mGame.mTypeBonus != TypeBonus.DISABLECHANGECOLOR)
		{
			Color temp = mColors[0];
			mColors[0] = mColors[1];
			mColors[1] = mColors[2];
			mColors[2] = temp;
			setColor(mColors[1]);
		}
	}
	
	private void setColor(Color newColor)
	{
		if (newColor == Color.ROUGE)
		{
			mActivity.mGame.setSpriteLeft(Color.JAUNE);
			mSprite.setLayout(mTexture[5]);
			mActivity.mGame.setSpriteRight(Color.VERT);
		}
		else if(newColor == Color.VERT)
		{
			mActivity.mGame.setSpriteLeft(Color.ROUGE);
			mSprite.setLayout(mTexture[1]);
			mActivity.mGame.setSpriteRight(Color.JAUNE);
		}
		else
		{
			mActivity.mGame.setSpriteLeft(Color.VERT);
			mSprite.setLayout(mTexture[3]);
			mActivity.mGame.setSpriteRight(Color.ROUGE);
		}
	}
	
	public Color getColor()
	{
		return mColors[1];
	}
	
	public int getIntTexture()
	{
		if ( mColors[1] == Color.VERT ) {
			return 0 + sens;
		} else if ( mColors[1] == Color.JAUNE ){
			return 2 + sens;
		} else {
			return 4 + sens;
		}
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
			/*if(mColors[1] == Color.ROUGE)
				drawColliders(gl,1f,0f,0f);
			else if(mColors[1] ==  Color.BLEU)
				drawColliders(gl,0f,0f,1f);
			else if(mColors[1] == Color.VERT)
				drawColliders(gl,0f,1f,0f);
			else
				drawColliders(gl,1f,1f,1f);*/
	}
	
	public void jump()
	{
		mActivity.mVibrator.vibrate(50);
		if (mActivity.mGame.mTypeBonus == TypeBonus.MOREJUMP)
			mVelocity.mY = -900;
		else if (mActivity.mGame.mTypeBonus == TypeBonus.LESSJUMP)
			mVelocity.mY = -450;
		else
			mVelocity.mY = -600;
		sndJump.play( ((float) mActivity.mOptions.mVolume) / 100f ,false);
	}
	
	public void changeSens()
	{
		if(mVelocity.mX<0) {
			sens=0;
		} else if(mVelocity.mX>0) {
			sens=1;
		}
		mSprite.setLayout(mTexture[getIntTexture()]);
	}
	
	public boolean collide(Bonus other)
	{
		if (mCircleColliders[0].test(other.collider()))
		{
			return true;
		} else {
			return false;
		}
		
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
		mPosition.mY = segmentCollider.getmObject().mPosition.mY + segmentCollider.getmA().mY - mRadius - 1;
	}
	
};
