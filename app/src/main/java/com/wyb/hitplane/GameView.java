package com.wyb.hitplane;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

public class GameView extends View{

    private Bitmap background, plane1;  //背景和玩家的飞机
    private Paint paint;                //画笔
    private int x = 100;                //x轴坐标

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            GameView.this.invalidate();
            sendEmptyMessageDelayed(0, 500);
        }
    };

    public GameView(Context context) {
        super(context);
        init();
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        background = BitmapFactory.decodeResource(getResources(), R.mipmap.background);
        plane1 = BitmapFactory.decodeResource(getResources(), R.mipmap.plane1);
        handler.sendEmptyMessage(0);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawBackground(canvas);
        drawPlane(canvas);
    }

    private void drawBackground(Canvas canvas) {
        //背景坐标的位移
        x += 30;
        x = x % background.getHeight();
        canvas.drawBitmap(background, 0, x, paint);
        canvas.drawBitmap(background, 0, -(background.getHeight() - x), paint);
    }

    private void drawPlane(Canvas canvas) {
        //画出飞机的位置
        canvas.drawBitmap(plane1, 100, 100, paint);
    }
}
