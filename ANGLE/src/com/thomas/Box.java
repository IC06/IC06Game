package com.thomas;

import javax.microedition.khronos.opengles.GL10;

import com.android.angle.AngleCircleCollider;
import com.android.angle.AnglePhysicObject;
import com.android.angle.AngleSegmentCollider;
import com.android.angle.AngleSprite;
import com.android.angle.AngleSpriteLayout;

/**
 * 
 * @author thomas
 *
 */
class Box extends AnglePhysicObject
{
	private AngleSprite mSprite;
	private float mWidth, mHeight;

	/**
	 * 
	 * @param layout sprite
	 * @param width width of the box
	 * @param height height of the box
	 * @param mass mass
	 * @param bounce Coefficient of restitution (1 return all the energy) 
	 */
	public Box(AngleSpriteLayout layout, float width, float height, float mass, float bounce)
	{
		super(4, 0); // Note : super (nb_segment, nb_circle);
		mSprite=new AngleSprite(layout);
		mWidth = width;
		mHeight = height;
		float w = width/2, h = height/2;
		// Note : la position des collider se fait par rapport à la position de l'objet : mPosition
		// qui represente le centre de l'objet, de plus il faut considerer les axes x,y dans le sens habituel mathématique
		//addSegmentCollider(new AngleSegmentCollider(0, 0, 50, 50));
		//addCircleCollider(new AngleCircleCollider(50,0,29));
		addSegmentCollider(new AngleSegmentCollider(-w, h, w, h)); // haut
		//addSegmentCollider(new AngleSegmentCollider(-w, -h, w, -h)); // bas
		//addSegmentCollider(new AngleSegmentCollider(-w, -h, -w, h)); // gauche
		//addSegmentCollider(new AngleSegmentCollider(w, -h, w, h)); // droite
		mMass = mass;
		mBounce = bounce;
	}

	/**
	 * @return surface of box
	 */
	@Override
	public float getSurface()
	{
		return mWidth * mHeight; 
	}

	/**
	 * Draw sprite and/or colliders
	 */
	@Override
	public void draw(GL10 gl)
	{
		mSprite.mPosition.set(mPosition);
		//mSprite.draw(gl);
		drawColliders(gl);
	}
	
	
};