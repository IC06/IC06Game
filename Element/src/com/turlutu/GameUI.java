package com.turlutu;

import android.graphics.Typeface;
import android.view.MotionEvent;

import com.android.angle.AngleActivity;
import com.android.angle.AngleFont;
import com.android.angle.AngleObject;
import com.android.angle.AnglePhysicObject;
import com.android.angle.AngleSegmentCollider;
import com.android.angle.AngleSpriteLayout;
import com.android.angle.AngleString;
import com.android.angle.AngleUI;

public class GameUI extends AngleUI {

	private AngleSpriteLayout mBallLayoutB, mBallLayoutV, mBallLayoutO, mBoxLayout, mBackGroundLayout;
	private MyPhysicsEngine mPhysics;
	private float WIDTH,HEIGHT;
	protected Ball mBall;
	private AngleObject ogDashboard;
	private AngleString mString;
	protected int mScore;
	
	public GameUI(AngleActivity activity)
	{
		super(activity);
		WIDTH = 320f;
		HEIGHT = 480f;
		mScore = 0;
		//TODO : réduire taille de la balle
		int d = 64;
		mBallLayoutB = new AngleSpriteLayout(activity.mGLSurfaceView, d, d, com.turlutu.R.drawable.ballb, 0, 0, 128, 128);
		mBallLayoutV = new AngleSpriteLayout(activity.mGLSurfaceView, d, d, com.turlutu.R.drawable.ballv, 0, 0, 128, 128);
		mBallLayoutO = new AngleSpriteLayout(activity.mGLSurfaceView, d, d, com.turlutu.R.drawable.ball, 0, 0, 128, 128);
		mBackGroundLayout =new AngleSpriteLayout(activity.mGLSurfaceView,com.turlutu.R.drawable.fond);
		
		// on ajoute le background en premier à MyDemo pour qu'il soit dessiné en premier
		Background mBackGround = new Background(mBackGroundLayout);
		addObject(mBackGround);
		
		// on ajoute la balle au moteur en premier pour qu'il la dessine en dernier, voir la fonction draw surchargé de MyPhysicEngine
		mPhysics=new MyPhysicsEngine(20,WIDTH,HEIGHT,activity.mGLSurfaceView,this);
		mPhysics.mViscosity = 0f; // Air viscosity
		addObject(mPhysics);

		// le score
		ogDashboard=addObject(new AngleObject());
		AngleFont fntCafe25 = new AngleFont(mActivity.mGLSurfaceView, 25, Typeface.createFromAsset(activity.getAssets(),"bazaronite.ttf"), 222, 0, 0, 30, 200, 255, 255);
		AngleFont fntBazaronite=new AngleFont(mActivity.mGLSurfaceView, 18, Typeface.createFromAsset(mActivity.getAssets(),"bazaronite.ttf"), 222, 0, 2, 255, 100, 255, 255);
		mString = (AngleString)ogDashboard.addObject(new AngleString(fntBazaronite,"0",50,20,AngleString.aCenter));
		
		mBall = new Ball (mBallLayoutB,mBallLayoutV,mBallLayoutO,32,20,1,this);
		mBall.mPosition.set(50,300);
		mPhysics.addObject(mBall);

		
		// ajoute une plateforme en bas qui prend toute la place pour le debut
		AnglePhysicObject mWall = new AnglePhysicObject(1, 0);
		mWall.mPosition.set(0, HEIGHT-15);
		mWall.addSegmentCollider(new AngleSegmentCollider(0, 0, WIDTH, 0));
		mWall.mBounce = 1f;
		mPhysics.addObject(mWall); // Down wall
		
		

		// add barre
		Plateforme mPlateforme = new Plateforme(mBoxLayout,100,1);
		mPlateforme.mPosition.set(140,420);
		mPhysics.addObject(mPlateforme);
		
		mPlateforme = new Plateforme(mBoxLayout,100,1);
		mPlateforme.mPosition.set(50,350);
		mPhysics.addObject(mPlateforme);
		
		mPlateforme = new Plateforme(mBoxLayout,100,1);
		mPlateforme.mPosition.set(160,300);
		mPhysics.addObject(mPlateforme);
		
		mPlateforme = new Plateforme(mBoxLayout,100,1);
		mPlateforme.mPosition.set(200,250);
		mPhysics.addObject(mPlateforme);
		
		mPlateforme = new Plateforme(mBoxLayout,100,1);
		mPlateforme.mPosition.set(130,200);
		mPhysics.addObject(mPlateforme);
		
		mPlateforme = new Plateforme(mBoxLayout,100,1);
		mPlateforme.mPosition.set(300,100);
		mPhysics.addObject(mPlateforme);
		
		
		
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		mBall.mVelocity.mX = (event.getX()-WIDTH/2);
		
		return true;
	}
	
	public void setGravity(float x, float y)
	{
		mPhysics.mGravity.set(x,y);
	}
	
	public void upScore(int value)
	{
		mScore += value;
		mString.set(""+mScore);
	}
	
	public void backToMenu()
	{
		((MainActivity) mActivity).setUI(((MainActivity) mActivity).mMenu);
	}
	
}