package com.wyb.hitplane.model;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.wyb.hitplane.R;

public class Plane extends GameObject {

    private Bitmap[] plane = new Bitmap[2];
    private int status = 0;
    private int count = 0;

    public Plane(Context context, Paint paint) {
        super(context, paint);
        plane[0] = BitmapFactory.decodeResource(context.getResources(), R.mipmap.plane1);
        plane[1] = BitmapFactory.decodeResource(context.getResources(), R.mipmap.plane2);
    }

    public boolean move(float x, float y) {
        this.x = x;
        this.y = y;

        return false;
    }

    @Override
    public void draw(Canvas canvas) {
        if (count % 3 == 0) {
            status = status == 0 ? 1 : 0;
        }
        //控制的点从飞机图片的左上角（x=0, y=0）换到了飞机图片的中心
        canvas.drawBitmap(plane[status], x - plane[status].getWidth() / 2, y - plane[status].getHeight() / 2, paint);
        count++;
    }
}
