package agh.oop.objects;

import agh.oop.map.Vector2d;

public interface WorldElement {
    public boolean isAt(Vector2d position);

    public Vector2d getPosition();
}
