package org.eccasts.sunburn;

import java.util.List;
import java.util.Random;

import org.uncommons.maths.random.MersenneTwisterRNG;
import org.uncommons.maths.random.Probability;
import org.uncommons.watchmaker.framework.CandidateFactory;
import org.uncommons.watchmaker.framework.EvolutionaryOperator;
import org.uncommons.watchmaker.framework.operators.EvolutionPipeline;

public class SunburnMain {
    public static void main(String[] args) {
        new SunburnMain().runSunburn();
    }

    private void runSunburn() {
        final int populationSize = 200;
        double crossoverProbability = 1.0;
        double mutationProbability = 1.0;
        
        Random rng = new MersenneTwisterRNG();
        
        CandidateFactory<SunburnGenome> candidateFactory = new SunburnFactory();
        
        /*List<SunburnIndividual> population = 
            candidateFactory.generateInitialPopulation(populationSize, rng);
        
        for(SunburnIndividual s : population)
            System.out.println(s.toCrossoverForm());*/
        
        EvolutionaryOperator<SunburnGenome> crossover = 
            new SunburnCrossover(2, new Probability(crossoverProbability));
        
        EvolutionaryOperator<SunburnGenome> mutation =
            new SunburnMutation(SunburnShip.ALPHABET, new Probability( mutationProbability));
        
        
        EvolutionaryOperator<SunburnGenome> pipeline =
            new EvolutionPipeline<SunburnGenome>(pipeline)
    }
}
