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
import android.graphics.Matrix;
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
import sprite.Shot;
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
    public Bitmap aaaa;
    public Bitmap vvvv;
    //public Bitmap block2;
    
    public static int TILE_SIZE;
    public static int TILE_WIDTH_NUM = 20;
    public static float Mult;
//map,playerインスタンスの生成
    public  Map m;
    public Point p_point;
    public Player player;
    //public Coin
    public int timer = 0;
    public int count = 0;
    public int playerTimer = 0;
    public int playerCount = 0;
    
  //shot
    private static final int NUM_SHOT = 5;
    private Shot[] shots;
    private static final int SHOT_CHARGE_TIME = 300;
    private long lastFire = 0;;
    
    
    float scale = getResources().getDisplayMetrics().density;
 //画面サイズの設定
   /* final float VIEW_WIDTH = 1200;
    final float VIEW_HEIGHT = 500;
    float scaleX;
    float scaleY;
    float scale;
*/

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
        loadImage();
       // loadMap();
        mediaPlayer = MediaPlayer.create(context,R.raw.spring_come);
        mediaPlayer.start();
    }
    
    private void loadMap(){
    		
    	    m = new Map(MainActivity.stageGet());
    	    p_point = MainActivity.playerGet(MainActivity.stageGet());
    	    player = new Player(p_point.x,p_point.y,50,m);
    }

    private void loadMusic(Context context){
        mSoundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        mSoundId = mSoundPool.load(context, R.raw.coin03,0);
    }


    private void loadImage(){
        Resources res = this.getContext().getResources();
        //サイズ変更設定
        //Matrix matrix = new Matrix();
        //matrix.postScale(screen_height/(9*100), 5);
        //
        ppp = BitmapFactory.decodeResource(res, R.drawable.enemy);
        block = BitmapFactory.decodeResource(res, R.drawable.block);
        //block2 = BitmapFactory.decodeResource(res, R.drawable.block2);
        dragon = BitmapFactory.decodeResource(res, R.drawable.player);
        coin = BitmapFactory.decodeResource(res, R.drawable.elect);
        goal = BitmapFactory.decodeResource(res, R.drawable.goaltile);
        aaaa = BitmapFactory.decodeResource(res, R.drawable.player);
        
        //vvvv = Bitmap.createBitmap(aaaa, 0, 0, aaaa.getWidth(),aaaa.getHeight(), matrix,true);
        //TILE_SIZE = block.getWidth();
        //使用する画像データの用意
    }



    @Override
    public void run() {

        //Log.d("x:", "" + screen_width);
        //Log.d("y:",""+screen_height);
    	
    	
    	
    	//TILE_SIZE = block.getWidth();
		TILE_SIZE =(screen_width / TILE_WIDTH_NUM);
		//怒りのfloat祭り
		Mult = (float)((float)((float)screen_width / (float)TILE_WIDTH_NUM) / (float)block.getWidth());
		

        int width = (int)(block.getWidth() * Mult);
        int height = (int)(block.getHeight() * Mult);
		
		 loadMap();
		//Log.d("Size", ""+TILE_SIZE);
		//TILE_SIZE = screen_height / 9;
		//Log.d("Now", ""+ Mult  + ":" + TILE_SIZE);
		
	//map,playerインスタンスの生成

        Canvas canvas = null;
        Paint paint = new Paint();
        Paint bgPaint = new Paint();

        //background
        bgPaint.setStyle(Paint.Style.FILL);
        bgPaint.setColor(Color.BLACK);
        //ball
        paint.setStyle(Paint.Style.FILL);
//loadImage
        //loadImage();

        int mapX = m.getMap()[0].length;
        int mapY = m.getMap().length;
        
        shots = new Shot[NUM_SHOT];
        for (int i = 0; i < NUM_SHOT; i++) {
            shots[i] = new Shot(block,m);
        }

        while (thread != null) {
        	
        	

            //オフセット計算
            int offsetX = screen_width / 2 - (int)player.getX();
            //端のスクロール
            offsetX = Math.min(offsetX, 0);
            offsetX = Math.max(offsetX, screen_width - mapX*TILE_SIZE);

            //横のスクロール
            int offsetY = screen_height / 2 - (int)player.getY() - 105;
            offsetY = Math.min(offsetY,0);
            offsetY = Math.max(offsetY, screen_height - mapY*TILE_SIZE);
            
            if(testflag){
            int maaa[][] = m.getMap();
            
            for (int y = 0; y < maaa.length; y++) {
                for (int x = 0; x < maaa[0].length; x++) {
                    switch (m.getMap()[y][x]) {
                        case 2:
                                sprites.add(new Coin(x, y,TILE_SIZE, coin, m));
                                Log.d("keisan", "sareteru");
                            break;
                        case 3:
                                sprites.add(new Goal(x,y,TILE_SIZE,goal,m,true));
                            break;
                        case 4:
                                sprites.add(new Srime(x*TILE_SIZE,y*TILE_SIZE,TILE_SIZE,ppp,m));
                            break;
                    }
                }
            }
            testflag = false;
            }

            

            try {

                //キャンバスロック
                canvas = holder.lockCanvas();
              //  canvas.translate((screen_width - VIEW_WIDTH)/2*scale, (screen_height - VIEW_HEIGHT)/2*scale);
              //  canvas.scale(scale, scale);

                canvas.drawRect(0, 0, screen_width, screen_height, bgPaint);
                
                //Log.d("aaaaa", ""+(float)block.getWidth() * (float)Mult);




//ブロックの描画

                int firstTileX = Map.pixelsToTiles(-offsetX);
                int lastTileX = firstTileX + Map.pixelsToTiles(mapX*TILE_SIZE) + 1;
                lastTileX = Math.min(lastTileX, mapX);

                int firstTileY = Map.pixelsToTiles(-offsetY);
                int lastTileY = firstTileY + Map.pixelsToTiles(mapY*TILE_SIZE) + 1;
                lastTileY = Math.min(lastTileY, mapY);
                

                paint.setColor(Color.GREEN);
                for (int y = firstTileY; y < lastTileY; y++) {
                    for (int x = firstTileX; x < lastTileX; x++) {
                    	//Log.d("tile", ""+firstTileY);
                        switch (m.getMap()[y][x]) {
                            case 1:
                                //canvas.drawRect(TILE_SIZE * x + offsetX, TILE_SIZE * y, TILE_SIZE * x + TILE_SIZE+offsetX, TILE_SIZE * y + TILE_SIZE, paint);
                               //int width = (int)(block.getWidth() * Mult);
                                //int height = (int)(block.getHeight() * Mult);
                                //Log.d("tes7uyfgd", ""+ width);
                                Rect src = new Rect(0,0,block.getWidth(),block.getHeight());
                                Rect dst = new Rect(width*x+offsetX, height*y+offsetY,width*x+offsetX+width, height*y+offsetY+height);
                                //Rect r = new Rect(width*x+offsetX,height*y+offsetY,width*x+offsetX+width,height*y+offsetY+height);
                               //canvas.drawBitmap(block,width*x+offsetX, height*y+offsetY ,paint);
                                canvas.drawBitmap(block, src,dst,paint);
                                break;
                         /*   case 2:
                               if(testflag) {
                                   sprites.add(new Coin(x, y, 32, coin, m));
                               }
                                break;
                            case 3:
                                if(testflag){
                                    sprites.add(new Goal(x,y,TILE_SIZE,ppp,m,true));
                                }
                                break;
                            case 4:
                                if(testflag){
                                    sprites.add(new Srime(x*TILE_SIZE,y*TILE_SIZE,TILE_SIZE,ppp,m));
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
                Rect src = new Rect(playerCount*size,dir*size,playerCount*size+size,dir*size+size);
                Rect dst = new Rect(px+offsetX ,py+offsetY , px+offsetX+size, py+offsetY+size);
                //canvas.drawBitmap(dragon, px+offsetX ,py,paint);// px+size+offsetX, py+size, paint);
               canvas.drawBitmap(dragon, src,dst,paint);
               //canvas.drawBitmap(vvvv, src,dst,paint);
               
               for (int i = 0; i < NUM_SHOT; i++) {
                   shots[i].move();
               }

                if(player.getB()){
                    paint.setColor(Color.GREEN);
                    paint.setTextSize(20);
                    
                 // 前との発射間隔がFIRE_INTERVAL以下だったら発射できない
                    if (System.currentTimeMillis() - lastFire < SHOT_CHARGE_TIME) {
                    }else{

                    lastFire = System.currentTimeMillis();
                    
                    for (int i = 0; i < NUM_SHOT; i++) {
                        if (shots[i].isInStorage()) {
                        	//Log.d("map", m);
                            shots[i].setPos(px+player.getSize()/2,py-player.getSize()/2);
                            break;
                        }
                    }
                    }
                    
                    canvas.drawText("ビーム発射！",px+offsetX ,py+offsetY,paint);
                }
                    
                    for (int i = 0; i < NUM_SHOT; i++) {
                    	shots[i].offsetX = offsetX;
                    	shots[i].offsetY = offsetY;
                        shots[i].draw(canvas,paint);
                    }

               // player.draw(canvas,paint,offsetX,offsetY,count);

//スプライトの描画
                Iterator iterator = sprites.iterator();
                while(iterator.hasNext()) {
                    Sprite sprite = (Sprite) iterator.next();
                    sprite.draw(canvas, paint, offsetX, offsetY, count);
                    sprite.update();
                    if (player.isCollision(sprite)) {
                        // それがコインだったら
                        if (sprite instanceof Coin) {

                            player.speedUp(1);
                            Coin coin = (Coin) sprite;
                            coinCount++;
                            sprites.remove(coin);
                            mSoundPool.play(mSoundId, 1.0F, 1.0F, 0, 0, 1.0F);
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
                            ((Srime) sprite).death();
                            //thread = null;
                            //getContext().startActivity(new Intent(getContext(), GameOverActivity.class));
                            //Thread.sleep(2000);
                            break;
                        }
                    }
                    
//////////////////////
                   for (int i = 0; i < NUM_SHOT; i++) {
                    if (shots[i].isCollision(sprite)) {
                    
                    if (shots[i].isCollision(sprite)) {
                        if (sprite instanceof Srime) {
                            //mediaPlayer.stop();
                            ((Srime) sprite).death();
                            shots[i].store();
                            break;
                        }
                    }
                    
                    }
                    }
                }


                paint.setColor(Color.GREEN);
                paint.setTextSize(50);
                canvas.drawText("Score:"+coinCount,20,50,paint);
                canvas.drawText("Time:"+Time / 33,screen_width-300,50,paint);

 //描画開始
                //Log.d("Size", ""+scale);
                
                
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

                if(player.getVX() != 0) playerTimer++;
                
                if(playerTimer == 7 && playerCount==1){
                    playerCount = 0;
                    playerTimer=0;
                }else if(playerTimer == 7 && playerCount==0){
                    playerCount = 1;
                    playerTimer = 0;
                }
                
                timer++;
                if(timer == 10 && count == 1){
                	count = 0;
                	timer = 0;
                }else if(timer == 10 && count == 0){
                	count = 1;
                	timer = 0;
                }
                
                Thread.sleep(20);
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
   /*     scaleX = w / VIEW_WIDTH;
        scaleY = h / VIEW_HEIGHT;
        scale = scaleX > scaleY ? scaleY : scaleX;*/
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