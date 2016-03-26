package com.wyb.hitplane.model;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.wyb.hitplane.R;

public class Plane extends GameObject{

    public float x;
    public float y;
    private Bitmap plane1;
    private Paint paint;

    public Plane(Context context, Paint paint) {
        this.paint = paint;
        plane1 = BitmapFactory.decodeResource(context.getResources(), R.mipmap.plane1);
    }

    public boolean move(float x, float y) {
        this.x = x;
        this.y = y;

        return false;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(plane1, x, y, paint);
    }
}
