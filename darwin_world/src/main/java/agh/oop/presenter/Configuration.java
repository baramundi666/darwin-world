package agh.oop.presenter;

import agh.oop.model.map.Earth;
import agh.oop.simulation.DataHolder;
import agh.oop.simulation.Simulation;
import agh.oop.view.SimulationApp;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;

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
    private Button saveConfiguration;
    @FXML
    private Spinner<Integer> simulationLength;
    @FXML
    private ToggleGroup saveStats;
    @FXML
    private TextField configurationName;
    private String mapID;
    private String isSavingStats;
    private int width;
    private int height;


    public DataHolder getSimulationParameters(){
        this.width = this.widthValue.getValue();
        this.height = this.heightValue.getValue();
        int simulationLength = this.simulationLength.getValue();
        int reproduceEnergy = this.reproduceEnergy.getValue();
        int copulateEnergy = this.copulateEnergy.getValue();
        int initialEnergy = this.initialEnergy.getValue();
        int genomeLength = this.genomeLength.getValue();
        int newAnimalNumber = this.newAnimalNumber.getValue();
        int newPlantNumber = this.newPlantNumber.getValue();
        int plantEnergy = this.plantEnergy.getValue();
        var mutationID = ((RadioButton) this.mutationVariant.getSelectedToggle()).getId();
        this.mapID = ((RadioButton) this.mapVariant.getSelectedToggle()).getId();
        var mutationRange = new int[]{mutationRangeMin.getValue(), mutationRangeMax.getValue()};
        this.isSavingStats = ((RadioButton) this.saveStats.getSelectedToggle()).getId();

        return new DataHolder(simulationLength, reproduceEnergy, copulateEnergy,
                newPlantNumber, plantEnergy, newAnimalNumber, genomeLength, initialEnergy,
                mutationRange, mutationID, mapID);
    }

    private String simulationParametersToString(DataHolder simulationParameters, String isSavingStats, int width, int height){
        return simulationParameters.simulationLength() + "\n" +
                simulationParameters.reproduceEnergy() + "\n" +
                simulationParameters.copulateEnergy() + "\n" +
                simulationParameters.newPlantNumber() + "\n" +
                simulationParameters.plantEnergy() + "\n" +
                simulationParameters.newAnimalNumber() + "\n" +
                simulationParameters.genomeLength() + "\n" +
                simulationParameters.initialEnergy() + "\n" +
                simulationParameters.mutationRange()[0] + "\n" + simulationParameters.mutationRange()[1] + "\n" +
                simulationParameters.mutationVariant() + "\n" +
                simulationParameters.mapVariant() + "\n" +
                isSavingStats + "\n" +
                width + "\n" +
                height;
    }

    public void saveConfiguration() {
        DataHolder simulationParameters = getSimulationParameters();
        String parameters = simulationParametersToString(simulationParameters, isSavingStats, width, height);
        String configurationName = this.configurationName.getText();
        writeToFile(parameters, configurationName);
        Stage stage = (Stage) saveConfiguration.getScene().getWindow();
        stage.close();
    }

    public synchronized void writeToFile(String parameters, String configurationName){
        Path path = Path.of("C:\\Users\\macie\\OneDrive\\Pulpit\\Studia\\YEAR 2\\obiektowe\\PO_PROJEKT_2023_KROL_MAKOWSKI\\darwin_world\\src\\main\\resources\\configurations\\" + configurationName + ".txt");
        File file = new File(path.toString());
        if(file.exists()){
            file.delete();
        }
        try (PrintWriter writer = new PrintWriter(new FileWriter(path.toString(), true))) {
            writer.println(parameters);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void launchSimulation(){
        DataHolder simulationParameters = getSimulationParameters();
        var isSavingStats = ((RadioButton) this.saveStats.getSelectedToggle()).getId();
        int width = this.widthValue.getValue();
        int height = this.heightValue.getValue();

        Earth earth = new Earth(width, height);
        Simulation simulationToRun = new Simulation(earth,simulationParameters);

        Stage stage = (Stage) saveConfiguration.getScene().getWindow();
        SimulationApp.startSimulation(simulationToRun,earth,mapID, isSavingStats);
        stage.close();
    }
}
