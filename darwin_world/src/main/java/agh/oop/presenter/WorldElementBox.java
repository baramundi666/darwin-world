package agh.oop.presenter;

import agh.oop.model.objects.WorldElement;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class WorldElementBox {

    private VBox vBox;
    public WorldElementBox(WorldElement worldElement, double width, double height) {
        addImage(worldElement, width, height);
    }

    public VBox getVBox() {
        return vBox;
    }

    private void addImage(WorldElement worldElement, double width, double height) {
        var image = new Image(worldElement.getImage());
        var imageView = new ImageView();
        imageView.setImage(image);
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        var box = new VBox();
        box.getChildren().addAll(imageView);
        box.setAlignment(Pos.CENTER);
        vBox = box;
    }
}
