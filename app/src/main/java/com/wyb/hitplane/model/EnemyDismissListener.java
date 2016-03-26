package com.wyb.hitplane.model;


public interface EnemyDismissListener {

    void onEnemyPassed(Enemy enemy);  //飞机消失（飞出背景）
    void onEnemyBomb(Enemy enemy);    //飞机爆炸

}
