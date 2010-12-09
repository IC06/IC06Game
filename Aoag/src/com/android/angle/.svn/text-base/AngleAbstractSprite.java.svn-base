package com.alt90.angle2;

import javax.microedition.khronos.opengles.GL10;

/**
 * Sprite base class
 * @author Ivan Pajuelo
 *
 */
public abstract class AngleAbstractSprite extends AngleObject
{
	protected AngleSpriteLayout lLayout; //Sprite Layout with information about how to draw the sprite
	protected int lFrame; //Frame number. (ReadOnly)
	public AngleVector fPosition_uu; //Set to change the position of the sprite
	public AngleVector fScale; //Set to change the scale of the sprite
	public boolean fFlipHorizontal;
	public boolean fFlipVertical;
	public AngleColor lColor;

	/**
	 * Every sprite needs an AngleSpriteLayout to know how to draw itself
	 * @param layout the AngleSpriteLayout
	 */
	public AngleAbstractSprite(AngleSpriteLayout layout)
	{
		fScale = new AngleVector(1, 1);
		fPosition_uu = new AngleVector(0, 0);
		lLayout=layout;
		lColor=AngleColor.cWhite;
	}

	/**
	 * Set current frame number
	 * @param frame frame number
	 */
	public abstract void setFrame(int frame);

	/**
	 * Draw all children in block
	 * @param gl
	 */
	public abstract void blockDraw(GL10 gl);

	/**
	 * Set if sprite is flipped horizontally or vertically  
	 * @param flipHorizontal
	 * @param flipVertical
	 */
	public void setFlip(boolean flipHorizontal, boolean flipVertical)
	{
		fFlipHorizontal=flipHorizontal;
		fFlipVertical=flipVertical;
		setFrame(lFrame);
	}
	
	/**
	 * Change the AngleSpriteLayout
	 * @param layout
	 */
	public void setLayout(AngleSpriteLayout layout)
	{
		lLayout=layout;
	}

	public void draw(GL10 gl)
	{
		if (lLayout != null)
			if (lLayout.bindTexture(gl))
				blockDraw (gl);
	}
}
