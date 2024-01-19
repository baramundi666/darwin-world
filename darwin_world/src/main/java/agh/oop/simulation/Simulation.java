package agh.oop.simulation;

import agh.oop.model.map.Boundary;
import agh.oop.model.map.Earth;
import agh.oop.model.objects.Animal;
import agh.oop.model.objects.inheritance.Mutation;
import agh.oop.model.objects.inheritance.StandardMutation;
import agh.oop.model.objects.inheritance.SwapMutation;
import agh.oop.presenter.ChangeListener;
import agh.oop.simulation.data.SimulationData;
import agh.oop.simulation.day.AbstractSimulationDay;
import agh.oop.simulation.day.DefaultSimulationDay;
import agh.oop.simulation.day.VariedSimulationDay;
import agh.oop.simulation.spawner.AbstractSpawner;
import agh.oop.simulation.spawner.DefaultPlantSpawner;
import agh.oop.simulation.spawner.VariedPlantSpawner;
import agh.oop.simulation.statistics.AnimalStatistics;
import agh.oop.simulation.statistics.DescendantsStatistics;
import agh.oop.simulation.statistics.PlantEatenCountStatistics;

import java.util.*;

public class Simulation implements Runnable{

    private SimulationInitializer simulationInitialization;
    private AbstractSimulationDay simulationDay;
    private final Earth earth;
    private AbstractSpawner spawner;
    private final HashSet<Animal> animals;
    private final List<ChangeListener> listeners = new LinkedList<>();
    private final SimulationData simulationParameters;
    private volatile boolean threadSuspended = false;
    private PlantEatenCountStatistics plantEatenCountStatistics;
    private DescendantsStatistics descendantsStatistics;

    public Simulation(Earth earth, SimulationData simulationParameters){
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

    public AnimalStatistics getAnimalStatistics(Animal animal){
        return new AnimalStatistics(animal, plantEatenCountStatistics, descendantsStatistics);
    }

    @Override
    public void run() {
        try {
            simulationInitialization.initialize();
            registerAnimalStatistics(animals);
            notifyListeners("Map " + earth.getId() + " has been initialized! Day " + 0);
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        int i = 1;
        while (i <= simulationParameters.simulationLength()) {
            if (threadSuspended) continue;
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

    private void configureVariants() {
        Mutation mutation;
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
        descendantsStatistics = new DescendantsStatistics(animals);
        plantEatenCountStatistics = new PlantEatenCountStatistics(animals);
        simulationDay.registerStatisticsListener(descendantsStatistics);
        simulationDay.registerStatisticsListener(plantEatenCountStatistics);
    }
}
