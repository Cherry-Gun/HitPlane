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

    private Bitmap[] enemy = new Bitmap[6];
    private int status = 0;
    private int v = 5;       //速度

    private EnemyDismissListener lEnemyDismiss;
    private float height;

    public Enemy(Context context, Paint paint, float width, float height) {
        super(context, paint);
        this.height = height;
        enemy[0] = BitmapFactory.decodeResource(context.getResources(), R.mipmap.enemy0);
        enemy[1] = BitmapFactory.decodeResource(context.getResources(), R.mipmap.enemy1);
        enemy[2] = BitmapFactory.decodeResource(context.getResources(), R.mipmap.enemy2);
        enemy[3] = BitmapFactory.decodeResource(context.getResources(), R.mipmap.enemy3);
        enemy[4] = BitmapFactory.decodeResource(context.getResources(), R.mipmap.enemy4);
        enemy[5] = BitmapFactory.decodeResource(context.getResources(), R.mipmap.enemy5);

        Random random = new Random();
        y = 0 - enemy[status].getHeight();  //敌机都从最顶部来，切自上而下
        x = random.nextInt((int) (width - enemy[status].getWidth()));  //敌机从最顶部的自左至右随机位置出现，但不会出现半个敌机身子（都放在背景布局里）
        v = random.nextInt(8) + 3;  //[3~11)  //给速度随机值一个范围
    }

    @Override
    public void draw(Canvas canvas) {
        y += v;
        if (y > height) {       //飞机飞出了天空
            onEnemyPassed();    //就消失
        }
        canvas.drawBitmap(enemy[status], x, y, paint);
        if (status == 5) {          //敌机伤势等于5
            onEnemyBomb();          //就会爆炸
        } else if (status >= 1) {   //一旦敌机开始受伤
            status++;               //就会不停受伤
        }
    }

    public RectF getRect() {   //确定敌机图形的范围，为了计算子弹是否打到
        RectF rect = new RectF();
        rect.left = x;
        rect.top = y;
        rect.right = x + enemy[status].getWidth();
        rect.bottom = y + enemy[status].getHeight();

        return rect;
    }

    public void bomb() {    //爆炸的方法就是
        if (status == 0) {  //如果敌机完好无损
            status = 1;     //就让敌机开始受伤
        }
    }

    private void onEnemyPassed() {
        if (lEnemyDismiss != null) {
            lEnemyDismiss.onEnemyPassed(this);
        }
    }

    private void onEnemyBomb() {
        if (lEnemyDismiss != null) {
            lEnemyDismiss.onEnemyBomb(this);
        }
    }

    public void setEnemyDismissListener(EnemyDismissListener lEnemyDismiss) {
        this.lEnemyDismiss = lEnemyDismiss;
    }

}
