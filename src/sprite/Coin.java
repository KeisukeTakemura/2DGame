package sprite;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import data.Map;

/**
 * Created by Keisuke on 2015/01/06.
 */
public class Coin extends Sprite {

    public Coin(int x, int y,int size,Bitmap image,Map map){
        super(x,y,size,image,map);
    }

    public void update(){}

    public void play(){}
    
//    public void draw(Canvas c, Paint p,int offsetX, int offsetY,int count){
//    	Rect src = new Rect(count*96,0,count*96+96,96);
//        Rect dst = new Rect((int)x+offsetX ,(int)y+offsetY , (int)x+offsetX+size, (int)y+offsetY+size);
//        //canvas.drawBitmap(dragon, px+offsetX ,py,paint);// px+size+offsetX, py+size, paint);
//        c.drawBitmap(this.image, src,dst,p);
//    }
}
