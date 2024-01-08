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
    private double imageWidth;
    private double imageHeight;

    public ImageGenerator(int width, int height, double imageWidth, double imageHeight) {
        this.width=width;
        this.height=height;
        this.imageWidth=imageWidth;
        this.imageHeight=imageHeight;
    }


    public LinkedList<Node> generateAnimalImageList() {
        var generatedAnimalImages = new LinkedList<Node>();
        for(int i=0; i<width; i++) {
            for(int j=0; j<height; j++) {
                var animalNode = new NodeGenerator("oldAnimal.png", imageWidth, imageHeight);
                var animalImage = animalNode.getNode();
                generatedAnimalImages.add(animalImage);
            }
        }
        return generatedAnimalImages;
    }

    public LinkedList<Node> generatePlantImageList() {
        var generatedPlantImages = new LinkedList<Node>();
        for(int i=0; i<width; i++) {
            for(int j=0; j<height; j++) {
                var plantNode = new NodeGenerator("plant.png", imageWidth, imageHeight);
                var plantImage = plantNode.getNode();
                generatedPlantImages.add(plantImage);
            }
        }
        return generatedPlantImages;
    }

    public LinkedList<Node> generatePoisonousPlantImageList() {
        var generatedPoisonousPlantImage = new LinkedList<Node>();
        for(int i=0; i<width*height*0.2; i++) {
            var plantNode = new NodeGenerator("poisonousPlant.png", imageWidth, imageHeight);
            var plantImage = plantNode.getNode();
            generatedPoisonousPlantImage.add(plantImage);

        }
        return generatedPoisonousPlantImage;
    }

    public LinkedList<Node> generateSteppeImageList() {
        var generatedSteppeImages = new LinkedList<Node>();
        for(int i=0; i<width*height; i++) {
            var steppeNode = new NodeGenerator("steppe.png", imageWidth, imageHeight);
            var steppeImage = steppeNode.getNode();
            generatedSteppeImages.add(steppeImage);
        }
        return generatedSteppeImages;
    }

    public LinkedList<Node> generateJungleImageList() {
        var generatedJungleImages = new LinkedList<Node>();
        for(int i=0; i<width*height*0.5; i++) {
            var jungleNode = new NodeGenerator("jungle.png", imageWidth, imageHeight);
            var jungleImage = jungleNode.getNode();
            generatedJungleImages.add(jungleImage);
        }
        return generatedJungleImages;
    }
}
