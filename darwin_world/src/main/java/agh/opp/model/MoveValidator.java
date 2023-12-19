package agh.opp.model;

import java.util.Optional;

public interface MoveValidator {
    Optional<Vector2d> mover(Vector2d newPosition);
}
