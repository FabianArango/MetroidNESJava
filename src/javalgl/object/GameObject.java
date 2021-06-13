package javalgl.object;

import java.awt.Graphics2D;
import java.util.LinkedList;

import javalgl.object.component.Transform;

public class GameObject {
    protected final LinkedList<GameObjectGroup> myGameObjectGroups = new LinkedList<GameObjectGroup>();

    public final Transform transform = new Transform();
    private int layer = 0;

    public LinkedList<GameObjectGroup> getMyGameObjectGroups() {
        return myGameObjectGroups;
    }

    public void setLayer(int layer) {
        this.layer = layer;
        for (GameObjectGroup gameObjectGroup : myGameObjectGroups) {
            gameObjectGroup.orderLayer();
        }
    }

    public int getLayer() {
        return layer;
    }

    public void destroy() {
        for (GameObjectGroup gameObjectGroup : myGameObjectGroups) {
            gameObjectGroup.getMyGameObjects().remove(this);
        }
        myGameObjectGroups.clear();
    }

    public void runTick() {
        
    }

    public void render(Graphics2D graphics2d) {

    }

}

