package com.wyb.hitplane.view;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.wyb.hitplane.R;
import com.wyb.hitplane.model.Plane;
import com.wyb.hitplane.model.Sky;

public class GameView extends View{

    private Sky sky;        //天空（背景）
    private Plane plane;    //玩家（飞机）
    private Paint paint;    //画笔

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            GameView.this.invalidate();
            sendEmptyMessageDelayed(0, 100);
        }
    };

    public GameView(Context context) {
        super(context);
        init(context);
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        paint = new Paint();
        sky = new Sky(context, paint);
        plane = new Plane(context, paint);
        handler.sendEmptyMessageDelayed(0, 100);   //开始飞
    }

    @Override
    protected void onDraw(Canvas canvas) {
        sky.draw(canvas);
        plane.draw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE :
                plane.move(event.getX(), event.getY());
                break;
        }
        return true;
    }
}
