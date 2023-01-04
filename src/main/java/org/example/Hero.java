package org.example;

import javax.swing.*;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class Hero {

    private URL heroPath = GamePanel.class.getClassLoader().getResource("images/hero1.png");

    private URL heroBlowPath = GamePanel.class.getClassLoader().getResource("images/hero2.png");

    private URL heroDieUrl = GamePanel.class.getClassLoader().getResource("images/hero_blowup_n3.png");

    private ImageIcon imageIcon = new ImageIcon(heroPath);

    private int width = 0;

    private int height = 0;

    private volatile boolean live = true;

    private int x = 0;

    private int y = 0;

    private Thread thread = null;

    public Hero(int x, int y) {
        this.x = x;
        this.y = y;
        this.imageIcon = new ImageIcon(heroPath);
        this.height = this.imageIcon.getIconHeight();
        this.width = this.imageIcon.getIconWidth();
        this.blow();
    }

    /**
     * boss机只要还活着，就要喷气
     */
    private void blow() {

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (live) {
                        imageIcon = new ImageIcon(heroPath);

                        try {
                            TimeUnit.MILLISECONDS.sleep(60);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }

                        imageIcon = new ImageIcon(heroBlowPath);

                        try {
                            TimeUnit.MILLISECONDS.sleep(60);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }


                }
            }
        }, "====== hero blow ======");

        thread.start();
    }



    public ImageIcon getImageIcon() {
        return imageIcon;
    }

    public void setImageIcon(ImageIcon imageIcon) {
        this.imageIcon = imageIcon;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isLive() {
        return live;
    }

    public void setLive(boolean live) {
        this.live = live;
    }

    public void die() {
        this.live = false;
        this.imageIcon = new ImageIcon(heroDieUrl);
        if (thread != null ) {
            thread.stop();
            thread = null;
        }
    }


}
