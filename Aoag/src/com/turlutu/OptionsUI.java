package com.turlutu;

import android.app.Dialog;
import android.graphics.Typeface;
import android.os.Looper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.angle.AngleActivity;
import com.android.angle.AngleFont;
import com.android.angle.AngleObject;
import com.android.angle.AnglePhysicObject;
import com.android.angle.AngleSegmentCollider;
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
	private AngleString strExit, strResetScores, strSensibility;

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

		
		mBall = new Ball (mBallLayout,32,80,1,null,null);
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
				askParameter();
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
	
	public void askParameter() {
		Dialog dialog = new Dialog(mActivity);
        dialog.setContentView(R.layout.horizontalslider);
        dialog.setTitle("Parametres :");
      
        ProgressBar myProgressBar=(ProgressBar) dialog.findViewById(R.id.options_slider);
        myProgressBar.setProgress(mSensibility);
        myProgressBar.setOnTouchListener(new MyTouchListener(dialog));
        
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
        		dialog.dismiss(); 
        }
	}

	protected class MyTouchListener implements OnTouchListener {
        private Dialog dialog;
        public MyTouchListener(Dialog dialog) {
                this.dialog = dialog;
        }
        
        public boolean onTouch(View v, MotionEvent event)
        {
	    	ProgressBar myProgressBar=(ProgressBar) dialog.findViewById(R.id.options_slider);
        	float x_mouse = event.getX();
			float width = myProgressBar.getWidth();
	        mSensibility = Math.round((float) myProgressBar.getMax() * (x_mouse / width));
	        if (mSensibility < 0)
	        	mSensibility = 0;
	        myProgressBar.setProgress(mSensibility);
	        return true;
        }
	}
	
	
	@Override
	public void onDeactivate()
	{
		Log.i("OptionUI", "onDeactivate debut");
		Log.i("OptionUI", "onDeactivate fin");
	}
	
}