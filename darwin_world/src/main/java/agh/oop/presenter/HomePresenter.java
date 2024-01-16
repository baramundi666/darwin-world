package agh.oop.presenter;

import agh.oop.model.map.Earth;
import agh.oop.simulation.DataHolder;
import agh.oop.simulation.Simulation;
import agh.oop.view.SimulationApp;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

import static java.util.concurrent.Executors.newFixedThreadPool;

public class HomePresenter {
    @FXML
    private ComboBox<String> savedConfigurationsBox;

    private Simulation simulationToRun;
    private String mapID;
    private String isSavingStats;
    private Earth earth;
    private DataHolder simulationParameters;

    private boolean comboBoxSelected = true;



    public void passOnParametersToHome(DataHolder parameters, String isSavingStats, int width, int height, String mapID) {
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
        Optional<List<String>> savedConfigurations = getSavedConfigurations();
        savedConfigurations.ifPresent(strings -> savedConfigurationsBox.setItems(FXCollections.observableArrayList(strings)));
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
            throw new IOException();
        }
        return simulationParameters;
    }

    private void setSimulationParametersFromFile() throws IOException {
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

        DataHolder simulationParameters = new DataHolder(simulationLength, reproduceEnergy, copulateEnergy,
                newPlantNumber, plantEnergy, newAnimalNumber, genomeLength, initialEnergy,
                mutationRange, mutationID, mapID);

        this.earth = new Earth(width, height);
        this.simulationToRun = new Simulation(earth,simulationParameters);
        this.mapID = mapID;
        this.isSavingStats = isSavingStats;
    }

    private void setSimulationParametersFromConfiguration() {
        this.simulationToRun = new Simulation(earth, simulationParameters);
    }

    public void onLaunchClicked() {
        if (comboBoxSelected) {
            try {
                setSimulationParametersFromFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            setSimulationParametersFromConfiguration();
        }
        SimulationApp.startSimulation(simulationToRun, earth, mapID, isSavingStats);
    }


    public void comboBoxUseHandler() {
        comboBoxSelected = true;
    }
}
