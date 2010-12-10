package com.turlutu;

import javax.microedition.khronos.opengles.GL10;

import android.os.Vibrator;

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
	private AngleSprite mSprite;
	private AngleSpriteLayout mTexture[];
	protected enum Color {JAUNE, VERT, ROUGE, TOUTE};
	private Color mColors[];
	protected float mRadius;
	private GameUI mGame;
	private OptionsUI mOptions = null;
	protected AngleVector mAcceleration;
	private AngleSound sndJump;
	public int sens = 1;
	
	
	public Ball(AngleSpriteLayout texture[], float radius, float mass, float bounce, AngleSound soundJump, GameUI game, OptionsUI options)
	{
		super(0, 1);
		mOptions = options; // Pour utiliser lors de l'optionsUI
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
		mGame = game;
	}
	/**
	 * 
	 * @param layout Img to use
	 * @param radius Radius of the ball
	 * @param mass Mass of the ball
	 * @param bounce Coefficient of restitution(1 return all the energy)
	 */
	public Ball(AngleSpriteLayout texture[], float radius, float mass, float bounce, AngleSound soundJump, GameUI game)
	{
		super(0, 1);
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
		mGame = game;
	}

	/**
	 * si on change de couleur en passant à travers le bord gauche de l'écran
	 * @author thomas
	 */
	public void changeColorLeft()
	{
		if (mGame == null || (mGame.mTypeBonus != TypeBonus.DISABLECHANGECOLOR))
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
	 * @author thomas
	 */
	public void changeColorRight()
	{
		if (mGame == null || (mGame.mTypeBonus != TypeBonus.DISABLECHANGECOLOR))
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
		if (mGame != null)
		{
			if (newColor == Color.ROUGE)
			{
				mGame.setSpriteLeft(Color.JAUNE);
				mSprite.setLayout(mTexture[5]);
				mGame.setSpriteRight(Color.VERT);
			}
			else if(newColor == Color.VERT)
			{
				mGame.setSpriteLeft(Color.ROUGE);
				mSprite.setLayout(mTexture[1]);
				mGame.setSpriteRight(Color.JAUNE);
			}
			else
			{
				mGame.setSpriteLeft(Color.VERT);
				mSprite.setLayout(mTexture[3]);
				mGame.setSpriteRight(Color.ROUGE);
			}
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
		((MainActivity)mGame.mActivity).mVibrator.vibrate(300);
		
		if (mGame != null && mGame.mTypeBonus == TypeBonus.MOREJUMP)
			mVelocity.mY = -900;
		else if (mGame != null && mGame.mTypeBonus == TypeBonus.LESSJUMP)
			mVelocity.mY = -450;
		else
			mVelocity.mY = -600;
		if (sndJump != null)
			if(mGame != null)
				sndJump.play( ((float) ((MainActivity)mGame.mActivity).mOptions.mVolume) / 100.f ,false);
			else
				sndJump.play( ((float) mOptions.mVolume) / 100.f ,false);
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
