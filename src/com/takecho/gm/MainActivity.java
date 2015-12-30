package com.takecho.gm;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.ImageButton;

public class MainActivity extends Activity {

    private SurfaceView surV;
    private SuperSurface msurV;
    static int stage;
    
    public ImageButton direction;
    public ImageButton a;
    public ImageButton b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        stage = intent.getIntExtra("Stage", 1);

        //setContentViewの前でタイトル非表示
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        surV = (SurfaceView) findViewById(R.id.surfaceView1);
        msurV = new SuperSurface(this, surV);


//set Button
        //Button right = (Button)findViewById(R.id.RightButton);
        direction = (ImageButton) findViewById(R.id.DirButton);
        a = (ImageButton) findViewById(R.id.jump);
        b = (ImageButton) findViewById(R.id.weapon);

//set Listener
        //right.setOnTouchListener(rightTouch);
        direction.setOnTouchListener(dirTouch);
        a.setOnTouchListener(aTouch);
        b.setOnTouchListener(bTouch);

    }


    public static int stageGet() {
        return stage;
    }

    //プレイヤー初期値をマップごとに指定
    public static Point playerGet(int s) {
        switch (s) {
            case 1:
                return new Point(150, 200);
            case 2:
                return new Point(150, 480);
            case 3:
                return new Point(150, 200);
        }
        return new Point(0, 0);
    }


    private OnTouchListener aTouch = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:    //タッチする
                    msurV.player.jump();
                    a.setImageResource(R.drawable.a_button_push);
                    break;
                case MotionEvent.ACTION_MOVE:    //タッチしたまま動かす
                    break;

                case MotionEvent.ACTION_UP:        //指を離す
                	a.setImageResource(R.drawable.a_button);
                    break;
            }
            return true;
        }
    };


    private OnTouchListener bTouch = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:    //タッチする
                    msurV.player.beam();
                    b.setImageResource(R.drawable.b_button_push);
                    break;
                case MotionEvent.ACTION_MOVE:    //タッチしたまま動かす
                    break;
                case MotionEvent.ACTION_UP:        //指を離す
                    msurV.player.beamout();
                    b.setImageResource(R.drawable.b_button);
                    break;
            }
            return true;
        }
    };


   /* private OnTouchListener rightTouch = new OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:    //タッチする
                    Log.d("action:",""+event);
                    msurV.player.accelerateRight();
                    break;
                case MotionEvent.ACTION_MOVE:    //タッチしたまま動かす
                    break;

                case MotionEvent.ACTION_UP:        //指を離す
                    msurV.player.stop();
                    break;
            }
            return true;
        }
    };*/


    private OnTouchListener dirTouch = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            float x = event.getX();
            float y = event.getY();
            int w = v.getWidth();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:    //タッチする
                    if (x < w / 2) {
                        msurV.player.accelerateLeft();
                        direction.setImageResource(R.drawable.dir_key_left);
                    } else {
                        msurV.player.accelerateRight();
                        direction.setImageResource(R.drawable.dir_key_right);
                    }
                    break;
                case MotionEvent.ACTION_MOVE:    //タッチしたまま動かす
                    if (x < w / 2) {
                        msurV.player.accelerateLeft();
                        direction.setImageResource(R.drawable.dir_key_left);
                    } else {
                        msurV.player.accelerateRight();
                        direction.setImageResource(R.drawable.dir_key_right);
                    }
                    break;

                case MotionEvent.ACTION_UP:        //指を離す
                    msurV.player.stop();
                    direction.setImageResource(R.drawable.dir_key);
                    break;
            }
            return true;
        }
    };

    
    public void changeWeapon(View v){
    	Log.d("test","change");
    	
    	//ここに処理を記述
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    protected void onPause() {
        super.onPause();
        }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}