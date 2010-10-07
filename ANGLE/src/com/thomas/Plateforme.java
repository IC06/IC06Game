package com.thomas;

import javax.microedition.khronos.opengles.GL10;

import com.android.angle.AnglePhysicObject;
import com.android.angle.AngleSegmentCollider;
import com.android.angle.AngleSprite;
import com.android.angle.AngleSpriteLayout;
import com.android.angle.AngleSurfaceView;

public class Plateforme extends AnglePhysicObject
{
	private AngleSprite mSprite;
	private float mWidth;

	/**
	 * 
	 * @param layout sprite
	 * @param width width of the plateform
	 * @param bounce Coefficient of restitution (1 return all the energy) 
	 */
	public Plateforme(AngleSpriteLayout layout, float width, float bounce)
	{
		super(1, 0); // Note : super (nb_segment, nb_circle);
		mSprite=new AngleSprite(layout);
		mWidth = width;
		float w = width/2;
		addSegmentCollider(new AngleSegmentCollider(-w, 0, w, 0)); // haut
		mMass = 0;
		mBounce = bounce;
	}
	
	public Plateforme(AngleSurfaceView view, float width, float bounce)
	{
		super(1, 0); // Note : super (nb_segment, nb_circle);
		mSprite = new AngleSprite(new AngleSpriteLayout(view, 128, 32, com.android.tutorial.R.drawable.box, 0, 0, 256, 64));
		mWidth = width;
		float w = width/2;
		addSegmentCollider(new AngleSegmentCollider(-w, 0, w, 0)); // haut
		mMass = 0;
		mBounce = bounce;
	}

	/**
	 * @return surface of box
	 */
	@Override
	public float getSurface()
	{
		return mWidth; 
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
	
}
