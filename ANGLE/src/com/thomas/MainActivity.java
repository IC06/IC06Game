package com.thomas;



import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import com.android.angle.AngleActivity;
import com.android.angle.AnglePhysicObject;
import com.android.angle.AngleSegmentCollider;
import com.android.angle.AngleSpriteLayout;
import com.android.angle.AngleUI;

// Pour voir si le jeu est bien fluide (à commenté dans la version finale) 
import com.android.angle.FPSCounter;

/**
 * Use some pseudo-physic
 * 
 * 
 * 
 * @author Ivan Pajuelo
 * 
 */
public class MainActivity extends AngleActivity
{
	private MyDemo mDemo;
	private Ball mBall;
	public float WIDTH, HEIGHT;
	
   private final SensorEventListener mListener = new SensorEventListener() 
   {
		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy)
		{
		}

		@Override
		public void onSensorChanged(SensorEvent event)
		{
			if (event.sensor.getType()==Sensor.TYPE_ACCELEROMETER)
			{
				mBall.mVelocity.mX = (-100*event.values[0]);
				//mDemo.setGravity(-10*event.values[0],10*event.values[1]);
				//mDemo.setGravity(-4*event.values[0],10);
			}
		}
   };
	private SensorManager mSensorManager; 	

	   

	
	
	private class MyDemo extends AngleUI
	{
		AngleSpriteLayout mBallLayoutB, mBallLayoutV, mBallLayoutO, mBoxLayout, mBackGroundLayout;
		MyPhysicsEngine mPhysics;
		
		public MyDemo(AngleActivity activity)
		{
			super(activity);
			WIDTH = 320f;
			HEIGHT = 480f;
			//TODO : réduire taille de la balle
			int d = 64;
			mBallLayoutB = new AngleSpriteLayout(mGLSurfaceView, d, d, com.android.tutorial.R.drawable.ballb, 0, 0, 128, 128);
			mBallLayoutV = new AngleSpriteLayout(mGLSurfaceView, d, d, com.android.tutorial.R.drawable.ballv, 0, 0, 128, 128);
			mBallLayoutO = new AngleSpriteLayout(mGLSurfaceView, d, d, com.android.tutorial.R.drawable.ball, 0, 0, 128, 128);
			mBackGroundLayout =new AngleSpriteLayout(mGLSurfaceView,320,480,com.android.tutorial.R.drawable.fondo,0,0,320,480);
			mBoxLayout = new AngleSpriteLayout(mGLSurfaceView, 128, 32, com.android.tutorial.R.drawable.box, 0, 0, 256, 64);
			
			// on ajoute le background en premier à MyDemo pour qu'il soit dessiné en premier
			Background mBackGround = new Background(mBackGroundLayout);
			addObject(mBackGround);
			
			// on ajoute la balle au moteur en premier pour qu'il la dessine en dernier, voir la fonction draw surchargé de MyPhysicEngine
			mPhysics=new MyPhysicsEngine(20,WIDTH,HEIGHT,mGLSurfaceView);
			mPhysics.mViscosity = 0f; // Air viscosity
			addObject(mPhysics);

			mBall = new Ball (mBallLayoutB,mBallLayoutV,mBallLayoutO,32,20,1);
			mBall.mPosition.set(50,300);
			mPhysics.addObject(mBall);

			// Add 4 segment colliders to simulate walls
			AnglePhysicObject mWall = new AnglePhysicObject(1, 0);
			mWall.mPosition.set(0, HEIGHT-15);
			mWall.addSegmentCollider(new AngleSegmentCollider(0, 0, WIDTH, 0));
			mWall.mBounce = 1f;
			mPhysics.addObject(mWall); // Down wall
			
			/*mWall = new AnglePhysicObject(1, 0);
			mWall.mPosition.set(0, 0);
			mWall.addSegmentCollider(new AngleSegmentCollider(0, 0, WIDTH, 0));
			mWall.mBounce = 1f;
			mPhysics.addObject(mWall); // Up wall
			
			mWall = new AnglePhysicObject(1, 0);
			mWall.mPosition.set(WIDTH, 0);
			mWall.addSegmentCollider(new AngleSegmentCollider(0, 0, 0, HEIGHT));
			mWall.mBounce = 1f;
			mPhysics.addObject(mWall); // Right wall
			
			mWall = new AnglePhysicObject(1, 0);
			mWall.mPosition.set(0, 0);
			mWall.addSegmentCollider(new AngleSegmentCollider(0, 0, 0, HEIGHT));
			mWall.mBounce = 1f;
			mPhysics.addObject(mWall); // Left wall*/

			// add barre
			Plateforme mPlateforme = new Plateforme(mBoxLayout,100,1);
			mPlateforme.mPosition.set(140,420);
			mPhysics.addObject(mPlateforme);
			
			/*mPlateforme = new Plateforme(mBoxLayout,100,1);
			mPlateforme.mPosition.set(200,410);
			mPhysics.addObject(mPlateforme);
			
			mPlateforme = new Plateforme(mBoxLayout,100,1);
			mPlateforme.mPosition.set(250,400);
			mPhysics.addObject(mPlateforme);*/
			
			mPlateforme = new Plateforme(mBoxLayout,100,1);
			mPlateforme.mPosition.set(50,350);
			mPhysics.addObject(mPlateforme);
			
			/*mPlateforme = new Plateforme(mBoxLayout,100,1);
			mPlateforme.mPosition.set(200,340);
			mPhysics.addObject(mPlateforme);*/
			
			mPlateforme = new Plateforme(mBoxLayout,100,1);
			mPlateforme.mPosition.set(160,300);
			mPhysics.addObject(mPlateforme);
			
			mPlateforme = new Plateforme(mBoxLayout,100,1);
			mPlateforme.mPosition.set(200,250);
			mPhysics.addObject(mPlateforme);
			
			mPlateforme = new Plateforme(mBoxLayout,100,1);
			mPlateforme.mPosition.set(130,200);
			mPhysics.addObject(mPlateforme);

			/*mPlateforme = new Plateforme(mBoxLayout,100,1);
			mPlateforme.mPosition.set(160,150);
			mPhysics.addObject(mPlateforme);*/
			
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
		
	}

	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

      mSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE); 
      
        // a commenté dans la version finale (pour voir la fluidité du jeu)      
		mGLSurfaceView.addObject(new FPSCounter());

		FrameLayout mMainLayout=new FrameLayout(this);
		mMainLayout.addView(mGLSurfaceView);
		setContentView(mMainLayout);
		
		mDemo=new MyDemo(this);
		setUI(mDemo);
		
		mDemo.setGravity(0f,10f);
	}


	//Overload onPause and onResume to enable and disable the accelerometer
	//Sobrecargamos onPause y onResume para activar y desactivar el aceler�metro
	@Override
	protected void onPause()
	{
      mSensorManager.unregisterListener(mListener); 
      
      super.onPause();
	}


	@Override
	protected void onResume()
	{
      mSensorManager.registerListener(mListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_FASTEST); 		

		super.onResume();
	}
	
}
