package com.android.angle;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;
import javax.microedition.khronos.opengles.GL11Ext;

/**
 * Fastest sprite with no rotation support
 * 
 * @author Ivan Pajuelo
 * 
 */
public class AngleSprite extends AngleAbstractSprite
{
	protected int[] lTextureIV_tx; // Texture coordinates

	/**
	 * 
	 * @param layout AngleSpriteLayout
	 */
	public AngleSprite(AngleSpriteLayout layout)
	{
		super(layout);
		doInit(0, 0, 1);
	}

	/**
	 * 
	 * @param x_uu Position
	 * @param y_uu Position
	 * @param layout AngleSpriteLayout
	 */
	public AngleSprite(float x_uu, float y_uu, AngleSpriteLayout layout)
	{
		super(layout);
		doInit(x_uu, y_uu, 1);
	}
	
	/**
	 * 
	 * @param x_uu Position
	 * @param y_uu Position
	 * @param alpha Normalized alpha channel value
	 * @param layout AngleSpriteLayout
	 */
	public AngleSprite(float x_uu, float y_uu, float alpha, AngleSpriteLayout layout)
	{
		super(layout);
		doInit(x_uu, y_uu, alpha);
	}

	private void doInit(float x_uu, float y_uu, float alpha)
	{
		lTextureIV_tx = new int[4];
		setLayout(lLayout);
		fPosition_uu.set(x_uu,y_uu);
		lColor.fAlpha=alpha;
	}

	@Override
	public void setLayout(AngleSpriteLayout layout)
	{
		super.setLayout(layout);
		if (lLayout != null)
		{
			setFrame(0);
		}
	}

	@Override
	public void setFrame(int frame)
	{
		if (lLayout.fillTextureValues(frame, lTextureIV_tx,fFlipHorizontal,fFlipVertical))
			lFrame = frame;
	}

	@Override
	public void blockDraw(GL10 gl)
	{
		gl.glColor4f(lColor.fRed, lColor.fGreen, lColor.fBlue, lColor.fAlpha);

		((GL11) gl).glTexParameteriv(GL10.GL_TEXTURE_2D, GL11Ext.GL_TEXTURE_CROP_RECT_OES, lTextureIV_tx, 0);
				
		AngleVector pos_px=lLayout.getPivot_px(lFrame);
		pos_px.mul(fScale);
		pos_px.subAt(AngleRenderer.coordsUserToViewport(fPosition_uu));
		AngleVector size_px=AngleRenderer.coordsUserToViewport(lLayout.lDimensions_uu);
		size_px.mul(fScale);

		((GL11Ext) gl).glDrawTexfOES(pos_px.fX, AngleRenderer.vViewportHeight_px - pos_px.fY - size_px.fY, 0, size_px.fX, size_px.fY);
	}
}
