package org.example;

import javax.swing.*;


public class GameMain {

    // 游戏界面大小
    public static int width = 500;
    public static int height = 850;

    // 静音信号
    public static volatile boolean sound = true;
    // 游戏运行状态信号
    public static volatile boolean runStatus = true;

    static {
        try {
            if (Integer.parseInt(System.getProperty("sound")) == 1) {
                sound = true;
            } else {
                sound = false;
            }
        }
        catch (Exception e) {
            sound = false;
        }
    }

    /**
     * 程序起点
     */
    public static void main(String[] args) {
        JFrame frame = new JFrame();

        frame.setSize(width, height);
        frame.setTitle("微信飞机大战Java版 powered by ZijianLiu 2023");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        GamePanel gamePanel = new GamePanel();
        gamePanel.setSize(width, height);
        frame.add(gamePanel);
        frame.addMouseMotionListener(gamePanel);
        frame.setVisible(true);

        gamePanel.init();
    }
}
