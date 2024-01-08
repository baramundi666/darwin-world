package agh.oop.simulation;

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


    public Simulation(Earth earth, DataHolder simulationParameters){
        this.earth = earth;
        this.simulationParameters = simulationParameters;
        this.animals = new HashSet<>();
        configureVariants();
    }

    @Override
    public void run() {
        try {
            simulationInitialization.initialize();
            notifyListeners("Map has been initialized! Day " + 0);
            Thread.sleep(700);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        for(int i=1;i<=simulationParameters.simulationLength();i++){
            try {
                simulationDay.simulateOneDay();
                notifyListeners("Map has been changed! Day " + i);
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void configureVariants() {
        switch(simulationParameters.mutationVariant()){//przeniesc switch
            case "m2":
                mutation = new SwapMutation(simulationParameters.mutationRange());
                break;
            case "m1":
                mutation = new StandardMutation(simulationParameters.mutationRange());
                break;
            default:
                throw new IllegalArgumentException("Unknown mutation variant");

        }

        switch (simulationParameters.plantVariant()) {
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
            default -> throw new IllegalArgumentException("Unknown plant variant");
        }

        simulationInitialization = new SimulationInitializer(earth, animals,
                spawner, simulationParameters);
    }

    public void registerListener(ChangeListener listener) {
        listeners.add(listener);
    }

    private void notifyListeners(String message) {
        for (ChangeListener listener : listeners) {
            listener.mapChanged(earth, message);
        }
    }
}
