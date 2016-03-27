package com.wyb.hitplane.model;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.wyb.hitplane.R;

public class Bullet extends GameObject{

    private Bitmap[] bullet = new Bitmap[2];
    private int status = 0;
    private BulletDismissListener lBulletDismiss;

    public Bullet(Context context, Paint paint, float x, float y) {
        super(context, paint);

        bullet[0] = BitmapFactory.decodeResource(context.getResources(), R.mipmap.bullet0);
        bullet[1] = BitmapFactory.decodeResource(context.getResources(), R.mipmap.bullet1);
        this.x = x;
        this.y = y;

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

}
