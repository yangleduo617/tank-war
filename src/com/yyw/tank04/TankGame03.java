package com.yyw.tank04;

import javax.swing.*;

/**
 * @author yywqd
 */
public class TankGame03 extends JFrame {
    MyPanel mp;

    public TankGame03() {
        mp = new MyPanel();
        // 启动线程
        new Thread(mp).start();

        this.add(mp);
        // 窗口JFrame 对象监听键盘事件
        this.addKeyListener(mp);
        this.setSize(1100, 850);
        this.setVisible(true);
        //  点击右上角的关闭按钮，退出进程
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        new TankGame03();
    }

}
