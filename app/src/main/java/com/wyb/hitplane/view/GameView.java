package com.wyb.hitplane.view;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.wyb.hitplane.model.Boss;
import com.wyb.hitplane.model.Bullet;
import com.wyb.hitplane.model.Enemy;
import com.wyb.hitplane.model.EnemyDismissListener;
import com.wyb.hitplane.model.GameListener;
import com.wyb.hitplane.model.Plane;
import com.wyb.hitplane.model.Sky;
import java.io.IOException;
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
    private int score = 0;          //分数
    private boolean isRun = true;
    private GameListener lGame;
    private MediaPlayer player;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            GameView.this.invalidate();
            if (isRun) {
                sendEmptyMessageDelayed(0, 100);
            }
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

    public boolean isRunning() {
        return isRun;
    }

    public void pause() {
        isRun = false;
        try {
            if (player != null) {
                player.pause();
            }
        } catch (IllegalStateException ex) {

        }
    }

    public void resume() {
        isRun = true;
        handler.sendEmptyMessageDelayed(0, 100);
        lastBossTime = System.currentTimeMillis();
        try {
            if (player != null) {
                player.start();
            }
        } catch (IllegalStateException ex) {

        }
    }

    public void start() {
        enemies.clear();  //清空敌机
        passed.clear();
        this.post(new Runnable() {
            @Override
            public void run() {
                int x = getWidth() / 2;
                int y = getHeight() - plane.getWidth() / 2;
                plane.move(x, y);
            }
        });
        resume();
    }

    public int getScore() {
        return score;
    }

    private void init(Context context) {
        paint = new Paint();                //画笔
        paint.setColor(Color.RED);
        paint.setTextSize(80);
        sky = new Sky(context, paint);      //天空
        plane = new Plane(context, paint, handler);  //玩家
        enemies = new Vector<>();           //敌机
        passed = new ArrayList<>();         //摧毁的飞机
        random = new Random();
        //resume();                           //开始飞
        player = new MediaPlayer();
        try {
            AssetFileDescriptor fileFd = context.getAssets().openFd("hitplane.mp3");
            player.setDataSource(fileFd.getFileDescriptor(), fileFd.getStartOffset(), fileFd.getLength());
            player.prepare();
            player.setLooping(true);
            player.start();
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
        }
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
        score += 2;
        canvas.drawText("分数: " + score, 100, 80, paint);
        if (System.currentTimeMillis() - lastBossTime >= 10000) {  //行驶了10000
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
            check(item);
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
                break;
        }
        return true;
    }

    private void check(Enemy enemy) {
        RectF enemyRect = enemy.getRect();          //得到敌机的图像范围
        RectF planeRect = plane.getRect();          //得到玩家的图像范围
        if (enemyRect.intersect(planeRect)) {       //两个图像范围有重合（玩家与敌机相撞）
            enemy.hited();                          //敌机爆炸
            if (!plane.isSuper) {                   //不在火力全开模式下
                onGameOver();                       //玩家才会死亡
            }
        }
        for (Bullet bullet : plane.bullets) {
            RectF bulletRect = bullet.getRect();    //得到子弹的图像范围
            if (enemyRect.intersect(bulletRect)) {  //两个图象范围有重合（子弹打到敌人）
                enemy.hited();                      //敌机爆炸
                bullet.dismiss();
            }
        }
    }

    public void setGameListener(GameListener l) {
        lGame = l;
    }

    protected void onGameOver() {
        if (player != null) {
            player.release();
        }
        if (lGame != null) {
            lGame.onGameOver();
        }
    }

    public void superPlane() {
        plane.superPlane();
    }

    @Override
    public void onEnemyPassed(Enemy enemy) {
        synchronized (passed) {
            passed.add(enemy);
        }
        score -= 10;
    }

    @Override
    public void onEnemyBomb(Enemy enemy) {
        synchronized (passed) {
            passed.add(enemy);
        }
        score += enemy.getCost();
    }

}
