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
        List<Integer> newGeneList = new ArrayList<>();
        var range = new ArrayList<>(IntStream.rangeClosed(0, genomeLength-1)
                .boxed().toList());
        Collections.shuffle(range);
        HashSet<Integer> indices = new HashSet<>(range.subList(0, mutationCount));
        var preMutationGeneList = genome.getGeneList();
        for(int i=0; i<genomeLength; i++) {
            if (indices.contains(i)) {
                var randomGeneShift = 1 + (int) (Math.random() * 7);
                newGeneList.add((preMutationGeneList.get(i)+randomGeneShift)%8);
            }
            else {
                newGeneList.add(preMutationGeneList.get(i));
            }
        }
        return newGeneList;
    }
}
