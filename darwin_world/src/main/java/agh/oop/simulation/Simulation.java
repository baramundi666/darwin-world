package agh.oop.simulation;

import agh.oop.model.map.Boundary;
import agh.oop.model.map.Earth;
import agh.oop.model.objects.Animal;
import agh.oop.model.objects.inheritance.Mutation;
import agh.oop.model.objects.inheritance.StandardMutation;
import agh.oop.model.objects.inheritance.SwapMutation;
import agh.oop.presenter.ChangeListener;
import agh.oop.simulation.day.AbstractSimulationDay;
import agh.oop.simulation.day.DefaultSimulationDay;
import agh.oop.simulation.day.VariedSimulationDay;
import agh.oop.simulation.spawner.AbstractSpawner;
import agh.oop.simulation.spawner.DefaultPlantSpawner;
import agh.oop.simulation.spawner.VariedPlantSpawner;
import agh.oop.simulation.statictics.DescendantsStatistics;
import agh.oop.simulation.statictics.PlantEatenCountStatistics;
import agh.oop.simulation.statictics.Statistics;

import java.util.*;

public class Simulation implements Runnable{

    private SimulationInitializer simulationInitialization;
    private AbstractSimulationDay simulationDay;
    private final Earth earth;
    private Mutation mutation;
    private AbstractSpawner spawner;
    private final HashSet<Animal> animals;
    private final List<ChangeListener> listeners = new LinkedList<>();
    private final DataHolder simulationParameters;

    private final Object simulationLock = new Object();
    private volatile boolean threadSuspended;

    public Simulation(Earth earth, DataHolder simulationParameters){
        this.earth = earth;
        this.simulationParameters = simulationParameters;
        this.animals = new HashSet<>();
        configureVariants();
    }

    public synchronized boolean isThreadSuspended() {
        return threadSuspended;
    }

    public void setThreadSuspended(boolean threadSuspended) {
        this.threadSuspended = threadSuspended;
    }

    public Earth getEarth() {
        return earth;
    }

    public Boundary getSpecialAreaBorders(){
        return spawner.getSpecialAreaBorders();
    }

    @Override
    public void run() {
        try {
            simulationInitialization.initialize();
            registerAnimalStatistics(animals);
            notifyListeners("Map " + earth.getId() + " has been initialized! Day " + 0);
            Thread.sleep(700);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        int i = 1;
        while (i <= simulationParameters.simulationLength()) {
            try {
                synchronized(this) {
                    while (threadSuspended)
                        wait();
                }
            } catch (InterruptedException e){
            }

            try {
                simulationDay.simulateOneDay();
                notifyListeners("Map " + earth.getId() + " has been changed! Day " + i);

                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            i++;
        }
    }

//    @Override
//    public void run() {
//        try {
//            simulationInitialization.initialize();
//            registerAnimalStatistics(animals);
//            notifyListeners("Map has been initialized! Day " + 0);
//            Thread.sleep(700);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//
//        for (int i = 1; i <= simulationParameters.simulationLength(); i++) {
//            try {
//                simulationDay.simulateOneDay();
//                notifyListeners("Map has been changed! Day " + i);
//                Thread.sleep(250);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//        }
//
//    }

    private void configureVariants() {
        switch(simulationParameters.mutationVariant()){
            case "m2":
                mutation = new SwapMutation(simulationParameters.mutationRange());
                break;
            case "m1":
                mutation = new StandardMutation(simulationParameters.mutationRange());
                break;
            default:
                throw new IllegalArgumentException("Unknown mutation variant");

        }

        switch (simulationParameters.mapVariant()) {
            case "p1" -> {
                spawner = new DefaultPlantSpawner(earth, simulationParameters);
                var notGrownFields = spawner.getNotGrownFields();
                simulationDay = new DefaultSimulationDay(earth, animals, notGrownFields, spawner,
                        mutation, simulationParameters);
            }
            case "p2" -> {
                spawner = new VariedPlantSpawner(earth, simulationParameters);
                var notGrownFields = spawner.getNotGrownFields();
                simulationDay = new VariedSimulationDay(earth, animals, notGrownFields, spawner,
                        mutation, simulationParameters);
            }
            default -> throw new IllegalArgumentException("Unknown map variant");
        }

        simulationInitialization = new SimulationInitializer(earth, animals,
                spawner, simulationParameters);
    }

    public void registerListener(ChangeListener listener) {
        listeners.add(listener);
    }

    private void notifyListeners(String message) {
        for (ChangeListener listener : listeners) {
            if (message.endsWith("Day 0")) listener.mapInitialized(earth, message);
            else listener.mapChanged(earth, message);
        }
    }

    private void registerAnimalStatistics(HashSet<Animal> animals){
        DescendantsStatistics descendantsStatistics = new DescendantsStatistics(animals);
        PlantEatenCountStatistics plantEatenCountStatistics = new PlantEatenCountStatistics(animals);
        simulationDay.registerListener(descendantsStatistics);
        simulationDay.registerListener(plantEatenCountStatistics);
    }
}
