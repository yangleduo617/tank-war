package com.yyw.tank04;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Vector;

/**
 * @author yywqd
 */
public class MyPanel extends JPanel implements KeyListener, Runnable {
    MyTank mt;
    /**
     * 考虑多线程的问题，这里用Vector集合，用于存放敌方坦克、爆炸效果的图片
     */
    Vector<EnemyTank> enemyTanks = new Vector<>();

    /**
     * 当子弹击中坦克时，加入一个Bomb对象到bombs
     */
    Vector<Bomb> bombs = new Vector<>();

    /**
     * 三张爆炸效果的图片
     */
    Image image1, image2, image3;

    // 敌方坦克的数量
    int enemySize = 3;

    public MyPanel() {
        // 己方坦克出生的位置
        mt = new MyTank(500, 200);
        mt.setSpeed(5);

        for (int i = 0; i < enemySize; i++) {
            EnemyTank enemyTank = new EnemyTank(100 * (i + 1), 0);
            // 初始化敌方坦克向下
            enemyTank.setDirect(2);
            // 敌人坦克加入到集合
            enemyTanks.add(enemyTank);

            // 启动坦克自由移动的线程
            new Thread(enemyTank).start();

            // 给enemy加一颗子弹
            Shot shot = new Shot(enemyTank.getX() + 20, enemyTank.getY() + 60, enemyTank.getDirect());
            enemyTank.shots.add(shot);
            // 启动子弹线程
            new Thread(shot).start();
        }
        // 初始化图片对象
        image1 = Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/bomb_1.gif"));
        image2 = Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/bomb_2.gif"));
        image3 = Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/bomb_3.gif"));
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        // 游戏区域，默认黑色
        g.fillRect(0, 0, 1000, 750);
        // 绘制己方坦克
        if (mt != null && mt.isLive) {
            drawTank(mt.getX(), mt.getY(), g, mt.getDirect(), 0);
        }

        /*
        绘制MyTank的子弹,需要将MyPanel设置成线程，才能不停地重绘子弹，一颗
        if (mt.shot != null && mt.shot.isLive) {
             g.fill3DRect(mt.shot.x, mt.shot.y, 2, 2, false);
        }
         */

        // 绘制己方坦克的多颗子弹，遍历集合
        for (int i = 0; i < mt.shots.size(); i++) {
            Shot shot = mt.shots.get(i);
            if (shot != null && shot.isLive) {
                g.fill3DRect(shot.x, shot.y, 2, 2, false);
            } else {
                mt.shots.remove(shot);
            }
        }

        // 如果bombs集合中有对象就画出
        for (int i = 0; i < bombs.size(); i++) {
            Bomb bomb = bombs.get(i);
            if (bomb.life > 6) {
                g.drawImage(image1, bomb.x, bomb.y, 60, 60, this);
            } else if (bomb.life > 3) {
                g.drawImage(image2, bomb.x, bomb.y, 60, 60, this);
            } else {
                g.drawImage(image3, bomb.x, bomb.y, 60, 60, this);
            }
            bomb.lifeDown();
            // 生命周期为0，就从集合删除
            if (bomb.life == 0) {
                bombs.remove(bomb);
            }
        }

        // 绘制敌方坦克
        for (int i = 0; i < enemyTanks.size(); i++) {
            EnemyTank et = enemyTanks.get(i);
            // 当enemyTank是存在的就绘制坦克，在hitTank方法中写了isLive逻辑，
            // 子弹到达坦克区域就会将isLive设置为false，是false就不会绘制坦克
            if (et.isLive) {
                drawTank(et.getX(), et.getY(), g, et.getDirect(), 1);
                // 绘制敌方坦克的子弹,遍历Shot集合
                for (int j = 0; j < et.shots.size(); j++) {
                    // 取出子弹
                    Shot shot = et.shots.get(j);
                    if (shot.isLive) {
                        g.fill3DRect(shot.x, shot.y, 2, 2, false);
                    } else {
                        // 移除
                        et.shots.remove(shot);
                    }
                }
            }
        }
    }

    /**
     * @param x      坦克的左上角横坐标
     * @param y      坦克的左上角纵坐标
     * @param g      画笔
     * @param direct 坦克的方向（0:上、1:右、2:下、3:左）
     * @param type   坦克的类型（0:己方、1:敌人）
     *               画坦克
     */
    public void drawTank(int x, int y, Graphics g, int direct, int type) {
        switch (type) {
            case 0:
                g.setColor(Color.cyan);
                break;
            case 1:
                g.setColor(Color.yellow);
                break;
        }

        // 根据方向绘制坦克
        switch (direct) {
            case 0:
                // 左边轮子
                g.fill3DRect(x, y, 10, 60, false);
                // 右边轮子
                g.fill3DRect(x + 30, y, 10, 60, false);
                // 坦克的身体
                g.fill3DRect(x + 10, y + 10, 20, 40, false);
                // 坦克的圆顶
                g.fillOval(x + 10, y + 20, 20, 20);
                // 坦克的炮管
                g.drawLine(x + 20, y, x + 20, y + 30);
                break;
            case 1:
                g.fill3DRect(x, y, 60, 10, false);
                g.fill3DRect(x, y + 30, 60, 10, false);
                g.fill3DRect(x + 10, y + 10, 40, 20, false);
                g.fillOval(x + 20, y + 10, 20, 20);
                g.drawLine(x + 30, y + 20, x + 60, y + 20);
                break;
            case 2:
                g.fill3DRect(x, y, 10, 60, false);
                g.fill3DRect(x + 30, y, 10, 60, false);
                g.fill3DRect(x + 10, y + 10, 20, 40, false);
                g.fillOval(x + 10, y + 20, 20, 20);
                g.drawLine(x + 20, y + 30, x + 20, y + 60);
                break;
            case 3:
                g.fill3DRect(x, y, 60, 10, false);
                g.fill3DRect(x, y + 30, 60, 10, false);
                g.fill3DRect(x + 10, y + 10, 40, 20, false);
                g.fillOval(x + 20, y + 10, 20, 20);
                g.drawLine(x, y + 20, x + 30, y + 20);
                break;
        }
    }

    /**
     * 判断敌人的子弹是否击中我方坦克
     */
    public void hitMyTank() {
        for (int i = 0; i < enemyTanks.size(); i++) {
            EnemyTank enemyTank = enemyTanks.get(i);
            for (int j = 0; j < enemyTank.shots.size(); j++) {
                Shot shot = enemyTank.shots.get(j);
                if (mt.isLive && shot.isLive) {
                    hitTank(shot, mt);
                }
            }
        }
    }

    /**
     * 有子弹遇到敌人不爆炸的原因，是hitTank只是判断一颗子弹，需要判断子弹集合中的所有子弹
     */
    public void hitEnemyTank() {
        for (int i = 0; i < mt.shots.size(); i++) {
            Shot shot = mt.shots.get(i);
            // 判断自己的子弹是否存活
            if (shot != null && shot.isLive) {
                for (int j = 0; j < enemyTanks.size(); j++) {
                    EnemyTank enemyTank = enemyTanks.get(j);
                    hitTank(shot, enemyTank);
                }
            }
        }
    }

    /**
     * 编写方法，我方子弹是否击中敌方坦克
     *  @param s         单颗子弹
     * @param tank 敌方坦克
     */
    public void hitTank(Shot s, Tank tank) {
        switch (tank.getDirect()) {
            case 0:
            case 2:
                if (s.x > tank.getX() && s.x < tank.getX() + 40
                        && s.y > tank.getY() && s.y < tank.getY() + 60) {
                    s.isLive = false;
                    tank.isLive = false;
                    // 创建bomb，加入到集合
                    Bomb bomb = new Bomb(tank.getX(), tank.getY());
                    bombs.add(bomb);
                    // 击中敌人坦克，将坦克从集合中移除
                    enemyTanks.remove(tank);
                }
                break;
            case 1:
            case 3:
                if (s.x > tank.getX() && s.x < tank.getX() + 60
                        && s.y > tank.getY() && s.y < tank.getY() + 40) {
                    s.isLive = false;
                    tank.isLive = false;
                    Bomb bomb = new Bomb(tank.getX(), tank.getY());
                    bombs.add(bomb);
                    // 击中敌人坦克，将坦克从集合中移除
                    enemyTanks.remove(tank);
                }
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_W) {
            mt.setDirect(0);
            if (mt.getY() > 0) {
                mt.moveUp();
            }
        } else if (e.getKeyCode() == KeyEvent.VK_D) {
            mt.setDirect(1);
            if (mt.getX() + 60 < 1000) {
                mt.moveRight();
            }
        } else if (e.getKeyCode() == KeyEvent.VK_S) {
            mt.setDirect(2);
            if (mt.getY() + 60 < 750) {
                mt.moveDown();
            }
        } else if (e.getKeyCode() == KeyEvent.VK_A) {
            mt.setDirect(3);
            if (mt.getX() > 0) {
                mt.moveLeft();
            }
        }

        // 监控到J按下
        if (e.getKeyCode() == KeyEvent.VK_J) {
            // 实现己方坦克只能发射一颗子弹，子弹线程消亡以后，但是子弹线程的对象并不为null
            // if (mt.shot == null || !mt.shot.isLive) {
            //     mt.shotEnemyTank();
            // }
            mt.shotEnemyTank();
        }

        // 面板重新绘制
        this.repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // 判断是否击中敌人的坦克
            hitEnemyTank();
            // 判断敌人坦克是否击中己方坦克
            hitMyTank();

            this.repaint();
        }
    }
}
