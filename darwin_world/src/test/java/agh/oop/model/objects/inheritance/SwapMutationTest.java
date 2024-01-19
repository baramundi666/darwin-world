package agh.oop.model.objects.inheritance;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class SwapMutationTest {

    @Test
    public void testMutateGenome1(){//to fix
        //Given
        var preMutationGeneList = List.of(1,5,6,7,4,3,2);
        var genome = new Genome(preMutationGeneList, 7);
        int genomeLength = genome.getGenomeLength();
        var mutationRange = new int[]{0,0};

        //When
        var mutation = new SwapMutation(mutationRange);
        var postMutationGeneList = mutation.mutateGenome(genome);
        int modifiedGenesCount = 0;
        for (int i=0; i<genomeLength; i++){
            if (!Objects.equals(preMutationGeneList.get(i), postMutationGeneList.get(i))) {
                modifiedGenesCount++;
            }
        }

        //Then
        assertEquals(0, modifiedGenesCount);
    }

    @Test
    public void testMutateGenome2(){
        //Given
        var preMutationGeneList = List.of(1,5,6,7,4,3,2);
        var genome = new Genome(preMutationGeneList, 7);
        int genomeLength = genome.getGenomeLength();
        var mutationRange = new int[]{2,2};

        //When
        var mutation = new SwapMutation(mutationRange);
        var postMutationGeneList = mutation.mutateGenome(genome);
        int modifiedGenesCount = 0;
        for (int i=0; i<genomeLength; i++){
            if (!Objects.equals(preMutationGeneList.get(i), postMutationGeneList.get(i))) {
                modifiedGenesCount++;
            }
        }

        //Then
        assertEquals(2, modifiedGenesCount/2);
    }

    @Test
    public void testMutateGenome3(){
        //Given

        var preMutationGeneList = List.of(2,3,5,7,1,0,6,4);
        var genome = new Genome(preMutationGeneList, 8);
        int genomeLength = genome.getGenomeLength();
        var mutationRange = new int[]{3, 4};

        //When
        var mutation = new SwapMutation(mutationRange);
        var postMutationGeneList = mutation.mutateGenome(genome);
        int modifiedGenesCount = 0;
        for (int i=0; i<genomeLength; i++){
            if (!Objects.equals(preMutationGeneList.get(i), postMutationGeneList.get(i))) {
                modifiedGenesCount++;
            }
        }


        System.out.println(modifiedGenesCount/2);

        //Then
        assertTrue(modifiedGenesCount/2==3 || modifiedGenesCount/2==4);
    }
}