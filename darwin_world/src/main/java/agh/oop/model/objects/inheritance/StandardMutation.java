package agh.oop.model.objects.inheritance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.IntStream;

public class StandardMutation extends Mutation {
    public StandardMutation(int[] mutationRange) {
        super(mutationRange);
    }
    @Override
    public List<Integer> mutateGenome(Genome genome){
        if(mutationCount==0) return genome.getGeneList();
        int genomeLength = genome.getGenomeLength();
        List<Integer> newGeneList = genome.getGeneList();

        for(int i=0; i<mutationCount; i++) {
            var randomGene = (int) (Math.random() * 8);
            var randomGeneIndex = (int) (Math.random() * genomeLength);
            newGeneList.set(randomGeneIndex, randomGene);
        }
        return newGeneList;
    }
}
