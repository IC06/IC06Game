package com.alt90.angle2;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Bitmap.Config;

/**
 * Texture created using font characters
 * @author Ivan Pajuelo
 *
 */
public class AngleFontTexture extends AngleTexture
{
	private AngleFont lFont;
	
	AngleFontTexture (AngleFont font)
	{
		lFont=font;
	}

	@Override
	public Bitmap create()
	{
		Bitmap mBitmap = null;
		Paint paint = new Paint();
		paint.setTypeface(lFont.lTypeface);
		paint.setTextSize(lFont.lFontSize);
		paint.setARGB(lFont.lAlpha, lFont.lRed, lFont.lGreen, lFont.lBlue);
		paint.setAntiAlias(true);

		Rect rect_tx = new Rect();
		int totalWidth_tx = 0;
		lFont.lHeight_tx = 0;
		int minTop_tx = 1000;
		int maxBottom_tx = -1000;
		for (int ch = 0; ch < lFont.lCharCount_ch; ch++)
		{
			paint.getTextBounds(new String(lFont.lCodePoints, ch, 1), 0, 1, rect_tx);
			lFont.lCharLeft_tx[ch] = (short) rect_tx.left;
			lFont.lCharRight_tx[ch] = (short) (rect_tx.right + lFont.lBorder_tx);
			totalWidth_tx += lFont.lCharRight_tx[ch] - lFont.lCharLeft_tx[ch];
			if (rect_tx.top < minTop_tx)
				minTop_tx = rect_tx.top;
			if (rect_tx.bottom > maxBottom_tx)
				maxBottom_tx = rect_tx.bottom;
		}
		lFont.lHeight_tx = (short) ((maxBottom_tx - minTop_tx) + lFont.lBorder_tx);
		lFont.lLineat_tx = (short) (minTop_tx - lFont.lBorder_tx/2);
		int area = lFont.lHeight_tx * totalWidth_tx;
		int textWidth_pw = 0;
		while ((area > ((1 << textWidth_pw) * (1 << textWidth_pw))) && (textWidth_pw < 11))
			textWidth_pw++;
		if (textWidth_pw < 11)
		{
			short x_tx = 0;
			short y_tx = 0;
			for (int ch = 0; ch < lFont.lCharCount_ch; ch++)
			{
				if (x_tx + (lFont.lCharRight_tx[ch] - lFont.lCharLeft_tx[ch]) > (1 << textWidth_pw))
				{
					x_tx = 0;
					y_tx += lFont.lHeight_tx;
				}
				if (y_tx + lFont.lHeight_tx > (1 << textWidth_pw))
				{
					if (textWidth_pw < 11)
					{
						textWidth_pw++;
						x_tx = 0;
						y_tx = 0;
						ch = -1;
						continue;
					}
					else
						break;
				}
				lFont.lCharX_tx[ch] = x_tx;
				lFont.lCharY_tx[ch] = y_tx;
				x_tx += (lFont.lCharRight_tx[ch] - lFont.lCharLeft_tx[ch]);
			}
			paint.getTextBounds(" ", 0, 1, rect_tx);
			lFont.lSpaceWidth_tx = (short) (rect_tx.right - rect_tx.left + lFont.lBorder_tx);
			if (lFont.lSpaceWidth_tx==0)
			{
				paint.getTextBounds("x", 0, 1, rect_tx);
				lFont.lSpaceWidth_tx = (short) (rect_tx.right - rect_tx.left + lFont.lBorder_tx);
			}
		}
		if (textWidth_pw < 11)
		{
			int textHeight_pw = 0;
			while ((lFont.lCharY_tx[lFont.lCharCount_ch - 1] + lFont.lHeight_tx) > (1 << textHeight_pw))
				textHeight_pw++;
			Bitmap paintBitmap = Bitmap.createBitmap((1 << textWidth_pw), (1 << textHeight_pw), Config.ARGB_8888);

			Canvas canvas = new Canvas(paintBitmap);

			for (int c = 0; c < lFont.lCharCount_ch; c++)
			{
				canvas.drawText(new String(lFont.lCodePoints, c, 1), 0, 1, lFont.lCharX_tx[c] - lFont.lCharLeft_tx[c] + (lFont.lBorder_tx / 2), lFont.lCharY_tx[c] - minTop_tx
						+ (lFont.lBorder_tx / 2), paint);
			}
			mBitmap=Bitmap.createBitmap(paintBitmap, 0, 0, paintBitmap.getWidth(), paintBitmap.getHeight());
			paintBitmap.recycle();
		}
		return mBitmap;
	}

	public boolean fontIs(AngleFont font)
	{
		return (font==lFont);
	}

}
