package com.wyb.hitplane.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;


public abstract class GameObject {

    public float x;
    public float y;
    protected Paint paint;
    protected Bitmap[] bitmap = new Bitmap[10];
    protected int status = 0;
    protected RectF rect = new RectF();

    public GameObject(Context context, Paint paint) {
        this.paint = paint;
        initBitmap(context);
    }

    public abstract void initBitmap(Context context);

    public abstract void draw(Canvas canvas);

    protected Bitmap getBitmap() {
        return bitmap[status];
    }

    public RectF getRect() {
        rect.left = x;
        rect.top = y;
        rect.right = x + bitmap[status].getWidth();
        rect.bottom = y + bitmap[status].getHeight();

        return rect;
    }

    public int getWidth() {
        return getBitmap().getWidth();
    }

    public int getHeight() {
        return getBitmap().getHeight();
    }

}
