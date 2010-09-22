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
         } else if (mPitch > 0 && mySpritePos.x+50 > 100 && mySpritePos.y+50 >= 100 && mySpritePos.y <= 150) {
        	 mySpritePos.x = 49;
	     } else if (mySpritePos.x < 150 && mySpritePos.y+50 >= 100 && mySpritePos.y <= 150) {
	    	 mySpritePos.x = 151;
	     }
         
         this.mySpritePos.y -= mHeading; 
          
         if (mySpritePos.y >= this.getHeight() - mySprite.getBounds().height()) { 
              mySpritePos.y = this.getHeight() - mySprite.getBounds().height(); 
         } else if (mySpritePos.y <= 0) { 
             mySpritePos.y = 0;
         } else if (mHeading > 0 && mySpritePos.y+50 >= 100 && mySpritePos.x+50 >= 100 && mySpritePos.x <= 150) {
        	 mySpritePos.y = 49;
         } else if (mySpritePos.y <= 150 && mySpritePos.x+50 >= 100 && mySpritePos.x <= 150) {
        	 mySpritePos.y = 151;
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