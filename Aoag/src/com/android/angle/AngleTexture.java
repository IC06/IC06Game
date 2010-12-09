package com.android.angle;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.opengl.GLUtils;
import android.util.Log;

/**
 * Texture that uses AngleTextureEngine
 * 
 * @author Ivan Pajuelo
 * 
 */
public abstract class AngleTexture
{
	protected int lRefernces = 0;
	protected int lHWTextureID = -1;
	protected int lWidth_tx = 0;
	protected int lHeight_tx = 0;
	
	public void linkToGL (GL10 gl)
	{
		lHWTextureID=AngleTextureEngine.generateTexture();
		load(gl);
	}

	public void load(GL10 gl)
	{
		if ((gl!=null)&&(lHWTextureID>-1))
		{
			gl.glBindTexture(GL10.GL_TEXTURE_2D, lHWTextureID);
			int error = gl.glGetError();
			if (error != GL10.GL_NO_ERROR)
				Log.e("AngleTexture", "load Bind GLError: " + error);
	
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
	
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);
			error = gl.glGetError();
			if (error != GL10.GL_NO_ERROR)
				Log.e("AngleTexture", "load Parameterf GLError: " + error);
	
			gl.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE, GL10.GL_REPLACE);
			error = gl.glGetError();
			if (error != GL10.GL_NO_ERROR)
				Log.e("AngleTexture", "load Envf GLError: " + error);
	
	
			Bitmap bitmap = create();
	
			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
			error = gl.glGetError();
			if (error != GL10.GL_NO_ERROR)
				Log.e("AngleTexture", "load Image2D GLError: " + error);
	
			lWidth_tx = bitmap.getWidth();
			lHeight_tx = bitmap.getHeight();
	
			bitmap.recycle();
		}
	}

	public boolean bind(GL10 gl)
	{
		if (lHWTextureID > -1)
		{
			gl.glBindTexture(GL10.GL_TEXTURE_2D, lHWTextureID);
			return true;
		}
		else
			linkToGL(gl);
		return false;
	}

	public abstract Bitmap create();
};
