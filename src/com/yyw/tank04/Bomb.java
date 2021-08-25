package com.yyw.tank04;

public class Bomb {
    int x, y;
    // 生命周期
    int life = 9;
    boolean isLive = true;

    public Bomb(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // 减少生命值，配合出现爆炸的效果更佳
    public void lifeDown() {
        if (life > 0) {
            life--;
        } else {
            isLive = false;
        }
    }
}
