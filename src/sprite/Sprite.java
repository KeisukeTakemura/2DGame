package sprite;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import data.Map;

/**
 * Created by Keisuke on 2015/01/06.
 */
public abstract class Sprite {
    protected double x; //位置
    protected double y;

    protected int width; //大きさ
    protected int height;

    protected  Bitmap image;
    protected Map map;

    protected int size;
    protected boolean flag = false;

    public Sprite(int x, int y,int size,Bitmap filename, Map map) {
        this.x = x;
        this.y = y;
        this.map = map;
        this.size = size;
        this.image = filename;

        width = 100;
        height = 100;

    }

    public Sprite(int x, int y,int size,Bitmap filename, Map map,boolean flag) {
        this.x = x;
        this.y = y;
        this.map = map;
        this.size = size;
        this.image = filename;
        this.flag = flag;

        width = 100;
        height = 100;

    }

    public abstract void update();

    public void draw(Canvas c, Paint p,int offsetX, int offsetY,int count){
        if(flag){count = 0;}
        update();
        Rect src = new Rect(count*size,0,count*size+size,size);
        Rect dst = new Rect(width*(int)x+offsetX ,height*(int)y+offsetY , width*(int)x+offsetX+size, height*(int)y+offsetY+size);
        c.drawBitmap(image, src,dst, p);
    }

    public boolean isCollision(Sprite sprite) {
        Rect playerRect = new Rect((int)x, (int)y, (int)x +width, (int)y +height);
       // Rect spriteRect = new Rect((int)sprite.getX(), (int)sprite.getY(), sprite.getWidth(), sprite.getHeight());
       // if (playerRect.intersects((int)sprite.getX(), (int)sprite.getY(), sprite.getWidth(), sprite.getHeight())) {
        int gx = map.tilesToPixels((int)sprite.getX());
        int gy = map.tilesToPixels((int)sprite.getY());

        if (playerRect.intersects(gx,gy, gx+sprite.getWidth(), gy+sprite.getHeight())) {
            return true;
        }
        return false;
    }


    public double getX(){ return x; }
    public double getY(){ return y; }
    public int getWidth(){ return width; }
    public int getHeight(){ return height; }
    public int getSize(){return size;}


}
