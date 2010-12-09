package com.android.angle;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;
import javax.microedition.khronos.opengles.GL11Ext;

/**
 * Have the string and its position. Length is automatically set when string
 * content is changed. But can be altered to create typing effect.
 * 
 * @author Ivan Pajuelo
 * 
 */
public class AngleString extends AngleObject
{
	public static final int aLeft = 0;
	public static final int aCenter = 1;
	public static final int aRight = 2;
	protected String lString;
	protected String lWantString;
	public int fLength_ch; // Length to display
	protected AngleFont lFont; // Font
	protected int[] lTextureIV_tx = new int[4]; // Texture coordinates
	public AngleVector fPosition_uu; // Position
	public int fAlignment; // Text alignment
	public AngleColor lColor;
	public float fDisplayWidth_uu;
	public int fDisplayLines_ln;
	protected boolean lIgnoreNL;
	protected int lTabLength_ch;
	private int lLinesCount_ln;
	private int[] lLineStart_ch;
	private int[] lLineEnd_ch;
	private float lWidth_uu;
	private boolean lNewString;

	/**
	 * 
	 * @param font
	 *           AngleFont
	 */
	public AngleString(AngleFont font)
	{
		init(font, 3, false);
	}

	/**
	 * 
	 * @param font
	 *           AngleFont
	 * @param tabLength_ch
	 *           Length in spaces of \t
	 * @param ignoreNL
	 *           Ignore \n
	 */
	public AngleString(AngleFont font, int tabLength_ch, boolean ignoreNL)
	{
		init(font, tabLength_ch, ignoreNL);
	}

	/**
	 * 
	 * @param font
	 *           AngleFont
	 * @param string
	 *           The text
	 * @param x_uu
	 *           Position
	 * @param y_uu
	 *           Position
	 * @param alignment
	 *           aLeft, aCenter or aRight
	 */
	public AngleString(AngleFont font, String string, int x_uu, int y_uu, int alignment)
	{
		init(font, 3, false);
		set(string);
		fPosition_uu.set(x_uu, y_uu);
		fAlignment = alignment;
	}

	private void init(AngleFont font, int tabLength_ch, boolean ignoreNL)
	{
		fPosition_uu = new AngleVector();
		lFont = font;
		fLength_ch = 0;
		lLinesCount_ln = 0;
		fAlignment = aLeft;
		lColor=AngleColor.cWhite;
		fDisplayWidth_uu = 0;
		fDisplayLines_ln = 1;
		lTabLength_ch = tabLength_ch;
		lIgnoreNL = ignoreNL;
		lNewString = false;
	}

	/**
	 * Changes the string content and hides it
	 * 
	 * @param src
	 */
	public void setAndHide(String src)
	{
		prepareString(src);
	}

	/**
	 * Changes the string content
	 * 
	 * @param src
	 */
	public void set(String src)
	{
		prepareString(src);
		fLength_ch = lWantString.length();
	}

	private void prepareString(String src)
	{
		fLength_ch = 0;
		lWantString = "";
		if (src == null)
			return;

		String strStep1 = "";
		int lineLength_ch = 0;
		for (int ch = 0; ch < src.length(); ch++)
		{
			if ((src.charAt(ch) == '\n') && lIgnoreNL)
			{
				strStep1 = strStep1.concat(" ");
				continue;
			}
			if (src.charAt(ch) == '\n')
			{
				strStep1 = strStep1.concat(src.substring(ch, ch + 1));
				lineLength_ch = 0;
			}
			else if (src.charAt(ch) == '\t')
			{
				if (lTabLength_ch > 0)
				{
					int tab = lTabLength_ch - (lineLength_ch % lTabLength_ch);
					if (tab == 0)
						tab = lTabLength_ch;
					for (int t = 0; t < tab; t++)
					{
						strStep1 = strStep1.concat(" ");
						lineLength_ch++;
					}
				}
			}
			else if (src.charAt(ch) >= ' ')
			{
				strStep1 = strStep1.concat(src.substring(ch, ch + 1));
				lineLength_ch++;
			}
		}
		if (fDisplayWidth_uu > 0)
		{
			int lineWidth_uu = 0;
			int flc_ch = 0; // FirstLineChar
			for (int ch = 0; ch < strStep1.length(); ch++)
			{
				if (strStep1.charAt(ch) == '\n')
				{
					lineWidth_uu = 0;
					flc_ch = ch + 1;
				}
				else
					lineWidth_uu += lFont.charWidth_uu(strStep1.charAt(ch));

				boolean copy = false;
				int llc_ch = ch; // Last Line Char
				if (lineWidth_uu > fDisplayWidth_uu)
				{
					while ((lineWidth_uu > fDisplayWidth_uu) && (llc_ch > flc_ch))
					{
						while ((strStep1.charAt(llc_ch) == ' ') && (llc_ch > flc_ch)) // Quita
																											// los
																											// espacios
																											// del
																											// final
						{
							lineWidth_uu -= lFont.charWidth_uu(strStep1.charAt(llc_ch));
							llc_ch--;
						}
						if (lineWidth_uu <= fDisplayWidth_uu)
							break;
						while ((strStep1.charAt(llc_ch) != ' ') && (llc_ch > flc_ch)) // Quita
																											// la
																											// �ltima
																											// palabra
						{
							lineWidth_uu -= lFont.charWidth_uu(strStep1.charAt(llc_ch));
							llc_ch--;
						}
					}
					if (llc_ch == flc_ch) // Hay una palabra + larga que la linea
					{
						lWantString = strStep1;
						break;
					}
					copy = true;
				}
				if (copy || (ch == strStep1.length() - 1))
				{
					lWantString = lWantString.concat(strStep1.substring(flc_ch, llc_ch + 1));
					if (llc_ch + 1 == strStep1.length())
						break;
					lWantString = lWantString.concat("\n");
					flc_ch = llc_ch + 1;
					while ((strStep1.charAt(flc_ch) == ' ') && (flc_ch < strStep1.length()))
						// Quita los espacios del final
						flc_ch++;
					ch = flc_ch;
					lineWidth_uu = 0;
				}
			}
		}
		else
			lWantString = strStep1;
		fDisplayLines_ln = 1;
		for (int c = 0; c < lWantString.length(); c++)
		{
			if (lWantString.charAt(c) == '\n')
				fDisplayLines_ln++;
		}
		lNewString = true;
	}

	private void tsSetString()
	{
		lString = lWantString;
		lNewString = false;
		lLinesCount_ln = 1;
		for (int c = 0; c < lString.length(); c++)
		{
			if (lString.charAt(c) == '\n')
				lLinesCount_ln++;
		}
		lLineStart_ch = new int[lLinesCount_ln];
		lLineEnd_ch = new int[lLinesCount_ln];
		int l = 0;
		lLineStart_ch[l] = 0;
		lLineEnd_ch[lLinesCount_ln - 1] = lString.length();
		for (int c = 0; c < lString.length(); c++)
		{
			if (lString.charAt(c) == '\n')
			{
				lLineEnd_ch[l++] = c;
				if (l < lLinesCount_ln)
					lLineStart_ch[l] = c + 1;
			}
		}
		lWidth_uu = 0;
		for (l = 0; l < lLinesCount_ln; l++)
		{
			float lineWidth_uu = getWidth_uu(lLineStart_ch[l], lLineEnd_ch[l]);
			if (lWidth_uu < lineWidth_uu)
				lWidth_uu = lineWidth_uu;
		}
	}

	/**
	 * Test if a point is within extent of the string
	 * 
	 * @param x_uu
	 *           Point
	 * @param y_uu
	 *           Point
	 * @return Returns true if point(x,y) is within string
	 */
	public boolean test(float x_uu, float y_uu)
	{
		float left_uu = fPosition_uu.fX;
		if (fAlignment == aRight)
			left_uu = fPosition_uu.fX - lWidth_uu;
		else if (fAlignment == aCenter)
			left_uu = fPosition_uu.fX - lWidth_uu / 2;

		float top_uu = fPosition_uu.fY + lFont.lLineat_tx * AngleRenderer.vVerticalFactor_uu;

		if (x_uu >= left_uu)
			if (y_uu >= top_uu)
				if (x_uu < left_uu + lWidth_uu)
					if (y_uu < top_uu + getHeight_uu())
						return true;
		return false;
	}

	private int drawLine(GL10 gl, float y_uu, int line_ln)
	{
		if ((line_ln >= 0) && (line_ln < lLinesCount_ln))
		{
			float x_uu = fPosition_uu.fX;
			if (fAlignment == aRight)
				x_uu -= getWidth_uu(lLineStart_ch[line_ln], lLineEnd_ch[line_ln]);
			else if (fAlignment == aCenter)
				x_uu -= getWidth_uu(lLineStart_ch[line_ln], lLineEnd_ch[line_ln]) / 2;
			for (int c = lLineStart_ch[line_ln]; (c < lLineEnd_ch[line_ln]) && (c < fLength_ch); c++)
			{
				int chr = lFont.getChar(lString.charAt(c));
				if (chr == -1)
				{
					x_uu += lFont.lSpaceWidth_tx * AngleRenderer.vHorizontalFactor_uu;
					continue;
				}
				int chrWidth = lFont.lCharRight_tx[chr] - lFont.lCharLeft_tx[chr];
				lTextureIV_tx[0] = lFont.lCharX_tx[chr];
				lTextureIV_tx[1] = lFont.lCharY_tx[chr] + lFont.lHeight_tx;
				lTextureIV_tx[2] = chrWidth;
				lTextureIV_tx[3] = -lFont.lHeight_tx;
				((GL11) gl).glTexParameteriv(GL11.GL_TEXTURE_2D, GL11Ext.GL_TEXTURE_CROP_RECT_OES, lTextureIV_tx, 0);

				((GL11Ext) gl).glDrawTexfOES((x_uu + lFont.lCharLeft_tx[chr])* AngleRenderer.vHorizontalFactor_px,
						AngleRenderer.vViewportHeight_px - (y_uu + lFont.lHeight_tx + lFont.lLineat_tx)*AngleRenderer.vVerticalFactor_px, 0,
						chrWidth * AngleRenderer.vHorizontalFactor_px, lFont.lHeight_tx * AngleRenderer.vVerticalFactor_px);
				x_uu += lFont.lCharRight_tx[chr] + lFont.lSpace_uu ;
			}
			return lFont.lHeight_tx;
		}
		return 0;
	}

	@Override
	public void draw(GL10 gl)
	{
		if (lFont != null)
		{
			if (lFont.lTexture != null)
			{
				if (lFont.lTexture.lHWTextureID > -1)
				{
					if (lNewString)
						tsSetString();
					else
					{
						if (fLength_ch > 0)
						{
							gl.glBindTexture(GL10.GL_TEXTURE_2D, lFont.lTexture.lHWTextureID);
							gl.glColor4f(lColor.fRed, lColor.fGreen, lColor.fBlue, lColor.fAlpha);

							int LC_ln = linesCount_ln();
							float y_uu = fPosition_uu.fY;
							for (int ln = LC_ln - fDisplayLines_ln; ln < LC_ln; ln++)
								y_uu += drawLine(gl, y_uu, ln);
						}
					}
				}
				else
					lFont.lTexture.linkToGL(gl);
			}
		}
		super.draw(gl);
	}

	private int linesCount_ln()
	{
		int result_ln = 1;
		if (fLength_ch > lString.length())
			fLength_ch = lString.length();
		for (int c = 0; c < fLength_ch; c++)
		{
			if (lString.charAt(c) == '\n')
				result_ln++;
		}
		return result_ln;
	}

	private float getWidth_uu(int flc_ch, int llc_ch)
	{
		float result_uu = 0;
		for (int c = flc_ch; (c < llc_ch) && (c < fLength_ch); c++)
		{
			int chr = lFont.getChar(lString.charAt(c));
			if (chr == -1)
			{
				result_uu += lFont.lSpaceWidth_tx * AngleRenderer.vHorizontalFactor_uu;
				continue;
			}
			result_uu += (lFont.lCharRight_tx[chr] + lFont.lSpace_uu) * AngleRenderer.vHorizontalFactor_uu;
		}
		return result_uu;
	}

	/**
	 * 
	 * @return String height in pixels
	 */

	public float getHeight_uu()
	{
		return lFont.lHeight_tx * AngleRenderer.vVerticalFactor_uu * linesCount_ln();
	}

	/**
	 * 
	 * @return String length
	 */
	public int getLength_ch()
	{
		if (lString != null)
			return lString.length();
		return 0;
	}
}
