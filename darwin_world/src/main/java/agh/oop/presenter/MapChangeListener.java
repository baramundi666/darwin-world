package agh.oop.presenter;

import agh.oop.model.map.Earth;

public interface MapChangeListener {
    void mapChanged(Earth earth, String message);
}
