package agh.oop.model.objects.inheritance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

public class StandardMutation extends Mutation {
    public StandardMutation(int[] mutationRange) {
        super(mutationRange);
    }
    @Override
    List<Integer> mutateGenome(Genome genome){
        int genomeLength = genome.getGenomeLength();
        List<Integer> newGeneList = new ArrayList<>();
        var range = new ArrayList<>(IntStream.rangeClosed(0, genomeLength)
                .boxed().toList());
        Collections.shuffle(range);
        range = (ArrayList<Integer>) range.subList(0, mutationCount);
        for(int i=0; i<genomeLength; i++) {
            if (range.contains(i)) {
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
