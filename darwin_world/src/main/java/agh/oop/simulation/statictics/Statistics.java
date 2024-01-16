package agh.oop.simulation.statictics;

import agh.oop.model.map.Earth;
import agh.oop.model.map.Vector2d;
import agh.oop.model.objects.Animal;
import agh.oop.presenter.ChangeListener;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.*;

public class Statistics implements ChangeListener {
    private String isSavingStats;
    private int numberOfAnimals=0;
    private int numberOfPlants=0;
    private int numberOfNotOccupiedFields=0;
    private double averageEnergy=0;
    private Optional<List<Integer>> dominantGenotype=Optional.empty();
    private double averageLifeLength=0;//for dead
    private double averageNumberOfChildren=0;//for alive

    public Statistics(String isSavingStats){
        this.isSavingStats = isSavingStats;
    }


    public void mapChanged(Earth earth, String message) {
        numberOfAnimals = earth.getAliveAnimals().size();
        numberOfPlants = earth.getPlants().size();
        numberOfNotOccupiedFields = countNotOccupiedFields(earth);
        averageEnergy = countAverageEnergy(earth);
        dominantGenotype = findDominantGenotype(earth);
        averageLifeLength = countAverageLifeLength(earth);
        averageNumberOfChildren = findAverageNumberOfChildren(earth);
        if(Objects.equals(isSavingStats, "true")) writeToFile(earth, message);
    }

    public String dominantGenotypeToString(){
        if(dominantGenotype.isEmpty()) return "No animals";
        StringBuilder sb = new StringBuilder();
        for(Integer gene: dominantGenotype.get()){
            sb.append(gene);
            sb.append(" ");
        }
        return sb.toString();
    }

    private int countNotOccupiedFields(Earth earth) {
        int notOccupiedFieldsCount = earth.getArea();
        HashSet<Vector2d> occupiedFields = new HashSet<>(earth.getAnimals().keySet());
        occupiedFields.addAll(earth.getPlants().keySet());

        return notOccupiedFieldsCount - occupiedFields.size();
    }

    private double countAverageEnergy(Earth earth){
        HashSet<Animal> animals = earth.getAliveAnimals();
        if(animals.isEmpty()) return 0;
        double sum = 0;
        for(Animal animal: animals){
            sum += animal.getEnergy();
        }
        return sum/animals.size();
    }
    
    private Optional<List<Integer>> findDominantGenotype(Earth earth){
        HashSet<Animal> animals = earth.getAliveAnimals();
        if(animals.isEmpty()) return Optional.empty();
        HashMap<List<Integer>,Integer> genotypeCount = new HashMap<>();
        for(Animal animal: animals){
            List<Integer> geneList = (animal.getGenome()).getGeneList();
            if(genotypeCount.containsKey(geneList)){
                genotypeCount.put(geneList, genotypeCount.get(geneList)+1);
            }
            else{
                genotypeCount.put(geneList, 1);
            }
        }

        List<Integer> maxGenotype = genotypeCount.keySet().iterator().next();
        int max = genotypeCount.get(maxGenotype);

        for(List<Integer> genotype: genotypeCount.keySet()){
            if(genotypeCount.get(genotype) > max){
                max = genotypeCount.get(genotype);
                maxGenotype = genotype;
            }
        }
        return Optional.of(maxGenotype);
    }
    //most popular genotypes???
    
    private double findAverageNumberOfChildren(Earth earth){
        HashSet<Animal> animals = earth.getAliveAnimals();
        if(animals.isEmpty()) return 0;
        double sum = 0;
        for(Animal animal: animals){
            sum += animal.getChildrenCount();
        }
        return sum/animals.size();
    }

    private double countAverageLifeLength(Earth earth) {
        HashSet<Animal> deadAnimals = earth.getDeadAnimals();
        if (deadAnimals.isEmpty()) return 0;
        double sum = 0;
        for (Animal animal : deadAnimals) {
            sum += animal.getLifeLength();
        }
        return sum / deadAnimals.size();
    }

    public synchronized void writeToFile(Earth earth, String message) {
        Path path = Path.of("src\\main\\resources\\statistics\\" + "map_" + earth.getId() + ".csv");

        try (PrintWriter writer = new PrintWriter(new FileWriter(path.toString(), true))) {
            writer.println(message);
            writer.println("Animal number: " + this.numberOfAnimals);
            writer.println("Plant number: " + this.numberOfPlants);
            writer.println("Not occupied fields number: " + this.numberOfNotOccupiedFields);
            writer.println("Average animal energy: " + this.averageEnergy);
            writer.println("Dominant genotype: " + dominantGenotypeToString());
            writer.println("Average dead animals live length: " + this.averageLifeLength);
            writer.println("Average alive animal number of children: " + this.averageNumberOfChildren);
            writer.println();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
