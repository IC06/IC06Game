package com.turlutu;


import com.android.angle.AngleCircleCollider;
import com.android.angle.AngleSegmentCollider;

public class BallCollider extends AngleCircleCollider {

	public BallCollider(float x, float y, float radius) {
		super(x, y, radius);
	}
	
	public boolean test(AngleSegmentCollider segmentCollider)
	{
		float CH = segmentCollider.closestDist(this);
		
		if (CH > mRadius)
			return false;
		else
		{
			float	Ax = segmentCollider.getmObject().mPosition.mX + segmentCollider.getmA().mX,
						Bx = segmentCollider.getmObject().mPosition.mX + segmentCollider.getmB().mX,
						Ay = segmentCollider.getmObject().mPosition.mY + segmentCollider.getmA().mY,
						By = segmentCollider.getmObject().mPosition.mY + segmentCollider.getmB().mY,
						X = mObject.mPosition.mX + mCenter.mX,
						Y = mObject.mPosition.mY + mCenter.mY + mRadius - 15;
			if (Y < Ay)
			{
				if (Ax < Bx && Ax < X && X < Bx)
					return true;
				else if (Ax < Bx && Bx <X && X < Ax)
					return true;
				/*else if ((X-Ax)*(X-Ax)+(Y-Ay)*(Y-Ay) < mRadius*mRadius)
					return true;
				else if ((X-Bx)*(X-Bx)+(Y-By)*(Y-By) < mRadius*mRadius)
					return true;*/
				else
					return false;
			}
			else
			{
				return false;
			}
		}
	}

}
