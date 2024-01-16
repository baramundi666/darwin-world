package agh.oop.presenter;

import agh.oop.model.map.Vector2d;
import agh.oop.model.objects.Animal;
import agh.oop.model.objects.Plant;
import agh.oop.model.objects.inheritance.Genome;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.util.LinkedList;
import java.util.List;

public class ImageGenerator {

    private final double width;
    private final double height;
    private final double imageWidth;
    private final double imageHeight;

    public ImageGenerator(int width, int height, double imageWidth, double imageHeight) {
        this.width=width;
        this.height=height;
        this.imageWidth=imageWidth;
        this.imageHeight=imageHeight;
    }

    public LinkedList<Node> generateImageList(String url, double coefficient) {
        var generatedImages = new LinkedList<Node>();
        for(int i=0; i<width*height*coefficient; i++) {
            var Node = new NodeGenerator(url, imageWidth, imageHeight);
            var Image = Node.getNode();
            generatedImages.add(Image);
        }
        return generatedImages;
    }
}
