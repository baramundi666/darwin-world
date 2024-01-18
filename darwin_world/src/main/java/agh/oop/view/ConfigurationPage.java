package agh.oop.view;

import agh.oop.simulation.data.SimulationData;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.Objects;

public class ConfigurationPage {


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
    private ToggleGroup saveStats;
    @FXML
    private Button saveConfiguration;
    @FXML
    private Spinner<Integer> simulationLength;
    @FXML
    private TextField configurationName;
    private String mapID;
    private String isSavingStats;
    private int width;
    private int height;
    private HomePage homePage;


    public void setHomePage(HomePage homePage) {
        this.homePage = homePage;
    }

    public SimulationData getSimulationParameters() throws IllegalArgumentException {
        try {
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

            if (Objects.equals(mapID, "p2") && (width < (int) Math.ceil(Math.sqrt(width * height * 0.2)) || height < (int) Math.ceil(Math.sqrt(width * height * 0.2)))) {
                throw new IllegalArgumentException("Map is too small for this variant");
            }

            return new SimulationData(simulationLength, reproduceEnergy, copulateEnergy,
                    newPlantNumber, plantEnergy, newAnimalNumber, genomeLength, initialEnergy,
                    mutationRange, mutationID, mapID);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    private String simulationParametersToString(SimulationData simulationParameters, String isSavingStats, int width, int height){
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

    public void useCurrentConfiguration() {
        try {
            SimulationData simulationParameters = getSimulationParameters();
            homePage.passOnParametersToHome(simulationParameters, isSavingStats, width, height, mapID);
            Stage stage = (Stage) saveConfiguration.getScene().getWindow();
            stage.close();
        } catch (IllegalArgumentException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid parameters");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    public void saveConfigurationToFile() {
        try {
            SimulationData simulationParameters = getSimulationParameters();
            String parameters = simulationParametersToString(simulationParameters, isSavingStats, width, height);
            String configurationName = this.configurationName.getText();
            writeToFile(parameters, configurationName);
        } catch (IllegalArgumentException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid parameters");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    public synchronized void writeToFile(String parameters, String configurationName){
        Path path = Path.of("src\\main\\resources\\configurations\\" + configurationName + ".txt");
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
}
