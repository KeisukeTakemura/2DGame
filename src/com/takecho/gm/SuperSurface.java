package com.takecho.gm;

/**
 * Created by Keisuke on 2014/12/26.
 */

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.SurfaceHolder.Callback;

import java.util.Iterator;
import java.util.LinkedList;

import data.Map;
import sprite.Coin;
import sprite.Goal;
import sprite.Sprite;
import sprite.Srime;

public class SuperSurface extends SurfaceView implements Callback,Runnable {
    private SurfaceHolder holder;
    private final String LOG = "SuperSurface";


//画像準備
    public Bitmap ppp;
    public Bitmap block;
    public Bitmap dragon;
    public Bitmap coin;
    public Bitmap goal;

    private static final int TILE_SIZE = 100;
//map,playerインスタンスの生成

    public  Map m = new Map(MainActivity.stageGet());
    public Point p_point = MainActivity.playerGet(MainActivity.stageGet());
    public Player player = new Player(p_point.x,p_point.y,96,m);
    //public Coin
    public int timer = 0;
    public int count = 0;


//その他準備
    Thread thread;
    public Intent i = new Intent();
    int screen_width, screen_height;
    public int coinCount = 0;
    public int Time = 9900;

    private SoundPool mSoundPool;
    private int mSoundId;
    private MediaPlayer mediaPlayer;

//スプライトリスト
    public LinkedList sprites = new LinkedList();

    public boolean testflag = true;
    public boolean bflag = false;

    public SuperSurface(Context context, SurfaceView sv) {
        super(context);
        holder = sv.getHolder();
        holder.addCallback(this);
        i = new Intent(context,StageSelectActivity.class);
        loadMusic(context);
        mediaPlayer = MediaPlayer.create(context,R.raw.spring_come);
        mediaPlayer.start();
    }

    private void loadMusic(Context context){
        mSoundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        mSoundId = mSoundPool.load(context, R.raw.coin03,0);
    }


    private void loadImage(){
        Resources res = this.getContext().getResources();
        ppp = BitmapFactory.decodeResource(res, R.drawable.enemy);
        block = BitmapFactory.decodeResource(res, R.drawable.block);
        dragon = BitmapFactory.decodeResource(res, R.drawable.player);
        coin = BitmapFactory.decodeResource(res, R.drawable.elect);
        goal = BitmapFactory.decodeResource(res, R.drawable.goaltile);
        //使用する画像データの用意
    }



    @Override
    public void run() {

        Log.d("x:", "" + screen_width);
        Log.d("y:",""+screen_height);

        Canvas canvas = null;
        Paint paint = new Paint();
        Paint bgPaint = new Paint();

        //background
        bgPaint.setStyle(Paint.Style.FILL);
        bgPaint.setColor(Color.BLACK);
        //ball
        paint.setStyle(Paint.Style.FILL);
//loadImage
        loadImage();

        int mapX = m.getMap()[0].length;
        int mapY = m.getMap().length;

        while (thread != null) {

            //オフセット計算
            int offsetX = screen_width / 2 - (int)player.getX();
            //端のスクロール
            offsetX = Math.min(offsetX, 0);
            offsetX = Math.max(offsetX, screen_width - mapX*100);

            //横のスクロール
            int offsetY = screen_height / 2 - (int)player.getY() - 205;
            offsetY = Math.min(offsetY,0);
            offsetY = Math.max(offsetY, screen_height - mapY*100);

            int maaa[][] = m.getMap();
            for (int y = 0; y < maaa.length; y++) {
                for (int x = 0; x < maaa[0].length; x++) {
                    switch (m.getMap()[y][x]) {
                        case 2:
                            if(testflag) {
                                sprites.add(new Coin(x, y, 96, coin, m));
                            }
                            break;
                        case 3:
                            if(testflag){
                                sprites.add(new Goal(x,y,100,goal,m,true));
                            }
                            break;
                        case 4:
                            if(testflag){
                                sprites.add(new Srime(x*100,y*100,100,ppp,m));
                            }
                            break;
                    }
                }
            }
            testflag = false;



            try {

                //キャンバスロック
                canvas = holder.lockCanvas();

                canvas.drawRect(0, 0, screen_width, screen_height, bgPaint);




//ブロックの描画

                int firstTileX = Map.pixelsToTiles(-offsetX);
                int lastTileX = firstTileX + Map.pixelsToTiles(mapX*100) + 1;
                lastTileX = Math.min(lastTileX, mapX);

                int firstTileY = Map.pixelsToTiles(-offsetY);
                int lastTileY = firstTileY + Map.pixelsToTiles(mapY*100) + 1;
                lastTileY = Math.min(lastTileY, mapY);



                paint.setColor(Color.GREEN);
                for (int y = firstTileY; y < lastTileY; y++) {
                    for (int x = firstTileX; x < lastTileX; x++) {
                        switch (m.getMap()[y][x]) {
                            case 1:
                                //canvas.drawRect(100 * x + offsetX, 100 * y, 100 * x + 100+offsetX, 100 * y + 100, paint);
                                int width = block.getWidth();
                                int height = block.getHeight();
                               canvas.drawBitmap(block,width*x+offsetX, height*y+offsetY ,paint);
                                break;
                         /*   case 2:
                               if(testflag) {
                                   sprites.add(new Coin(x, y, 32, coin, m));
                               }
                                break;
                            case 3:
                                if(testflag){
                                    sprites.add(new Goal(x,y,100,ppp,m,true));
                                }
                                break;
                            case 4:
                                if(testflag){
                                    sprites.add(new Srime(x*100,y*100,100,ppp,m));
                                }
                                break;*/
                        }
                    }
                }
                testflag = false;
//プレイヤーの描画

                int px = (int)player.getX();
                int py = (int)player.getY();
                int dir = player.getDir();
                int size = player.getSize();
                paint.setColor(Color.BLUE);
                Rect src = new Rect(count*size,dir*size,count*size+size,dir*size+size);
                Rect dst = new Rect(px+offsetX ,py+offsetY , px+offsetX+size, py+offsetY+size);
                //canvas.drawBitmap(dragon, px+offsetX ,py,paint);// px+size+offsetX, py+size, paint);
               canvas.drawBitmap(dragon, src,dst,paint);

                if(player.getB()){
                    paint.setColor(Color.GREEN);
                    paint.setTextSize(20);
                    canvas.drawText("ビーム発射！",px+offsetX-30 ,py+offsetY-20,paint);
                }

               // player.draw(canvas,paint,offsetX,offsetY,count);

//スプライトの描画
                Iterator iterator = sprites.iterator();
                while(iterator.hasNext()) {
                    Sprite sprite = (Sprite) iterator.next();
                    sprite.draw(canvas, paint, offsetX, offsetY, count);
                    sprite.update();
                    if (player.isCollision(sprite)) {
                        Log.d("chad", "の例圧が・・・消えた？");
                        // それがコインだったら
                        if (sprite instanceof Coin) {

                            player.speedUp(1);
                            Coin coin = (Coin) sprite;
                            coinCount++;
                            sprites.remove(coin);
                            Log.d("chad", "の例圧が・・・消えた？");
                            mSoundPool.play(mSoundId, 1.0F, 1.0F, 0, 0, 1.0F);
                            // ちゃり〜ん
                            coin.play();
                            // spritesから削除したので
                            // breakしないとiteratorがおかしくなる
                            break;

                        } else if (sprite instanceof Goal) {

                          /*  paint.setTextSize(300);
                            canvas.drawRect(0, 0, screen_width, screen_height, bgPaint);
                            canvas.drawText("GOOOAL!!", 230, 400, paint);*/
                            mediaPlayer.stop();
                            thread = null;
                            getContext().startActivity(new Intent(getContext(), GoalActivity.class));
                            //Thread.sleep(2000);
                            break;
                        }
                    }
                    if (player.isCollisionE(sprite)) {
                        if (sprite instanceof Srime) {
                            mediaPlayer.stop();
                           /* paint.setTextSize(300);
                            canvas.drawRect(0, 0, screen_width, screen_height, bgPaint);
                            canvas.drawText("GAME OVER!!", 230, 400, paint);*/
                            thread = null;
                            getContext().startActivity(new Intent(getContext(), GameOverActivity.class));
                            //Thread.sleep(2000);
                            break;
                        }
                    }
                }


                paint.setColor(Color.GREEN);
                paint.setTextSize(50);
                canvas.drawText("Score:"+coinCount,20,50,paint);
                canvas.drawText("Time:"+Time / 33,screen_width-300,50,paint);

 //描画開始
                holder.unlockCanvasAndPost(canvas);

                Time--;
                if(player.getY()>=m.getRow()*TILE_SIZE-player.getSize()){
                    mediaPlayer.stop();
                 /*   paint.setTextSize(300);
                    canvas.drawRect(0, 0, screen_width, screen_height, bgPaint);
                    canvas.drawText("GAME OVER!!", 230, 400, paint);*/
                    thread = null;
                    getContext().startActivity(new Intent(getContext(), GameOverActivity.class));
                }
                player.update();

                timer++;
                if(timer == 10 && count==1){
                    count = 0;
                    timer=0;
                }else if(timer == 10 && count==0){
                    count = 1;
                    timer = 0;
                }
                Thread.sleep(30);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    //surfaceChanged
    @Override
    public void surfaceChanged(SurfaceHolder holder, int f, int w, int h) {
        Log.d(LOG, "surfaceChanged");
        screen_width = w;
        screen_height = h;
    }


    //surfaceCreated
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(LOG, "surfaceCreated");

        //paint
        thread = new Thread(this);
        thread.start();
    }


    //surfaceDestroyed
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(LOG, "surfaceDestroyed");
        mediaPlayer.stop();
        thread = null;
    }

}