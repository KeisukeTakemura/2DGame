package sprite;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;

import data.Map;

/**
 * Created by Keisuke on 2015/01/09.
 */
public class Srime extends Sprite{

    private static final int SPEED = 5;


    private  double vx;
    private  double vy;

    public Srime(int x, int y,int size,Bitmap filename, Map map){
        super(x,y,size,filename,map);
        //this.x = x*size;
        //this.y = y*size;
        this.vx = -SPEED;
        this.vy = 0;
    }

    public void draw(Canvas c, Paint p,int offsetX, int offsetY,int count){
        p.setColor(Color.BLUE);
        Rect src = new Rect(count*size,0,count*size+size,size);
        Rect dst = new Rect((int)x+offsetX ,(int)y+offsetY , (int)x+offsetX+size, (int)y+offsetY+size);
        //canvas.drawBitmap(dragon, px+offsetX ,py,paint);// px+size+offsetX, py+size, paint);
        c.drawBitmap(this.image, src,dst,p);
    }

    public void update(){
        vy += 2.0;
//x,yがタイル
        double newX = x + vx;
        Point tile = map.getTileCollision(this, newX, y, map.getMap());
        //Log.d("point",""+ tile);
        if (tile == null) {
            x = newX;
        } else {
            if (vx > 0) {
                x = Map.tilesToPixels(tile.x) - size;
            } else if (vx < 0) {
                x = Map.tilesToPixels(tile.x + 1);
            }
            vx = -vx;
        }

        double newY = y + vy;
        tile = map.getTileCollision(this, x, newY, map.getMap());
        if (tile == null) {
            y = newY;
        } else {
            if (vy > 0) {
                y = Map.tilesToPixels(tile.y) - size;
                vy = 0;
            } else if (vy < 0) {
                y = Map.tilesToPixels(tile.y + 1);
                vy = 0;
            }
        }
    }

    }
