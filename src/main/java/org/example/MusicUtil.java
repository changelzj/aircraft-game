package org.example;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;

public class MusicUtil {


    /**
     * 游戏音效的工具类
     */
    public static void playMusic(String classpath, boolean loop){

        if (!GameMain.sound) {
            return;
        }

        try {
            URL url = MusicUtil.class.getClassLoader().getResource(classpath);
            InputStream openStream = url.openStream();
            Clip clip = AudioSystem.getClip();
            BufferedInputStream buffer = new BufferedInputStream(openStream);
            AudioInputStream ais = AudioSystem.getAudioInputStream(buffer);

            clip.open(ais);

            if (loop) {
                clip.loop(Clip.LOOP_CONTINUOUSLY);//循环播放
            } else {
                clip.start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
