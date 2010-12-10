package com.turlutu;

import android.app.Dialog;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Looper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.android.angle.AngleActivity;
import com.android.angle.AngleFont;
import com.android.angle.AngleObject;
import com.android.angle.AnglePhysicObject;
import com.android.angle.AngleSegmentCollider;
import com.android.angle.AngleSound;
import com.android.angle.AngleSpriteLayout;
import com.android.angle.AngleString;
import com.android.angle.AngleUI;
import com.android.angle.AngleVector;

public class OptionsUI  extends AngleUI
{

	protected AngleSpriteLayout mBallLayout[];
	protected Ball mBall;

	private MyPhysicsEngine mPhysics;
	private float WIDTH,HEIGHT;
	private AngleObject ogMenuTexts;
	//TODO faire ça mieu
	private boolean dbEmpty;
	private AngleString strExit, strResetScores, strSensibility, strVolume;

	protected int mSensibility = 50;
	protected int mVolume = 100;

	public OptionsUI(AngleActivity activity)
	{
		super(activity);
		Log.i("OptionsUI", "constructor debut");
		WIDTH = 320f;
		HEIGHT = 480f;
		
		ogMenuTexts = new AngleObject();
		
		addObject(ogMenuTexts);

		AngleFont fntCafe=new AngleFont(mActivity.mGLSurfaceView, 25, Typeface.createFromAsset(mActivity.getAssets(),"cafe.ttf"), 222, 0, 0, 30, 200, 255, 255);

		strSensibility = (AngleString) ogMenuTexts.addObject(new AngleString(fntCafe, "Set Sensibility", 160, 200, AngleString.aCenter));
		strVolume = (AngleString) ogMenuTexts.addObject(new AngleString(fntCafe, "Set Volume", 160, 250, AngleString.aCenter));
		strResetScores = (AngleString) ogMenuTexts.addObject(new AngleString(fntCafe, "Reset Scores", 160, 300, AngleString.aCenter));
		strExit = (AngleString) ogMenuTexts.addObject(new AngleString(fntCafe, "Retour", 160, 390, AngleString.aCenter));

		mBallLayout = new AngleSpriteLayout[6];
		mBallLayout[0] = new AngleSpriteLayout(activity.mGLSurfaceView, 42, 64, com.turlutu.R.drawable.persos,0,0,42,64);
		mBallLayout[1] = new AngleSpriteLayout(activity.mGLSurfaceView, 42, 64, com.turlutu.R.drawable.persos,62,0,42,64);
		mBallLayout[2] = new AngleSpriteLayout(activity.mGLSurfaceView, 42, 64, com.turlutu.R.drawable.persos,124,0,42,64);
		mBallLayout[3] = new AngleSpriteLayout(activity.mGLSurfaceView, 42, 64, com.turlutu.R.drawable.persos,186,0,42,64);
		mBallLayout[4] = new AngleSpriteLayout(activity.mGLSurfaceView, 42, 64, com.turlutu.R.drawable.persos,248,0,42,64);
		mBallLayout[5] = new AngleSpriteLayout(activity.mGLSurfaceView, 42, 64, com.turlutu.R.drawable.persos,310,0,42,64);

		mPhysics = new MyPhysicsEngine(20,WIDTH,HEIGHT, mActivity.mGLSurfaceView, null);
		mPhysics.mViscosity = 1f; // Air viscosity
		mPhysics.mGravity = new AngleVector(0,10f);
		addObject(mPhysics);

		AngleSound sndJump = new AngleSound(activity,R.raw.jump);
		mBall = new Ball (mBallLayout,32,80,1,sndJump,null,this);
		mPhysics.addObject(mBall);
		
		// ajoute une plateforme en bas qui prend toute la place pour le debut
		AnglePhysicObject mWall = new AnglePhysicObject(1, 0);
		mWall.mPosition.set(0, HEIGHT-15);
		mWall.addSegmentCollider(new AngleSegmentCollider(0, 0, WIDTH, 0));
		mWall.mBounce = 1f;
		mPhysics.addObject(mWall); // Down wall
		Log.i("OptionsUI", "constructor fin");
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		mBall.mVelocity.mX = (event.getX()-WIDTH/2)*((MainActivity)mActivity).mOptions.mSensibility/25;
		
		if (event.getAction() == MotionEvent.ACTION_DOWN)
		{
			float eX = event.getX();
			float eY = event.getY();

			if (strResetScores.test(eX, eY))
				resetScores();
			else if (strSensibility.test(eX, eY))
				setSensibility();
			else if (strVolume.test(eX, eY))
				setVolume();
			else if (strExit.test(eX, eY))
				((MainActivity) mActivity).setUI(((MainActivity) mActivity).mMenu);

			return true;
		}
		return false;
	}

	private void resetScores()
	{
		DBScores db = new DBScores(mActivity);
		db.open();
		if (db.reset())
			Toast.makeText(mActivity, "Score reset", Toast.LENGTH_SHORT).show();
		else
			Toast.makeText(mActivity, "Une erreur est survenue", Toast.LENGTH_SHORT).show();
		db.close();
	}
	
	private void setSensibility()
	{
		new Thread() 
		{
			@Override 
			public void run() 
			{
				Looper.prepare();
				askParameter(1);
				Looper.loop();
			}
		}.start();
	}
	
	private void setVolume()
	{
		new Thread() 
		{
			@Override 
			public void run() 
			{
				Looper.prepare();
				askParameter(2);
				Looper.loop();
			}
		}.start();
	}
	
	@Override
	public void onActivate()
	{
		Log.i("OptionUI", "onActivate debut");
		super.onActivate();
		init();
		Log.i("OptionUI", "onActivate fin");
	}
	
	public void init()
	{
		mBall.mPosition.set(50,300);
		mBall.jump();

		// ajoute une plateforme en bas qui prend toute la place pour le debut
		AnglePhysicObject mWall = new AnglePhysicObject(1, 0);
		mWall.mPosition.set(0, HEIGHT-15);
		mWall.addSegmentCollider(new AngleSegmentCollider(0, 0, WIDTH, 0));
		mWall.mBounce = 1f;
		mPhysics.addObject(mWall); // Down wall
	}
	
	public void askParameter(int type) {
		DBOptions db = new DBOptions(mActivity);
		db.open();
		Cursor c = db.getOptions();
		if (c.getCount() >= 1)
    	{
			dbEmpty = false;
    		c.moveToFirst();
    		mSensibility = c.getInt(1);
    	}
    	else
    	{
    		dbEmpty = true;
    		mSensibility = 50;
    	}
		db.close();
			
		Dialog dialog = new Dialog(mActivity);
        dialog.setContentView(R.layout.horizontalslider);
        
        SeekBar mySeekBar=(SeekBar) dialog.findViewById(R.id.options_slider);
        
        if(type == 1) {
        	dialog.setTitle("Sensibilité :");
            mySeekBar.setProgress(mSensibility);
        } else if(type == 2) {
        	dialog.setTitle("Volume :");
            mySeekBar.setProgress(mVolume);
        }

        mySeekBar.setOnSeekBarChangeListener(new MySeekBarListener(type));
        
        Button buttonOK = (Button) dialog.findViewById(R.id.options_ok);        
        buttonOK.setOnClickListener(new OKListener(dialog));
        
        dialog.show();
	}
	protected class OKListener implements OnClickListener {	 
        private Dialog dialog;
        public OKListener(Dialog dialog) {
                this.dialog = dialog;
        }

        public void onClick(View v) {
	    		DBOptions db = new DBOptions(mActivity);
	    		db.open();
	    		if (dbEmpty)
	    			db.insert(mSensibility,"anonyme");
	    		else
	    			db.replace(1, mSensibility, "anonyme");
	    		db.close();
        		dialog.dismiss(); 
        }
	}

	protected class MySeekBarListener implements OnSeekBarChangeListener {
		private int mType;

		public MySeekBarListener(int type) {
			mType = type;
		}
		
		public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
			if(mType==1)
				mSensibility = arg1;
			else if(mType==2)
				mVolume = arg1;
		}

		public void onStartTrackingTouch(SeekBar arg0) {
			// TODO Auto-generated method stub
			
		}

		public void onStopTrackingTouch(SeekBar arg0) {
			// TODO Auto-generated method stub
			
		}
	}
	
	
	@Override
	public void onDeactivate()
	{
		Log.i("OptionUI", "onDeactivate debut");
		Log.i("OptionUI", "onDeactivate fin");
	}
	
}
