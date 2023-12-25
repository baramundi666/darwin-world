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
        int genomeLength = genome.getGenomeLength();
        List<Integer> newGeneList = new ArrayList<>();
        var range = new ArrayList<>(IntStream.rangeClosed(0, genomeLength-1)
                .boxed().toList());
        Collections.shuffle(range);
        var indices = new ArrayList<>(range.subList(0, 2*mutationCount));
        var indicesToSwap = new HashMap<Integer, Integer>();
        for (int i=0; i<2*mutationCount; i++) {
            indicesToSwap.put(indices.get(i), indices.get(2*mutationCount-1-i));
        }
        var preMutationGeneList = genome.getGeneList();
        for(int i=0; i<genomeLength; i++) {
            newGeneList.add(preMutationGeneList.get(indicesToSwap.getOrDefault(i, i)));
        }
        return newGeneList;
    }
}
