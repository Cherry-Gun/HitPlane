package com.wyb.hitplane.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import com.wyb.hitplane.R;
import java.util.Random;


public class Enemy extends GameObject{

    protected int v = 5;       //速度
    protected int cost;

    private EnemyDismissListener lEnemyDismiss;
    private float height;

    public Enemy(Context context, Paint paint, float width, float height) {
        super(context, paint);
        this.height = height;

        Random random = new Random();
        y = 0 - getBitmap().getHeight();  //敌机都从最顶部来，切自上而下
        x = random.nextInt((int) (width - getBitmap().getWidth()));  //敌机从最顶部的自左至右随机位置出现，但不会出现半个敌机身子（都放在背景布局里）
        v = random.nextInt(13) + 3;  //[3~11)  //给速度随机值一个范围
        cost = v * 2;
    }

    @Override
    public void initBitmap(Context context) {
        bitmap = new Bitmap[6];
        bitmap[0] = BitmapFactory.decodeResource(context.getResources(), R.mipmap.enemy0);
        bitmap[1] = BitmapFactory.decodeResource(context.getResources(), R.mipmap.enemy1);
        bitmap[2] = BitmapFactory.decodeResource(context.getResources(), R.mipmap.enemy2);
        bitmap[3] = BitmapFactory.decodeResource(context.getResources(), R.mipmap.enemy3);
        bitmap[4] = BitmapFactory.decodeResource(context.getResources(), R.mipmap.enemy4);
        bitmap[5] = BitmapFactory.decodeResource(context.getResources(), R.mipmap.enemy5);
    }

    @Override
    public void draw(Canvas canvas) {
        y += v;
        if (y > height) {       //敌机飞出了天空
            onEnemyPassed();    //就消失
            return;
        }
        canvas.drawBitmap(getBitmap(), x, y, paint);
        if (status == bitmap.length - 1) { //敌机伤势等于5
            onEnemyBomb();                 //就会爆炸
        } else if (status >= 1) {          //一旦敌机开始受伤
            status++;                      //就会不停受伤
        }
    }

    public void hited() {
        bomb();
    }

    public int getCost() {
        return cost;
    }

    public void bomb() {    //爆炸的方法就是
        if (status == 0) {  //如果敌机完好无损
            status = 1;     //就让敌机开始受伤
        }
    }

    protected void onEnemyPassed() {
        if (lEnemyDismiss != null) {
            lEnemyDismiss.onEnemyPassed(this);
        }
    }

    protected void onEnemyBomb() {
        if (lEnemyDismiss != null) {
            lEnemyDismiss.onEnemyBomb(this);
        }
    }

    public void setEnemyDismissListener(EnemyDismissListener lEnemyDismiss) {
        this.lEnemyDismiss = lEnemyDismiss;
    }

}
