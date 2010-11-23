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
	private AngleSound sndJump;
	
	/**
	 * 
	 * @param layout Img to use
	 * @param radius Radius of the ball
	 * @param mass Mass of the ball
	 * @param bounce Coefficient of restitution(1 return all the energy)
	 */
	public Ball(AngleSpriteLayout textureB, AngleSpriteLayout textureV, AngleSpriteLayout textureR, float radius, float mass, float bounce, AngleSound soundJump, GameUI game)
	{
		super(0, 1);
		sndJump = soundJump;
		mAcceleration = new AngleVector(0f,0f);
		mColors = new Color[3];
		mColors[0] = Color.ROUGE;
		mColors[1] = Color.VERT;
		mColors[2] = Color.BLEU;
		mSprite=new AngleSprite(textureV);
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
			mGame.setSpriteLeft(Color.BLEU);
			mSprite.setLayout(mTextureR);
			mGame.setSpriteRight(Color.VERT);
		}
		else if(newColor == Color.VERT)
		{
			mGame.setSpriteLeft(Color.ROUGE);
			mSprite.setLayout(mTextureV);
			mGame.setSpriteRight(Color.BLEU);
		}
		else
		{
			mGame.setSpriteLeft(Color.VERT);
			mSprite.setLayout(mTextureB);
			mGame.setSpriteRight(Color.ROUGE);
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
			//mSprite.draw(gl);
			if(mColors[1] == Color.ROUGE)
				drawColliders(gl,1f,0f,0f);
			else if(mColors[1] ==  Color.BLEU)
				drawColliders(gl,0f,0f,1f);
			else if(mColors[1] == Color.VERT)
				drawColliders(gl,0f,1f,0f);
			else
				drawColliders(gl,1f,1f,1f);
	}
	
	public void jump()
	{
		mVelocity.mY = -600;
		sndJump.play(1,false);
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
