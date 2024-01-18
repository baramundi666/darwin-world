package agh.oop.view;

import agh.oop.model.map.Earth;
import agh.oop.simulation.data.SimulationData;
import agh.oop.simulation.Simulation;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public class HomePage {
    @FXML
    private ComboBox<String> savedConfigurationsBox;

    private Simulation simulationToRun;
    private String mapID;
    private String isSavingStats;
    private Earth earth;
    private SimulationData simulationParameters;
    private boolean comboBoxSelected = false;



    public void passOnParametersToHome(SimulationData parameters, String isSavingStats, int width, int height, String mapID) {
        this.simulationParameters = parameters;
        this.isSavingStats = isSavingStats;
        this.earth = new Earth(width, height);
        this.mapID = mapID;
        comboBoxSelected = false;
    }

    public void newConfiguration() {
        SimulationApp.newConfiguration();
    }

    private Optional<List<String>> getSavedConfigurations(){
        Path path = Path.of("src\\main\\resources\\configurations");
        File folder = new File(path.toString());
        if (!folder.exists()) {
            throw new IllegalArgumentException("Folder configurations does not exist");
        }
        File[] listOfFiles = folder.listFiles();
        if(listOfFiles == null){
            return Optional.empty();
        }
        List<String> savedConfigurations = new LinkedList<>();

        for (File listOfFile : listOfFiles) {
            if (listOfFile.isFile()) {
                savedConfigurations.add(listOfFile.getName());
            }
        }
        return Optional.of(savedConfigurations);
    }

    public void displaySavedConfigurations() {
        try {
            Optional<List<String>> savedConfigurations = getSavedConfigurations();
            savedConfigurations.ifPresent(strings -> savedConfigurationsBox.setItems(FXCollections.observableArrayList(strings)));
        } catch (IllegalArgumentException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    private List<String> setSavedConfigurations() throws IOException {
        String fileName = savedConfigurationsBox.getValue();
        Path path = Path.of("src\\main\\resources\\configurations\\" + fileName);
        List<String> simulationParameters = new LinkedList<>();
        try(Scanner scanner = new Scanner(path)) {
            while(scanner.hasNextLine()){
                String[] tokens = scanner.nextLine().split("\n");
                String token = tokens[0];
                simulationParameters.add(token);
            }
        }catch (IOException e){
            throw new IOException("File not found");
        }
        return simulationParameters;
    }

    private void setSimulationParametersFromFile() throws IOException {
        try {
            List<String> parameters = setSavedConfigurations();
            int simulationLength = Integer.parseInt(parameters.get(0));
            int reproduceEnergy = Integer.parseInt(parameters.get(1));
            int copulateEnergy = Integer.parseInt(parameters.get(2));
            int newPlantNumber = Integer.parseInt(parameters.get(3));
            int plantEnergy = Integer.parseInt(parameters.get(4));
            int newAnimalNumber = Integer.parseInt(parameters.get(5));
            int genomeLength = Integer.parseInt(parameters.get(6));
            int initialEnergy = Integer.parseInt(parameters.get(7));
            int[] mutationRange = {Integer.parseInt(parameters.get(8)), Integer.parseInt(parameters.get(9))};
            String mutationID = parameters.get(10);
            String mapID = parameters.get(11);
            String isSavingStats = parameters.get(12);
            int width = Integer.parseInt(parameters.get(13));
            int height = Integer.parseInt(parameters.get(14));

            SimulationData simulationParameters = new SimulationData(simulationLength, reproduceEnergy, copulateEnergy,
                    newPlantNumber, plantEnergy, newAnimalNumber, genomeLength, initialEnergy,
                    mutationRange, mutationID, mapID);

            this.earth = new Earth(width, height);
            this.simulationToRun = new Simulation(earth, simulationParameters);
            this.mapID = mapID;
            this.isSavingStats = isSavingStats;
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        }
    }

    private void setSimulationParametersFromConfiguration() {
        try{
            var oldMapBounds = earth.getBounds();
            var newEarth = new Earth(oldMapBounds.upperRight().getX()+1, oldMapBounds.upperRight().getY()+1);
            this.simulationToRun = new Simulation(newEarth, simulationParameters);
        } catch(NullPointerException e){
            throw new IllegalArgumentException("No configuration selected");
        }
    }

    public void onLaunchClicked() {
        try {
            if (comboBoxSelected) setSimulationParametersFromFile();
            else setSimulationParametersFromConfiguration();
            SimulationApp.startSimulation(simulationToRun, earth, mapID, isSavingStats);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void comboBoxUseHandler() {
        comboBoxSelected = true;
    }
}
