package agh.oop.presenter;


import agh.oop.model.map.Earth;
import agh.oop.model.map.Vector2d;
import agh.oop.model.objects.Animal;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SimulationPresenter implements MapChangeListener {

    @FXML
    public GridPane mapGrid;

    @FXML
    private TextField input;

    @FXML
    private Label infoLabel;


    @Override
    public void mapChanged(Earth earth, String message) {
        Platform.runLater(() -> {
            drawMap(earth);
            infoLabel.setText(message);
        });
    }

    public void drawMap(Earth earth) {
        clearGrid();
        var boundary = earth.getBounds();
        int lowerX = boundary.lowerLeft().getX();
        int upperX = boundary.upperRight().getX();
        int lowerY = boundary.lowerLeft().getY();
        int upperY = boundary.upperRight().getY();
        int rows = upperY-lowerY+1;
        int columns = upperX-lowerX+1;
        double width = (double) 300/columns;
        double height = (double) 300/rows;
        var plants = earth .getPlants();
        var animals = earth.getAnimals();

        mapGrid.getColumnConstraints().add(new ColumnConstraints(width));
        mapGrid.getRowConstraints().add(new RowConstraints(height));
        Label axis = new Label("y\\x");
        mapGrid.add(axis,0,0);
        GridPane.setHalignment(axis, HPos.CENTER);

        for (int i=0;i<rows;i++){
            mapGrid.getRowConstraints().add(new RowConstraints(height));
            Label label = new Label(String.valueOf(i+lowerY));
            mapGrid.add(label,0,i+1);
            GridPane.setHalignment(label, HPos.CENTER);
        }
        for (int i=0;i<columns;i++){
            mapGrid.getColumnConstraints().add(new ColumnConstraints(width));
            var label = new Label(String.valueOf(i+lowerX));
            mapGrid.add(label,i+1,0);
            GridPane.setHalignment(label, HPos.CENTER);
        }

        for(Vector2d position: plants.keySet()){
            var plant = plants.get(position);
            var label = new Label(plant.toString());
            mapGrid.add(label,position.getX()-lowerX+1,position.getY()-lowerY+1);
            GridPane.setHalignment(label, HPos.CENTER);
        }

        for(Vector2d position: animals.keySet()){
            var animalsSet = animals.get(position);
            Iterator<Animal> animalIterator = animalsSet.iterator();
            //roboczo
            int animalCount = 1;
            var exampleAnimal =animalIterator.next();
            while (animalIterator.hasNext()) {
                animalCount++;
                animalIterator.next();
            }

            var label = new Label(exampleAnimal.toString());
            label.setText(String.valueOf(animalCount));
            mapGrid.add(label,position.getX()-lowerX+1,position.getY()-lowerY+1);
            GridPane.setHalignment(label, HPos.CENTER);
        }
    }



    @FXML
    private void onSimulationStartClicked() {
        // arguments - mapa: nazwa argumentu -> wartosc liczbowa argumentu
        int argumentCount = 2;
        List<String> inputlist = new ArrayList<>(List.of("width","height"));
        var parameters = Stream.of(input.getText().split(" ")).map(Integer::valueOf).toList();
        Map<String, Integer> arguments = new HashMap<>();
        for(int i=0; i<argumentCount; i++) {
            arguments.put(inputlist.get(i), parameters.get(i));
        }
        var map = new Earth(arguments.get("width"), arguments.get("height"));
        map.registerObserver(this);
        var simulation = new SimulationDemo()

    }

    private void clearGrid() {
        mapGrid.getChildren().retainAll(mapGrid.getChildren().get(0)); // hack to retain visible grid lines
        mapGrid.getColumnConstraints().clear();
        mapGrid.getRowConstraints().clear();
    }
}

