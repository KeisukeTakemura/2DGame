package com.takecho.gm;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;


public class StageSelectActivity extends Activity {

    private MediaPlayer bgm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentViewの前でタイトル非表示
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_stage_select);

       // bgm = MediaPlayer.create(this, R.raw.dream_oncemore);
        //bgm.start();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.stage_select, menu);
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

    public void stage1(View view){
        //bgm.stop();
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        intent.putExtra("Stage",1);
        startActivity(intent);
    }

    public void stage2(View view){
        //bgm.stop();
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        intent.putExtra("Stage",2);
        startActivity(intent);
    }

    public void stage3(View view){
        //bgm.stop();
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        intent.putExtra("Stage",3);
        startActivity(intent);

    }

@Override
    protected void onResume(){
        super.onResume();
      //  bgm.stop();
    }

}
