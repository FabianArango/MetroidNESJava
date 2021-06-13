package main.java.script.general;

import javalgl.object.AudioSource;
import main.java.Main;

public class Sound {

    public static final AudioSource PAUSE = new AudioSource(AudioSource.load(Main.class.getResource("/main/resources/sound/sound fx/Pause.wav")));

    public static final AudioSource NORMAL_BEAM_0 = new AudioSource(AudioSource.load(Main.class.getResource("/main/resources/sound/sound fx/Normal Beam.wav")));
    public static final AudioSource NORMAL_BEAM_1 = new AudioSource(AudioSource.load(Main.class.getResource("/main/resources/sound/sound fx/Normal Beam.wav")));
    public static final AudioSource WAVE_BEAM_0 = new AudioSource(AudioSource.load(Main.class.getResource("/main/resources/sound/sound fx/Wave Beam.wav")));
    public static final AudioSource WAVE_BEAM_1 = new AudioSource(AudioSource.load(Main.class.getResource("/main/resources/sound/sound fx/Wave Beam.wav")));
    public static final AudioSource ICE_BEAM_0 = new AudioSource(AudioSource.load(Main.class.getResource("/main/resources/sound/sound fx/Ice Beam.wav")));
    public static final AudioSource ICE_BEAM_1 = new AudioSource(AudioSource.load(Main.class.getResource("/main/resources/sound/sound fx/Ice Beam.wav")));
    public static final AudioSource MISSILE_0 = new AudioSource(AudioSource.load(Main.class.getResource("/main/resources/sound/sound fx/Missile.wav")));
    public static final AudioSource MISSILE_1 = new AudioSource(AudioSource.load(Main.class.getResource("/main/resources/sound/sound fx/Missile.wav")));
    public static final AudioSource PUT_A_BOMB_0 = new AudioSource(AudioSource.load(Main.class.getResource("/main/resources/sound/sound fx/Put a Bomb.wav")));
    public static final AudioSource PUT_A_BOMB_1 = new AudioSource(AudioSource.load(Main.class.getResource("/main/resources/sound/sound fx/Put a Bomb.wav")));
    public static final AudioSource BOMB_0 = new AudioSource(AudioSource.load(Main.class.getResource("/main/resources/sound/sound fx/Bomb.wav")));
    public static final AudioSource BOMB_1 = new AudioSource(AudioSource.load(Main.class.getResource("/main/resources/sound/sound fx/Bomb.wav")));

    public static final AudioSource SAMUS_JUMP = new AudioSource(AudioSource.load(Main.class.getResource("/main/resources/sound/sound fx/Samus Jump.wav")));
    public static final AudioSource SCREW_ATTACK = new AudioSource(AudioSource.load(Main.class.getResource("/main/resources/sound/sound fx/Screw Attack.wav")));

}
