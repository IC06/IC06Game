package com.turlutu;




import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.FrameLayout;

import com.android.angle.AngleActivity;
import com.android.angle.AngleFont;
import com.android.angle.FPSCounter;
import com.turlutu.Bonus.TypeBonus;


public class MainActivity extends AngleActivity
{
	protected GameUI mGame;
	protected MenuUI mMenu;
	protected ScoresUI mScores;
	protected OptionsUI mOptions;
	protected OnLineScoresUI mScoresOnLine;
	private boolean loaded = false;
	protected FrameLayout mMainLayout;
	protected int mSensibility;
	protected MainActivity mActivity;
	protected Dialog dialog;
	protected AngleFont fntGlobal;
	
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
		mActivity = this;
		new Thread() 
		{
			@Override 
			public void run() 
			{
				Looper.prepare();
				dialog = new Dialog(mActivity);
		        dialog.setContentView(R.layout.loading);

		        dialog.setTitle("Chargement en cours !");
		        
		        dialog.show();
				Looper.loop();
			}
		}.start();
		
		
		mSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE); 
      
        // a commenté dans la version finale (pour voir la fluidité du jeu)      
		mGLSurfaceView.addObject(new FPSCounter());
		
		fntGlobal = new AngleFont(mActivity.mGLSurfaceView, 25, Typeface.createFromAsset(this.getAssets(),"cafe.ttf"), 222, 0, 0, 30, 30, 30, 255);
		mMainLayout=new FrameLayout(this);
		mMainLayout.addView(mGLSurfaceView);
		setContentView(mMainLayout);

		new Thread() {
			@Override 
			public void run() {
				load();
			}
		}.start();

		
		Log.i("MainActivity", "FIN");
	}
	
	public void load()
	{
		Log.i("MainActivity", "Load() start");
		mMenu=new MenuUI(this);
		mOptions=new OptionsUI(this);
		mScores=new ScoresUI(this);
		mScoresOnLine = new OnLineScoresUI(this);
		mGame=new GameUI(this);
		mGame.setGravity(0f,10f);
		setUI(mMenu);
		Log.i("MainActivity", "Load() fin");
		loaded = true;
		
		Thread endLoading = new Thread() {
			@Override 
			public void run() {
				dialog.dismiss();
			}
		};
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		endLoading.start();
		

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
