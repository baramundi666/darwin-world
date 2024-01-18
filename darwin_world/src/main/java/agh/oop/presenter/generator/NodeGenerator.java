package agh.oop.presenter.generator;

import agh.oop.model.objects.WorldElement;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class NodeGenerator {

    private Node node;
    public NodeGenerator(String imageURL, double width, double height) {
        addImage(imageURL, width, height);
    }

    public Node getNode() {
        return node;
    }

    private void addImage(String imageURL, double width, double height) {
        var image = new Image(imageURL);
        var imageView = new ImageView();
        imageView.setImage(image);
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        node = imageView;
    }
}
