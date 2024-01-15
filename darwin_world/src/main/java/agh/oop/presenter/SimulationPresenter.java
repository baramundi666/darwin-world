package agh.oop.presenter;


import agh.oop.model.map.Boundary;
import agh.oop.model.map.Earth;
import agh.oop.model.map.Vector2d;
import agh.oop.simulation.DataHolder;
import agh.oop.simulation.Simulation;
import agh.oop.simulation.statictics.Statistics;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Paint;

import java.util.*;
import java.util.stream.Stream;

public class SimulationPresenter implements ChangeListener {

    @FXML
    public GridPane mapGrid;
    @FXML
    private Label infoLabel;
    private int width;
    private int height;
    private List<Node> animalImageList;
    private List<Node> normalPlantImageList;
    private Optional<List<Node>> poisonousPlantImageList;
    private List<Node> steppeImageList;
    private List<Node> specialAreaImageList;
    private Simulation simulationToRun;
    private Statistics statistics;

    public void setSimulation(Simulation simulationToRun, Earth earth, String mapID, String isSavingStats) {
        this.simulationToRun = simulationToRun;
        this.width = earth.getBounds().upperRight().getX() + 1;
        this.height = earth.getBounds().upperRight().getY() + 1;

        var imageGenerator = new ImageGenerator(width, height, (double) 400 /width, (double) 400 /height);
        animalImageList = imageGenerator.generateImageList("oldAnimal.png", 1.0);
        normalPlantImageList = imageGenerator.generateImageList("plant.png", 1.0);
        poisonousPlantImageList = Optional.empty();
        steppeImageList = imageGenerator.generateImageList("steppe.png", 0.85);

        switch (mapID) {
            case "p1":
                specialAreaImageList = imageGenerator.generateImageList("jungle.png", 0.3);
                break;
            case "p2":
                specialAreaImageList = imageGenerator.generateImageList("poisonedArea.png", 0.3);
                poisonousPlantImageList = Optional.of(imageGenerator.generateImageList("poisonousPlant.png", 0.3));
                break;
            default: throw  new IllegalArgumentException("Invalid mapID");
        }
        statistics = new Statistics(isSavingStats);
    }

    @Override
    public void mapChanged(Earth earth, String message) {
        Platform.runLater(() -> {
            drawGrid();
            drawDefaultBackground();
            drawMap(earth);
            infoLabel.setText(message);
        });
    }

    private boolean inSpecialArea(int i, int j) {
        Vector2d position = new Vector2d(i,j);
        Boundary borders = simulationToRun.getSpecialAreaBorders();
        return position.follows(borders.lowerLeft()) && position.precedes(borders.upperRight());
    }

    public void drawGrid(){ // to do - canvas
        clearGrid(mapGrid);
        double cellWidth = (double) 400 /width;
        double cellHeight = (double) 400 /height;

        mapGrid.getColumnConstraints().add(new ColumnConstraints(cellWidth));
        mapGrid.getRowConstraints().add(new RowConstraints(cellHeight));
        Label axis = new Label("y\\x");
        mapGrid.add(axis,0,0);//assume that left upper corner is (0,0)
        GridPane.setHalignment(axis, HPos.CENTER);

        for (int i=0;i<height;i++){
            mapGrid.getRowConstraints().add(new RowConstraints(cellHeight));
            Label label = new Label(String.valueOf(i));
            mapGrid.add(label,0,i+1);
            GridPane.setHalignment(label, HPos.CENTER);
        }
        for (int i=0;i<width;i++){
            mapGrid.getColumnConstraints().add(new ColumnConstraints(cellWidth));
            var label = new Label(String.valueOf(i));
            mapGrid.add(label,i+1,0);
            GridPane.setHalignment(label, HPos.CENTER);
        }
    }

    public void drawDefaultBackground(){
        var steppeImageIterator = steppeImageList.iterator();
        var specialAreaImageIterator = specialAreaImageList.iterator();

        for(int i=0; i<height; i++) {
            for(int j=0; j<width; j++) {
                if (inSpecialArea(i,j)) {
                    var specialAreaImage = specialAreaImageIterator.next();
                    mapGrid.add(specialAreaImage, i+1, j+1);
                    GridPane.setHalignment(specialAreaImage, HPos.CENTER);
                }
                else {
                    var steppeImage = steppeImageIterator.next();
                    mapGrid.add(steppeImage, i+1, j+1);
                    GridPane.setHalignment(steppeImage, HPos.CENTER);
                }
            }
        }
    }

    public void drawMap(Earth earth) {
        var plantsMap = earth.getPlants();
        var animalsMap = earth.getAnimals();
        var animalImageIterator = animalImageList.iterator();
        var normalPlantImageIterator = normalPlantImageList.iterator();
        var poisonousPlantImageIterator = poisonousPlantImageList.map(List::iterator).orElse(null);

        for(Vector2d position: plantsMap.keySet()){
            var plant = plantsMap.get(position);
            Node plantImage;
            if (poisonousPlantImageList.isPresent() && plant.isPoisonous()) {
                plantImage = poisonousPlantImageIterator.next();
            }
            else {
                plantImage = normalPlantImageIterator.next();
            }
            mapGrid.add(plantImage, position.getX() + 1, position.getY() + 1);
            GridPane.setHalignment(plantImage, HPos.CENTER);
        }

        for(Vector2d position: animalsMap.keySet()){
            if(!animalsMap.get(position).isEmpty()) {
                int animalCount = animalsMap.get(position).size();
                var countLabel = new Label(String.valueOf(animalCount));
                countLabel.setTextFill(Paint.valueOf("black"));
                var animalImage = animalImageIterator.next();
                mapGrid.add(animalImage, position.getX() + 1, position.getY() + 1);
                mapGrid.add(countLabel, position.getX() + 1, position.getY() + 1);
                GridPane.setHalignment(animalImage, HPos.CENTER);
                GridPane.setHalignment(countLabel, HPos.CENTER);
            }
        }
    }

    @FXML
    private void onSimulationStartClicked() {
        simulationToRun.registerListener(statistics);
        simulationToRun.registerListener(this);
        Thread engineThread = new Thread(simulationToRun);
        engineThread.start();
    }

    private void clearGrid(GridPane grid) {
        grid.getChildren().retainAll(grid.getChildren().get(0)); // hack to retain visible grid lines
        grid.getColumnConstraints().clear();
        grid.getRowConstraints().clear();
    }
}

