package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class GamePanel extends JPanel implements MouseMotionListener {

    URL bgPath = GamePanel.class.getClassLoader().getResource("images/bg.png");

    ImageIcon bgImageIcon = new ImageIcon(bgPath);

    //敌机
    List<Enemy> enemies = new ArrayList<>();

    //炮弹
    List<Bullet> bullets = new CopyOnWriteArrayList<>();

    // boss
    Hero hero = new Hero(300, 400);

    // 敌机的炮灰
    List<EnemyBomb> enemyBombs = new CopyOnWriteArrayList<>();

    private AtomicInteger count = new AtomicInteger(0);

    private volatile boolean runStatus = true;

    public GamePanel() {
        for (int i = 0; i < 20; i++) {
            enemies.add(new Enemy());
        }
    }

    public void init() {

        MusicUtil.playMusic("sounds/bg-music.wav", true);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {

                    if (runStatus) {
                        Bullet bullet = new Bullet(hero.getX() + hero.getWidth()/2 -3, hero.getY()-16);
                        bullets.add(bullet);

                        try {
                            TimeUnit.MILLISECONDS.sleep(90);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }

                }
            }
        }, "====== bullet create ======").start();



        Thread t =  new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (runStatus) {
                        for(int i = 0; i < enemyBombs.size(); i++) {
                            EnemyBomb enemyBomb = enemyBombs.get(i);
                            try {
                                TimeUnit.MILLISECONDS.sleep(30);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }

                            // 敌机被击中后，二次爆炸
                            enemyBomb.secondBomb();

                            try {
                                TimeUnit.MILLISECONDS.sleep(30);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }

                            enemyBombs.remove(enemyBomb);
                        }
                    }

                }

            }
        }, "====== enemyBombs create ======");
        t.start();



        while (true) {
            if (runStatus) {

                for (int i = 0; i < enemies.size(); i++) {
                    Enemy enemy = enemies.get(i);

                    enemy.move();

                    if (enemy.getY() > GameMain.height) {
                        enemies.remove(enemy);
                        enemies.add(new Enemy());
                    }
                }

                for (int i = 0; i < bullets.size(); i++) {
                    Bullet bullet = bullets.get(i);
                    bullet.move();

                    if (bullet.getY() < -10) {
                        bullets.remove(bullet);
                    }

                }

                for (int i = 0; i < bullets.size(); i++) {
                    Bullet bullet = bullets.get(i);

                    for (int j = 0; j < enemies.size(); j++) {
                        Enemy enemy = enemies.get(j);

                        if (isHit(enemy, bullet)) {
                            // 命中敌机
                            //MusicUtil.playMusic("sounds/enemy-blowup.wav", false);
                            count.addAndGet(1);
                            enemies.remove(enemy);
                            bullets.remove(bullet);
                            enemyBombs.add(new EnemyBomb(enemy.getX(), enemy.getY()));
                            enemies.add(new Enemy());

                        }
                    }
                }


                for (int j = 0; j < enemies.size(); j++) {
                    Enemy enemy = enemies.get(j);
                    if (isAntikill(enemy, hero)) {
                        runStatus = false;
                        // 反杀
                        hero.die();

                        repaint();

                        int f = JOptionPane.showConfirmDialog(null, "再来一局吗", "游戏结束", JOptionPane.YES_NO_OPTION);
                        if (f == JOptionPane.YES_OPTION) {
                            hero = new Hero(300, 400);
                            bullets.clear();

                            enemies.clear();
                            for (int c = 0; c < 20; c++) {
                                enemies.add(new Enemy());
                            }

                            enemyBombs.clear();
                            count.set(0);
                            runStatus = true;
                            break ;
                        }
                        else {
                            System.exit(0);
                        }

                    }
                }

                repaint();

                try {
                    TimeUnit.MILLISECONDS.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }


        }


    }




    @Override
    public void paint(Graphics g) {
        super.paint(g);

        g.drawImage(bgImageIcon.getImage(), 0, 0, this.getWidth(), this.getHeight(), this);

        g.setFont(new Font("", Color.red.getRed(), 30));
        g.drawString("score: " + count.get()*10, 10, 30);

        g.drawImage(hero.getImageIcon().getImage(), hero.getX(), hero.getY(), null );

        for (int i = 0; i < enemies.size(); i++) {
            Enemy enemy = enemies.get(i);
            g.drawImage(enemy.getEnemyIcon().getImage(), enemy.getX(), enemy.getY(), null);
        }

        for (int i = 0; i < bullets.size(); i++) {
            Bullet bullet = bullets.get(i);
            g.drawImage(bullet.bulletIcon.getImage(),  bullet.getX(), bullet.getY(), null);
        }

        for (int i = 0; i < enemyBombs.size(); i++) {
            EnemyBomb enemyBomb = enemyBombs.get(i);
            g.drawImage(enemyBomb.getImageIcon().getImage(), enemyBomb.getX(), enemyBomb.getY(), null);
        }

    }




    @Override
    public void mouseDragged(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        if (hero.isLive()) {
            hero.setX(x - (hero.getWidth() / 2) - 8 );
            hero.setY(y - (hero.getHeight() / 2) -10 );
        }

        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        if (hero.isLive()) {
            hero.setX(x - (hero.getWidth() / 2) - 8 );
            hero.setY(y - (hero.getHeight() / 2) -10 );
        }
        repaint();

    }

    /**
     * 命中敌机
     */
    public boolean isHit(Enemy enemy, Bullet bullet) {
        Rectangle rect = new Rectangle(enemy.getX(), enemy.getY(), enemy.getWidth(), enemy.getHeight());
        Point point = new Point(bullet.getX() + bullet.getWidth()/2-3, bullet.getY() + bullet.getHeight()/2);
        return rect.contains(point);
    }

    /**
     * 被敌机反杀，撞到机翼和机尾时认为被反杀
     */
    public boolean isAntikill(Enemy enemy, Hero hero) {
        Rectangle rect = new Rectangle(hero.getX(), hero.getY() + hero.getHeight()/2, hero.getWidth(),  hero.getHeight()/2 );
        Point point = new Point(enemy.getX() + enemy.getWidth()/2-3, enemy.getY() + enemy.getHeight()/2);
        return rect.contains(point);
    }

}
