package com.takecho.gm;

import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;

import data.Map;
import sprite.Sprite;

/**
 * Created by Keisuke on 2014/12/31.
 */
public class Player{
    private static final int JUMP_SPEED = 30;
    public int SPEED = 10;

    private double x;
    private double y;
    private double vx;
    private double vy;

    private int dir;

    private static final int RIGHT = 0;
    private static final int LEFT = 1;

    private Map map;
    private boolean onG;
    private int playerSize;
    private boolean beam;


    public Player(int x, int y,int playerSize, Map m) {
        this.x = x;
        this.y = y;
        this.map = m;
        this.playerSize = playerSize;
        dir = RIGHT;
        vx = 0;
        vy = 0;
        onG = false;
    }

    public void jump() {
        if (onG) {
            vy = -JUMP_SPEED;
            onG = false;
        }
    }


    public boolean isCollision(Sprite sprite) {
        Rect playerRect = new Rect((int)x, (int)y, (int)x +playerSize, (int)y + playerSize);
        Rect spriteRect = new Rect((int)sprite.getX(), (int)sprite.getY(), sprite.getWidth(), sprite.getHeight());


       int gx = map.tilesToPixels((int)sprite.getX());
       int gy = map.tilesToPixels((int)sprite.getY());

        if (playerRect.intersects(gx,gy, gx+playerSize, gy+playerSize)) {
            return true;
        }
        return false;
    }


    public boolean isCollisionE(Sprite sprite) {
        Rect playerRect = new Rect((int)x, (int)y, (int)x +playerSize, (int)y + playerSize);
       // Rect spriteRect = new Rect((int)sprite.getX()/100, (int)sprite.getY()/100, sprite.getWidth(), sprite.getHeight());


        int gx =(int)sprite.getX();
        int gy =(int)sprite.getY();

        if (playerRect.intersects(gx+20,gy+20, gx+80, gy+70)) {
            return true;
        }
        return false;
    }

    public void update() {
        vy += 1.8;

        double newX = x + vx;
        Point tile = map.getTileCollision(this, newX, y, map.getMap());
        if (tile == null) {
            x = newX;
        } else {
            if (vx > 0) {
                Log.d("why","なんでや");
                x = Map.tilesToPixels(tile.x) - playerSize;
            } else if (vx < 0) {
                x = Map.tilesToPixels(tile.x + 1);
            }
            //vx = 0;
        }


        double newY = y + vy;
        tile = map.getTileCollision(this, x, newY, map.getMap());
        if (tile == null) {
            y = newY;
            onG = false;
        } else {
            if (vy > 0) {
                y = Map.tilesToPixels(tile.y) - playerSize;
                vy = 0;
                onG = true;
            } else if (vy < 0) {
                y = Map.tilesToPixels(tile.y + 1);
                vy = 0;
            }
        }
}

    public void beam(){
        Log.d("beam", "発射！");
        beam = true;
    }

    public void beamout(){
        Log.d("beam", "発射！");
        beam =  false;
    }

    public boolean getB(){
        return beam;
    }

    public double getX(){
        return x;
    }

    public double getY(){
        return y;
    }

    public int getSize() { return playerSize;}

    public int getDir(){ return dir;}

    public void accelerateRight(){
        dir = RIGHT;
        vx = SPEED;
    }

    public void accelerateLeft(){
        dir = LEFT;
        vx = -SPEED;
    }

    public void speedUp(int s){
        SPEED += s;
    }

    public void stop(){
        vx = 0;
    }

}
