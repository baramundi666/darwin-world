package agh.oop.view;

import agh.oop.model.map.Earth;
import agh.oop.presenter.Configuration;
import agh.oop.presenter.HomePresenter;
import agh.oop.presenter.SimulationPresenter;
import agh.oop.simulation.Simulation;
import agh.oop.simulation.statictics.Statistics;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.concurrent.ExecutorService;

import static java.util.concurrent.Executors.newFixedThreadPool;


public class SimulationApp extends Application {

    private static final ExecutorService executor = newFixedThreadPool(8);

    private static HomePresenter homePage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("home.fxml"));
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
            loader.setLocation(SimulationApp.class.getClassLoader().getResource("simulation.fxml"));
            BorderPane viewRoot = loader.load();
            SimulationPresenter presenter = loader.getController();
            System.out.println(presenter);
            // mowimy mu jaka symulacje wyswietlic
            presenter.setSimulation(simulationToRun,earth, mapID, isSavingStats);
            simulationToRun.registerListener(presenter.getStatistics());
            simulationToRun.registerListener(presenter);
            // i tutaj ja wykonujemy
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
            loader.setLocation(SimulationApp.class.getClassLoader().getResource("configuration.fxml"));
            BorderPane viewRoot = loader.load();
            Configuration controller = loader.getController();
            controller.setHomePage(homePage);
            Stage primaryStage = new Stage();
            configureStage(primaryStage,viewRoot);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
