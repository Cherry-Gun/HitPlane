package com.wyb.hitplane.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import com.wyb.hitplane.R;


public class Sky extends GameObject{

    public Sky(Context context, Paint paint) {
        super(context, paint);
    }

    @Override
    public void initBitmap(Context context) {
        bitmap[0] = BitmapFactory.decodeResource(context.getResources(), R.mipmap.background);
    }

    @Override
    public void draw(Canvas canvas) {
        //背景坐标的位移
        y += (y / 10000 + 1) * 10;
//        if (y >= 10000) {   //当飞行距离超过10000
//            y += 20;        //将加速飞行
//        } else {            //否则
//            y += 10;        //原速飞行
//        }

        float x = y % getBitmap().getHeight();  //y是飞机图片的高度，对背景的高度求余等于飞机图片的一半

        canvas.drawBitmap(getBitmap(), 0, x, paint);
        canvas.drawBitmap(getBitmap(), 0, -(getBitmap().getHeight() - x), paint);
    }
}
