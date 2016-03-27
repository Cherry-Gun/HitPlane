package com.wyb.hitplane.view;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.wyb.hitplane.model.Boss;
import com.wyb.hitplane.model.Bullet;
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
    private long lastBossTime;

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
        paint.setColor(Color.RED);
        sky = new Sky(context, paint);      //天空
        plane = new Plane(context, paint);  //玩家
        enemies = new Vector<>();           //敌机
        passed = new ArrayList<>();         //摧毁的飞机
        random = new Random();
        handler.sendEmptyMessageDelayed(0, 100);//开始飞
        lastBossTime = System.currentTimeMillis();
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
        canvas.drawText(sky.y + "", 10, 10, paint);
        if(System.currentTimeMillis() - lastBossTime >= 10000) {  //行驶了10000
            Boss boss = new Boss(getContext(), paint, getWidth(), getHeight());
            boss.setEnemyDismissListener(this);
            enemies.add(boss);
            lastBossTime = System.currentTimeMillis();
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
                        item.hited();                                           //敌机爆炸
                        break;
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                plane.move(event.getX(), event.getY());
                for (Enemy item : enemies) {  //实施打击！ TODO 修改只有玩家移动的时候子弹才能发挥作用
                    check(item);
                }
                break;
        }
        return true;
    }

    private void check(Enemy enemy) {
        RectF enemyRect = enemy.getRect();          //得到敌机的图像范围
        RectF planeRect = plane.getRect();          //得到玩家的图像范围
        if (enemyRect.intersect(planeRect)) {       //两个图像范围有重合（玩家与敌机相撞）
            enemy.hited();                           //敌机爆炸
            //TODO 玩家此时相当于无敌模式，不合情理
        }
        for (Bullet bullet : plane.bullets) {
            RectF bulletRect = bullet.getRect();    //得到子弹的图像范围
            if (enemyRect.intersect(bulletRect)) {  //两个图象范围有重合（子弹打到敌人）
                enemy.hited();                      //敌机爆炸
                bullet.dismiss();
            }
        }
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
