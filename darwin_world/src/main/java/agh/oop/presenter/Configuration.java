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
    public Spinner<Integer> widthValue;
    @FXML
    public Spinner<Integer> heightValue;
    @FXML
    public Spinner<Integer> reproduceEnergy;
    @FXML
    public Spinner<Integer> copulateEnergy;
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
    public Spinner<Integer> mutationRangeMax;
    @FXML
    public Spinner<Integer> mutationRangeMin;

    @FXML
    public ToggleGroup mutationVariant;
    @FXML
    public ToggleGroup plantVariant;
    @FXML
    public Button saveSettings;
    @FXML
    public Spinner<Integer> simulationLength;


    private Simulation simulationToRun;
    private Earth earth;

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

        var mutationId = ((RadioButton) this.mutationVariant.getSelectedToggle()).getId();
        var plantId = ((RadioButton) this.plantVariant.getSelectedToggle()).getId();
        var mutationRange = new int[]{mutationRangeMin.getValue(), mutationRangeMax.getValue()};

        var earth = new Earth(width, height);
        var simulationParameters = new DataHolder(simulationLength, reproduceEnergy, copulateEnergy,
                newPlantNumber, plantEnergy, newAnimalNumber, genomeLength, initialEnergy,
                mutationRange, mutationId, plantId);


        this.simulationToRun = new Simulation(earth, simulationParameters);
        this.earth = earth;
    }

    public void onLaunchClicked(){
        SimulationApp.startSimulation(simulationToRun,earth);
    }
}
