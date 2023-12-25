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

    private double width;
    private double height;

    public ImageGenerator(double width, double height) {
        this.width=width;
        this.height=height;
    }


    public LinkedList<Node> generateAnimalImageList() {
        var generatedAnimalImages = new LinkedList<Node>();
        for(int i=0; i<width; i++) {
            for(int j=0; j<height; j++) {
                var animalNode = new NodeGenerator("animal.png", 500/width, 500/height);
                var animalImage = animalNode.getNode();
                generatedAnimalImages.add(animalImage);
            }
        }
        return generatedAnimalImages;
    }

    public LinkedList<Node> generatePlantImageList(boolean isPoisonous) {
        var generatedPlantImages = new LinkedList<Node>();
        String plantURL;
        if (isPoisonous) {
            plantURL = "poisonousPlant.png";
        }
        else {
            plantURL = "plant.png";
        }
        for(int i=0; i<width; i++) {
            for(int j=0; j<height; j++) {
                var plantNode = new NodeGenerator(plantURL, 500/width, 500/height);
                var plantImage = plantNode.getNode();
                generatedPlantImages.add(plantImage);
            }
        }
        return generatedPlantImages;
    }

    public LinkedList<Node> generateSteppeImageList() {
        var generatedSteppeImages = new LinkedList<Node>();
        var position = new Vector2d(0,0);
        for(int i=0; i<width; i++) {
            for(int j=0; j<height; j++) {
                var steppeNode = new NodeGenerator("steppe.png", 500/width, 500/height);
                var steppeImage = steppeNode.getNode();
                generatedSteppeImages.add(steppeImage);
            }
        }
        return generatedSteppeImages;
    }
}
