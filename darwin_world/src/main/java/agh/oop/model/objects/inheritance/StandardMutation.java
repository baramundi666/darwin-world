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
    List<Integer> mutateGenome(Genome genome){
        if(mutationCount==0) return genome.getGeneList();
        int genomeLength = genome.getGenomeLength();
        List<Integer> newGeneList = new ArrayList<>();
        var range = new ArrayList<>(IntStream.rangeClosed(0, genomeLength-1)
                .boxed().toList());
        Collections.shuffle(range);
        HashSet<Integer> indexes = new HashSet<>(range.subList(0, mutationCount));
        for(int i=0; i<genomeLength; i++) {
            if (indexes.contains(i)) {
                var randomGene = (int) (Math.random() * 8);
                newGeneList.add(randomGene);
            }
            else {
                newGeneList.add(genome.getGeneList().get(i));
            }
        }
        return newGeneList;
    }
}
