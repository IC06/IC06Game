package com.turlutu;

import android.graphics.Typeface;
import com.android.angle.AngleActivity;
import com.android.angle.AngleFont;
import com.android.angle.AngleObject;
import com.android.angle.AngleSprite;
import com.android.angle.AngleSpriteLayout;
import com.android.angle.AngleString;
import com.android.angle.AngleUI;

public class LoadingUI  extends AngleUI
{
	
	private AngleObject ogMenuTexts;
	
	private AngleString strLoad;



	public LoadingUI(AngleActivity activity)
	{
		super(activity);
		//addObject(new AngleSprite(160, 240, new AngleSpriteLayout(mActivity.mGLSurfaceView, 320, 480,  com.turlutu.R.drawable.bg_menu)));
		ogMenuTexts = new AngleObject();
		addObject(ogMenuTexts);
		AngleFont fntCafe=new AngleFont(mActivity.mGLSurfaceView, 25, Typeface.createFromAsset(mActivity.getAssets(),"cafe.ttf"), 222, 0, 0, 30, 200, 255, 255);
		strLoad = (AngleString) ogMenuTexts.addObject(new AngleString(fntCafe, "Loading...", 160, 180, AngleString.aCenter));
		
	}

	@Override
	public void onActivate()
	{
		new Thread() {
			@Override public void run() {
				//TODO l'écran loading ne sert pas, ce n'est pas la création des UI qui prend du temps, ce doit être le chargement de toutes les options
				/*
				while(true)
				{
					if (false)
						break;
				}*/
				((MainActivity) mActivity).load();
			}
		}.start();
		super.onActivate();
	}
	
}
