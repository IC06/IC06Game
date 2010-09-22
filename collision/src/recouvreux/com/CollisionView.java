package recouvreux.com;

import android.content.Context; 
import android.graphics.Canvas; 
import android.graphics.Point; 
import android.graphics.drawable.Drawable; 
import android.view.KeyEvent;
import android.view.View; 

public class CollisionView extends View { 

     /* Our Ball together with the location it will be painted*/ 
     protected Drawable mySprite; 
     protected Point mySpritePos = new Point(30,30); 
     protected Point sprite2Pos = new Point(100,100);
      
     /* Working with a Enum is 10000% 
      * safer than working with int's 
      * to 'remember' the direction. */ 
     static float mRoll, mPitch, mHeading;
     protected enum HorizontalDirection {LEFT, RIGHT} 
     protected enum VerticalDirection {UP, DOWN} 
     protected HorizontalDirection myXDirection = HorizontalDirection.RIGHT; 
     protected VerticalDirection myYDirection = VerticalDirection.UP; 


     public CollisionView(Context context) { 
          super(context); 
          // Set the background 
          this.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.icon)); 
          // Load our "Ball" 
          this.mySprite = this.getResources().getDrawable(R.drawable.icon);
     } 
     
     @Override 
     protected void onDraw(Canvas canvas) { 
          /* Check if the Ball started to leave 
           * the screen on left or right side */ 
         
         this.mySpritePos.x -= mPitch; 
         
         if (mySpritePos.x >= this.getWidth() - mySprite.getBounds().width()) { 
        	 mySpritePos.x = this.getWidth() - mySprite.getBounds().width();
         } else if (mySpritePos.x <= 0) {
        	 mySpritePos.x = 0;
         } 
         
         this.mySpritePos.y -= mHeading; 
          
         if (mySpritePos.y >= this.getHeight() - mySprite.getBounds().height()) { 
              mySpritePos.y = this.getHeight() - mySprite.getBounds().height(); 
         } else if (mySpritePos.y <= 0) { 
             mySpritePos.y = 0;
         }
         
         if( mySpritePos.x + 50 >= 100 && mySpritePos.x <= 150 && mySpritePos.y +50 >= 100 && mySpritePos.y <= 150 ) 
         // suis-je dans la zone ou il y a collision ?
         {
        	if( (100 - (mySpritePos.x + 50)) > 0 )
        	//Si collision en x alors collision à gauche
        	{
        		if( (100 - (mySpritePos.y + 50) ) > 0 )
        		//Si collision en y alors collision en haut
        		{
        			if( (100 - (mySpritePos.x + 50)) < (100 - (mySpritePos.y + 50) ) )
        			//collision en x, en gauche
        			{
        				mySpritePos.x = 50;
        			} else {
        			// collision en y, en haut
        				mySpritePos.y = 50;
        			}
        		} else if( (mySpritePos.y - 150 ) > 0) {
        		// collision en y (bas) possible
        			if( (100 - (mySpritePos.x + 50)) < (mySpritePos.y - 150) )
            		//collision en x, en gauche
            		{
            			mySpritePos.x = 50;
            		} else {
            		// collision en y, en bas
            			mySpritePos.y = 150;
            		}
        		}
        	} else if( (mySpritePos.x - 150) > 0 )
        	//Si collision en x alors collision à droite
        	{
        		if( (100 - (mySpritePos.y + 50) ) > 0 )
        		//Si collision en y alors collision en haut
        		{
        			if( (mySpritePos.x - 150) < (100 - (mySpritePos.y + 50) ) )
        			//collision en x, a droite
        			{
        				mySpritePos.x = 150;
        			} else {
        			// collision en y, en haut
        				mySpritePos.y = 50;
        			}
        		} else if( (mySpritePos.y - 150) > 0) {
        		// collision en y (bas) possible
        			if( (mySpritePos.x - 150) < (mySpritePos.y - 150)  )
            		//collision en x, a droite
            		{
            			mySpritePos.x = 150;
            		} else {
            		// collision en y, en bas
            			mySpritePos.y = 150;
            		}
        		}
        	}
         }

          /* Set the location, where the sprite 
           * will draw itself to the canvas */ 
          this.mySprite.setBounds(100, 100, 
                    150, 150); 
           
          /* Make the sprite draw itself to the canvas */ 
          this.mySprite.draw(canvas); 

          /* Set the location, where the sprite 
           * will draw itself to the canvas */ 
          this.mySprite.setBounds(this.mySpritePos.x, this.mySpritePos.y, 
                    this.mySpritePos.x + 50, this.mySpritePos.y + 50); 
           
          /* Make the sprite draw itself to the canvas */ 
          this.mySprite.draw(canvas); 
     } 
     
     /*
      * handles key events in the game. Update the direction our snake is traveling
      * based on the DPAD. Ignore events that would cause the snake to immediately
      * turn back on itself.
      * 
      * (non-Javadoc)
      * 
      * @see android.view.View#onKeyDown(int, android.os.KeyEvent)
      */
     @Override
     public boolean onKeyDown(int keyCode, KeyEvent msg) {
    	 this.mPitch = 10;
    	 return true;
     }
}