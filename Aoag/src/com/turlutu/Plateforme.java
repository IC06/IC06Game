package com.turlutu;


import javax.microedition.khronos.opengles.GL10;

import com.android.angle.AnglePhysicObject;
import com.android.angle.AngleSprite;
import com.turlutu.Ball.Color;

public class Plateforme extends AnglePhysicObject
{
	private AngleSprite mSprite;
	private float mWidth;
	protected Color mColor;
	
	static public float SIZE = 80;

	public Plateforme(AngleSprite plateformeblanche)
	{
		super(1,0);
		init(plateformeblanche,SIZE,0,1,Color.TOUTE); 
	}
	
	public Plateforme(AngleSprite plateforme, Color color)
	{
		super(1, 0); // Note : super (nb_segment, nb_circle);
		init(plateforme,SIZE,0,1,color); 
	}

	public void init(AngleSprite plateforme, float width, float mass, float bounce, Color color)
	{
		mSprite=plateforme;
		mWidth = width;
		float w = width/2;
		addSegmentCollider(new PlateformeCollider(-w,w, 0)); // haut
		mMass = mass;
		mBounce = bounce;
		mColor = color;
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
