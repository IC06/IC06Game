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
        dialog.setContentView(R.layout.name_activity);
        dialog.setTitle("Entrer votre nom :");
        Button buttonOK = (Button) dialog.findViewById(R.id.ok);        
        buttonOK.setOnClickListener(new OKListener(dialog));
        Button buttonCancel = (Button) dialog.findViewById(R.id.cancel);        
        buttonCancel.setOnClickListener(new CancelListener(dialog));
        dialog.show();
	}
	protected class OKListener implements OnClickListener {	 
        private Dialog dialog;
        public OKListener(Dialog dialog) {
                this.dialog = dialog;
        }

        public void onClick(View v) {
        		TextView input = (TextView) dialog.findViewById(R.id.entry);
        		CharSequence name = input.getText();
        		dialog.dismiss(); 
        		DBScores db = new DBScores(mActivity);
        		db.open();
        		long i = db.insertScore( ((MainActivity) mActivity).mGame.mScore, ""+name);
        		Log.i("ScoresUI", "ScoresUI on click on ok insert : " + i);
        		db.close();
        		((MainActivity) mActivity).mGame.mScore = 0;
        }
	}
	
	protected class CancelListener implements OnClickListener {	 
        private Dialog dialog;
        public CancelListener(Dialog dialog) {
                this.dialog = dialog;
        }

        public void onClick(View v) {
                dialog.dismiss();    
        		((MainActivity) mActivity).mGame.mScore = 0;
        }
	}
	
	@Override
	public void onDeactivate()
	{
	}
	
}