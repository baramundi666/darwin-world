package agh.oop.presenter;


import agh.oop.model.map.Boundary;
import agh.oop.model.map.Earth;
import agh.oop.model.map.Vector2d;
import agh.oop.model.objects.Animal;
import agh.oop.presenter.generator.ImageGenerator;
import agh.oop.simulation.Simulation;
import agh.oop.simulation.statictics.AnimalStatistics;
import agh.oop.simulation.statictics.Statistics;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

import java.util.*;

import static java.lang.Math.max;

public class SimulationPresenter implements ChangeListener {

    @FXML
    public GridPane mapGrid;
    @FXML
    private Label infoLabel;
    @FXML
    private Label animalNumber;
    @FXML
    private Label plantNumber;
    @FXML
    private Label freeFields;
    @FXML
    private Label avgEnergy;
    @FXML
    private Label avgLifeLength;
    @FXML
    private Label avgChildrenNumber;
    @FXML
    private Label dominantGenotype;
    @FXML
    private Label dayOfDeath;
    @FXML
    private Label childrenNumber;
    @FXML
    private Label genotype;
    @FXML
    private Label energy;
    @FXML
    private Label activeGene;
    @FXML
    private Label plantEatenNumber;
    @FXML
    private Label lifeLength;
    @FXML
    private Label descendantsNumber;
    private int width;
    private int height;
    private List<Node> steppeImageList;
    private List<Node> specialAreaImageList;
    private Simulation simulationToRun;
    private Statistics statistics;
    private final List<Label> toBeCleared = new LinkedList<>();
    private Optional<Animal> spectatedAnimal = Optional.empty();
    private AnimalStatistics spectatedAnimalStatistics;


    public void setSimulation(Simulation simulationToRun, Earth earth, String mapID, String isSavingStats) {
        this.simulationToRun = simulationToRun;
        this.width = earth.getBounds().upperRight().getX() + 1;
        this.height = earth.getBounds().upperRight().getY() + 1;

        var imageGenerator = new ImageGenerator(width, height, (double) 450 / max(width, height), (double) 450 / max(width, height));

        steppeImageList = imageGenerator.generateImageList("images/steppe.png", 0.85);

        switch (mapID) {
            case "p1":
                specialAreaImageList = imageGenerator.generateImageList("images/jungle.png", 0.5);
                break;
            case "p2":
                specialAreaImageList = imageGenerator.generateImageList("images/poisonedArea.png", 0.5);
                break;
            default:
                throw new IllegalArgumentException("Invalid mapID");
        }
        statistics = new Statistics(isSavingStats);
    }

    public Statistics getStatistics() {
        return this.statistics;
    }

    private void setStatistics(){
        animalNumber.setText(String.valueOf(statistics.getNumberOfAnimals()));
        plantNumber.setText(String.valueOf(statistics.getNumberOfPlants()));
        freeFields.setText(String.valueOf(statistics.getNumberOfNotOccupiedFields()));
        avgEnergy.setText(String.format("%.2f", statistics.getAverageEnergy()));
        avgLifeLength.setText(String.format("%.2f", statistics.getAverageLifeLength()));
        avgChildrenNumber.setText(String.format("%.2f",statistics.getAverageNumberOfChildren()));
        dominantGenotype.setText(statistics.getDominantGenotype().map(Object::toString).orElse("No dominant genotype"));
    }

    @Override
    public void mapInitialized(Earth earth, String message) {
        Platform.runLater(() -> {
            drawGrid();
            drawDefaultBackground();
            infoLabel.setText(message);
            setStatistics();
        });
    }

    @Override
    public void mapChanged(Earth earth, String message) {
        Platform.runLater(() -> {
            drawMapElements(earth);
            infoLabel.setText(message);
            setStatistics();
            spectatedAnimal.ifPresent(this::spectateAnimal);
        });
    }

    private boolean inSpecialArea(int i, int j) {
        Vector2d position = new Vector2d(i, j);
        Boundary borders = simulationToRun.getSpecialAreaBorders();
        return position.follows(borders.lowerLeft()) && position.precedes(borders.upperRight());
    }

    public void drawGrid() {
        double cellSize = (double) 500 / (max(width, height) + 1);

        mapGrid.getColumnConstraints().add(new ColumnConstraints(cellSize));
        mapGrid.getRowConstraints().add(new RowConstraints(cellSize));
        Label axis = new Label("y\\x");
        axis.setTextFill(Paint.valueOf("black"));
        mapGrid.add(axis, 0, 0);
        GridPane.setHalignment(axis, HPos.CENTER);

        for (int i = 0; i < height; i++) {
            mapGrid.getRowConstraints().add(new RowConstraints(cellSize));
            Label label = new Label(String.valueOf(i));
            mapGrid.add(label, 0, i + 1);
            GridPane.setHalignment(label, HPos.CENTER);
        }
        for (int i = 0; i < width; i++) {
            mapGrid.getColumnConstraints().add(new ColumnConstraints(cellSize));
            var label = new Label(String.valueOf(i));
            mapGrid.add(label, i + 1, 0);
            GridPane.setHalignment(label, HPos.CENTER);
        }
    }

    public void drawDefaultBackground() {
        var steppeImageIterator = steppeImageList.iterator();
        var specialAreaImageIterator = specialAreaImageList.iterator();

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (inSpecialArea(i, j)) {
                    var specialAreaImage = specialAreaImageIterator.next();
                    mapGrid.add(specialAreaImage, i + 1, j + 1);
                    GridPane.setHalignment(specialAreaImage, HPos.CENTER);
                } else {
                    var steppeImage = steppeImageIterator.next();
                    mapGrid.add(steppeImage, i + 1, j + 1);
                    GridPane.setHalignment(steppeImage, HPos.CENTER);
                }
            }
        }
    }

    public void drawMapElements(Earth earth) {
        clearGrid(mapGrid);
        var plantsMap = earth.getPlants();
        var animalsMap = earth.getAnimals();
        for (Vector2d position : plantsMap.keySet()) {
            var plant = plantsMap.get(position);
            var plantImage = new Label("\u2022");
            plantImage.setFont(Font.font(30));
            plantImage.setTextFill(Paint.valueOf(plant.getPlantColor()));
            plantImage.setAlignment(Pos.CENTER);
            toBeCleared.add(plantImage);
            mapGrid.add(plantImage, position.getX() + 1, position.getY() + 1);
            GridPane.setHalignment(plantImage, HPos.CENTER);
        }

        for (Vector2d position : animalsMap.keySet()) {
            if (!animalsMap.get(position).isEmpty()) {
                var firstAnimal = animalsMap.get(position).iterator().next();
                var animalImage = new Label("\u25FC");
                animalImage.setOnMouseClicked((mouseEvent) ->
                    setSpectatedAnimal(firstAnimal)
                );
                animalImage.setTextFill(Paint.valueOf(firstAnimal.getAnimalColor()));
                animalImage.setAlignment(Pos.CENTER);
                toBeCleared.add(animalImage);
                mapGrid.add(animalImage, position.getX() + 1, position.getY() + 1);
                GridPane.setHalignment(animalImage, HPos.CENTER);
            }
        }
    }

    private void setSpectatedAnimal(Animal animal){
        spectatedAnimal = Optional.of(animal);
        spectateAnimal(animal);
    }

    private void spectateAnimal(Animal animal) {
        spectatedAnimalStatistics = simulationToRun.getAnimalStatistics(animal);
        setAnimalStatistics();
        if(!animal.isDead()) setSpecialAnimalLabel("blue", animal);
    }

    @FXML
    private void onClickStopSpectatingAnimal(){
        spectatedAnimal = Optional.empty();
        childrenNumber.setText("");
        energy.setText("");
        plantEatenNumber.setText("");
        descendantsNumber.setText("");
        lifeLength.setText("");
        dayOfDeath.setText("");
        activeGene.setText("");
        genotype.setText("");
    }

    private void setAnimalStatistics(){
        var statisticsData = spectatedAnimalStatistics.getStatisticsData();
        childrenNumber.setText(String.valueOf(statisticsData.childrenCount()));
        energy.setText(String.valueOf(statisticsData.energy()));
        plantEatenNumber.setText(String.valueOf(statisticsData.plantEatenCount()));
        descendantsNumber.setText(String.valueOf(statisticsData.descendantsCount()));
        lifeLength.setText(String.valueOf(statisticsData.lifeLength()));
        dayOfDeath.setText(statisticsData.dayOfDeath());
        if(dayOfDeath.getText().equals("Alive")) {
            dayOfDeath.setTextFill(Paint.valueOf("green"));
            activeGene.setText(String.valueOf(statisticsData.activeGene()));
        }
        else{
            dayOfDeath.setTextFill(Paint.valueOf("red"));
            activeGene.setText("");
        }
        genotype.setText(statisticsData.genotype().toString());
    }

    @FXML
    private void onSimulationPauseClicked() {
        var isSuspended = simulationToRun.isThreadSuspended();
        if (isSuspended) {
            System.out.println("Simulation is already paused!");
        }
        else {
            simulationToRun.setThreadSuspended(true);
        }
    }

    @FXML
    private void onSimulationResumeClicked() {
        var isSuspended = simulationToRun.isThreadSuspended();
        if (!isSuspended) {
            System.out.println("Simulation is already running!");
        }
        else {
            simulationToRun.setThreadSuspended(false);
        }
    }

    private void clearGrid(GridPane grid) {
        var children = grid.getChildren();
        for (Label label : toBeCleared) {
            children.remove(label);
        }
        toBeCleared.clear();
    }

    public void highlightDominantGenotype() {
        var dominantGenotype = statistics.getDominantGenotype();
        if (dominantGenotype.isEmpty()) return;
        var dominantGenotypeList = dominantGenotype.get();
        var animals = simulationToRun.getEarth().getAnimals();
        var animalsKeys = animals.keySet();
        for (Vector2d position : animalsKeys) {
            for (var animal : animals.get(position)) {
                if (animal.getGenome().getGeneList().equals(dominantGenotypeList)) {
                    setSpecialAnimalLabel("yellow", animal);
                }
            }
        }
    }

    public void setSpecialAnimalLabel(String color, Animal animal){
        var animalImage = new Label("\u25FC");
        animalImage.setTextFill(Paint.valueOf(color));
        animalImage.setAlignment(Pos.CENTER);
        toBeCleared.add(animalImage);
        mapGrid.add(animalImage, animal.getPosition().getX() + 1, animal.getPosition().getY() + 1);
        GridPane.setHalignment(animalImage, HPos.CENTER);
    }
}

