package agh.oop.map;

import agh.oop.map.Vector2d;

import java.util.Optional;

public interface MoveValidator {
    Optional<Vector2d> mover(Vector2d newPosition);
}
