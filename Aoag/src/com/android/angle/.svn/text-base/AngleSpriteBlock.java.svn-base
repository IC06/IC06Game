package com.alt90.angle2;

import javax.microedition.khronos.opengles.GL10;

public class AngleSpriteBlock extends AngleObject
{
	public AngleSpriteBlock ()
	{
		super (100);
	}

	public AngleSpriteBlock (int maxChildren)
	{
		super (maxChildren);
	}

	@Override
	public void draw(GL10 gl)
	{
		boolean isBinded=false;
		for (int t=0;t<lChildrenCount;t++)
		{
			if (lChildren[t] instanceof AngleAbstractSprite)
			{
				if (!isBinded)
				{
					((AngleAbstractSprite)lChildren[t]).draw(gl);
					isBinded=true;					
				}
				else
					((AngleAbstractSprite)lChildren[t]).blockDraw(gl);
			}
		}
	}

}
