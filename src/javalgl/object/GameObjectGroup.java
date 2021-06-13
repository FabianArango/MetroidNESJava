package javalgl.object;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Collections;
import java.util.Comparator;

import java.awt.Graphics2D;

public class GameObjectGroup implements Iterable<GameObject> {
    private final LinkedList<GameObject> myGameObjects = new LinkedList<GameObject>();

    public LinkedList<GameObject> getMyGameObjects() {
        return myGameObjects;
    }

    @Override
    public Iterator<GameObject> iterator() {
        return getMyGameObjects().iterator();
    }

    
    public void orderLayer(){
        Collections.sort(myGameObjects, new Comparator<GameObject>(){
            public int compare(GameObject gameObject1, GameObject gameObject2) {
                if(gameObject1.getLayer() == gameObject2.getLayer())
                    return 0;
                return gameObject1.getLayer() < gameObject2.getLayer() ? -1 : 1;
            }    
        });
    }

    public void add(GameObject gameObject) {
        myGameObjects.add(gameObject);
        gameObject.getMyGameObjectGroups().add(this);
        orderLayer();
    }

    public void add(GameObject[] gameObjects) {
        for (GameObject gameObject : gameObjects) {
            add(gameObject);
        }
    }

    public void remove(GameObject gameObject) {
        myGameObjects.remove(gameObject);
        gameObject.getMyGameObjectGroups().remove(this);
    }

    public void remove(GameObject[] gameObjects) {
        for (GameObject gameObject : gameObjects) {
            remove(gameObject);
        }
    }

    public void removeAtIndex(int index) {
        int i = 0;

        index = (size()-1)+(index+1);
        if (index >= size()) {
            index -= size();
        }

        for (GameObject gameObject : myGameObjects) {
            if (i == index) {
                remove(gameObject);
                break;
            }
            i ++;            
        }
    }

    public void removeFisrt() {
        removeAtIndex(0);
    }

    public void removeLast() {
        removeAtIndex(-1);
    }

    public int size() {
        return myGameObjects.size();
    }

    public void clear() {
        myGameObjects.clear();
    }

    public void runTick() {
        for (GameObject gameObject : myGameObjects) {
            gameObject.runTick();
        }
    }

    public void render(Graphics2D graphics2d) {
        for (GameObject gameObject : myGameObjects) {
            gameObject.render(graphics2d);
        }
    }

}

