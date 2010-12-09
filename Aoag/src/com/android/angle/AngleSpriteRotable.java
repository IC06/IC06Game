package com.android.angle;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

/**
 * Sprite with rotating capabilities. Uses hardware buffers if available
 * 
 * @author Ivan Pajuelo
 * 
 */
public class AngleSpriteRotable extends AngleAbstractSprite
{
	public static final char[] sIndexValues = new char[] { 0, 1, 2, 1, 2, 3 };
	public static int sIndexBufferIndex = -1;
	protected float[] lTexCoordValues;
	protected int lTextureCoordBufferIndex;
	private boolean isFrameInvalid;
	public float[] fVertexValues;
	public int fVertBufferIndex;
	public float fRotation;

	public static void generateIndexBuffer (GL10 gl)
	{
		int[] hwBuffers=new int[1];
		((GL11)gl).glGenBuffers(1, hwBuffers, 0);

		// Allocate and fill the index buffer.
		sIndexBufferIndex = hwBuffers[0];
		((GL11)gl).glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, sIndexBufferIndex);
		((GL11)gl).glBufferData(GL11.GL_ELEMENT_ARRAY_BUFFER, 6 * 2, CharBuffer.wrap(sIndexValues), GL11.GL_STATIC_DRAW);
		((GL11)gl).glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, 0);
	}

	public static void releaseIndexBuffer (GL10 gl)
	{
		int[] hwBuffers = new int[1];
		hwBuffers[0]=sIndexBufferIndex;
		((GL11) gl).glDeleteBuffers(1, hwBuffers, 0);
		sIndexBufferIndex=-1;
	}
	/**
	 * 
	 * @param layout
	 *           AngleSpriteLayout
	 */
	public AngleSpriteRotable(AngleSpriteLayout layout)
	{
		super(layout);
		doInit(0, 0, 1);
	}

	/**
	 * 
	 * @param x_uu
	 *           Position
	 * @param y_uu
	 *           Position
	 * @param layout
	 *           AngleSpriteLayout
	 */
	public AngleSpriteRotable(float x_uu, float y_uu, AngleSpriteLayout layout)
	{
		super(layout);
		doInit(x_uu, y_uu, 1);
	}

	/**
	 * 
	 * @param x_uu
	 *           Position
	 * @param y_uu
	 *           Position
	 * @param alpha
	 *           Normalized alpha channel value
	 * @param layout
	 *           AngleSpriteLayout
	 */
	public AngleSpriteRotable(float x_uu, float y_uu, float alpha, AngleSpriteLayout layout)
	{
		super(layout);
		doInit(x_uu, y_uu, alpha);
	}

	private void doInit(float x_uu, float y_uu, float alpha)
	{
		fRotation = 0;
		lTexCoordValues = new float[8];
		lTextureCoordBufferIndex = -1;
		fVertexValues = new float[8];
		fVertBufferIndex = -1;
		setLayout(lLayout);
		fPosition_uu.set(x_uu, y_uu);
		lColor.fAlpha = alpha;
		isFrameInvalid = true;
	}

	@Override
	public void setLayout(AngleSpriteLayout layout)
	{
		super.setLayout(layout);
		setFrame(0);
	}

	@Override
	public void setFrame(int frame)
	{
		if (lLayout != null)
		{
			if (frame < lLayout.lFrameCount)
			{
				lFrame = frame;
				float W_tx = lLayout.lTexture.lWidth_tx;
				float H_tx = lLayout.lTexture.lHeight_tx;
				if ((W_tx > 0) & (H_tx > 0))
				{
					float frameLeft_tx = (lFrame % lLayout.lFrameColumns) * lLayout.lCrop_tx.fSize.fX;
					float frameTop_tx = (lFrame / lLayout.lFrameColumns) * lLayout.lCrop_tx.fSize.fY;

					float left_tx = (lLayout.lCrop_tx.fPosition.fX + frameLeft_tx) / W_tx;
					float bottom_tx = (lLayout.lCrop_tx.fPosition.fY + lLayout.lCrop_tx.fSize.fY + frameTop_tx) / H_tx;
					float right_tx = (lLayout.lCrop_tx.fPosition.fX + lLayout.lCrop_tx.fSize.fX + frameLeft_tx) / W_tx;
					float top_tx = (lLayout.lCrop_tx.fPosition.fY + frameTop_tx) / H_tx;

					if (fFlipHorizontal)
					{
						lTexCoordValues[0] = right_tx;
						lTexCoordValues[2] = left_tx;
						lTexCoordValues[4] = right_tx;
						lTexCoordValues[6] = left_tx;
					}
					else
					{
						lTexCoordValues[0] = left_tx;
						lTexCoordValues[2] = right_tx;
						lTexCoordValues[4] = left_tx;
						lTexCoordValues[6] = right_tx;
					}
					if (fFlipVertical)
					{
						lTexCoordValues[1] = top_tx;
						lTexCoordValues[3] = top_tx;
						lTexCoordValues[5] = bottom_tx;
						lTexCoordValues[7] = bottom_tx;
					}
					else
					{
						lTexCoordValues[1] = bottom_tx;
						lTexCoordValues[3] = bottom_tx;
						lTexCoordValues[5] = top_tx;
						lTexCoordValues[7] = top_tx;
					}

					lLayout.fillVertexValues(lFrame, fVertexValues);
					isFrameInvalid = false;
				}
				lTextureCoordBufferIndex = -1;
				fVertBufferIndex = -1;
			}
		}
	}

	@Override
	public void invalidateHardwareBuffers(GL10 gl)
	{
		int[] hwBuffers = new int[2];
		((GL11) gl).glGenBuffers(2, hwBuffers, 0);

		// Allocate and fill the texture buffer.
		lTextureCoordBufferIndex = hwBuffers[0];
		((GL11) gl).glBindBuffer(GL11.GL_ARRAY_BUFFER, lTextureCoordBufferIndex);
		((GL11) gl).glBufferData(GL11.GL_ARRAY_BUFFER, 8 * 4, FloatBuffer.wrap(lTexCoordValues), GL11.GL_STATIC_DRAW);
		fVertBufferIndex = hwBuffers[1];
		((GL11) gl).glBindBuffer(GL11.GL_ARRAY_BUFFER, fVertBufferIndex);
		((GL11) gl).glBufferData(GL11.GL_ARRAY_BUFFER, 8 * 4, FloatBuffer.wrap(fVertexValues), GL11.GL_STATIC_DRAW);
		((GL11) gl).glBindBuffer(GL11.GL_ARRAY_BUFFER, 0);

		super.invalidateHardwareBuffers(gl);

	}

	@Override
	public void releaseHardwareBuffers(GL10 gl)
	{
		int[] hwBuffers = new int[2];
		hwBuffers[0] = lTextureCoordBufferIndex;
		hwBuffers[1] = fVertBufferIndex;
		if (gl != null)
			((GL11) gl).glDeleteBuffers(2, hwBuffers, 0);
		lTextureCoordBufferIndex = -1;
		fVertBufferIndex = -1;
	}

	@Override
	public void blockDraw(GL10 gl)
	{
		if (isFrameInvalid)
			setFrame(lFrame);

		gl.glPushMatrix();
		gl.glLoadIdentity();

		AngleVector pos_px=AngleRenderer.coordsUserToViewport(fPosition_uu);

		gl.glTranslatef(pos_px.fX, pos_px.fY, 0);
		if (fRotation != 0)
			gl.glRotatef(-fRotation, 0, 0, 1);
		if ((fScale.fX != 1) || (fScale.fY != 1))
			gl.glScalef(fScale.fX, fScale.fY, 1);

		gl.glBindTexture(GL10.GL_TEXTURE_2D, lLayout.lTexture.lHWTextureID);
		gl.glColor4f(lColor.fRed, lColor.fGreen, lColor.fBlue, lColor.fAlpha);

		if (AngleRenderer.sUseHWBuffers)
		{
			if (sIndexBufferIndex<0)
				generateIndexBuffer (gl);
			if ((lTextureCoordBufferIndex < 0) || (fVertBufferIndex < 0))
				invalidateHardwareBuffers(gl);

			((GL11) gl).glBindBuffer(GL11.GL_ARRAY_BUFFER, fVertBufferIndex);
			((GL11) gl).glVertexPointer(2, GL10.GL_FLOAT, 0, 0);

			((GL11) gl).glBindBuffer(GL11.GL_ARRAY_BUFFER, lTextureCoordBufferIndex);
			((GL11) gl).glTexCoordPointer(2, GL10.GL_FLOAT, 0, 0);

			((GL11) gl).glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, sIndexBufferIndex);
			((GL11) gl).glDrawElements(GL10.GL_TRIANGLES, 6, GL10.GL_UNSIGNED_SHORT, 0);

			((GL11) gl).glBindBuffer(GL11.GL_ARRAY_BUFFER, 0);
			((GL11) gl).glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, 0);
		}
		else
		{
			CharBuffer mIndexBuffer;
			FloatBuffer mVertexBuffer;
			FloatBuffer mTexCoordBuffer;

			mIndexBuffer = ByteBuffer.allocateDirect(sIndexValues.length * 2).order(ByteOrder.nativeOrder()).asCharBuffer();
			mVertexBuffer = ByteBuffer.allocateDirect(fVertexValues.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
			mTexCoordBuffer = ByteBuffer.allocateDirect(lTexCoordValues.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();

			for (int i = 0; i < sIndexValues.length; ++i)
				mIndexBuffer.put(i, sIndexValues[i]);
			for (int i = 0; i < fVertexValues.length; ++i)
				mVertexBuffer.put(i, fVertexValues[i]);
			for (int i = 0; i < lTexCoordValues.length; ++i)
				mTexCoordBuffer.put(i, lTexCoordValues[i]);

			gl.glVertexPointer(2, GL10.GL_FLOAT, 0, mVertexBuffer);
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTexCoordBuffer);
			gl.glDrawElements(GL10.GL_TRIANGLES, sIndexValues.length, GL10.GL_UNSIGNED_SHORT, mIndexBuffer);

		}

		gl.glPopMatrix();
	}

}
