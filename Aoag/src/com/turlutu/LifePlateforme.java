package com.turlutu;


import javax.microedition.khronos.opengles.GL10;

import com.android.angle.AnglePhysicObject;
import com.android.angle.AngleSegmentCollider;

public class LifePlateforme extends AnglePhysicObject
{
	private final float WIDTH = 320f,HEIGHT = 480f;
	private int mLife;

	public LifePlateforme()
	{
		super(1,0);
		mPosition.set(0, HEIGHT-8);
		addSegmentCollider(new AngleSegmentCollider(0, 0, WIDTH, 0));
		mBounce = 1f;
	}

	
	public void setLife(int life) {
		mLife = life;
	}
	
	public void less() {
		if(mLife>0)
			mLife -=1;
	}
	
	public boolean alive() {
		if(mLife == 0) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Draw sprite and/or colliders
	 */
	@Override
	public void draw(GL10 gl)
	{
		// To debug a supprimer TODO
		if(alive()) {
			drawColliders(gl,1f,1f,1f);
		}
	}
	
}
