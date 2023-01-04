package org.example;

import javax.sound.sampled.*;
import java.io.InputStream;
import java.net.URL;

public class MusicUtil {

    public static void playMusic(String classpath, boolean loop){

        new Thread(new Runnable() {
            @Override
            public void run() {
                do {

                    AudioInputStream audioStream = null;
                    AudioFormat audioFormat = null;
                    SourceDataLine sourceDataLine = null;

                    try {
                        URL url = MusicUtil.class.getClassLoader().getResource(classpath);
                        InputStream inputStream = url.openStream();

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

                    }

                }
                while (loop);

            }

        }, "====== bg music~ ======").start();



    }

}
