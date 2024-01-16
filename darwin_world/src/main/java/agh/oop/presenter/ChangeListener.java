package agh.oop.presenter;

import agh.oop.model.map.Boundary;
import agh.oop.model.map.Earth;

public interface ChangeListener {
    void mapInitialized(Earth earth, String message);

    void mapChanged(Earth earth, String message);
}
