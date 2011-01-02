package com.exitjump;


import com.android.angle.AngleCircleCollider;
import com.android.angle.AngleSegmentCollider;
import com.android.angle.AngleVector;

/**
 * @author thomas
 *
 */
public class PlateformeCollider extends AngleSegmentCollider {

	public PlateformeCollider(float x1, float x2, float y)
	{
		super();
		mA = new AngleVector(x1,y);
		mB = new AngleVector(x2,y);
	}

	
	public float closestDist(AngleCircleCollider other)
	{
		return Math.abs(mA.mY + mObject.mPosition.mY - other.getmObject().mPosition.mY - other.getmCenter().mY);
	}
	
}
