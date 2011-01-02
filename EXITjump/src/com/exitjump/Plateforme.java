package com.exitjump;


import javax.microedition.khronos.opengles.GL10;

import com.android.angle.AnglePhysicObject;
import com.android.angle.AngleSprite;
import com.exitjump.Ball.Color;

public class Plateforme extends AnglePhysicObject
{
	enum PlateformeType { REBOND, TRAVERSE };
	static public final float SIZE = 60;
	
	protected Color mColor;
	protected PlateformeType mType;
	
	private AngleSprite mSprite;
	private float mWidth;
	

	public Plateforme(AngleSprite plateformeblanche)
	{
		super(1,0);
		init(plateformeblanche,SIZE,0,1,Color.TOUTE, PlateformeType.REBOND, 0); 
	}
	
	public Plateforme(AngleSprite plateforme, Color color, PlateformeType type, int velocity)
	{
		super(1, 0); // Note : super (nb_segment, nb_circle);
		init(plateforme,SIZE,0,1,color, type, velocity);
	}

	public void init(AngleSprite plateforme, float width, float mass, float bounce, Color color, PlateformeType type, int velocity)
	{
		mSprite=plateforme;
		mWidth = width;
		float w = width/2;
		addSegmentCollider(new PlateformeCollider(-w,w, 0)); // haut
		mMass = mass;
		mBounce = bounce;
		mColor = color;
		mType = type;
		mVelocity.mX = velocity;
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
		mSprite.draw(gl);
		/*if(mColor == Color.ROUGE)
			drawColliders(gl,1f,0f,0f);
		else if(mColor ==  Color.JAUNE)
			drawColliders(gl,0f,0f,1f);
		else if(mColor == Color.VERT)
			drawColliders(gl,0f,1f,0f);
		else
			drawColliders(gl,1f,1f,1f);*/
		
	}

	public PlateformeCollider collider()
	{
		return (PlateformeCollider) mSegmentColliders[0];
	}
	
}
