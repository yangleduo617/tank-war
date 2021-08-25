package com.yyw.tank04;

import java.util.Vector;

/**
 * @author yyw
 * 敌人的坦克
 */
public class EnemyTank extends Tank implements Runnable {
    /**
     * 在敌人坦克类，使用Vector 保存多个Shot
     */
    Vector<Shot> shots = new Vector<>();
    boolean isLive = true;

    public EnemyTank(int x, int y) {
        super(x, y);
    }

    /**
     * 线程睡眠时长，注意在switch语句中的位置，是for循环内
     */
    public void delay() {
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true) {
            // 让坦克发射子弹消亡后继续发射子弹，条件可以控制发射的子弹数量
            if (isLive && shots.size() < 2) {
                Shot s = null;
                switch (getDirect()) {
                    case 0:
                        s = new Shot(getX() + 20, getY(), 0);
                        break;
                    case 1:
                        s = new Shot(getX() + 60, getY() + 20, 1);
                        break;
                    case 2:
                        s = new Shot(getX() + 20, getY() + 60, 2);
                        break;
                    case 3:
                        s = new Shot(getX(), getY() + 20, 3);
                        break;
                }
                shots.add(s);
                new Thread(s).start();
            }

            // 根据坦克的方向继续移动
            switch (getDirect()) {
                case 0:
                    for (int i = 0; i < 30; i++) {
                        if (getY() > 0) {
                            moveUp();
                        }
                        delay();
                    }
                    break;
                case 1:
                    for (int i = 0; i < 30; i++) {
                        if (getX() + 60 < 1000) {
                            moveRight();
                        }
                        delay();
                    }

                    break;
                case 2:
                    for (int i = 0; i < 30; i++) {
                        if (getY() + 60 < 750) {
                            moveDown();
                        }
                        delay();
                    }
                    break;
                case 3:
                    for (int i = 0; i < 30; i++) {
                        if (getX() > 0) {
                            moveLeft();
                        }
                        delay();
                    }
                    break;
            }
            // 随机改变坦克的方向
            setDirect((int) (Math.random() * 4));
            // 线程的终止条件，多线程程序编写必须考虑
            if (!isLive) {
                break;
            }
        }
    }
}
