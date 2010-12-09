package com.android.angle;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLSurfaceView.Renderer;

public class AngleRenderer implements Renderer
{
	public static final boolean sUseHWBuffers = true;
	private static long lCTM;
	private static AngleRect lViewport_px=null; 
	private static AngleVector lUserExtent_uu=new AngleVector(0,0);
	private static AngleObject lRenderTree=null;

	public static AngleVector rViewportExtent_uu=new AngleVector(0,0);
	public static float vViewportHeight_px=0;
	public static float vHorizontalFactor_uu=0;
	public static float vVerticalFactor_uu=0;
	public static float vHorizontalFactor_px=0;
	public static float vVerticalFactor_px=0;

	public AngleRenderer(float width_uu, float height_uu)
	{
		lUserExtent_uu.set(width_uu, height_uu);
		rViewportExtent_uu.set(lUserExtent_uu);
	}
	
	public static void setRenderTree(AngleObject newRenderTree)
	{
		lRenderTree=newRenderTree;
	}
	
	public static boolean isRendering(AngleObject object)
	{
		return (object.getRoot()==lRenderTree);
	}

	public static AngleVector coordsUserToViewport(AngleVector coords_uu)
	{
		AngleVector result=new AngleVector(coords_uu);
		result.div(lUserExtent_uu);
		result.mul(lViewport_px.fSize);

		return result;
	}

	public static AngleVector coordsViewportToUser(AngleVector coords_px)
	{
		AngleVector result=new AngleVector(coords_px);
		result.div(lViewport_px.fSize);
		result.mul(lUserExtent_uu);

		return result;
	}

	public static AngleVector coordsScreenToUser(AngleVector coords_px)
	{
		AngleVector result=new AngleVector(coords_px);
		result.sub(lViewport_px.fPosition);
		result.div(lViewport_px.fSize);
		result.mul(lUserExtent_uu);

		return result;
	}

	private static float secondsElapsed()
	{
		float secondsElapsed = 0;
		long CTM = android.os.SystemClock.uptimeMillis();
		if (lCTM > 0)
			secondsElapsed = (CTM - lCTM) / 1000.f;
		lCTM = CTM;
		return secondsElapsed;
	}

	// @Override
	public void onDrawFrame(GL10 gl)
	{
      gl.glMatrixMode(GL10.GL_MODELVIEW);
      gl.glLoadIdentity();
		if (lRenderTree!=null)
		{
			AngleObject.processInQueue();
			lRenderTree.step(secondsElapsed());
			lRenderTree.draw(gl);
			AngleObject.processOutQueue();
		}
	}

	// @Override
	public void onSurfaceChanged(GL10 gl, int surfaceWidth, int surfaceHeight)
	{
		lViewport_px=new AngleRect(0,0,surfaceWidth,surfaceHeight);

		if (!lUserExtent_uu.isZero())
		{
			if ((lUserExtent_uu.fX / lUserExtent_uu.fY) > (surfaceWidth / surfaceHeight))
				lViewport_px.fSize.fY = (int) (lViewport_px.fSize.fX * lUserExtent_uu.fY / lUserExtent_uu.fX);
			else
				lViewport_px.fSize.fX = (int) (lViewport_px.fSize.fY * lUserExtent_uu.fX / lUserExtent_uu.fY);

			lViewport_px.fPosition.fX = surfaceWidth/2 - lViewport_px.fSize.fX/2;
			lViewport_px.fPosition.fY = surfaceHeight/2 - lViewport_px.fSize.fY/2;
		}
		gl.glViewport((int)lViewport_px.fPosition.fX, (int)lViewport_px.fPosition.fY, (int)lViewport_px.fSize.fX, (int)lViewport_px.fSize.fY);
		
		vViewportHeight_px=lViewport_px.fSize.fY;
		vHorizontalFactor_uu=lUserExtent_uu.fX/lViewport_px.fSize.fX;
		vVerticalFactor_uu=lUserExtent_uu.fY/lViewport_px.fSize.fY;
		vHorizontalFactor_px=lViewport_px.fSize.fX/lUserExtent_uu.fX;
		vVerticalFactor_px=lViewport_px.fSize.fY/lUserExtent_uu.fY;

		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrthof(0.0f, lViewport_px.fSize.fX, lViewport_px.fSize.fY, 0.0f, 0.0f, 1.0f);
		
		gl.glShadeModel(GL10.GL_FLAT);
		gl.glDisable(GL10.GL_DITHER);
		gl.glDisable(GL10.GL_MULTISAMPLE);

		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glEnable(GL10.GL_BLEND);

		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
		
      gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
      gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

      gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		gl.glColor4f(1, 1, 1, 1);
		gl.glClearColor(0, 0, 0, 1);
		
		if (lRenderTree!=null)
			lRenderTree.invalidateHardwareBuffers(gl);
	}

	// @Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config)
	{
		AngleTextureEngine.loadTextures(gl);
	}

}
