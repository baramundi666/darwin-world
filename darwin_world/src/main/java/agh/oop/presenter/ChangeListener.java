package agh.oop.presenter;

import agh.oop.model.map.Earth;

public interface ChangeListener {
    void mapChanged(Earth earth, String message);
}
