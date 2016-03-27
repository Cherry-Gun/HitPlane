package com.wyb.hitplane.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import com.wyb.hitplane.R;


public class Boss extends Enemy{

    private int blood = 25;

    public Boss(Context context, Paint paint, float width, float height) {
        super(context, paint, width, height);
        v = 8;
        cost = 50;
    }

    @Override
    public void initBitmap(Context context) {
        bitmap = new Bitmap[4];
        bitmap[0] = BitmapFactory.decodeResource(context.getResources(), R.mipmap.boss0);
        bitmap[1] = BitmapFactory.decodeResource(context.getResources(), R.mipmap.boss1);
        bitmap[2] = BitmapFactory.decodeResource(context.getResources(), R.mipmap.boss2);
        bitmap[3] = BitmapFactory.decodeResource(context.getResources(), R.mipmap.boss3);
    }

//    @Override
//    public void draw(Canvas canvas) {
//        super.draw(canvas);
//    }

    @Override
    public void hited() {
        blood -= 1;

        if (blood <= 0) {
            super.hited();
        }

    }
}
