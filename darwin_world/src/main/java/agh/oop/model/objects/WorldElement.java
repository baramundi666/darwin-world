package agh.oop.model.objects;

import agh.oop.model.map.Vector2d;

public interface WorldElement {
    public boolean isAt(Vector2d position);
    public Vector2d getPosition();

    String getImage();
}
