package agh.oop.model.map;

import java.util.Optional;

public interface MapOptions {
    Optional<Vector2d> mover(Vector2d newPosition);//name to change??

    boolean isInBounds(Vector2d position);
}
