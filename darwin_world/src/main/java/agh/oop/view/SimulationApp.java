package agh.oop.view;

import agh.oop.model.map.Earth;
import agh.oop.presenter.Configuration;
import agh.oop.presenter.SimulationPresenter;
import agh.oop.simulation.Simulation;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class SimulationApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("configuration.fxml"));
        BorderPane viewRoot = loader.load();
        Configuration presenter = loader.getController();
        configureStage(primaryStage,viewRoot);
        primaryStage.show();
    }

//    public void startSimulation() throws Exception {
//        FXMLLoader loader = new FXMLLoader();
//        loader.setLocation(getClass().getClassLoader().getResource("simulation.fxml"));
//        BorderPane viewRoot = loader.load();
//        SimulationPresenter presenter = loader.getController();
//        Stage primaryStage = new Stage();
//        configureStage(primaryStage,viewRoot);
//        primaryStage.show();
//    }

    private static void configureStage(Stage primaryStage, BorderPane viewRoot) {
        var scene = new Scene(viewRoot);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Simulation app");
        primaryStage.minWidthProperty().bind(viewRoot.minWidthProperty());
        primaryStage.minHeightProperty().bind(viewRoot.minHeightProperty());
    }
    public static void startSimulation(Simulation simulationToRun, Earth earth, String mapID, String isSavingStats) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(SimulationApp.class.getClassLoader().getResource("simulation.fxml"));
            BorderPane viewRoot = loader.load();
            SimulationPresenter presenter = loader.getController();
            presenter.setSimulation(simulationToRun,earth, mapID, isSavingStats);
            Stage primaryStage = new Stage();
            configureStage(primaryStage,viewRoot);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
