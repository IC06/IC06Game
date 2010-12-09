package com.android.angle;

import javax.microedition.khronos.opengles.GL10;

public class AngleObject
{
	private static final int sDefaultChildren = 2;
	private static final int sInQueueSize = 10;
	private static final int sOutQueueSize = 10;
	protected static AngleObject[]lInQueue=new AngleObject[sInQueueSize];
	protected static int lInQueueCount=0;
	protected static AngleObject[]lOutQueue=new AngleObject[sOutQueueSize];
	protected static int lOutQueueCount=0;
	public int fTag;
	protected int lChildrenCount;
	protected int lChildrenSize;
	protected AngleObject[]lChildren;
	protected AngleObject lParent;
	protected AngleObject lFutureParent;

	/**
	 * Create a new AngleObject with the default amount of children
	 */
	public AngleObject ()
	{
		Create(sDefaultChildren);
	}

	/**
	 * Create a new AngleObject with a defined amount of children
	 * @param maxChildren amount of children
	 */
	public AngleObject (int maxChildren)
	{
		Create(maxChildren);
	}

	/**
	 * Get the root object of the object's tree
	 * @return root object
	 */
	public AngleObject getRoot()
	{
		AngleObject result;
		if (lParent!=null)
			result=lParent.getRoot();
		else
			result=this;
		return result;
	}
	
	/**
	 * 
	 * @param object Object to add
	 */
	public AngleObject addObject(AngleObject object)
	{
		if (object.lParent!=this)
		{
			//Current object is rendering enqueue the new child addition
			if (AngleRenderer.isRendering(this))
			{
				object.lFutureParent=this;
				if (object.lParent!=null)
					lOutQueue[lOutQueueCount++]=object;
				lInQueue[lInQueueCount++]=object;
			}
			else
				addObjectNow(object);
		}
		return object;
	}

	/**
	 * Remove this object from its parent if it has one
	 */
	public void killMe()
	{
		if (lParent!=null)
			lParent.removeObject(this);
	}

	/**
	 * Remove (object) from the current childs
	 * @param object Object to remove
	 */
	public void removeObject(AngleObject object)
	{
		if (object.lParent==this)
		{
			//Current object is rendering enqueue the child for remove
			if (AngleRenderer.isRendering(this))
				lOutQueue[lOutQueueCount++]=object;
			else
				removeObjectNow(object);
		}
	}

	/**
	 * Called every frame for logic
	 * @param secondsElapsed Seconds elapsed since last frame
	 */
	protected void step(float secondsElapsed)
	{
		for (int t=0;t<lChildrenCount;t++)
			lChildren[t].step(secondsElapsed);
	}

	/**
	 * Called every frame for drawing 
	 * @param gl OpenGL ES object
	 */
	public void draw(GL10 gl)
	{
		for (int t=0;t<lChildrenCount;t++)
			lChildren[t].draw(gl);
	}

	/**
	 * Called when this object dies. User overridable 
	 */
	protected void onDie()
	{
	}

	/**
	 * Process the in queue. Called by the renderer
	 */
	public static void processInQueue()
	{
		while (lInQueueCount>0)
		{
			lInQueueCount--;
			lInQueue[lInQueueCount].lFutureParent.addObjectNow(lInQueue[lInQueueCount]);
		}
	}

	/**
	 * Process the out queue. Called by the renderer
	 */
	public static void processOutQueue()
	{
		while (lOutQueueCount>0)
		{
			lOutQueueCount--;
			lOutQueue[lOutQueueCount].lParent.removeObjectNow(lOutQueue[lOutQueueCount]);
		}
	}

	private void Create(int maxChildren)
	{
		fTag=0;
		lChildrenCount=0;
		lChildrenSize=maxChildren;
		lChildren=new AngleObject[lChildrenSize];
		lParent=null;
	}
	protected void addObjectNow(AngleObject object)
	{
		object.lFutureParent=null;
		if (object.lParent!=null)
			object.lParent.removeObjectNow(object);
		if (lChildrenCount<lChildrenSize)
			lChildren[lChildrenCount++]=object;
	}

	protected void removeChildNow(int idx)
	{
		lChildren[idx].onDie();
		lChildrenCount--;
		for (int d = idx; d < lChildrenCount; d++)
			lChildren[d] = lChildren[d + 1];
		lChildren[lChildrenCount] = null;
		lChildren[idx].lParent=null;
	}

	protected void removeObjectNow(AngleObject object)
	{
		for (int t=0;t<lChildrenCount;t++)
		{
			if (lChildren[t]==object)
				removeChildNow(t);
		}
	}

	public void invalidateHardwareBuffers(GL10 gl)
	{
		for (int t=0;t<lChildrenCount;t++)
			lChildren[t].invalidateHardwareBuffers(gl);
	}

	public void releaseHardwareBuffers(GL10 gl)
	{
		for (int t=0;t<lChildrenCount;t++)
			lChildren[t].releaseHardwareBuffers(gl);
	}

}
