package agh.oop.model.objects.inheritance;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class StandardMutationTest {

    @Test
    public void testMutateGenome1(){
        //Given
        var preMutationGeneList = List.of(1,5,6,7,4,3,2);
        var genome = new Genome(preMutationGeneList, 7);
        int genomeLength = genome.getGenomeLength();
        var mutationRange = new int[]{0,0};

        //When
        var mutation = new StandardMutation(mutationRange);
        var postMutationGeneList = mutation.mutateGenome(genome);
        int mutatedGenesCount = 0;
        for (int i=0; i<genomeLength; i++){
            if (!Objects.equals(preMutationGeneList.get(i), postMutationGeneList.get(i))) {
                mutatedGenesCount++;
            }
        }

        //Then
        assertEquals(0, mutatedGenesCount);
    }

    @Test
    public void testMutateGenome2(){
        //Given
        var preMutationGeneList = List.of(1,5,6,7,4,3,2);
        var genome = new Genome(preMutationGeneList, 7);
        int genomeLength = genome.getGenomeLength();
        var mutationRange = new int[]{2,2};

        //When
        var mutation = new StandardMutation(mutationRange);
        var postMutationGeneList = mutation.mutateGenome(genome);
        int mutatedGenesCount = 0;
        for (int i=0; i<genomeLength; i++){
            if (!Objects.equals(preMutationGeneList.get(i), postMutationGeneList.get(i))) {
                mutatedGenesCount++;
            }
        }

        //Then
        assertEquals(2, mutatedGenesCount);
    }

    @Test
    public void testMutateGenome3(){
        //Given

        var preMutationGeneList = List.of(2,3,5,7,1,0,3,4);
        var genome = new Genome(preMutationGeneList, 8);
        int genomeLength = genome.getGenomeLength();
        var mutationRange = new int[]{3,4};

        //When
        var mutation = new StandardMutation(mutationRange);
        var postMutationGeneList = mutation.mutateGenome(genome);
        int mutatedGenesCount = 0;
        for (int i=0; i<genomeLength; i++){
            if (!Objects.equals(preMutationGeneList.get(i), postMutationGeneList.get(i))) {
                mutatedGenesCount++;
            }
        }

        //Then
        assertTrue(mutatedGenesCount==3 || mutatedGenesCount==4);
    }
}