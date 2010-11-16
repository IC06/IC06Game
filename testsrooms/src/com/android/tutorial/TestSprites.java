package com.android.tutorial;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.android.angle.AngleActivity;
import com.android.angle.AngleRotatingSprite;
import com.android.angle.AngleSprite;
import com.android.angle.AngleSpriteLayout;
import com.android.angle.FPSCounter;

/**
 * Use FPSCounter class to see performance, put the Activity in full screen (look in AndroidManifest.xml) and play with alpha
 * >Veremos como usar FPSCounter para ver el rendimiento, pondremos la Activity a pantalla completa (mirar en AndroidManifest.xml) y jugaremos con el canal alfa 
 * 
 * @author Ivan Pajuelo
 * 
 */
public class TestSprites extends AngleActivity
{
	private class MyAnimatedSprite extends AngleRotatingSprite
	{
		private static final float sRotationSpeed = 20;
		private static final float sAlphaSpeed = 0.5f;
		private float mAplhaDir;

		public MyAnimatedSprite(int x, int y, AngleSpriteLayout layout)
		{
			super(x, y, layout);
			mAplhaDir=sAlphaSpeed;
		}

		@Override
		public void step(float secondsElapsed)
		{
			mRotation+=secondsElapsed*sRotationSpeed;
			mAlpha+=secondsElapsed*mAplhaDir;
			if (mAlpha>1)
			{
				mAlpha=1;
				mAplhaDir=-sAlphaSpeed;
			}
			if (mAlpha<0)
			{
				mAlpha=0;
				mAplhaDir=sAlphaSpeed;
			}
			super.step(secondsElapsed);
		}
		
	};
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		AngleSprite s = new AngleSprite(50,50,new AngleSpriteLayout(mGLSurfaceView,64,256,R.drawable.test,0,0,64,256,2,2));
		s.setFrame(1);
		mGLSurfaceView.addObject(s);
		mGLSurfaceView.addObject(new AngleSprite(160,240,new AngleSpriteLayout(mGLSurfaceView, 64,256, R.drawable.test2)));
		
		//Add FPS counter. See LogCat
		//>A�adimos un contador de im�genes por segundo. Mirar en LogCat
		mGLSurfaceView.addObject(new FPSCounter());

		FrameLayout mMainLayout=new FrameLayout(this);
		mMainLayout.addView(mGLSurfaceView);
		setContentView(mMainLayout);
	}
}
