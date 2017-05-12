package game.dival.fireflyghter.engine.entity.components;

import java.util.ArrayList;
import java.util.Random;

import game.dival.fireflyghter.engine.GameEngine;
import game.dival.fireflyghter.engine.draw.Color;
import game.dival.fireflyghter.engine.entity.Entity;
import game.dival.fireflyghter.engine.entity.components.model3d.Model3D;
import game.dival.fireflyghter.engine.math.Vector3D;

/**
 * Created by arauj on 27/03/2017.
 */

public class FireParticles extends Entity {

    private final int PARTICLE_MAX = 30;
    private ArrayList<ParticleTriangle> triangles;
    private Random gerador = new Random();
    private GameEngine gameEngine;

    public FireParticles(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
        triangles = new ArrayList<>();
    }

    public void initFire() {
        for (int i = 0; i < PARTICLE_MAX; i++) {
            triangles.add(new ParticleTriangle(this));
        }
    }

    public void run(GameEngine engine, float[] mvp) {
        for (ParticleTriangle particle : triangles) {
            if (particle.isOver())
                particle = new ParticleTriangle(this);
            particle.run(engine);
        }
    }


    public class ParticleTriangle extends Entity {

        private final float[] RED = {1, 0, 0, 1};
        private final float[] BLACK = {0, 1, 1, 0};

        private final float TRIANGLE_SIZE = 0.25f;
        private final float FIRE_HEIGHT = 2f;

        private final Vector3D direction;
        private final Physics physics;
        private final Transformation transformation;
        private final Model3D modelDraw;
        private final FireParticles fireEmissor;

        public ParticleTriangle(FireParticles fireEmissor) {
            this.fireEmissor = fireEmissor;
            direction = new Vector3D(gerador.nextFloat() - 0.5f, 1, gerador.nextFloat() - 0.5f);
            physics = new Physics(this, direction, FIRE_HEIGHT, true);
            transformation = new Transformation(this);
            transformation.setTranslation(new Vector3D(
                    fireEmissor.getTransformation().getTranslation().getX(),
                    fireEmissor.getTransformation().getTranslation().getY(),
                    fireEmissor.getTransformation().getTranslation().getZ()
            ));
            modelDraw = new Model3D(this, TRIANGLE_SIZE, gameEngine);

            addComponent(transformation);
            addComponent(modelDraw);
            addComponent(physics);
        }

        public boolean isOver() {
            return physics.inertiaVector.getY() < 0;
        }

        public void run(GameEngine engine) {
            double distanceYPercent = (1.0d - (getTransformation().getTranslation().getY()
                    - fireEmissor.getTransformation().getTranslation().getY()) /
                    (fireEmissor.getTransformation().getTranslation().getY() + FIRE_HEIGHT -
                            fireEmissor.getTransformation().getTranslation().getY()));
            float[] rgbaf = new float[4];
            for (int index = 0; index < 4; index++)
                rgbaf[index] = (float) (RED[index] * distanceYPercent + BLACK[index] * (1 - distanceYPercent));

            getModel3D().setColor(new Color(rgbaf[0], rgbaf[1], rgbaf[2], rgbaf[3]));

            for (Component component : this.getComponents()) {
                component.run(gameEngine);
            }
        }
    }
}
