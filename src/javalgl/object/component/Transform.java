package javalgl.object.component;

import javalgl.object.Vector2;
import javalgl.controll.Base;

public class Transform {
        public Vector2 position = new Vector2(0, 0);
        private float rotation = 0;
        private float[] scale = {1, 1};

        public void setRotation(float rotation) {
            this.rotation = rotation;
        }

        public float getRotation() {
            return rotation;
        }

        public void setScale(float x, float y) {
            this.scale = new float[] {x, y} ;
        }

        public float[] getScale() {
            return scale;
        }

        public void setScreenPosition(float xScreen, float yScreen) {
            float x = xScreen+Base.cameraPosition[0];
            float y = yScreen+Base.cameraPosition[1];
            position.setPoints(x, y, x, y);
        }

        public float[] getScreenPosition() {
            return new float[] {position.getX()-Base.cameraPosition[0], position.getY()-Base.cameraPosition[1]};
        }
}
