package agh.oop.model.objects.inheritance;

import agh.oop.model.map.MapDirection;
import agh.oop.model.map.Vector2d;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class GenomeTest {

    @Test
    public void testGetGeneList() {
        //Given
        var genList1 = List.of(1, 3, 7);
        var genList2 = List.of(5, 5, 6, 7, 1, 2, 3);

        //When
        var genome1 = new Genome(genList1, 3);
        var genome2 = new Genome(genList2, 7);

        //Then
        assertEquals(genList1, genome1.getGeneList());
        assertEquals(genList2, genome2.getGeneList());
        assertNotEquals(genList1, genome2.getGeneList());
    }

    @Test
    public void testNextGene() {
        //Given
        var genList = List.of(1, 3, 7);

        //When
        var genome = new Genome(genList, 3);
        var initialGene = genome.getActiveGene();
        var genomeLength = genome.getGenomeLength();
        genome.nextGene();

        //Then
        assertEquals((initialGene+1)%genomeLength,genome.getActiveGene());
    }

    @Test
    public void testMerge() {
        //Given
        var genList1 = List.of(1, 3, 7);
        var genList2 = List.of(5, 5, 6);
        int energy1 = 1;
        int energy2 = 2;

        //When
        var genome1 = new Genome(genList1, 3);
        var genome2 = new Genome(genList2, 7);
        var genomeMerged = genome1.merge(genome2, energy1, energy2);

        //Then
        boolean bool1 = Objects.equals(genomeMerged.getGeneList(), List.of(1, 5, 6));
        boolean bool2 = Objects.equals(genomeMerged.getGeneList(), List.of(5, 5, 7));
        assertTrue(bool1 || bool2);
    }
}