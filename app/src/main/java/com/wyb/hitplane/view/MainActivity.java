package com.wyb.hitplane.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.wyb.hitplane.R;
import com.wyb.hitplane.model.GameListener;

public class MainActivity extends AppCompatActivity implements GameListener, View.OnClickListener {

    private GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gameView = (GameView) findViewById(R.id.gameView);
        gameView.setGameListener(this);
        findViewById(R.id.btn_pause).setOnClickListener(this);
        findViewById(R.id.btn_superPlane).setOnClickListener(this);
    }

    @Override
    public void onGameOver() {
        Toast.makeText(this, "Game Over!", Toast.LENGTH_SHORT).show();
        gameView.pause();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_pause:
                if (gameView.isRunning()) {
                    gameView.pause();
                } else {
                    gameView.resume();
                }
                break;
            case R.id.btn_superPlane:
                gameView.superPlane();
                break;
        }
    }
}
