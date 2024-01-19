package agh.oop.model.objects.inheritance;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.IntStream;

public class SwapMutation extends Mutation {
    public SwapMutation(int[] mutationRange) {
        super(mutationRange);
    }
    @Override
    public List<Integer> mutateGenome(Genome genome) {
        if(mutationCount==0) return genome.getGeneList();
        int genomeLength = genome.getGenomeLength();
        List<Integer> newGeneList = genome.getGeneList();

        for(int i=0; i<mutationCount; i++) {
            var firstRandomGeneIndex = (int) (Math.random() * genomeLength);
            var secondRandomGeneIndex = (int) (Math.random() * (genomeLength - 1));
            if (firstRandomGeneIndex <= secondRandomGeneIndex) secondRandomGeneIndex++;
            int tmp = newGeneList.get(firstRandomGeneIndex);
            newGeneList.set(firstRandomGeneIndex, newGeneList.get(secondRandomGeneIndex));
            newGeneList.set(secondRandomGeneIndex, tmp);
        }
        return newGeneList;
    }
}
