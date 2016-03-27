package com.wyb.hitplane.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import com.wyb.hitplane.R;


public class Bullet extends GameObject{

    private BulletDismissListener lBulletDismiss;
    private static int count = 0;

    public Bullet(Context context, Paint paint, float x, float y) {
        super(context, paint);
        this.x = x;
        this.y = y;

        count++;
        status = count % 2 == 0 ? 0 : 1;
    }

    @Override
    public void initBitmap(Context context) {
        bitmap[0] = BitmapFactory.decodeResource(context.getResources(), R.mipmap.bullet0);
        bitmap[1] = BitmapFactory.decodeResource(context.getResources(), R.mipmap.bullet1);
    }

    @Override
    public void draw(Canvas canvas) {
        y -= 20;
        if (y < 0) {
            onBulletPassed();
            return;
        }
        canvas.drawBitmap(getBitmap(), x, y, paint);
    }

    public int getWidth() {
        return getBitmap().getWidth();
    }

    public void dismiss() {
        onBulletPassed();
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
