package com.turlutu;


import javax.microedition.khronos.opengles.GL10;

import com.android.angle.AnglePhysicObject;
import com.android.angle.AngleSegmentCollider;

public class LifePlateforme extends AnglePhysicObject
{
	private final float WIDTH = 320f,HEIGHT = 480f;
	private int mLife;
	private GameUI mGame = null;

	public LifePlateforme(GameUI Game)
	{
		super(1,0);
		mPosition.set(0, HEIGHT-8);
		addSegmentCollider(new AngleSegmentCollider(0, 0, WIDTH, 0));
		mBounce = 1f;
		mGame = Game;
	}

	
	public void setLife(int life) {
		mLife = life;
	}
	
	public int get() {
		return mLife;
	}
	
	public void less() {
		if(mLife>0) {
			mLife -=1;
			updateGame();
		}
	}
	
	public void add() {
		if(mLife>=0) {
			mLife += 1;
			updateGame();
		}
	}
	
	private void updateGame() {
		if(mGame != null) {
			mGame.setLife();
		}
	}
	

	public boolean alive() {
		if(mLife == 0) {
			return false;
		} else {
			return true;
		}
	}
	
}
