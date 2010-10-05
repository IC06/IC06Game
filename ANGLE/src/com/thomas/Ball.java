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

	/**
	 * 
	 * @param layout Img to use
	 * @param radius Radius of the ball
	 * @param mass Mass of the ball
	 * @param bounce Coefficient of restitution(1 return all the energy)
	 */
	public Ball(AngleSpriteLayout layout, float radius, float mass, float bounce)
	{
		super(0, 1);
		mSprite=new AngleSprite(layout);
		addCircleCollider(new AngleCircleCollider(0, 0, radius));
		mMass = mass;
		mBounce = bounce; // Coefficient of restitution (1 return all the energy)  >Coeficiente de restituci�n (1 devuelve toda la energia)
	}

	/**
	 * I think this function does nothing important
	 * @return surface
	 */
	@Override
	public float getSurface()
	{
		return 29 * 2; // Radius * 2  >Radio * 2
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
		//Draw colliders (beware calls GC)
		//drawColliders(gl);
	}
	
	
};