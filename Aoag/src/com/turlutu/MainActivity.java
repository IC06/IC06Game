package com.turlutu;




import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

import com.android.angle.AngleActivity;
import com.android.angle.FPSCounter;
import com.turlutu.Bonus.TypeBonus;


public class MainActivity extends AngleActivity
{
	protected GameUI mGame;
	protected MenuUI mMenu;
	protected ScoresUI mScores;
	protected OptionsUI mOptions;
	protected LoadingUI mLoading;
	private boolean loaded = false;
	protected FrameLayout mMainLayout;
	protected int mSensibility;
	
   private final SensorEventListener mListener = new SensorEventListener() 
   {
		//@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy)
		{
		}

		//@Override
		public void onSensorChanged(SensorEvent event)
		{
			if (loaded & event.sensor.getType()==Sensor.TYPE_ACCELEROMETER)
			{
				mGame.mBall.mVelocity.mX = (-mOptions.mSensibility*2*event.values[0]);
				mOptions.mBall.mVelocity.mX = (-mOptions.mSensibility*2*event.values[0]);
				if (mGame.mTypeBonus == TypeBonus.CHANGEPHYSICS)
					mGame.mBall.mVelocity.mX = -mGame.mBall.mVelocity.mX;
			}
		}
   };
	private SensorManager mSensorManager; 	

	   

	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		Log.i("MainActivity", "START");
		super.onCreate(savedInstanceState);

		mSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE); 
      
        // a commenté dans la version finale (pour voir la fluidité du jeu)      
		mGLSurfaceView.addObject(new FPSCounter());
		
		
		mMainLayout=new FrameLayout(this);
		mMainLayout.addView(mGLSurfaceView);
		setContentView(mMainLayout);

		
		mLoading=new LoadingUI(this);
		setUI(mLoading);
		
		Log.i("MainActivity", "FIN");
	}
	
	public void load()
	{
		Log.i("MainActivity", "Load() start");
		mMenu=new MenuUI(this);
		mOptions=new OptionsUI(this);
		mScores=new ScoresUI(this);
		mGame=new GameUI(this);
		mGame.setGravity(0f,10f);
		setUI(mMenu);
		Log.i("MainActivity", "Load() fin");
		loaded = true;
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
