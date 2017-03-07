package game.dival.fireflyghter.engine.entity;

import java.util.ArrayList;
import java.util.List;

import game.dival.fireflyghter.engine.entity.components.BoxCollision;
import game.dival.fireflyghter.engine.entity.components.Component;
import game.dival.fireflyghter.engine.entity.components.model3d.Model3D;
import game.dival.fireflyghter.engine.entity.components.Physics;
import game.dival.fireflyghter.engine.entity.components.Transformation;

/**
 * Created by arauj on 05/03/2017.
 */

public class Entity {
    public final String label;
    public List<Component> components;

    public Entity() {
        label = getClass().getCanonicalName();
        components = new ArrayList<>();
    }

    public Entity(String label) {
        this.label = label;
        components = new ArrayList<>();
    }

    /**
     * Get the index of the first Transformation component in the list, return the correct index if the old is not updated
     *
     * @param oldIndex the last index with the Transformation, if -1, it will be crate a new one
     * @return if the oldIndex is the right one, it will be returned, if not, it will search on the list
     */
    public int getTransformation(int oldIndex) {

        Transformation transformation;
        try {
            transformation = (Transformation) components.get(oldIndex);
            return oldIndex;
        } catch (IndexOutOfBoundsException | ClassCastException exception) {
            for (int newIndex = 0; newIndex < components.size(); newIndex++) {
                if (components.get(newIndex) instanceof Transformation) {
                    return newIndex;
                }
            }
            throw new NullPointerException(label + ": Does'nt found Transformation property index!");
        }
    }


    /**
     * Get the index of the first Model3D component in the list, return the correct index if the old is not updated
     *
     * @param oldIndex the last index with the Model3D, if -1, it will be crate a new one
     * @return if the oldIndex is the right one, it will be returned, if not, it will search on the list
     */
    public int getModel3D(int oldIndex) {

        Model3D model3D;
        try {
            model3D = (Model3D) components.get(oldIndex);
            return oldIndex;
        } catch (IndexOutOfBoundsException | ClassCastException exception) {
            for (int newIndex = 0; newIndex < components.size(); newIndex++) {
                if (components.get(newIndex) instanceof Model3D) {
                    return newIndex;
                }
            }
            throw new NullPointerException(label + ": Does'nt found Model3D property index!");
        }
    }


    /**
     * Get the index of the first BoxCollision component in the list, return the correct index if the old is not updated
     *
     * @param oldIndex the last index with the BoxCollision, if -1, it will be crate a new one
     * @return if the oldIndex is the right one, it will be returned, if not, it will search on the list
     */
    public int getBoxCollision(int oldIndex) {

        BoxCollision model;
        try {
            model = (BoxCollision) components.get(oldIndex);
            return oldIndex;
        } catch (IndexOutOfBoundsException | ClassCastException exception) {
            for (int newIndex = 0; newIndex < components.size(); newIndex++) {
                if (components.get(newIndex) instanceof BoxCollision) {
                    return newIndex;
                }
            }
            throw new NullPointerException(label + ": Does'nt found BoxCollision property index!");
        }
    }


    /**
     * Get the index of the first Physics component in the list, return the correct index if the old is not updated
     *
     * @param oldIndex the last index with the Physics, if -1, it will be crate a new one
     * @return if the oldIndex is the right one, it will be returned, if not, it will search on the list
     */
    public int getPhysics(int oldIndex) {
        Physics physics;
        try {
            physics = (Physics) components.get(oldIndex);
            return oldIndex;
        } catch (IndexOutOfBoundsException | ClassCastException exception) {
            for (int newIndex = 0; newIndex < components.size(); newIndex++) {
                if (components.get(newIndex) instanceof Physics) {
                    return newIndex;
                }
            }
            throw new NullPointerException(label + ": Does'nt found Physics property index!");
        }
    }


}
