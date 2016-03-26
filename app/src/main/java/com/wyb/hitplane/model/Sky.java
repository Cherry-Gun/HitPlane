package com.wyb.hitplane.model;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.wyb.hitplane.R;

public class Sky extends GameObject{

    public int position;
    private Bitmap background;
    private Paint paint;

    public Sky(Context context, Paint paint) {
        background = BitmapFactory.decodeResource(context.getResources(), R.mipmap.background);
        this.paint = paint;
    }

    @Override
    public void draw(Canvas canvas) {
        //背景坐标的位移
        position += 10;
        int x = position % background.getHeight();

        canvas.drawBitmap(background, 0, x, paint);
        canvas.drawBitmap(background, 0, -(background.getHeight() - x), paint);
    }
}
