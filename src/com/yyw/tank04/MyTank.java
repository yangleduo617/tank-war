package com.yyw.tank04;

import java.util.Vector;

/**
 * @author yywqd
 */
public class MyTank extends Tank {
    Shot shot = null;

    /**
     * 保存多颗子弹
     */
    Vector<Shot> shots = new Vector<>();

    public MyTank(int x, int y) {
        super(x, y);
    }

    public void shotEnemyTank() {

        // 控制子弹只能有5颗
        if (shots.size() == 5) {
            return;
        }
        // 根据己方坦克的方向确定子弹的方向
        switch (getDirect()) {
            case 0:
                shot = new Shot(getX() + 20, getY(), 0);
                break;
            case 1:
                shot = new Shot(getX() + 60, getY() + 20, 1);
                break;
            case 2:
                shot = new Shot(getX() + 20, getY() + 60, 2);
                break;
            case 3:
                shot = new Shot(getX(), getY() + 20, 3);
                break;
        }
        // 将新创建的shot放入到集合
        shots.add(shot);
        // 启动线程
        new Thread(shot).start();
    }
}
