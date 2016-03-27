package com.wyb.hitplane.model;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import com.wyb.hitplane.R;

public class Bullet extends GameObject{

    private Bitmap[] bullet = new Bitmap[2];
    private int status = 0;
    private BulletDismissListener lBulletDismiss;
    private static int count = 0;
    private RectF rect = new RectF();

    public Bullet(Context context, Paint paint, float x, float y) {
        super(context, paint);

        bullet[0] = BitmapFactory.decodeResource(context.getResources(), R.mipmap.bullet0);
        bullet[1] = BitmapFactory.decodeResource(context.getResources(), R.mipmap.bullet1);
        this.x = x;
        this.y = y;

        count++;
        status = count % 2 == 0 ? 0 : 1;
    }

    @Override
    public void draw(Canvas canvas) {
        y -= 20;
        if (y < 0) {
            onBulletPassed();
            return;
        }
        canvas.drawBitmap(bullet[status], x, y, paint);
    }

    protected void onBulletPassed() {
        if (lBulletDismiss != null) {
            lBulletDismiss.onBulletPassed(this);
        }
    }

    public  void setOnBulletDismiss(BulletDismissListener lBulletDismiss) {
        this.lBulletDismiss = lBulletDismiss;
    }

    public int getWidth() {
        return bullet[status].getWidth();
    }

    public RectF getRect() {
        rect.left = x;
        rect.top = y;
        rect.right = x + bullet[status].getWidth();
        rect.bottom = y + bullet[status].getWidth();

        return rect;
    }

}
