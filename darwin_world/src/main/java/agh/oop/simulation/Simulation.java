package agh.oop.simulation;

import agh.oop.model.map.Earth;
import agh.oop.model.objects.Animal;
import agh.oop.model.objects.inheritance.Mutation;
import agh.oop.model.objects.inheritance.StandardMutation;
import agh.oop.model.objects.inheritance.SwapMutation;
import agh.oop.presenter.ChangeListener;

import java.util.*;

public class Simulation implements Runnable{
    private final Earth earth;
    private final int reproduceEnergy;
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


    public Simulation(Earth earth, int reproduceEnergy,
                      int newPlantNumber, int plantEnergy, int animalNumber,
                      int genomeLength, int initialEnergy,
                      int[] mutationRange, String mutationVariant, String plantVariant){
        this.earth = earth;
        this.reproduceEnergy = reproduceEnergy;
        this.newPlantNumber = newPlantNumber;
        this.plantEnergy = plantEnergy;
        this.animalNumber = animalNumber;
        this.genomeLength = genomeLength;
        this.initialEnergy = initialEnergy;
        this.animals = new HashSet<>();
        this.mutationRange = mutationRange;
        this.mutationVariant = mutationVariant;
        this.plantVariant = plantVariant;
    }

    @Override
    public void run() {
        switch(mutationVariant){
            case "m2":
                mutation = new SwapMutation(mutationRange);
                break;
            case "m1":
                mutation = new StandardMutation(mutationRange);
                break;
            default:
                throw new IllegalArgumentException("Unknown mutation variant");

        }

        AbstractSpawner spawner = switch (plantVariant) {
            case "p2" -> new DefaultPlantSpawner(earth, newPlantNumber, plantEnergy);
            case "p1" -> new VariedPlantSpawner(earth, newPlantNumber, plantEnergy);
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


        var notGrownFields = spawner.getNotGrownFields();
        AbstractSimulationDay simulationDay = switch (plantVariant) {
            case "p2" -> new DefaultSimulationDay(earth, animals, notGrownFields, newPlantNumber,
                    plantEnergy, reproduceEnergy,spawner, mutation);
//            case "p1" -> new VariedSimulationDay(earth, animals, notGrownFields, newPlantNumber,
//                    plantEnergy, reproduceEnergy,spawner, mutation);
            default -> throw new IllegalArgumentException("Unknown plant variant");
        };


        for(int i=1;i<=1000;i++){
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
