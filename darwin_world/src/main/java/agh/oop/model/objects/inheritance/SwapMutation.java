package agh.oop.model.objects.inheritance;

import java.util.List;

public class SwapMutation extends Mutation {
    protected SwapMutation(int[] mutationRange) {
        super(mutationRange);
    }
    @Override
    List<Integer> mutateGenome(Genome genome) {
        return null;
        //to do
    }
}
