package com.android.angle;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.microedition.khronos.opengles.GL10;

import android.util.Log;

/**
 * Texture engine
 * 
 * @author Ivan Pajuelo
 * 
 */
public class AngleTextureEngine
{
	public static AngleTextureEngine uInstance=null;
	private static CopyOnWriteArrayList<AngleTexture> mTexturesX = new CopyOnWriteArrayList<AngleTexture>();
	private static GL10 mGl;

	/**
	 * Not called
	 * @param gl
	 */
	public static void destroy(GL10 gl)
	{
		mGl = gl;
		onContextLost();
	}

	/**
	 * Not called
	 * @param gl
	 */
	public static void onContextLost()
	{
		if (mGl != null)
		{
			int d = 0;
			int[] textures = new int[mTexturesX.size()];
			Iterator<AngleTexture> it = mTexturesX.iterator();
			while (it.hasNext())
				textures[d++] = it.next().lHWTextureID;

			mGl.glDeleteTextures(d, textures, 0);
			mTexturesX.clear();
		}
	}

	public static void loadTextures(GL10 gl)
	{
		mGl = gl;
		if (mGl != null)
		{
			Iterator<AngleTexture> it = mTexturesX.iterator();
			while (it.hasNext())
				it.next().linkToGL(mGl);
			Log.v("TextureEngine", "loadTexture");
		}
	}

	public static AngleTexture createTextureFromFont(AngleFont font)
	{
		AngleTexture tex = null;
		Iterator<AngleTexture> it = mTexturesX.iterator();
		while (it.hasNext())
		{
			tex = it.next();
			if (tex instanceof AngleFontTexture)
			{
				// Texture already exists
				if (((AngleFontTexture) tex).fontIs(font))
				{
					tex.lRefernces++;
					return tex;
				}
			}
		}

		tex = new AngleFontTexture(font);
		mTexturesX.add(tex);
		if (mGl != null)
			tex.linkToGL(mGl);
		return tex;
	}
	
	public static AngleTexture createTextureFromResourceId(int resourceId)
	{
		AngleTexture tex = null;
		Iterator<AngleTexture> it = mTexturesX.iterator();
		while (it.hasNext())
		{
			tex = it.next();
			if (tex instanceof AngleResourceTexture)
			{
				// Texture already exists
				if (((AngleResourceTexture) tex).fResourceID == resourceId)
				{
					tex.lRefernces++;
					return tex;
				}
			}
		}

		tex = new AngleResourceTexture(resourceId);
		mTexturesX.add(tex);
		if (mGl != null)
			tex.linkToGL(mGl);
		return tex;
	}

	public static int generateTexture()
	{
		if (mGl != null)
		{
			int[] textureIDs = new int[1];

			mGl.glGenTextures(1, textureIDs, 0);

			int error = mGl.glGetError();
			if (error != GL10.GL_NO_ERROR)
				Log.e("TextureEngine", "generateTexture GLError: " + error);
			else
			{
				Log.v("TextureEngine", "generateTexture id="+textureIDs[0]);
				return textureIDs[0];
			}
		}
		return -1;
	}

	public static void deleteTexture(AngleTexture tex)
	{
		if (mTexturesX.indexOf(tex) > -1)
		{
			tex.lRefernces--;
			if (tex.lRefernces < 0)
				mTexturesX.remove(tex);
		}
		if (tex.lHWTextureID > -1)
		{
			int[] texture = new int[1];
			texture[0] = tex.lHWTextureID;
			if (mGl != null)
				mGl.glDeleteTextures(1, texture, 0);
		}
	}
}