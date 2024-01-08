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
    private final Earth earth;
    private final int reproduceEnergy;

    private final int copulateEnergy;
    private Mutation mutation;
    private final int newPlantNumber;
    private final int plantEnergy;
    private final int animalNumber;
    private final int genomeLength;
    private final int initialEnergy;
    private final HashSet<Animal> animals;
    private final List<ChangeListener> listeners = new LinkedList<>();
    private final int[] mutationRange;
    private final String mutationVariant;
    private final String plantVariant;


    public Simulation(Earth earth, DataHolder simulationParameters){
        this.earth = earth;
        this.simul
        this.reproduceEnergy = data.reproduceEnergy();
        this.copulateEnergy = data.copulateEnergy();
        this.newPlantNumber = data.newPlantNumber();
        this.plantEnergy = data.plantEnergy();
        this.animalNumber = data.newAnimalNumber();
        this.genomeLength = data.genomeLength();
        this.initialEnergy = data.initialEnergy();
        this.animals = new HashSet<>();
        this.mutationRange = data.mutationRange();
        this.mutationVariant = data.mutationVariant();
        this.plantVariant = data.plantVariant();
    }

    @Override
    public void run() {
        switch(mutationVariant){//przeniesc switch
            case "m2":
                mutation = new SwapMutation(mutationRange);
                break;
            case "m1":
                mutation = new StandardMutation(mutationRange);
                break;
            default:
                throw new IllegalArgumentException("Unknown mutation variant");

        }

        AbstractSpawner spawner;
        AbstractSimulationDay simulationDay;

        switch (plantVariant) {
            case "p1" -> {
                spawner = new DefaultPlantSpawner(earth, newPlantNumber, plantEnergy);
                var notGrownFields = spawner.getNotGrownFields();
                simulationDay = new DefaultSimulationDay(earth, animals, notGrownFields, newPlantNumber,
                        plantEnergy, reproduceEnergy,spawner, mutation);
            }
            case "p2" -> {
                System.out.println("not working now");
                spawner = new VariedPlantSpawner(earth, newPlantNumber, plantEnergy);
                var notGrownFields = spawner.getNotGrownFields();
                simulationDay = new VariedSimulationDay(earth, animals, notGrownFields, newPlantNumber,
                        plantEnergy, copulateEnergy,spawner, mutation);
            }
            default -> throw new IllegalArgumentException("Unknown plant variant");
        };


        var simulationInitialization = new SimulationInitializer(earth, animals,
                reproduceEnergy, animalNumber,
                genomeLength, initialEnergy, spawner);


        try {
            simulationInitialization.initialize();
            notifyListeners("Map has been initialized! Day " + 0);
            Thread.sleep(700);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        for(int i=1;i<=50;i++){
            try {
                simulationDay.simulateOneDay();
                notifyListeners("Map has been changed! Day " + i);
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
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
