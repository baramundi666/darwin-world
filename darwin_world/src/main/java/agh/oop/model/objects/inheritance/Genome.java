package agh.oop.model.objects.inheritance;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class Genome {
    private final List<Integer> geneList;
    private final int genomeLength;
    private int activeGene;

    public Genome(List<Integer> geneList, int genomeLength) {
        this.geneList = geneList;
        this.genomeLength = genomeLength;
        this.activeGene=(int) (Math.random()*genomeLength);
    }
    public List<Integer> getGeneList() {
        return new ArrayList<>(geneList);
    }
    public int getGenomeLength(){
        return genomeLength;
    }

    public int getActiveGene() {
        return activeGene;
    }
    public int getActiveGeneValue() {
        return geneList.get(activeGene);
    }

    public int takeAnotherGene() {
        int activeGeneValue = geneList.get(activeGene);
        nextGene();
        return activeGeneValue;
    }

    public void nextGene() {
        if (activeGene==genomeLength-1) {
            activeGene=0;
        }
        else {
            activeGene++;
        }
    }

    public Genome generateNewGenome(Mutation mutation, Genome other, double percentage) {
        var newGenome = merge(other, percentage);
        newGenome = newGenome.mutate(mutation);
        return newGenome;
    }

    public Genome merge(Genome other, double percentage) {
        List<Integer> newGeneList = new ArrayList<>();
        int cut = (int) (percentage*genomeLength);
        int mergeType = (int)(Math.random()*2);
        if (mergeType==0) {
            for(int i=0; i<cut; i++) {
                newGeneList.add(this.geneList.get(i));
            }
            for(int i=cut; i<genomeLength; i++) {
                newGeneList.add(other.geneList.get(i));
            }
        }
        else {
            for(int i=0; i<genomeLength-cut; i++) {
                newGeneList.add(other.geneList.get(i));
            }
            for(int i=genomeLength-cut; i<genomeLength; i++) {
                newGeneList.add(this.geneList.get(i));
            }
        }
        return new Genome(newGeneList, genomeLength);
    }
    public Genome mutate(Mutation mutation) {
        return new Genome(mutation.mutateGenome(this), genomeLength);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(Integer gene: geneList){
            sb.append(gene);
            sb.append(" ");
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (Objects.isNull(o) || getClass() != o.getClass()) return false;
        Genome genome = (Genome) o;
        return genomeLength == genome.genomeLength && Objects.equals(geneList, genome.geneList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(geneList, genomeLength);
    }
}
