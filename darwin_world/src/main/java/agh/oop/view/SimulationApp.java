package agh.oop.view;

import agh.oop.model.map.Earth;
import agh.oop.presenter.SimulationPresenter;
import agh.oop.simulation.Simulation;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.concurrent.ExecutorService;

import static java.util.concurrent.Executors.newFixedThreadPool;


public class SimulationApp extends Application {

    private static final ExecutorService executor = newFixedThreadPool(4);

    private static HomePage homePage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("windows/home.fxml"));
        BorderPane viewRoot = loader.load();
        homePage = loader.getController();
        configureStage(primaryStage,viewRoot);
        primaryStage.show();
    }

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
            loader.setLocation(SimulationApp.class.getClassLoader().getResource("windows/simulation.fxml"));
            BorderPane viewRoot = loader.load();

            SimulationPresenter presenter = loader.getController();
            presenter.setSimulation(simulationToRun,earth, mapID, isSavingStats);

            simulationToRun.registerListener(presenter.getStatistics());
            simulationToRun.registerListener(presenter);
            var thread = new Thread(simulationToRun);
            executor.submit(thread);

            Stage primaryStage = new Stage();
            configureStage(primaryStage,viewRoot);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void newConfiguration() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(SimulationApp.class.getClassLoader().getResource("windows/configuration.fxml"));
            BorderPane viewRoot = loader.load();
            ConfigurationPage controller = loader.getController();
            controller.setHomePage(homePage);
            Stage primaryStage = new Stage();
            configureStage(primaryStage,viewRoot);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
