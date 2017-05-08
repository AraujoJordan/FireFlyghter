package game.dival.fireflyghter.engine.entity;

import java.util.ArrayList;
import java.util.List;

import game.dival.fireflyghter.engine.GameEngine;
import game.dival.fireflyghter.engine.entity.components.BoxCollision;
import game.dival.fireflyghter.engine.entity.components.Component;
import game.dival.fireflyghter.engine.entity.components.Physics;
import game.dival.fireflyghter.engine.entity.components.Transformation;
import game.dival.fireflyghter.engine.entity.components.model3d.Model3D;

/**
 * Created by arauj on 05/03/2017.
 */

public class Entity {
    public final String label;
    public List<Component> components;

    // COMPONENTS REFERENCE FOR OPTIMIZATION
    private Transformation transformation;
    private Model3D model3D;
    private Physics physics;
    private BoxCollision boxCollision;

    public Entity() {
        label = getClass().getCanonicalName();
        components = new ArrayList<>();
    }

    public Entity(String label) {
        this.label = label;
        components = new ArrayList<>();
    }

    public Transformation getTransformation() {
        if (transformation != null)
            return transformation;
        for (Component component : components) {
            if (component instanceof Transformation) {
                transformation = (Transformation) component;
                return transformation;
            }
        }
        throw new NullPointerException(label + ": Does'nt have Transformation component!");
    }

    public Model3D getModel3D() {
        if (model3D != null)
            return model3D;
        for (Component component : components) {
            if (component instanceof Model3D) {
                model3D = (Model3D) component;
                return model3D;
            }
        }
        throw new NullPointerException(label + ": Does'nt have Model3D component!");
    }

    public BoxCollision getBoxCollision() {
        if (boxCollision != null)
            return boxCollision;
        for (Component component : components) {
            if (component instanceof BoxCollision) {
                boxCollision = (BoxCollision) component;
                return boxCollision;
            }
        }
        throw new NullPointerException(label + ": Does'nt have BoxCollision component!");
    }

    public Physics getPhysics() {
        if (physics != null)
            return physics;
        for (Component component : components) {
            if (component instanceof Physics) {
                physics = (Physics) component;
                return physics;
            }
        }
        throw new NullPointerException(label + ": Does'nt have Physics component!");
    }

    /**
     * Remove the component from optimization variables (references) and from the list
     *
     * @param removeMe the variable that you want to be removed
     */
    public void removeComponent(Component removeMe) {
        //IS THIS NECESSARY TO CLEAR THE OBJECT REFERENCE? OR JUST REMOVE FROM LIST IT WILL BE NULL TOO?
        if (removeMe instanceof Transformation)
            transformation = null;
        if (removeMe instanceof Model3D)
            model3D = null;
        if (removeMe instanceof BoxCollision)
            boxCollision = null;
        if (removeMe instanceof Physics)
            physics = null;

        //REMOVE FROM LIST TOO
        components.remove(removeMe);
    }

    public void run(GameEngine engine, float[] mvp) {
        for (Component component : components)
            component.run(engine, mvp);
    }

    public void addComponent(Component addMe) {
        if (addMe instanceof Transformation)
            transformation = (Transformation) addMe;
        if (addMe instanceof Model3D)
            model3D = (Model3D) addMe;
        if (addMe instanceof BoxCollision)
            boxCollision = (BoxCollision) addMe;
        if (addMe instanceof Physics)
            physics = (Physics) addMe;

        components.add(addMe);
    }

}
