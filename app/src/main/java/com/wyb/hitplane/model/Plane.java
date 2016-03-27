package com.wyb.hitplane.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import com.wyb.hitplane.R;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;


public class Plane extends GameObject implements BulletDismissListener {

    private int count = 0;

    public List<Bullet> bullets = new Vector<>();
    private List<Bullet> passed = new ArrayList<>();
    private Context context;

    public Plane(Context context, Paint paint) {
        super(context, paint);
        this.context = context;
    }

    @Override
    public void initBitmap(Context context) {
        bitmap[0] = BitmapFactory.decodeResource(context.getResources(), R.mipmap.plane1);
        bitmap[1] = BitmapFactory.decodeResource(context.getResources(), R.mipmap.plane2);
    }

    public boolean move(float x, float y) {
        this.x = x;
        this.y = y;

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
        //控制的点从飞机图片的左上角（x=0, y=0）换到了飞机图片的中心
        canvas.drawBitmap(getBitmap(), x - getBitmap().getWidth() / 2, y - getBitmap().getHeight() / 2, paint);
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
        float bulletX = x - 6;    //飞机的x坐标已经在飞机的中心，所以子弹的x坐标和飞机的x坐标一样，保证子弹从飞机的横坐标中心射出
        float bulletY = y - 20 - getBitmap().getHeight() / 2;  //飞机的y坐标已经在飞机的中心，减去飞机的一半，保证子弹在飞机的纵坐标顶部射出，同时再向上XX距离
        //子弹实例化
        Bullet bullet = new Bullet(context, paint, bulletX, bulletY);
        bullet.setOnBulletDismiss(this);
        bullets.add(bullet);
    }

    @Override
    public void onBulletPassed(Bullet bullet) {
        synchronized (passed) {
            passed.add(bullet);
        }
    }

    public RectF getRect() {
        rect.left = x - getBitmap().getWidth() / 2;
        rect.top = y - getBitmap().getHeight() / 2;
        rect.right = x + getBitmap().getWidth() / 2;
        rect.bottom = y + getBitmap().getHeight() / 2;

        return rect;
    }

}
