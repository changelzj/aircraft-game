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

    URL bgUrl = GamePanel.class.getClassLoader().getResource("images/bg.png");
    ImageIcon bgImageIcon1 = new ImageIcon(bgUrl);
    ImageIcon bgImageIcon2 = new ImageIcon(bgUrl);
    private int y1 = 0;
    private int y2 = - this.getHeight();

    //敌机
    List<Enemy> enemies = new CopyOnWriteArrayList<>();
    //炮弹
    List<Bullet> bullets = new CopyOnWriteArrayList<>();
    // boss
    Hero hero = new Hero(190, 700);
    // 敌机的炮灰
    List<EnemyBomb> enemyBombs = new CopyOnWriteArrayList<>();
    // 得分计数器
    private AtomicInteger count = new AtomicInteger(0);

    /**
     *  开始游戏
     */
    public void init() {

        // bgm~
        MusicUtil.playMusic("sounds/bg-music.wav", true);

        // 不断产生敌机
        Thread enemyThread = new Thread(new Runnable() {
            @Override
            public void run() {
                // 为保证多线程下内存可见性，此处必须是while (true)，然后if (GameMain.runStatus), 配合volatile修饰的变量使用，以下同理
                while (true) {

                    if (GameMain.runStatus) {
                        if (enemies.size() <= 20) {
                            enemies.add(new Enemy());
                        }

                        try {
                            TimeUnit.MILLISECONDS.sleep(50);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }

                    }


                }
            }
        }, "=== enemy create ===");


        // 不断产生子弹
        Thread bulletThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {

                    if (GameMain.runStatus) {
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
        }, "====== bullet create ======");



        // 展示敌机的炮灰，二次爆炸，回收炮灰
        Thread enemyBombThread =  new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (GameMain.runStatus) {
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



        enemyThread.start();
        bulletThread.start();
        enemyBombThread.start();


        // 主线程循环高刷，执行判断逻辑等
        while (true) {
            if (GameMain.runStatus) {

                // 游戏背景交替滚动，一前一后
                if (y2 == 0) {
                    y2 = - this.getHeight();
                }
                y2 ++;

                if (y1 == this.getHeight()) {
                    y1 = 0;
                }
                y1++;

                // 敌机移动和回收
                for (int i = 0; i < enemies.size(); i++) {
                    Enemy enemy = enemies.get(i);

                    enemy.move();

                    if (enemy.getY() > GameMain.height) {
                        enemies.remove(enemy);
                    }
                }

                // 子弹运动和回收
                for (int i = 0; i < bullets.size(); i++) {
                    Bullet bullet = bullets.get(i);
                    bullet.move();

                    if (bullet.getY() < -10) {
                        bullets.remove(bullet);
                    }

                }

                // 判断命中敌机
                for (int i = 0; i < bullets.size(); i++) {
                    Bullet bullet = bullets.get(i);

                    for (int j = 0; j < enemies.size(); j++) {
                        Enemy enemy = enemies.get(j);

                        // 是否命中敌机
                        if (isHit(enemy, bullet)) {
                            MusicUtil.playMusic("sounds/enemy-blowup.wav", false);
                            count.addAndGet(1);
                            enemies.remove(enemy);
                            bullets.remove(bullet);
                            enemyBombs.add(new EnemyBomb(enemy.getX(), enemy.getY()));

                        }
                    }
                }

                //判断被敌机反杀
                for (int j = 0; j < enemies.size(); j++) {
                    Enemy enemy = enemies.get(j);
                    if (isAntikill(enemy, hero)) {
                        this.fail();
                        break;
                    }
                }

                //重绘，重绘回调会执行
                repaint();

                try {
                    TimeUnit.MILLISECONDS.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }


        }


    }


    /**
     * 失败
     */
    private void fail() {
        GameMain.runStatus = false;
        hero.die();
        MusicUtil.playMusic("sounds/hero-blowup.wav", false);
        repaint();
        int f = JOptionPane.showConfirmDialog(null, "失败了，再来一局吗？", "游戏结束", JOptionPane.YES_NO_OPTION);
        if (f == JOptionPane.YES_OPTION) {
            hero = new Hero(190, 700);
            bullets.clear();
            enemies.clear();
            enemyBombs.clear();
            count.set(0);
            GameMain.runStatus = true;
        }
        else {
            System.exit(0);
        }
    }




    // 重绘回调

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        g.drawImage(bgImageIcon1.getImage(), 0, y1, this.getWidth(), this.getHeight(), null);
        g.drawImage(bgImageIcon2.getImage(), 0, y2, this.getWidth(), this.getHeight(), null);

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


   // 鼠标事件处理，移动和点按处理相同逻辑即可~
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
    private boolean isHit(Enemy enemy, Bullet bullet) {
        Rectangle rect = new Rectangle(enemy.getX(), enemy.getY(), enemy.getWidth(), enemy.getHeight());
        Point point = new Point(bullet.getX() + bullet.getWidth()/2-3, bullet.getY() + bullet.getHeight()/2);
        return rect.contains(point);
    }

    /**
     * 被敌机反杀，撞到机翼和机尾时认为被反杀
     */
    private boolean isAntikill(Enemy enemy, Hero hero) {
        Rectangle rect = new Rectangle(hero.getX(), hero.getY() + hero.getHeight()/2, hero.getWidth(),  hero.getHeight()/2 );
        Point point = new Point(enemy.getX() + enemy.getWidth()/2-3, enemy.getY() + enemy.getHeight()/2);
        return rect.contains(point);
    }

}
