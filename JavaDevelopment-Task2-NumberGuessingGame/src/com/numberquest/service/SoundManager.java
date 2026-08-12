package com.numberquest.service;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

/**
 * Service providing synthesized sound effects using pure Java Sound API 
 * (javax.sound.sampled), requiring zero external audio assets.
 */
public class SoundManager {
    private boolean soundEnabled;
    private static final float SAMPLE_RATE = 8000f;

    public SoundManager() {
        this.soundEnabled = true;
    }

    public boolean isSoundEnabled() {
        return soundEnabled;
    }

    public void setSoundEnabled(boolean soundEnabled) {
        this.soundEnabled = soundEnabled;
    }

    public void toggleSound() {
        this.soundEnabled = !this.soundEnabled;
    }

    /**
     * Plays a high-pitched beep when guess is "Too High".
     */
    public void playTooHighSound() {
        if (!soundEnabled) return;
        playToneSequence(new int[]{784, 880}, new int[]{80, 80}); // G5 -> A5
    }

    /**
     * Plays a low-pitched tone when guess is "Too Low".
     */
    public void playTooLowSound() {
        if (!soundEnabled) return;
        playToneSequence(new int[]{261, 220}, new int[]{80, 80}); // C4 -> A3
    }

    /**
     * Plays a triumphant melody sequence on victory.
     */
    public void playVictorySound() {
        if (!soundEnabled) return;
        playToneSequence(new int[]{523, 659, 784, 1046}, new int[]{100, 100, 100, 250}); // C5-E5-G5-C6
    }

    /**
     * Plays a descending defeat sequence on game over.
     */
    public void playDefeatSound() {
        if (!soundEnabled) return;
        playToneSequence(new int[]{440, 392, 349, 293}, new int[]{120, 120, 120, 300}); // A4-G4-F4-D4
    }

    /**
     * Plays a simple click sound for UI actions.
     */
    public void playClickSound() {
        if (!soundEnabled) return;
        playToneSequence(new int[]{600}, new int[]{40});
    }

    /**
     * Synthesizes audio frequencies asynchronously so UI thread never blocks.
     */
    private void playToneSequence(int[] frequencies, int[] durationsMs) {
        new Thread(() -> {
            try {
                AudioFormat af = new AudioFormat(SAMPLE_RATE, 8, 1, true, false);
                SourceDataLine sdl = AudioSystem.getSourceDataLine(af);
                sdl.open(af);
                sdl.start();

                for (int i = 0; i < frequencies.length; i++) {
                    byte[] buf = generateSineWave(frequencies[i], durationsMs[i]);
                    sdl.write(buf, 0, buf.length);
                }

                sdl.drain();
                sdl.close();
            } catch (LineUnavailableException | IllegalArgumentException e) {
                // Audio device unavailable or restricted; fail gracefully silently
            }
        }).start();
    }

    private byte[] generateSineWave(int frequencyHz, int durationMs) {
        int samples = (int) (SAMPLE_RATE * durationMs / 1000);
        byte[] buffer = new byte[samples];
        for (int i = 0; i < samples; i++) {
            double angle = 2.0 * Math.PI * i / (SAMPLE_RATE / frequencyHz);
            // Apply simple fade out to avoid speaker pops
            double fade = 1.0 - ((double) i / samples);
            buffer[i] = (byte) (Math.sin(angle) * 127.0 * fade);
        }
        return buffer;
    }
}
