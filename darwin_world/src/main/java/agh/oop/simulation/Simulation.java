package agh.oop.simulation;

import agh.oop.model.map.Earth;
import agh.oop.model.map.Vector2d;
import agh.oop.model.objects.Animal;
import agh.oop.model.objects.inheritance.Genome;
import agh.oop.model.objects.inheritance.Mutation;
import agh.oop.presenter.ChangeListener;

import java.util.*;

public class Simulation implements Runnable{
    private final Earth earth;
    private final int reproduceEnergy;
    private final Mutation mutation;
    private final int newPlantNumber;
    private final int plantEnergy;
    private final int animalNumber;
    private final int genomeLength;
    private final int initialEnergy;
    private final HashSet<Animal> animals;
    private final List<ChangeListener> listeners = new LinkedList<>();

    public Simulation(Earth earth, int reproduceEnergy, int newPlantNumber, int plantEnergy, int animalNumber, int genomeLength, int initialEnergy, Mutation mutation){
        this.earth = earth;
        this.reproduceEnergy = reproduceEnergy;
        this.mutation = mutation;
        this.newPlantNumber = newPlantNumber;
        this.plantEnergy = plantEnergy;
        this.animalNumber = animalNumber;
        this.genomeLength = genomeLength;
        this.initialEnergy = initialEnergy;
        this.animals = new HashSet<>();
    }



    @Override
    public void run() {
        SimulationInitializer simulationInitialization = new SimulationInitializer(earth, animals,
                newPlantNumber, plantEnergy, reproduceEnergy, animalNumber, genomeLength, initialEnergy);
        try {
            simulationInitialization.initialize();
            notifyListeners("Map has been initialized! Day " + 0);
            Thread.sleep(700);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        var notGrownFields = simulationInitialization.getNotGrownFields();
        SimulationDay simulationDay = new SimulationDay(earth, animals, notGrownFields, newPlantNumber,
                plantEnergy, reproduceEnergy, mutation);
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
