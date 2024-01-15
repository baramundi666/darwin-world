package agh.oop.presenter;

import agh.oop.model.map.Earth;
import agh.oop.simulation.DataHolder;
import agh.oop.simulation.Simulation;
import agh.oop.view.SimulationApp;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.ToggleGroup;

public class Configuration {

    @FXML
    private Spinner<Integer> widthValue;
    @FXML
    private Spinner<Integer> heightValue;
    @FXML
    private Spinner<Integer> reproduceEnergy;
    @FXML
    private Spinner<Integer> copulateEnergy;
    @FXML
    private Spinner<Integer> initialEnergy;
    @FXML
    private Spinner<Integer> genomeLength;
    @FXML
    private Spinner<Integer> newAnimalNumber;
    @FXML
    private Spinner<Integer> newPlantNumber;
    @FXML
    private Spinner<Integer> plantEnergy;
    @FXML
    private Spinner<Integer> mutationRangeMax;
    @FXML
    private Spinner<Integer> mutationRangeMin;
    @FXML
    private ToggleGroup mutationVariant;
    @FXML
    private ToggleGroup mapVariant;
    @FXML
    private Button saveSettings;
    @FXML
    private Spinner<Integer> simulationLength;
    @FXML
    private ToggleGroup saveStats;


    private Simulation simulationToRun;
    private Earth earth;
    private String mapID;
    private String isSavingStats;

    public void saveSettings() {
        int width = this.widthValue.getValue();
        int height = this.heightValue.getValue();
        int simulationLength = this.simulationLength.getValue();

        int reproduceEnergy = this.reproduceEnergy.getValue();
        int copulateEnergy = this.copulateEnergy.getValue();
        int initialEnergy = this.initialEnergy.getValue();
        int genomeLength = this.genomeLength.getValue();
        int newAnimalNumber = this.newAnimalNumber.getValue();
        int newPlantNumber = this.newPlantNumber.getValue();
        int plantEnergy = this.plantEnergy.getValue();

        var mutationID = ((RadioButton) this.mutationVariant.getSelectedToggle()).getId();
        var mapID = ((RadioButton) this.mapVariant.getSelectedToggle()).getId();
        var isSavingStats = ((RadioButton) this.saveStats.getSelectedToggle()).getId();
        var mutationRange = new int[]{mutationRangeMin.getValue(), mutationRangeMax.getValue()};

        var earth = new Earth(width, height);
        var simulationParameters = new DataHolder(simulationLength, reproduceEnergy, copulateEnergy,
                newPlantNumber, plantEnergy, newAnimalNumber, genomeLength, initialEnergy,
                mutationRange, mutationID, mapID);


        this.simulationToRun = new Simulation(earth, simulationParameters);
        this.earth = earth;
        this.mapID  = mapID;
        this.isSavingStats = isSavingStats;
    }

    public void onLaunchClicked(){
        SimulationApp.startSimulation(simulationToRun,earth, mapID, isSavingStats);
    }
}
