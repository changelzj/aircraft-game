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

        /*new Thread(new Runnable() {
            @Override
            public void run() {





                    *//*AudioInputStream audioStream = null;
                    AudioFormat audioFormat = null;
                    SourceDataLine sourceDataLine = null;

                    try {
                        URL url = MusicUtil.class.getClassLoader().getResource(classpath);
                        System.out.println(url);
                        InputStream openStream = url.openStream();
                        byte[] music = new byte[openStream.available()];
                        openStream.read(music);

                        InputStream inputStream = new ByteArrayInputStream(music) ;

                        int count;

                        byte buf[] = new byte[1024];

                        //获取音频输入流
                        audioStream = AudioSystem.getAudioInputStream(inputStream);
                        //获取音频的编码格式
                        audioFormat = audioStream.getFormat();

                        DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class,
                                audioFormat, AudioSystem.NOT_SPECIFIED);

                        sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);

                        sourceDataLine.open(audioFormat);
                        sourceDataLine.start();

                        while ((count = audioStream.read(buf, 0, buf.length)) != -1) {
                            sourceDataLine.write(buf, 0, count);
                        }

                    }
                    catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    finally {
                        try {
                            sourceDataLine.drain();
                            sourceDataLine.close();
                            audioStream.close();
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }

                    }*//*



            }

        }, "====== bg music~ ======").start();*/



    }

}
