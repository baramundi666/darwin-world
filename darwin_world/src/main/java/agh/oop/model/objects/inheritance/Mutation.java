package agh.oop.model.objects.inheritance;

import java.util.List;

public abstract class Mutation {
    protected final int mutationCount;

    protected Mutation(int[] mutationRange){
        this.mutationCount = (int) (Math.random()*(mutationRange[1]-mutationRange[0]+1))+mutationRange[0];
    }
    abstract List<Integer> mutateGenome(Genome genome);
}
