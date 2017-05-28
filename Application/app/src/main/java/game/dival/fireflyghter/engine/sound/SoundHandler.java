package game.dival.fireflyghter.engine.sound;

/**
 * Created by caiomcg on 28/05/2017.
 */

public class SoundHandler {
    private int id;
    private boolean loop;
    private String source;
    private float volume = 1.0f;

    public SoundHandler(String source, boolean loop) {
        this.source = source;
        this.loop = loop;
    }

    public SoundHandler setVolume(float volume) {
        this.volume = volume;
        return this;
    }

    public float getVolume() {
        return this.volume;
    }

    public Integer getID() {
        return this.id;
    }

    public void setID(Integer id) {
        this.id = id;
    }

    public boolean shouldLoop() {
        return this.loop;
    }

    public String getSourceName() {
        return this.source;
    }
}
