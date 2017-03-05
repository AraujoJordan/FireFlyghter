package game.dival.fireflighter.engine.Engine.gameentity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by arauj on 05/03/2017.
 */

abstract class Entity {
    public final String label;
    public List<Component> properties;

    Entity() {
        label = getClass().getCanonicalName();
        properties = new ArrayList<>();
    }

    Entity(String label) {
        this.label = label;
        properties = new ArrayList<>();
    }

    /**
     * Update with the first Transformation property in the gamePropList
     * @param oldIndex the last index with the Transformation, if -1 will be crate a new one
     * @return if the oldIndex is the right one, it will be returned, if not, it will search on the list
     */
    public int updateTransformationIndex(int oldIndex) {

        Transformation transformation;
        try {
            transformation = (Transformation) properties.get(oldIndex);
            return oldIndex;
        } catch (IndexOutOfBoundsException | ClassCastException exception) {
            for (int newIndex = 0; newIndex <= properties.size(); newIndex++) {
                if (properties.get(newIndex) instanceof Transformation) {
                    return newIndex;
                }
            }
            throw new NullPointerException(label+": Does'nt found Transformation property index!");
        }
    }


    /**
     * Update with the first Model property in the gamePropList
     * @param oldIndex the last index with the Model, if -1 will be crate a new one
     * @return if the oldIndex is the right one, it will be returned, if not, it will search on the list
     */
    public int updateModelIndex(int oldIndex) {

        Model model;
        try {
            model = (Model) properties.get(oldIndex);
            return oldIndex;
        } catch (IndexOutOfBoundsException | ClassCastException exception) {
            for (int newIndex = 0; newIndex <= properties.size(); newIndex++) {
                if (properties.get(newIndex) instanceof Transformation) {
                    return newIndex;
                }
            }
            throw new NullPointerException(label+": Does'nt found Model property index!");
        }
    }


    /**
     * Update with the first BoxCollision property in the gamePropList
     * @param oldIndex the last index with the BoxCollision, if -1 will be crate a new one
     * @return if the oldIndex is the right one, it will be returned, if not, it will search on the list
     */
    public int updateBoxColliderIndex(int oldIndex) {

        BoxCollision model;
        try {
            model = (BoxCollision) properties.get(oldIndex);
            return oldIndex;
        } catch (IndexOutOfBoundsException | ClassCastException exception) {
            for (int newIndex = 0; newIndex <= properties.size(); newIndex++) {
                if (properties.get(newIndex) instanceof BoxCollision) {
                    return newIndex;
                }
            }
            throw new NullPointerException(label+": Does'nt found BoxCollision property index!");
        }
    }


}
