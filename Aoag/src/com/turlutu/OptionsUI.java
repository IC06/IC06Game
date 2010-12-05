package com.turlutu;

import android.app.Dialog;
import android.graphics.Typeface;
import android.os.Looper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ProgressBar;

import com.android.angle.AngleActivity;
import com.android.angle.AngleFont;
import com.android.angle.AngleObject;
import com.android.angle.AngleSprite;
import com.android.angle.AngleSpriteLayout;
import com.android.angle.AngleString;
import com.android.angle.AngleUI;
import com.turlutu.ScoresUI.CancelListener;
import com.turlutu.ScoresUI.OKListener;

public class OptionsUI  extends AngleUI
{
	
	private AngleObject ogMenuTexts;
	
	private AngleString strExit, strResetScores, strSensibility;

	protected int sensibility = 50;

	public OptionsUI(AngleActivity activity)
	{
		super(activity);
		
		ogMenuTexts = new AngleObject();


		addObject(new AngleSprite(160, 240, new AngleSpriteLayout(mActivity.mGLSurfaceView, 320, 480,  com.turlutu.R.drawable.bg_menu)));
		
		addObject(ogMenuTexts);

		AngleFont fntCafe=new AngleFont(mActivity.mGLSurfaceView, 25, Typeface.createFromAsset(mActivity.getAssets(),"cafe.ttf"), 222, 0, 0, 30, 200, 255, 255);

		strSensibility = (AngleString) ogMenuTexts.addObject(new AngleString(fntCafe, "Set Sensibility", 160, 200, AngleString.aCenter));
		strResetScores = (AngleString) ogMenuTexts.addObject(new AngleString(fntCafe, "Reset Scores", 160, 300, AngleString.aCenter));
		strExit = (AngleString) ogMenuTexts.addObject(new AngleString(fntCafe, "Retour", 160, 390, AngleString.aCenter));

	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
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

	}
	
	@Override
	public void onActivate()
	{
		super.onActivate();
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
	
	public void askParameter() {
		Dialog dialog = new Dialog(mActivity);
        dialog.setContentView(R.layout.options);
        dialog.setTitle("Parametres :");
        
        ProgressBar myProgressBar=(ProgressBar) dialog.findViewById(R.id.options_slider);
        myProgressBar.setProgress(sensibility);
        
        Button slideless = (Button) dialog.findViewById(R.id.slider_less);        
        slideless.setOnClickListener(new SlideLessListener(dialog));
        
        Button slidemore = (Button) dialog.findViewById(R.id.slider_more);        
        slidemore.setOnClickListener(new SlideMoreListener(dialog));
        
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
	
	protected class SlideMoreListener implements OnClickListener {	 
        private Dialog dialog;
        public SlideMoreListener(Dialog dialog) {
                this.dialog = dialog;
        }

        public void onClick(View v) {
        	if(sensibility < 100)
        		sensibility++;
        	ProgressBar myProgressBar=(ProgressBar) dialog.findViewById(R.id.options_slider);
            myProgressBar.setProgress(sensibility);
        }
	}
	
	protected class SlideLessListener implements OnClickListener {	 
        private Dialog dialog;
        public SlideLessListener(Dialog dialog) {
                this.dialog = dialog;
        }

        public void onClick(View v) {
        	if(sensibility > 0)
        		sensibility--;
        	ProgressBar myProgressBar=(ProgressBar) dialog.findViewById(R.id.options_slider);
            myProgressBar.setProgress(sensibility);
        }
	}
	
	@Override
	public void onDeactivate()
	{
	}
	
}