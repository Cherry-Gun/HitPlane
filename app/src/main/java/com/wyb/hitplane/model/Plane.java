package com.wyb.hitplane.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;

import com.wyb.hitplane.R;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;


public class Plane extends GameObject implements BulletDismissListener {

    private int count = 0;

    public List<Bullet> bullets = new Vector<>();
    private List<Bullet> passed = new ArrayList<>();
    private Context context;

    public boolean isSuper = false;
    private Handler handler;

    public Plane(Context context, Paint paint, Handler handler) {
        super(context, paint);
        this.context = context;
        this.handler = handler;
    }

    @Override
    public void initBitmap(Context context) {
        bitmap[0] = BitmapFactory.decodeResource(context.getResources(), R.mipmap.plane1);
        bitmap[1] = BitmapFactory.decodeResource(context.getResources(), R.mipmap.plane2);
    }

    public boolean move(float x, float y) {
        this.x = x - getBitmap().getWidth() / 2;
        this.y = y - getBitmap().getHeight() / 2;

        return false;
    }

    @Override
    public void draw(Canvas canvas) {
        if (count % 2 == 0) {   //子弹射出的频率
            this.shoot();
        }

        //绘制飞机
        if (count % 3 == 0) {   //飞机喷气的频率
            status = status == 0 ? 1 : 0;
        }
        canvas.drawBitmap(getBitmap(), x, y, paint);
        count++;

        //绘制子弹
        synchronized (passed) {
            bullets.removeAll(passed);
            passed.clear();
        }
        for (Bullet item : bullets) {
            item.draw(canvas);
        }
    }

    private void shoot() {
        //计算子弹的坐标
        float bulletX = x + getBitmap().getWidth() / 2 - 6;    //飞机的x坐标已经在飞机的中心，所以子弹的x坐标和飞机的x坐标一样，保证子弹从飞机的横坐标中心射出
        float bulletY = y - 20;  //飞机的y坐标已经在飞机的中心，减去飞机的一半，保证子弹在飞机的纵坐标顶部射出，同时再向上XX距离
        //子弹实例化
        Bullet bullet = new Bullet(context, paint, bulletX, bulletY);
        bullet.setOnBulletDismiss(this);
        bullets.add(bullet);

        if (isSuper) {      //火力全开
            bullet = new Bullet(context, paint, bulletX - 50, bulletY);
            bullet.setOnBulletDismiss(this);
            bullets.add(bullet);

            bullet = new Bullet(context, paint, bulletX - 20, bulletY);
            bullet.setOnBulletDismiss(this);
            bullets.add(bullet);

            bullet = new Bullet(context, paint, bulletX + 20, bulletY);
            bullet.setOnBulletDismiss(this);
            bullets.add(bullet);

            bullet = new Bullet(context, paint, bulletX + 50, bulletY);
            bullet.setOnBulletDismiss(this);
            bullets.add(bullet);
        }

    }

    @Override
    public void onBulletPassed(Bullet bullet) {
        synchronized (passed) {
            passed.add(bullet);
        }
    }

    public void superPlane() {
        isSuper = true;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                isSuper =false;
            }
        }, 5000);
    }

}
