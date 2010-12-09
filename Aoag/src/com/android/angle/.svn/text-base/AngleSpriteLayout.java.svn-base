package com.alt90.angle2;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

/**
 * 
 * @author Ivan Pajuelo
 * 
 */
public class AngleSpriteLayout
{
	protected AngleTexture lTexture;
	protected AngleRect lCrop_tx;	//Texels
	protected int lFrameCount;		
	protected int lFrameColumns;
	protected int lFrame;

	protected AngleVector lDimensions_uu; 	//User units
	protected AngleVector[] lPivot_uu;		//User units

	/**
	 * 
	 * @param width_uu
	 *           Width in pixels
	 * @param height_uu
	 *           Height in pixels
	 * @param resourceId
	 *           Resource bitmap
	 * @param cropLeft_tx
	 *           Most left pixel in texture
	 * @param cropTop_tx
	 *           Most top pixel in texture
	 * @param cropWidth_tx
	 *           Width of the cropping rectangle in texture
	 * @param cropHeight_tx
	 *           Height of the cropping rectangle in texture
	 */
	public AngleSpriteLayout(int width_uu, int height_uu, int resourceId, int cropLeft_tx, int cropTop_tx, int cropWidth_tx,
			int cropHeight_tx)
	{
		doInit(width_uu, height_uu, resourceId, cropLeft_tx, cropTop_tx, cropWidth_tx, cropHeight_tx, 1, 1);
	}

	/**
	 * 
	 * @param width_uu
	 *           Width in pixels
	 * @param height_uu
	 *           Height in pixels
	 * @param resourceId
	 *           Resource bitmap
	 * @param cropLeft_tx
	 *           Most left pixel in texture
	 * @param cropTop_tx
	 *           Most top pixel in texture
	 * @param cropWidth_tx
	 *           Width of the cropping rectangle in texture
	 * @param cropHeight_tx
	 *           Height of the cropping rectangle in texture
	 * @param frameCount
	 *           Number of frames in animation
	 * @param frameColumns
	 *           Number of frames horizontally in texture
	 */
	public AngleSpriteLayout(int width_uu, int height_uu, int resourceId, int cropLeft_tx, int cropTop_tx, int cropWidth_tx,
			int cropHeight_tx, int frameCount, int frameColumns)
	{
		doInit(width_uu, height_uu, resourceId, cropLeft_tx, cropTop_tx, cropWidth_tx, cropHeight_tx, frameCount, frameColumns);
	}

	public AngleSpriteLayout(int width_uu, int height_uu, int resourceId)
	{
		doInit(width_uu, height_uu, resourceId, 0, 0, 0, 0, 1, 1);
	}

	public AngleSpriteLayout(int resourceId)
	{
		doInit(0, 0, resourceId, 0, 0, 0, 0, 1, 1);
	}

	private void doInit(float width_uu, float height_uu, int resourceId, int cropLeft_tx, int cropTop_tx, int cropWidth_tx, int cropHeight_tx, int frameCount,
			int frameColumns)
	{
		lTexture = AngleTextureEngine.createTextureFromResourceId(resourceId);
		lCrop_tx=new AngleRect(cropLeft_tx,cropTop_tx,cropWidth_tx,cropHeight_tx);
		lDimensions_uu=new AngleVector(width_uu,height_uu);
		if ((lCrop_tx.fSize.fX==0)||(lCrop_tx.fSize.fY==0))
		{
			InputStream is = AngleActivity.uInstance.getResources().openRawResource(resourceId);			
			try
			{
				Bitmap bitmap = BitmapFactory.decodeStream(is, null, new BitmapFactory.Options());
				lCrop_tx.fSize.fX = bitmap.getWidth();
				lCrop_tx.fSize.fY = bitmap.getHeight();
				bitmap.recycle();
			}
			finally
			{
				try
				{
					is.close();
				}
				catch (IOException e)
				{
					Log.e("AngleTextureEngine", "loadTexture::InputStream.close error: " + e.getMessage());
				}
			}
		}
		if (lDimensions_uu.fX==0)
			lDimensions_uu.fX = lCrop_tx.fSize.fX;
		if (lDimensions_uu.fY==0)
			lDimensions_uu.fY = lCrop_tx.fSize.fY;
		lFrameCount = frameCount;
		lFrameColumns = frameColumns;

		lPivot_uu=new AngleVector[lFrameCount];
		//Set all frame pivots at center point by default
		for (int f=0;f<lFrameCount;f++)
			lPivot_uu[f]=new AngleVector(lDimensions_uu.fX / 2, lDimensions_uu.fY / 2);

	}

	/**
	 * Set pivot point of one frame
	 * @param frame
	 * @param x
	 * @param y
	 */
	public void setPivot_uu(int frame, float x, float y)
	{
		if (frame<lFrameCount)
			lPivot_uu[frame].set(x,y);
	}

	/**
	 * Set pivot point of all frames
	 * @param x
	 * @param y
	 */
	public void setPivot_uu(float x, float y) //Texels
	{
		for (int f=0;f<lFrameCount;f++)
			lPivot_uu[f].set(x,y);
	}

	/**
	 * get pivot point of one frame
	 * 
	 * @param frame
	 * @return pivot point 
	 */
	public AngleVector getPivot_px(int frame) //User units
	{
		if (frame<lFrameCount)
			return AngleRenderer.coordsUserToViewport(lPivot_uu[frame]);
		return null;
	}

	public boolean fillTextureValues(int frame, int[] lTextureIV_tx, boolean flipHorizontal, boolean flipVertical)
	{
		if (frame<lFrameCount)
		{
			if (flipHorizontal)
			{
				lTextureIV_tx[0] = (int) ((lCrop_tx.fPosition.fX + lCrop_tx.fSize.fX) + ((lFrame % lFrameColumns) * lCrop_tx.fSize.fX));// Ucr
				lTextureIV_tx[2] = (int) -lCrop_tx.fSize.fX; // Wcr
			}
			else
			{
				lTextureIV_tx[0] = (int) (lCrop_tx.fPosition.fX + ((lFrame % lFrameColumns) * lCrop_tx.fSize.fX));// Ucr
				lTextureIV_tx[2] = (int) lCrop_tx.fSize.fX; // Wcr
			}

			if (flipVertical)
			{
				lTextureIV_tx[1] = (int) (lCrop_tx.fPosition.fY + ((lFrame / lFrameColumns) * lCrop_tx.fSize.fY));// Vcr
				lTextureIV_tx[3] = (int) lCrop_tx.fSize.fY; // Hcr
			}
			else
			{
				lTextureIV_tx[1] = (int) ((lCrop_tx.fPosition.fY + lCrop_tx.fSize.fY) + ((lFrame / lFrameColumns) * lCrop_tx.fSize.fY));// Vcr
				lTextureIV_tx[3] = (int) -lCrop_tx.fSize.fY; // Hcr
			}
			return true;
		}
		return false;
	}
	public boolean fillVertexValues(int frame, float[] vertexValues)
	{
		if (frame<lFrameCount)
		{
			AngleVector size_px=AngleRenderer.coordsUserToViewport(lDimensions_uu);
			AngleVector pivot_px=AngleRenderer.coordsUserToViewport(lPivot_uu[frame]);
			vertexValues[0] = -pivot_px.fX;
			vertexValues[1] = size_px.fY - pivot_px.fY;
			vertexValues[2] = size_px.fX - pivot_px.fX;
			vertexValues[3] = size_px.fY - pivot_px.fY;
			vertexValues[4] = -pivot_px.fX;
			vertexValues[5] = -pivot_px.fY;
			vertexValues[6] = size_px.fX - pivot_px.fX;
			vertexValues[7] = -pivot_px.fY;
			return true;
		}
		return false;
	}
	
	public void onDestroy(GL10 gl)
	{
		AngleTextureEngine.deleteTexture(lTexture);
	}
	
	/**
	 * Change the content of the texture
	 * @param resourceId Durable
	 */
	public void changeTexture (int resourceId)
	{
		AngleTextureEngine.deleteTexture(lTexture);
		lTexture = AngleTextureEngine.createTextureFromResourceId(resourceId);
	}

	/**
	 * Change the entire layout
	 * @param width_uu in viewport units
	 * @param height_uu in viewport units
	 * @param resourceId
	 */
	public void changeLayout(float width_uu, float height_uu, int resourceId)
	{
		changeLayout(width_uu, height_uu, resourceId, 0, 0, 0, 0, 1, 1);
	}
	
	/**
	 * Change the entire layout
	 * @param width_uu in viewport units
	 * @param height_uu in viewport units
	 * @param resourceId
	 * @param cropLeft_tx in texels
	 * @param cropTop_tx in texels
	 * @param cropWidth_tx in texels
	 * @param cropHeight_tx in texels
	 */
	public void changeLayout(float width_uu, float height_uu, int resourceId, int cropLeft_tx, int cropTop_tx, int cropWidth_tx, int cropHeight_tx)
	{
		changeLayout(width_uu, height_uu, resourceId, cropLeft_tx, cropTop_tx, cropWidth_tx, cropHeight_tx, 1, 1);
	}
	
	/**
	 * Change the entire layout
	 * @param width_uu in viewport units
	 * @param height_uu in viewport units
	 * @param resourceId
	 * @param cropLeft_tx in texels
	 * @param cropTop_tx in texels
	 * @param cropWidth_tx in texels
	 * @param cropHeight_tx in texels
	 * @param frameCount
	 * @param frameColumns
	 */
	public void changeLayout(float width_uu, float height_uu, int resourceId, int cropLeft_tx, int cropTop_tx, int cropWidth_tx, int cropHeight_tx, int frameCount, int frameColumns)
	{
		lDimensions_uu.fX = width_uu;
		lDimensions_uu.fY = height_uu;
		lCrop_tx.fPosition.fX = cropLeft_tx;
		lCrop_tx.fPosition.fY = cropTop_tx;
		lCrop_tx.fSize.fX = cropWidth_tx;
		lCrop_tx.fSize.fY = cropHeight_tx;
		lFrameCount = frameCount;
		lFrameColumns = frameColumns;
		changeTexture (resourceId);
	}

	public boolean bindTexture(GL10 gl)
	{
		if (lTexture != null)
			return lTexture.bind(gl);
		return false;
	}
}
