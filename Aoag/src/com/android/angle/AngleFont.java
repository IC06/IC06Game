package com.android.angle;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.util.Log;

/**
 * Have the texture and parameters to draw characters
 * 
 * @author Ivan Pajuelo
 * 
 */
public class AngleFont
{
	private static final short DEFAULT_FONT_CHARS = 93;
	protected AngleTexture lTexture;
	protected float lFontSize;
	protected Typeface lTypeface;
	protected int lAlpha;
	protected int lRed;
	protected int lGreen;
	protected int lBlue;

	protected int[] lCodePoints; // Unicode 'chars'
	protected short lCharCount_ch; //Characters in font

	// --- Texture creation parameters ---
	protected int lBorder_tx;
	protected short lHeight_tx;
	protected short lSpaceWidth_tx;
	protected short lLineat_tx;
	protected short[] lCharX_tx;
	protected short[] lCharY_tx;
	protected short[] lCharLeft_tx;
	protected short[] lCharRight_tx;
	// -----------------------------------

	protected float lSpace_uu; //Space between printed characters

	/**
	 * 
	 * @param view			Main AngleSurfaceView
	 * @param fontSize	Size of the font
	 * @param typeface	Typeface
	 * @param space		Space between characters
	 * @param red			Color of the font (Red)
	 * @param green		Color of the font (Green)
	 * @param blue			Color of the font (Blue)
	 * @param alpha		Color of the font (Alpha channel)
	 */
	public AngleFont(float fontSize, Typeface typeface, int space, int red, int green, int blue, int alpha)
	{
		doInit(fontSize, typeface, DEFAULT_FONT_CHARS, (short) space, red, green, blue, alpha);
		for (int c = 0; c < lCharCount_ch; c++)
			lCodePoints[c] = 33 + c;
		lBorder_tx=1;
		lTexture=AngleTextureEngine.createTextureFromFont(this);
	}

	/**
	 * 
	 * @param view			Main AngleSurfaceView
	 * @param fontSize	Size of the font
	 * @param typeface	Typeface
	 * @param charCount	Number of characters into font
	 * @param border		Border of every character
	 * @param space		Space between characters
	 * @param red			Color of the font (Red)
	 * @param green		Color of the font (Green)
	 * @param blue			Color of the font (Blue)
	 * @param alpha		Color of the font (Alpha channel)
	 */
	public AngleFont(float fontSize, Typeface typeface, int charCount, int border, int space, int red, int green,
			int blue, int alpha)
	{
		doInit(fontSize, typeface, (short) charCount, (short) space, red, green, blue, alpha);
		for (int c = 0; c < lCharCount_ch; c++)
			lCodePoints[c] = 33 + c;
		lBorder_tx=border;
		lTexture=AngleTextureEngine.createTextureFromFont(this);
	}

	/**
	 * 
	 * @param view			Main AngleSurfaceView
	 * @param fontSize	Size of the font
	 * @param typeface	Typeface
	 * @param chars		String with characters into font 
	 * @param border		Border of every character
	 * @param space		Space between characters
	 * @param red			Color of the font (Red)
	 * @param green		Color of the font (Green)
	 * @param blue			Color of the font (Blue)
	 * @param alpha		Color of the font (Alpha channel)
	 */
	public AngleFont(float fontSize, Typeface typeface, char[] chars, int border, int space, int red, int green,
			int blue, int alpha)
	{
		doInit(fontSize, typeface, (short) chars.length, (short) space, red, green, blue, alpha);
		for (int c = 0; c < chars.length; c++)
			lCodePoints[c] = (int) chars[c];
		lBorder_tx=border;
		lTexture=AngleTextureEngine.createTextureFromFont(this);
	}

	/**
	 * 
	 * @param view			Main AngleSurfaceView
	 * @param asset		FNT file (to load font from) 
	 * @param resourceId PNG file (to load font from)
	 * @param space		Space between characters
	 */
	public AngleFont(String asset, int resourceId, int space_uu)
	{
		loadFrom(asset);
		lTexture=AngleTextureEngine.createTextureFromResourceId(resourceId);
		lSpace_uu = (short) space_uu;
	}

	private void doInit(short charCount)
	{
		lCharCount_ch = charCount;
		lTexture=null;
		lCodePoints = new int[lCharCount_ch];
		lCharX_tx = new short[lCharCount_ch];
		lCharLeft_tx = new short[lCharCount_ch];
		lCharY_tx = new short[lCharCount_ch];
		lCharRight_tx = new short[lCharCount_ch];
		lHeight_tx = 0;
	}

	private void doInit(float fontSize, Typeface typeface, short charCount, short space_uu, int red, int green, int blue, int alpha)
	{
		doInit(charCount);
		lSpace_uu = space_uu;
		lFontSize = fontSize;
		lTypeface = typeface;
		lAlpha = alpha;
		lRed = red;
		lGreen = green;
		lBlue = blue;
	}

	/**
	 * Save texture to the root of the SD card using 2 files. 1 PNG with graphics and 1 FNT with data.
	 * Edit the PNG and use it as a drawable resource and put the FNT into asset folder to reload edited font.  
	 * @param fileName
	 */
	public void saveTo(String fileName)
	{
		Bitmap bitmap=lTexture.create();
		if (bitmap != null)
		{
			try
			{
				FileOutputStream stream = new FileOutputStream("/sdcard/" + fileName + ".png");
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
				stream.flush();
				stream.close();
				ByteBuffer out = ByteBuffer.allocate(10 + lCharCount_ch * 12);
				out.putShort(lCharCount_ch);
				out.putShort(lHeight_tx);
				out.putShort((short)lSpace_uu); //TODO update fnt format
				out.putShort(lSpaceWidth_tx);
				out.putShort(lLineat_tx);
				for (int c = 0; c < lCharCount_ch; c++)
				{
					out.putInt(lCodePoints[c]);
					out.putShort(lCharX_tx[c]);
					out.putShort(lCharLeft_tx[c]);
					out.putShort(lCharY_tx[c]);
					out.putShort(lCharRight_tx[c]);
				}
				stream = new FileOutputStream("/sdcard/" + fileName + ".fnt");
				stream.write(out.array());
				stream.flush();
				stream.close();
				out = null;
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			bitmap.recycle();
		}
	}

	private void loadFrom(String asset)
	{
		InputStream is=null;
		try
		{
			is = AngleActivity.uInstance.getAssets().open(asset);
			ByteBuffer in = ByteBuffer.allocate(10);
			is.read(in.array());
			doInit(in.getShort(0));
			lHeight_tx = in.getShort(2);
			lSpace_uu = in.getShort(4);
			lSpaceWidth_tx = in.getShort(6);
			lLineat_tx = in.getShort(8);
			in = ByteBuffer.allocate(lCharCount_ch * 12);
			is.read(in.array());
			for (int c = 0; c < lCharCount_ch; c++)
			{
				lCodePoints[c] = in.getInt(c * 12);
				lCharX_tx[c] = in.getShort(c * 12 + 4);
				lCharLeft_tx[c] = in.getShort(c * 12 + 6);
				lCharY_tx[c] = in.getShort(c * 12 + 8);
				lCharRight_tx[c] = in.getShort(c * 12 + 10);
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				is.close();
			}
			catch (IOException e)
			{
				Log.e("AngleFont", "loadFrom::InputStream.close error: " + e.getMessage());
			}
		}
	}

	protected int getChar(char chr)
	{
		for (int c = 0; c < lCharCount_ch; c++)
		{
			if (lCodePoints[c] == chr)
				return c;
		}
		return -1;
	}

	/**
	 * 
	 * @param c Index of the character in font character array
	 * @return The character width in pixels
	 */
	public float charWidth_uu(char c)
	{
		int chr = getChar(c);
		if (chr == -1)
			return lSpaceWidth_tx*AngleRenderer.vHorizontalFactor_uu;
		else
			return lCharRight_tx[chr]*AngleRenderer.vHorizontalFactor_uu + lSpace_uu;
	}
}
