package agh.oop.presenter;

import agh.oop.model.map.Vector2d;
import agh.oop.model.objects.Animal;
import agh.oop.model.objects.Plant;
import agh.oop.model.objects.inheritance.Genome;
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


    public LinkedList<VBox> generateAnimalImageList() {
        var generatedAnimalImages = new LinkedList<VBox>();
        var position = new Vector2d(0,0);
        var genome = new Genome(List.of(0),1);
        for(int i=0; i<width; i++) {
            for(int j=0; j<height; j++) {
                var generatedAnimal = new Animal(position, 0, genome , 0);
                var animalBox = new WorldElementBox(generatedAnimal, 500/width, 500/height);
                var animalImage = animalBox.getVBox();
                generatedAnimalImages.add(animalImage);
            }
        }
        var exampleNormalPlant = new Plant(new Vector2d(0, 0), 0, false);
        var normalPlantBox = new WorldElementBox(exampleNormalPlant, width, height);
        var normalPlantImage = normalPlantBox.getVBox();
        var examplePoisonousPlant = new Plant(new Vector2d(0, 0), 0, true);
        var poisonousPlantBox = new WorldElementBox(examplePoisonousPlant, width, height);
        var poisonousPlantImage = poisonousPlantBox.getVBox();

        return generatedAnimalImages;
    }

    public LinkedList<VBox> generateNormalPlantImageList() {
        var generatedNormalPlantImages = new LinkedList<VBox>();
        var position = new Vector2d(0,0);
        for(int i=0; i<width; i++) {
            for(int j=0; j<height; j++) {
                var generatedAnimal = new Plant(position, 0, false);
                var animalBox = new WorldElementBox(generatedAnimal, 500/width, 500/height);
                var animalImage = animalBox.getVBox();
                generatedNormalPlantImages.add(animalImage);
            }
        }
        return generatedNormalPlantImages;
    }

    public LinkedList<VBox> generatePoisonousPlantImageList() {
        var generatedPoisonousPlantImages = new LinkedList<VBox>();
        var position = new Vector2d(0,0);
        for(int i=0; i<width; i++) {
            for(int j=0; j<height; j++) {
                var generatedAnimal = new Plant(position, 0, false);
                var animalBox = new WorldElementBox(generatedAnimal, 500/width, 500/height);
                var animalImage = animalBox.getVBox();
                generatedPoisonousPlantImages.add(animalImage);
            }
        }
        return generatedPoisonousPlantImages;
    }
}
