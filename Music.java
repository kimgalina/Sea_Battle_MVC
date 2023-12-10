import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Music {
    private AudioInputStream inputStream;
    private Clip clip;
    private FloatControl volumeControl;
    private boolean playing;
    private boolean initialization;

    public Music(File file) {
        try {
            inputStream = AudioSystem.getAudioInputStream(file);
            clip = AudioSystem.getClip();
            clip.open(inputStream);
            volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            initialization = true;
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException exc) {
            System.err.println(exc);
            initialization = false;
            close();
        }
    }

    public void play() {
        if (initialization) {
            if (!isPlaying()) {
                clip.setFramePosition(0);
                clip.start();
                playing = true;
                return;
            }

            clip.stop();
            clip.setFramePosition(0);
            clip.start();
            playing = true;
        }
    }

    public void playLoop() {
        if (initialization) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            playing = true;
        }
    }

    public void stop() {
        if (playing) {
            clip.stop();
        }
    }

    public void close() {
        if (clip != null)
            clip.close();

        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException ioe) {
                System.err.println(ioe);
            }
        }
    }

    public boolean isInitialization() {
        return initialization;
    }

    public boolean isPlaying() {
        return playing;
    }

    public Clip getClip() {
        return clip;
    }

    public void setVolume(float x) {
        if (x < 0.0f) x = 0.0f;
        if (x > 1.0f) x = 1.0f;
        float min = volumeControl.getMinimum();
        float max = volumeControl.getMaximum();
        float scaledVolume = (max - min) * x + min;
        volumeControl.setValue(scaledVolume);
    }


    public float getVolume() {
        if (clip.isControlSupported(FloatControl.Type.VOLUME)) {
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.VOLUME);
            return gainControl.getValue();
        }

        return -1;
    }
}
