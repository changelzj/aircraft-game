package org.example;

import javax.swing.*;


public class GameMain {

    public static int width = 500;

    public static int height = 850;

    public static boolean sound = true;

    public static void main(String[] args) {
        JFrame frame = new JFrame();

        frame.setSize(width, height);
        frame.setTitle("微信飞机大战Java版 powered by ZijianLiu");
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
