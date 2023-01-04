package org.example;

import javax.swing.*;
import java.net.URL;

public class EnemyBomb {
    private int x;
    private int y;

    private int width;
    private int height;

    URL url = GamePanel.class.getClassLoader().getResource("images/enemy1_down2.png");

    URL secondUrl = GamePanel.class.getClassLoader().getResource("images/enemy1_down3.png");

    private ImageIcon imageIcon = new ImageIcon(url);

    public EnemyBomb(int x, int y) {
        this.x = x;
        this.y = y;
        this.width = imageIcon.getIconWidth();
        this.height = imageIcon.getIconHeight();
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

    public ImageIcon getImageIcon() {
        return imageIcon;
    }

    public void setImageIcon(ImageIcon imageIcon) {
        this.imageIcon = imageIcon;
    }

    public void secondBomb() {
        this.imageIcon = new ImageIcon(secondUrl);
    }
}
