package ir.sharif.math.ap2023.project.controller.sound;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

public final class SoundManager {
    private static SoundManager instance;
    private Clip clip;
    private Clip SEClip;
    private HashMap<SoundEffectType, URL> soundEffects = new HashMap<>();
    private HashMap<BackgroundMusicType, URL> backgroundMusics = new HashMap<>();

    private SoundManager() {
        try {
            clip = AudioSystem.getClip();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }

        soundEffects.put(SoundEffectType.DIE, getClass().getResource("/sound/sound-effect/Die.wav"));
        soundEffects.put(SoundEffectType.JUMP, getClass().getResource("/sound/sound-effect/Jump.wav"));
        soundEffects.put(SoundEffectType.BREAK, getClass().getResource("/sound/sound-effect/Break.wav"));
        soundEffects.put(SoundEffectType.COIN, getClass().getResource("/sound/sound-effect/Coin.wav"));
        soundEffects.put(SoundEffectType.FIREBALL, getClass().getResource("/sound/sound-effect/Fire Ball.wav"));
        soundEffects.put(SoundEffectType.FLAG, getClass().getResource("/sound/sound-effect/Flagpole.wav"));
        soundEffects.put(SoundEffectType.GAME_OVER, getClass().getResource("/sound/sound-effect/Game Over.wav"));
        soundEffects.put(SoundEffectType.ITEM, getClass().getResource("/sound/sound-effect/Item.wav"));
        soundEffects.put(SoundEffectType.KICK, getClass().getResource("/sound/sound-effect/Kick.wav"));
        soundEffects.put(SoundEffectType.PAUSE, getClass().getResource("/sound/sound-effect/Pause.wav"));
        soundEffects.put(SoundEffectType.POWERUP, getClass().getResource("/sound/sound-effect/Powerup.wav"));
        soundEffects.put(SoundEffectType.SQUISH, getClass().getResource("/sound/sound-effect/Squish.wav"));

        backgroundMusics.put(BackgroundMusicType.BOSS, getClass().getResource("/sound/background/boss.wav"));
        backgroundMusics.put(BackgroundMusicType.CASTLE, getClass().getResource("/sound/background/castle.wav"));
        backgroundMusics.put(BackgroundMusicType.INVINCIBLE, getClass().getResource("/sound/background/invincible.wav"));
        backgroundMusics.put(BackgroundMusicType.OVERWORLD, getClass().getResource("/sound/background/overworld.wav"));
        backgroundMusics.put(BackgroundMusicType.UNDERGROUND, getClass().getResource("/sound/background/underground.wav"));
        backgroundMusics.put(BackgroundMusicType.END, getClass().getResource("/sound/background/end.wav"));
        backgroundMusics.put(BackgroundMusicType.FLAG, getClass().getResource("/sound/background/flag.wav"));
    }

    public static SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }

    public void playBackgroundMusic(BackgroundMusicType music) {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(backgroundMusics.get(music));
            clip.open(ais);
        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException | IllegalStateException e) {
            e.printStackTrace();
        }
        clip.start();
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void playSoundEffect(SoundEffectType sound) {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundEffects.get(sound));
            SEClip = AudioSystem.getClip();
            SEClip.open(ais);
        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }
        SEClip.start();
    }

    public void pauseMusic() {
        clip.stop();
        clip.close();
    }
}
