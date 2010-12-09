package com.alt90.angle2;

public class AngleVector
{
	public float fX;
	public float fY;

	public AngleVector()
	{
		fX = 0.0f;
		fY = 0.0f;
	}

	public AngleVector(float x, float y)
	{
		fX = x;
		fY = y;
	}

	public AngleVector(AngleVector src)
	{
		fX = src.fX;
		fY = src.fY;
	}

	public void set(AngleVector src)
	{
		fX = src.fX;
		fY = src.fY;
	}

	public void set(float x, float y)
	{
		fX = x;
		fY = y;
	}

	/*
	 * 
	 * public float length() { return (float) Math.sqrt((mX * mX) + (mY * mY)); }
	 * 
	 * public void normalize() { float len = length();
	 * 
	 * if (len != 0.0f) { mX /= len; mY /= len; } else { mX = 0.0f; mY = 0.0f; }
	 * }
	 */
	public void add(AngleVector vector)
	{
		fX += vector.fX;
		fY += vector.fY;
	}

	/*
	 * public void add(float x, float y) { x += x; y += y; }
	 */
	public void sub(AngleVector vector)
	{
		fX -= vector.fX;
		fY -= vector.fY;
	}

	public void subAt(AngleVector vector)
	{
		fX = vector.fX - fX;
		fY = vector.fY - fY;
	}

	public void mul(AngleVector vector)
	{
		fX *= vector.fX;
		fY *= vector.fY;
	}

	public void div(AngleVector vector)
	{
		fX /= vector.fX;
		fY /= vector.fY;
	}

	/*
	 * public void sub(float x, float y) { x -= x; y -= y; }
	 * 
	 * public void mul(AngleVector vector) { mX *= vector.mX; mY *= vector.mY; }
	 * 
	 * public void mul(float x, float y) { x += x; y += y; }
	 */
	public void mul(float scalar)
	{
		fX *= scalar;
		fY *= scalar;
	}

	public void div(float scalar)
	{
		fX /= scalar;
		fY /= scalar;
	}

	public float dot(AngleVector vector)
	{
		return (fX * vector.fX) + (fY * vector.fY);
	}

	public void rotate(float dAlfa)
	{
		float nCos = (float) Math.cos(dAlfa);
		float nSin = (float) Math.sin(dAlfa);

		float iX = fX * nCos - fY * nSin;
		float iY = fY * nCos + fX * nSin;

		fX = iX;
		fX = iY;
	}

	public boolean isZero()
	{
		return ((fX==0)&&(fY==0));
	}
}
