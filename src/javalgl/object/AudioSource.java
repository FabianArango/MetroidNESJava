package javalgl.object;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.BooleanControl;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;


public class AudioSource { // Añadir try catch
    private Clip audioClip;

    @Deprecated
    public static AudioInputStream load(String path) { 
        try {
            return AudioSystem.getAudioInputStream(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static AudioInputStream load(URL url) {
        try {
            return AudioSystem.getAudioInputStream(url);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
            return null;
        }
    }

    public AudioSource(AudioInputStream audioInputStream) {
        setAudioClip(audioInputStream);
    }

    public void setAudioClip(AudioInputStream audioInputStream) {
        try {
            this.audioClip = AudioSystem.getClip();
            this.audioClip.open(audioInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public Clip getAudioClip() {
        return audioClip;
    }

    public void play() {
        audioClip.start();
    }

    public void pause() {
        audioClip.stop();
    }

    public void stop() {
        pause();
        setFramePosition(0);
    }

    public void setFramePosition(int frames) {
        audioClip.setFramePosition(frames);
    }

    public int getFramePosition() {
        return audioClip.getFramePosition();
    }

    public int getFrameLength() {
        return audioClip.getFrameLength();
    }

    public void setMute(boolean value) {
        BooleanControl mute = (BooleanControl) audioClip.getControl(BooleanControl.Type.MUTE);
        mute.setValue(value);
    }

    public boolean getMute() {
        BooleanControl mute = (BooleanControl) audioClip.getControl(BooleanControl.Type.MUTE);
        return mute.getValue();
    }

    public void setMasterGain(float newValue) {
        try {
            FloatControl masterGain = (FloatControl) audioClip.getControl(FloatControl.Type.MASTER_GAIN);
            masterGain.setValue(newValue);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();

        } 
    }

    public float getMasterGain() {
        FloatControl masterGain = (FloatControl) audioClip.getControl(FloatControl.Type.MASTER_GAIN);
        return masterGain.getValue();
    }

    public void setPan(float newValue) {
        FloatControl pan = (FloatControl)audioClip.getControl(FloatControl.Type.PAN);
        pan.setValue(newValue);
    }

    public float getPan() {
        FloatControl pan = (FloatControl)audioClip.getControl(FloatControl.Type.PAN);
        return pan.getValue();
    }

}
