package com.android.angle;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

/**
 * circular collider
 * 
 * @author Ivan Pajuelo
 * 
 */
public class AngleCircleCollider
{
	protected AnglePhysicObject mObject;
	protected AngleVector mCenter;
	protected float mRadius; 
	protected float mNormal;

	public AngleCircleCollider(float x, float y, float radius)
	{
		mCenter = new AngleVector(x, y);
		mRadius = radius;
	}

	/**
	 * Edited by Thomas
	 * Note by Thomas : si on a pas besoin de faire rebondir, on a pas besoin de calculer la normale,
	 * de même on pourrait supprimer la racin carré qui prend du CPU
	 */
	public boolean test(AngleCircleCollider otherCollider)
	{
		float dX = (otherCollider.mObject.mPosition.mX + otherCollider.mCenter.mX) - (mObject.mPosition.mX + mCenter.mX);
		float dY = (otherCollider.mObject.mPosition.mY + otherCollider.mCenter.mY) - (mObject.mPosition.mY + mCenter.mY);
		float dist = (float) Math.sqrt(dX * dX + dY * dY); // edited
		//float dist2 = dX * dX + dY * dY;
		// The normal in a circle is the direction to the center of the other
		// collider
		if (dX > 0)
			otherCollider.mNormal = (float) Math.acos(dY / dist); // edited
			//mNormal = (float) Math.atan(dY / dX);
		else
			otherCollider.mNormal = (float) (Math.PI * 2 - Math.acos(dY / dist)); // edited
			//mNormal = (float) (Math.PI + Math.atan(dY / dX));

		return (dist < mRadius + otherCollider.mRadius); // edited
		//return (dist2 < (mRadius + otherCollider.mRadius)*(mRadius + otherCollider.mRadius));
	}

	/**
	 * Modified by Thomas
	 * @param segmentCollider
	 * @return true if collision
	 */
	public boolean test(AngleSegmentCollider segmentCollider)
	{
		float CH = segmentCollider.closestDist(this);
		
		if (CH > mRadius)
			return false;
		else
		{
			float 	Ax = segmentCollider.mObject.mPosition.mX + segmentCollider.mA.mX, 
						Ay = segmentCollider.mObject.mPosition.mY + segmentCollider.mA.mY,
						Bx = segmentCollider.mObject.mPosition.mX + segmentCollider.mB.mX,
						By = segmentCollider.mObject.mPosition.mY +  segmentCollider.mB.mY,
						Cx = mObject.mPosition.mX + mCenter.mX,
						Cy = mObject.mPosition.mY + mCenter.mY;
			double L2 =  (Bx-Ax)* (Bx-Ax)+(By-Ay)*(By-Ay); // AB²
			double AC2 = (Cx-Ax)* (Cx-Ax)+(Cy-Ay)*(Cy-Ay); // AC²
			double BC2 = (Cx-Bx)* (Cx-Bx)+(Cy-By)*(Cy-By); // BC²
			
			double AH2 = AC2 - CH*CH;
			double BH2 = BC2 - CH*CH;

			
			if ((AH2 + BH2) < (L2+1))
			{
				mNormal = segmentCollider.mNormal;
				return true;
			}
			else if (BC2 < mRadius*mRadius)
			{
				float dX = Bx-Cx, dY = By-Cy;
				float dist = (float) Math.sqrt(BC2);
				if (dX > 0)
					mNormal = (float) Math.acos(dY / dist); // edited
					//mNormal = (float) Math.atan(dY / dX);
				else
					mNormal = (float) (Math.PI * 2 - Math.acos(dY / dist)); // edited
				return true;
			}
			else if (AC2 < mRadius*mRadius)
			{
				float dX =Ax-Cx, dY = Ay-Cy;
				float dist = (float) Math.sqrt(AC2);
				if (dX > 0)
					mNormal = (float) Math.acos(dY / dist); // edited
					//mNormal = (float) Math.atan(dY / dX);
				else
					mNormal = (float) (Math.PI * 2 - Math.acos(dY / dist)); // edited
				return true;
			}
			else
			{
				return false;
			}
			
			
		}
	}

	protected void draw(GL10 gl,float R,float V, float B)
	{
		final int segments = 20;

		gl.glDisable(GL11.GL_TEXTURE_2D);
		gl.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);

		gl.glPushMatrix();
		gl.glLoadIdentity();
		gl.glColor4f(R, V, B, 1f);
		gl.glTranslatef(mObject.mPosition.mX + mCenter.mX, mObject.mPosition.mY + mCenter.mY, 0.0f);
		FloatBuffer vertices;
		vertices = ByteBuffer.allocateDirect(segments * 2 * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();

		int count = 0;
		for (float i = 0; i < Math.PI * 2; i += ((Math.PI * 2) / segments))
		{
			vertices.put(count++, (float) (Math.cos(i) * mRadius));
			vertices.put(count++, (float) (Math.sin(i) * mRadius));
		}
		gl.glVertexPointer(2, GL11.GL_FLOAT, 0, vertices);
		gl.glDrawArrays(GL11.GL_LINE_LOOP, 0, segments);
		gl.glPopMatrix();

		gl.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
		gl.glEnable(GL11.GL_TEXTURE_2D);
	}


	/**
	 * @author thomas
	 * @return the mObject
	 */
	public AnglePhysicObject getmObject() {
		return mObject;
	}
	

	/**
	 * @author thomas
	 * @return the mObject
	 */
	public AngleVector getmCenter() {
		return mCenter;
	}

}
