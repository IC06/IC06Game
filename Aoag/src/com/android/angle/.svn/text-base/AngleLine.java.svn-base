package com.alt90.angle2;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

public class AngleLine extends AngleObject
{
	private FloatBuffer lVertices;
	private AngleColor lColor;
	private AngleVector lA;
	private AngleVector lB;

	AngleLine (float x1_uu, float y1_uu, float x2_uu, float y2_uu, AngleColor color)
	{
		lColor=color;
		lVertices = ByteBuffer.allocateDirect(2 * 2 * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
		lA=new AngleVector(x1_uu,y1_uu);
		lB=new AngleVector(x2_uu,y2_uu);
	}
	
	@Override
	public void draw(GL10 gl)
	{
		lVertices.put(0, AngleRenderer.vHorizontalFactor_px*lA.fX);
		lVertices.put(1, AngleRenderer.vVerticalFactor_px*lA.fY);
		lVertices.put(2, AngleRenderer.vHorizontalFactor_px*lB.fX);
		lVertices.put(3, AngleRenderer.vVerticalFactor_px*lB.fY);

		gl.glDisable(GL10.GL_TEXTURE_2D);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

		gl.glPushMatrix();
		gl.glLoadIdentity();
		gl.glColor4f(lColor.fRed, lColor.fGreen, lColor.fBlue, lColor.fAlpha);
		gl.glTranslatef(0, 0, 0.0f);

		gl.glVertexPointer(2, GL10.GL_FLOAT, 0, lVertices);
		gl.glDrawArrays(GL10.GL_LINES, 0, 2);
		gl.glPopMatrix();

		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glEnable(GL10.GL_TEXTURE_2D);
	}

}
