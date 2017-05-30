package game.dival.fireflyghter.engine.entity.components;

import android.content.Context;
import android.graphics.Color;

import game.dival.fireflyghter.engine.GameEngine;
import game.dival.fireflyghter.engine.VrActivity;
import game.dival.fireflyghter.engine.entity.Entity;
import game.dival.fireflyghter.engine.particles.Geometry;
import game.dival.fireflyghter.engine.particles.ParticleShaderProgram;
import game.dival.fireflyghter.engine.particles.ParticleShooter;
import game.dival.fireflyghter.engine.particles.ParticleSystem;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;

/**
 * Created by luzenildojunior on 30/05/17.
 */

public class Particles extends Component{
    private final Context context;
    private GameEngine gameEngine;
    private ParticleShaderProgram particleProgram;
    private ParticleSystem particleSystem;
    private ParticleShooter redParticleShooter;
    private long globalStartTime;

    public Particles(Context context, GameEngine gameEngine){
        this.context = context;
        this.gameEngine = gameEngine;
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        particleProgram = new ParticleShaderProgram(context);
        particleSystem = new ParticleSystem(10000);
        globalStartTime = System.nanoTime();
        final Geometry.Vector particleDirection = new Geometry.Vector(0f, 0.5f, 0f);
        redParticleShooter = new ParticleShooter(new Geometry.Point(-1f, 0f, 0f), particleDirection, Color.rgb(255, 50, 5));
        glClear(GL_COLOR_BUFFER_BIT);
        float currentTime = (System.nanoTime() - globalStartTime) / 1000000000f;
        redParticleShooter.addParticles(particleSystem, currentTime, 5);
        particleProgram.useProgram();
        particleProgram.setUniforms(VrActivity.mProjectionViewMatrix, currentTime);
        particleSystem.bindData(particleProgram);
        particleSystem.draw();
    }

    @Override
    public void run(GameEngine engine) {
        //particleSystem.draw();
    }
}
