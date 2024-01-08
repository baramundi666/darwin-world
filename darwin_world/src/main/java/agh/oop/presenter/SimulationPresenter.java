package agh.oop.presenter;


import agh.oop.model.map.Earth;
import agh.oop.model.map.Vector2d;
import agh.oop.simulation.Simulation;
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
    public GridPane backgroundGrid;
    @FXML
    public Spinner<Integer> width;
    @FXML
    public Spinner<Integer> height;
    @FXML
    public Spinner<Integer> reproduceEnergy;
    @FXML
    public Spinner<Integer> initialEnergy;
    @FXML
    public Spinner<Integer> genomeLength;
    @FXML
    public Spinner<Integer> newAnimalNumber;
    @FXML
    public Spinner<Integer> newPlantNumber;
    @FXML
    public Spinner<Integer> plantEnergy;
    @FXML
    public ToggleGroup mutationVariant;
    @FXML
    public ToggleGroup plantVariant;
    @FXML
    public Button saveSettings;
    @FXML
    private Label infoLabel;

    private List<Node> animalImageList;
    private List<Node> normalPlantImageList;
    private List<Node> poisonousPlantImageList;
    private List<Node> steppeImageList;
    private List<Node> jungleImageList;
    private Simulation simulationToRun;


    @Override
    public void mapChanged(Earth earth, String message) {
        Platform.runLater(() -> {
            drawDefaultMap(earth);
            infoLabel.setText(message);
        });
    }
    public void drawDefaultMap(Earth earth) {
        clearGrid(mapGrid);
        var boundary = earth.getBounds();
        int lowerX = boundary.lowerLeft().getX();
        int upperX = boundary.upperRight().getX();
        int lowerY = boundary.lowerLeft().getY();
        int upperY = boundary.upperRight().getY();
        int lowerEquatorBorder = (int)(Math.ceil(upperY/5.0 *2));
        int upperEquatorBorder = lowerEquatorBorder + (int)(Math.ceil((upperY+1)/5.0)-1);
        int rows = upperY-lowerY+1;
        int columns = upperX-lowerX+1;
        double width = (double) 500 /columns;
        double height = (double) 500 /rows;

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
        var jungleImageIterator = jungleImageList.iterator();

        for(int i=0; i<rows; i++) {
            for(int j=0; j<columns; j++) {
                var position= new Vector2d(i, j);
                if (j>=lowerEquatorBorder && j<=upperEquatorBorder) {
                    var jungleImage = jungleImageIterator.next();
                    mapGrid.add(jungleImage, position.getX() - lowerX + 1, position.getY() - lowerY + 1);
                    GridPane.setHalignment(jungleImage, HPos.CENTER);
                }
                else {
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
        simulationToRun.registerListener(this);
        Thread engineThread = new Thread(simulationToRun);
        engineThread.start();
    }

    private void clearGrid(GridPane grid) {
        grid.getChildren().retainAll(grid.getChildren().get(0)); // hack to retain visible grid lines
        grid.getColumnConstraints().clear();
        grid.getRowConstraints().clear();
    }

    public void onSaveClicked() {
        int width = this.width.getValue();
        int height = this.height.getValue();
        int reproduceEnergy = this.reproduceEnergy.getValue();
        int initialEnergy = this.initialEnergy.getValue();
        int genomeLength = this.genomeLength.getValue();
        int newAnimalNumber = this.newAnimalNumber.getValue();
        int newPlantNumber = this.newPlantNumber.getValue();
        int plantEnergy = this.plantEnergy.getValue();

        var mutationId = ((RadioButton) this.mutationVariant.getSelectedToggle()).getId();
        var plantId = ((RadioButton) this.plantVariant.getSelectedToggle()).getId();

        var imageGenerator = new ImageGenerator(width, height, (double) 500 /width, (double) 500 /height);
        animalImageList = imageGenerator.generateAnimalImageList();
        normalPlantImageList = imageGenerator.generatePlantImageList();
        poisonousPlantImageList = imageGenerator.generatePoisonousPlantImageList();
        jungleImageList = imageGenerator.generateJungleImageList();
        steppeImageList = imageGenerator.generateSteppeImageList();
        var map = new Earth(width, height);
        //Mutation mutation = new SwapMutation(new int[]{2, 5});
        var mutationRange = new int[]{2, 5};
        simulationToRun = new Simulation(map, reproduceEnergy, newPlantNumber,
                plantEnergy, newAnimalNumber, genomeLength, initialEnergy,
                mutationRange, mutationId, plantId);
    }
}

