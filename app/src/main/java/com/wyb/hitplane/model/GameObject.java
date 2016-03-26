package com.wyb.hitplane.model;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;


public abstract class GameObject {

    public float x;
    public float y;
    protected Paint paint;

    public GameObject(Context context, Paint paint) {
        this.paint = paint;
    }

    public abstract void draw(Canvas canvas);

}
