package agh.oop.presenter;


import agh.oop.model.map.Earth;
import agh.oop.model.map.Vector2d;
import agh.oop.model.objects.Animal;
import agh.oop.model.objects.Plant;
import agh.oop.model.objects.inheritance.Genome;
import agh.oop.model.objects.inheritance.Mutation;
import agh.oop.model.objects.inheritance.StandardMutation;
import agh.oop.model.objects.inheritance.SwapMutation;
import agh.oop.simulation.Simulation;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;

import java.util.*;
import java.util.stream.Stream;

public class SimulationPresenter implements ChangeListener {

    @FXML
    public GridPane mapGrid;

    @FXML
    private TextField input;

    @FXML
    private Label infoLabel;

    private List<Node> animalImageList;
    private List<Node> normalPlantImageList;
    private List<Node> poisonousPlantImageList;
    private List<Node> steppeImageList;


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
        double width = (double) 500/columns;
        double height = (double) 500/rows;

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

        var plantsMap = earth.getPlants();
        var animalsMap = earth.getAnimals();
        var animalImageIterator = animalImageList.iterator();
        var normalPlantImageIterator = normalPlantImageList.iterator();
        var poisonousPlantImageIterator = poisonousPlantImageList.iterator();
        var steppeImageIterator = steppeImageList.iterator();
        var plantKeys = plantsMap.keySet();
        for(int i=0; i<rows; i++) {
            for(int j=0; j<columns; j++) {
                var position= new Vector2d(i, j);
                if (!plantKeys.contains(position)) {
                    var steppeImage = steppeImageIterator.next();
                    mapGrid.add(steppeImage, position.getX() - lowerX + 1, position.getY() - lowerY + 1);
                    GridPane.setHalignment(steppeImage, HPos.CENTER);
                }
            }
        }

        for(Vector2d position: plantsMap.keySet()){
            var plant = plantsMap.get(position);
            Node plantImage;
            if (plant.isPoisonous()) {
                plantImage = poisonousPlantImageIterator.next();
            }
            else {
                plantImage = normalPlantImageIterator.next();
            }
            mapGrid.add(plantImage, position.getX() - lowerX + 1, position.getY() - lowerY + 1);
            GridPane.setHalignment(plantImage, HPos.CENTER);
        }

        for(Vector2d position: animalsMap.keySet()){
            if(!animalsMap.get(position).isEmpty()) {
                int animalCount = animalsMap.get(position).size();
                var countLabel = new Label(String.valueOf(animalCount));
                countLabel.setTextFill(Paint.valueOf("black"));
                var animalImage = animalImageIterator.next();
                mapGrid.add(animalImage, position.getX() - lowerX + 1, position.getY() - lowerY + 1);
                mapGrid.add(countLabel, position.getX() - lowerX + 1, position.getY() - lowerY + 1);
                GridPane.setHalignment(animalImage, HPos.CENTER);
                GridPane.setHalignment(countLabel, HPos.CENTER);
            }
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
        var imageGenerator = new ImageGenerator(arguments.get("width"), arguments.get("height"));
        animalImageList = imageGenerator.generateAnimalImageList();
        normalPlantImageList = imageGenerator.generatePlantImageList(false);
        poisonousPlantImageList = imageGenerator.generatePlantImageList(true);
        steppeImageList = imageGenerator.generateSteppeImageList();
        var map = new Earth(arguments.get("width"), arguments.get("height"));
        Mutation mutation = new SwapMutation(new int[]{2, 5});
        map.registerObserver(this);
        var simulation = new Simulation(map, 10, 20, 10, 50, 32, 5, mutation);
        simulation.registerListener(this);
        Thread engineThread = new Thread(simulation);
        engineThread.start();
    }

    private void clearGrid() {
        mapGrid.getChildren().retainAll(mapGrid.getChildren().get(0)); // hack to retain visible grid lines
        mapGrid.getColumnConstraints().clear();
        mapGrid.getRowConstraints().clear();
    }
}

