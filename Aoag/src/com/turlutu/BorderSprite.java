package com.turlutu;


import javax.microedition.khronos.opengles.GL10;

import com.android.angle.AngleActivity;
import com.android.angle.AngleSprite;
import com.android.angle.AngleSpriteLayout;
import com.turlutu.Bonus.TypeBonus;

/** 
* @author thomas
*/
public class BorderSprite extends AngleSprite
{
	protected MainActivity mActivity;
	public BorderSprite(AngleActivity Activity, int x, int y, AngleSpriteLayout layout)
	{
		super(x,y,layout);
		mActivity = (MainActivity) Activity;
	}
	
	@Override
	public void draw(GL10 gl)
	{
		if( ((MainActivity) mActivity).mGame.mTypeBonus != TypeBonus.DISABLECHANGECOLOR)
			super.draw(gl);
	}


}
