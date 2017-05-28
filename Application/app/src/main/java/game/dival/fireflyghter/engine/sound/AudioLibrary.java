package game.dival.fireflyghter.engine.sound;

import android.content.Context;

import com.google.vr.sdk.audio.GvrAudioEngine;

import java.util.ArrayList;

import static com.google.vr.sdk.audio.GvrAudioEngine.INVALID_ID;

/**
 * Created by caiomcg on 28/05/2017.
 */

public final class AudioLibrary {
    private GvrAudioEngine engine;
    private ArrayList<SoundHandler> audioSources;

    public AudioLibrary(Context context) {
        engine = new GvrAudioEngine(context, GvrAudioEngine.RenderingMode.STEREO_PANNING);
        audioSources = new ArrayList<>();
    }

    public AudioLibrary addStereoSource(SoundHandler soundHandler) {
        if (!audioSources.contains(soundHandler)) {
            audioSources.add(soundHandler);
            soundHandler.setID(engine.createStereoSound(soundHandler.getSourceName()));
            engine.setSoundVolume(soundHandler.getID(), soundHandler.getVolume());
        }
        return this;
    }

    public void startAll() {
        for (SoundHandler s : audioSources) {
            engine.playSound(s.getID(), s.shouldLoop());
        }
    }

    public void pauseAll() {
        for (SoundHandler s : audioSources) {
            engine.pauseSound(s.getID());
        }
    }

    public void startHandler(SoundHandler s) {
        if (!audioSources.contains(s)) {
            addStereoSource(s);
        }
        engine.playSound(s.getID(), s.shouldLoop());
    }
}
