package com.wyb.hitplane.view;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.wyb.hitplane.R;
import com.wyb.hitplane.model.Enemy;
import com.wyb.hitplane.model.EnemyDismissListener;
import com.wyb.hitplane.model.Plane;
import com.wyb.hitplane.model.Sky;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

public class GameView extends View implements EnemyDismissListener {

    private Sky sky;                //天空（背景）
    private Plane plane;            //玩家（飞机）
    private List<Enemy> enemies;    //敌机
    private List<Enemy> passed;     //摧毁的敌机
    private Paint paint;            //画笔
    private Random random;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            GameView.this.invalidate();
            sendEmptyMessageDelayed(0, 100);
        }
    };

    public GameView(Context context) {
        super(context);
        init(context);
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        paint = new Paint();                //画笔
        sky = new Sky(context, paint);      //天空
        plane = new Plane(context, paint);  //玩家
        enemies = new Vector<>();           //敌机
        passed = new ArrayList<>();         //摧毁的飞机
        random = new Random();
        handler.sendEmptyMessageDelayed(0, 100);   //开始飞
    }

    @Override
    protected void onDraw(Canvas canvas) {

        sky.draw(canvas);                   //绘制天空背景

        plane.draw(canvas);                 //绘制玩家的飞机

        if (random.nextInt(100) < 8) {      //绘制敌机 ---> 8%的概率产生一个敌机
            Enemy enemy = new Enemy(getContext(), paint, getWidth(), getHeight());
            enemy.setEnemyDismissListener(this);
            enemies.add(enemy);
        }
        synchronized (passed) {
            enemies.removeAll(passed);
            passed.clear();
        }
        for (Enemy item : enemies) {
            item.draw(canvas);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                for (Enemy item : enemies) {
                    if (item.getRect().contains(event.getX(), event.getY())) {  //在屏幕上按下敌机
                        item.bomb();                                            //敌机爆炸
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                plane.move(event.getX(), event.getY());
                break;
        }
        return true;
    }

    @Override
    public void onEnemyPassed(Enemy enemy) {
        synchronized (passed) {
            passed.add(enemy);
        }
    }

    @Override
    public void onEnemyBomb(Enemy enemy) {
        synchronized (passed) {
            passed.add(enemy);
        }
    }

}
