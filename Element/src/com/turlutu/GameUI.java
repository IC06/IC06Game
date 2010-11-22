package com.turlutu;

import android.graphics.Typeface;
import android.util.Log;
import android.view.MotionEvent;

import com.android.angle.AngleActivity;
import com.android.angle.AngleFont;
import com.android.angle.AngleObject;
import com.android.angle.AnglePhysicObject;
import com.android.angle.AngleSound;
import com.android.angle.AngleSprite;
import com.android.angle.AngleSegmentCollider;
import com.android.angle.AngleSpriteLayout;
import com.android.angle.AngleString;
import com.android.angle.AngleUI;
import com.android.angle.AngleVector;
import com.turlutu.Ball.Color;

public class GameUI extends AngleUI {

	protected AngleSpriteLayout mBallLayoutB, mBallLayoutV, mBallLayoutR, mPlateformeLayout, mBackGroundLayout;
	private MyPhysicsEngine mPhysics;
	private float WIDTH,HEIGHT;
	protected Ball mBall;
	private AngleObject ogDashboard;
	private AngleString mString;
	protected int mScore,lastupdate;
	protected AngleSprite mSpriteLeft, mSpriteRight;
	private AngleSpriteLayout mBordsLayout[];
	private AngleSound sndJump;
	
	public GameUI(AngleActivity activity)
	{
		super(activity);
	}
	
	public void create(AngleActivity activity)
	{
		Log.i("GameUI", "GameUI constructor debut");
		WIDTH = 320f;
		HEIGHT = 480f;
		mScore = 0;
		sndJump=new AngleSound(mActivity,R.raw.jump);
		mPlateformeLayout = new AngleSpriteLayout(activity.mGLSurfaceView, 32, 32, com.turlutu.R.drawable.ball, 0, 0, 128, 128);
		int d = 64;
		mBallLayoutB = new AngleSpriteLayout(activity.mGLSurfaceView, d, d, com.turlutu.R.drawable.ballb, 0, 0, 128, 128);
		mBallLayoutV = new AngleSpriteLayout(activity.mGLSurfaceView, d, d, com.turlutu.R.drawable.ballv, 0, 0, 128, 128);
		mBallLayoutR = new AngleSpriteLayout(activity.mGLSurfaceView, d, d, com.turlutu.R.drawable.ball, 0, 0, 128, 128);
		mBordsLayout = new AngleSpriteLayout[3];
		// TODO l'image bleu n'est pas à la bonne taille dans les fichiers
		mBordsLayout[0] = new AngleSpriteLayout(activity.mGLSurfaceView, 64, 256, com.turlutu.R.drawable.bord_bleu);
		mBordsLayout[1] = new AngleSpriteLayout(activity.mGLSurfaceView, 64, 256, com.turlutu.R.drawable.bord_rouge);
		mBordsLayout[2] = new AngleSpriteLayout(activity.mGLSurfaceView, 64, 256, com.turlutu.R.drawable.bord_vert);
		// TODO voir quelle image convient le mieu au background
		/*mBackGroundLayout =new AngleSpriteLayout(activity.mGLSurfaceView,320,480,com.turlutu.R.drawable.fond,0,0,320,480);
		
		// on ajoute le background en premier à MyDemo pour qu'il soit dessiné en premier
		Background mBackGround = new Background(mBackGroundLayout);
		addObject(mBackGround);*/
		
		mSpriteLeft = new AngleSprite(0,240,mBordsLayout[0]);
		mSpriteRight = new AngleSprite(320,240,mBordsLayout[2]);
		addObject(mSpriteLeft);
		addObject(mSpriteRight);
		
		
		
		// on ajoute la balle au moteur en premier pour qu'il la dessine en dernier, voir la fonction draw surchargé de MyPhysicEngine
		mPhysics=new MyPhysicsEngine(20,WIDTH,HEIGHT,activity.mGLSurfaceView, this);
		mPhysics.mViscosity = 1f; // Air viscosity
		mPhysics.mGravity = new AngleVector(0,10f);
		addObject(mPhysics);

		// le score
		ogDashboard=addObject(new AngleObject());
		AngleFont fntCafe25 = new AngleFont(mActivity.mGLSurfaceView, 25, Typeface.createFromAsset(activity.getAssets(),"cafe.ttf"), 222, 0, 0, 30, 200, 255, 255);
		//AngleFont fntBazaronite=new AngleFont(mActivity.mGLSurfaceView, 18, Typeface.createFromAsset(mActivity.getAssets(),"bazaronite.ttf"), 222, 0, 2, 255, 100, 255, 255);
		mString = (AngleString)ogDashboard.addObject(new AngleString(fntCafe25,"0",50,20,AngleString.aCenter));
		

		mBall = new Ball (mBallLayoutB,mBallLayoutV,mBallLayoutR,32,80,1,sndJump,this);
		mPhysics.addObject(mBall);

		//init();
		Log.i("GameUI", "GameUI constructor fin");
	}
	
	@Override
	public void onActivate()
	{
		Log.i("GameUI", "GameUI onActivate debut");
		mScore=0;
		mString.set("0");
		init();
		super.onActivate();
		Log.i("GameUI", "GameUI onActivate fin");
	}

	@Override
	public void onDeactivate()
	{
		Log.i("GameUI", "GameUI onDeactivate debut");
		for (int i=1; i<20; i++)
		{
			if (mPhysics.childAt(i) instanceof AnglePhysicObject)
			{
				mPhysics.removeObject(i);
			}
		}
		super.onDeactivate();
		Log.i("GameUI", "GameUI onDeactivate fin");
	}
	/*
	@Override
	public void onPause()
	{
      mSensorManager.unregisterListener(mListener); 
      super.onPause();
	}


	@Override
	public void onResume()
	{
      mSensorManager.registerListener(mListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_FASTEST); 		
		super.onResume();
	}
	*/
	private void init()
	{

		mBall.mPosition.set(50,300);
		mBall.mVelocity.mY = -600;
		
		// ajoute une plateforme en bas qui prend toute la place pour le debut
		AnglePhysicObject mWall = new AnglePhysicObject(1, 0);
		mWall.mPosition.set(0, HEIGHT-15);
		mWall.addSegmentCollider(new AngleSegmentCollider(0, 0, WIDTH, 0));
		mWall.mBounce = 1f;
		mPhysics.addObject(mWall); // Down wall
		
		

		// add barre
		Plateforme mPlateforme = new Plateforme(mPlateformeLayout,100,1);
		mPlateforme.mPosition.set(140,420);
		mPhysics.addObject(mPlateforme);
		
		mPlateforme = new Plateforme(mPlateformeLayout,100,1);
		mPlateforme.mPosition.set(50,350);
		mPhysics.addObject(mPlateforme);
		
		mPlateforme = new Plateforme(mPlateformeLayout,100,1);
		mPlateforme.mPosition.set(160,300);
		mPhysics.addObject(mPlateforme);
		
		mPlateforme = new Plateforme(mPlateformeLayout,100,1);
		mPlateforme.mPosition.set(200,250);
		mPhysics.addObject(mPlateforme);
		
		mPlateforme = new Plateforme(mPlateformeLayout,100,1);
		mPlateforme.mPosition.set(130,200);
		mPhysics.addObject(mPlateforme);
		
		mPlateforme = new Plateforme(mPlateformeLayout,100,1);
		mPlateforme.mPosition.set(300,100);
		mPhysics.addObject(mPlateforme);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		mBall.mVelocity.mX = (event.getX()-WIDTH/2)*2;
		
		return true;
	}
	
	public void setGravity(float x, float y)
	{
		mPhysics.mGravity.set(x,y);
	}
	
	public void upScore(int value)
	{
		mScore += value;
		lastupdate++;
		if(lastupdate>7)
		{
			mString.set(String.valueOf(mScore));
			lastupdate=0;
		}
	}
	
	public void backToMenu()
	{
		((MainActivity) mActivity).setUI(((MainActivity) mActivity).mMenu);
	}
	
	@Override
	public void step(float secondsElapsed)
	{
		if (secondsElapsed > 0.08)
			secondsElapsed = (float) 0.04;
		
		super.step(secondsElapsed);
	}
	
	public void setSpriteRight(Color newColor)
	{
		if (newColor == Color.BLEU)
			mSpriteRight.setLayout(mBordsLayout[0]);
		else if (newColor == Color.ROUGE)
			mSpriteRight.setLayout(mBordsLayout[1]);
		else
			mSpriteRight.setLayout(mBordsLayout[2]);
	}
	
	public void setSpriteLeft(Color newColor)
	{
		if (newColor == Color.BLEU)
			mSpriteLeft.setLayout(mBordsLayout[0]);
		else if (newColor == Color.ROUGE)
			mSpriteLeft.setLayout(mBordsLayout[1]);
		else
			mSpriteLeft.setLayout(mBordsLayout[2]);
	}
	
}