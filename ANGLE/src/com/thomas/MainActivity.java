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
import com.android.angle.AngleObject;
import com.android.angle.AnglePhysicObject;
import com.android.angle.AnglePhysicsEngine;
import com.android.angle.AngleSegmentCollider;
import com.android.angle.AngleSpriteLayout;
import com.android.angle.AngleUI;
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
	
	
   private final SensorEventListener mListener = new SensorEventListener() 
   {
		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy)
		{
		}

		@Override
		public void onSensorChanged(SensorEvent event)
		{
			/*if (event.sensor.getType()==Sensor.TYPE_ACCELEROMETER)
			{
				mDemo.setGravity(-10*event.values[0],10*event.values[1]);
			}*/
		}
   };
	private SensorManager mSensorManager; 	

	   

	
	
	private class MyDemo extends AngleUI
	{
		AngleSpriteLayout mBallLayout, mBoxLayout;
		AnglePhysicsEngine mPhysics;
		Ball mBall;
		float lastEventX, lastEventY;
		
		public MyDemo(AngleActivity activity)
		{
			super(activity);
			mBallLayout = new AngleSpriteLayout(mGLSurfaceView, 64, 64, com.android.tutorial.R.drawable.ball, 0, 0, 128, 128);
			mBoxLayout = new AngleSpriteLayout(mGLSurfaceView, 128, 32, com.android.tutorial.R.drawable.box, 0, 0, 256, 64);
			mPhysics=new AnglePhysicsEngine(20);
			mPhysics.mViscosity = 0f; // Air viscosity
			addObject(mPhysics);
			

			// Add 4 segment colliders to simulate walls
			AnglePhysicObject mWall = new AnglePhysicObject(1, 0);
			mWall.mPosition.set(160, 479);
			mWall.addSegmentCollider(new AngleSegmentCollider(-160, 0, 160, 0));
			mWall.mBounce = 1f;
			mPhysics.addObject(mWall); // Down wall
			
			mWall = new AnglePhysicObject(1, 0);
			mWall.mPosition.set(160, 0);
			mWall.addSegmentCollider(new AngleSegmentCollider(160, 0, -160, 0));
			mWall.mBounce = 1f;
			mPhysics.addObject(mWall); // Up wall
			
			mWall = new AnglePhysicObject(1, 0);
			mWall.mPosition.set(319, 240);
			mWall.addSegmentCollider(new AngleSegmentCollider(0, 240, 0, -240));
			mWall.mBounce = 1f;
			mPhysics.addObject(mWall); // Right wall
			
			mWall = new AnglePhysicObject(1, 0);
			mWall.mPosition.set(0, 240);
			mWall.addSegmentCollider(new AngleSegmentCollider(0, -240, 0, 240));
			mWall.mBounce = 1f;
			mPhysics.addObject(mWall); // Left wall
			

			// add barre
			Box mBox = new Box(mBoxLayout,128,32,0,1);
			mBox.mPosition.set(160,150);
			mPhysics.addObject(mBox);

			mBox = new Box(mBoxLayout,128,32,0,1);
			mBox.mPosition.set(50,300);
			mPhysics.addObject(mBox);

			mBox = new Box(mBoxLayout,128,32,0,1);
			mBox.mPosition.set(150,450);
			mPhysics.addObject(mBox);
			

			mBall = new Ball (mBallLayout,29,10,1);
			mBall.mPosition.set(50,50);
			mPhysics.addObject(mBall);
			
		}

		@Override
		public boolean onTouchEvent(MotionEvent event)
		{
			if (event.getAction()==MotionEvent.ACTION_MOVE)
			{
				mBall.mVelocity.mX = (event.getX()-mBall.mPosition.mX);
				mBall.mVelocity.mY = (event.getY()-mBall.mPosition.mY);
				lastEventX = event.getX();
				lastEventY = event.getY();
			}
			
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
