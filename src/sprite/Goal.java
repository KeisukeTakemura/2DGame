package sprite;

import android.graphics.Bitmap;

import data.Map;

/**
 * Created by Keisuke on 2015/01/09.
 */
public class Goal extends Sprite{
    public Goal(int x, int y,int size,Bitmap image, Map map,boolean flag){
        super(x,y,size,image,map,flag);
    }

    public void update(){}

    public void play(){}
}
