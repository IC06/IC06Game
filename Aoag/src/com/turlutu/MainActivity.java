package com.turlutu;




import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Looper;
import android.os.Vibrator;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.FrameLayout;

import com.android.angle.AngleActivity;
import com.android.angle.AngleFont;
import com.android.angle.AngleSpriteLayout;
import com.android.angle.FPSCounter;
import com.turlutu.Bonus.TypeBonus;


public class MainActivity extends AngleActivity
{
	protected GameUI mGame;
	protected MenuUI mMenu;
	protected ScoresUI mScores;
	protected OptionsUI mOptions;
	protected InstructionsUI mInstructions;
	protected OnLineScoresUI mScoresOnLine;
	private boolean loaded = false;
	protected FrameLayout mMainLayout;
	protected int mSensibility;
	protected MainActivity mActivity;
	protected ProgressDialog  dialog;
	protected AngleFont fntGlobal, fntGlobal1;
	protected Vibrator mVibrator;
	private final SensorEventListener mListener = new SensorEventListener() 
	{
		//@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy)
		{
		}
		
		// @Override
		public void onSensorChanged(SensorEvent event)
		{
			if (loaded & event.sensor.getType()==Sensor.TYPE_ACCELEROMETER)
			{
				mGame.mBall.mVelocity.mX = (-mOptions.mSensibility*2*event.values[0]);
				if(mOptions.mBall != null)
					mOptions.mBall.mVelocity.mX = (-mOptions.mSensibility*2*event.values[0]);
				if (mGame.mTypeBonus == TypeBonus.CHANGEPHYSICS)
					mGame.mBall.mVelocity.mX = -mGame.mBall.mVelocity.mX;
			}
		}
	};
	private SensorManager mSensorManager; 	

	   
	 @Override
	    public boolean onKeyDown(int keyCode, KeyEvent event) 
	    {
	               
	        if (keyCode == KeyEvent.KEYCODE_BACK) {
				new Thread() 
				{
					@Override 
					public void run() 
					{
						Looper.prepare();
						AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
						builder.setMessage("Do you really want to exit ?")
						       .setCancelable(false)
						       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
						           public void onClick(DialogInterface dialog, int id) {
						        	  finish();
						           }})
							    .setNeutralButton("No", new DialogInterface.OnClickListener() {
							           public void onClick(DialogInterface dialog, int id) {
							        	   onResume();
							           }
						       });
						AlertDialog alert = builder.create();
						alert.show();
						Looper.loop();
					}
				}.start();
				onPause();
	        	return true;
	        }
	           return false;
	     }

	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		Log.i("MainActivity", "START");
		
		mActivity = this;
		super.onCreate(savedInstanceState);
		
		new Thread() 
		{
			@Override 
			public void run() 
			{
				Looper.prepare();
		        dialog = ProgressDialog.show(mActivity, "", 
                        "Chargement en cours, veuillez patienter...", true);
				Looper.loop();
			}
		}.start();
		
		mSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE); 
		mVibrator =(Vibrator)getSystemService(Context.VIBRATOR_SERVICE);   
		
        // a commenté dans la version finale (pour voir la fluidité du jeu)      
		mGLSurfaceView.addObject(new FPSCounter());
		
		fntGlobal = new AngleFont(this.mGLSurfaceView, 25, Typeface.createFromAsset(this.getAssets(),"cafe.ttf"), 222, 0, 0, 30, 30, 30, 255);
		// Text of instructions
		fntGlobal1 = new AngleFont(this.mGLSurfaceView, 17, Typeface.createFromAsset(this.getAssets(),"cafe.ttf"), 222, 0, 0, 30, 30, 30, 255);
		mMainLayout=new FrameLayout(this);
		mMainLayout.addView(mGLSurfaceView);
		setContentView(mMainLayout);

		new Thread() 
		{
			@Override 
			public void run() 
			{
				load();
			}
		}.start();

		
		Log.i("MainActivity", "FIN");
	}
	
	public void load()
	{
		Log.i("MainActivity", "Load() start");
		
		AngleSpriteLayout mLayoutOptions = new AngleSpriteLayout(mGLSurfaceView,320,480,com.turlutu.R.drawable.fonds, 320, 0, 320, 480);
		AngleSpriteLayout mLayoutGame = new AngleSpriteLayout(mGLSurfaceView,320,480,com.turlutu.R.drawable.fonds, 7*320, 0, 320, 480);
		AngleSpriteLayout mLayoutWhite = new AngleSpriteLayout(mGLSurfaceView,320,480,com.turlutu.R.drawable.fonds, 8*320, 0, 320, 480);
		AngleSpriteLayout mLayoutMenu = new AngleSpriteLayout(mGLSurfaceView,320,480,com.turlutu.R.drawable.fonds, 6*320, 0, 320, 480);
		AngleSpriteLayout mLayoutScore = new AngleSpriteLayout(mGLSurfaceView,320,480,com.turlutu.R.drawable.fonds, 2*320, 0, 320, 480);
		AngleSpriteLayout mLayoutOnLineScore = new AngleSpriteLayout(mGLSurfaceView,320,480,com.turlutu.R.drawable.fonds, 3*320, 0, 320, 480);
				
		
		Background mBackgroundOptions = null;
		Background mBackgroundGame = null;
		Background mBackgroundWhite = null;
		Background mBackgroundMenu = null;
		Background mBackgroundScore = null;
		Background mBackgroundOnLineScore = null;
		// TODO RELEASE a decommenter pour avoir des background dans les differentes UI (autre que le menu)
		mBackgroundOptions= new Background(mLayoutOptions);
		mBackgroundGame = new Background(mLayoutGame);
		mBackgroundWhite = new Background(mLayoutWhite);
		mBackgroundMenu = new Background(mLayoutMenu);
		mBackgroundScore = new Background(mLayoutScore);
		mBackgroundOnLineScore = new Background(mLayoutOnLineScore);
		
		
		mOptions=new OptionsUI(this,mBackgroundOptions);
		mInstructions= new InstructionsUI(this,mBackgroundWhite);
		mScores=new ScoresUI(this,mBackgroundScore);
		mScoresOnLine = new OnLineScoresUI(this,mBackgroundOnLineScore);
		mGame=new GameUI(this,mBackgroundGame);
		mGame.setGravity(0f,10f);
		mMenu=new MenuUI(this,mBackgroundMenu);
		
		
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
