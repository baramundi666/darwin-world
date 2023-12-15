package agh.opp.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class Genome {
    private final List<Integer> geneList;
    private final int[] mutationRange;
    private final int genomeLength;

    private int activeGene;

    public Genome(List<Integer> geneList, int[] mutationRange, int genomeLength) {
        this.geneList = geneList;
        this.mutationRange = mutationRange;
        this.genomeLength = genomeLength;
        this.activeGene=0;
    }

    public List<Integer> getGeneList() {
        return new ArrayList<>(geneList);
    }

    public int getActiveGene() {
        return activeGene;
    }

    public void nextGene() {
        if (activeGene==genomeLength-1) {
            activeGene=0;
        }
        else {
            activeGene++;
        }
    }

    public Genome merge(Genome other, float percentage) {
        List<Integer> newGeneList = new ArrayList<>();
        int cut = (int) (percentage*genomeLength);
        int rnd = (int)(Math.random()*2);
        // percentage - procent udzialu silniejszego
        // this - gen silniejszego, other - gen slabszego
        if (rnd==0) { // silniejsze lewe
            for(int i=0; i<cut; i++) {
                newGeneList.add(this.geneList.get(i));
            }
            for(int i=cut; i<genomeLength; i++) {
                newGeneList.add(other.geneList.get(i));
            }
        }
        else { // silniejsze prawe
            for(int i=0; i<genomeLength-cut; i++) {
                newGeneList.add(other.geneList.get(i));
            }
            for(int i=genomeLength-cut; i<genomeLength; i++) {
                newGeneList.add(this.geneList.get(i));
            }
        }
        return new Genome(newGeneList, mutationRange, genomeLength);
    }

    public Genome mutate() {
        List<Integer> newGeneList = new ArrayList<>();
        int howMany = (int)(Math.random()*genomeLength);
        var range = new ArrayList<>(IntStream.rangeClosed(0, genomeLength)
                .boxed().toList());
        Collections.shuffle(range);
        range = (ArrayList<Integer>) range.subList(0, howMany);
        for(int i=0; i<genomeLength; i++) {
            if (range.contains(i)) {
                var rndGene = (int) (Math.random() * 8);
                newGeneList.add(rndGene);
            }
            else {
                newGeneList.add(geneList.get(i));
            }
        }

        return new Genome(newGeneList, mutationRange, genomeLength);
    }
}
