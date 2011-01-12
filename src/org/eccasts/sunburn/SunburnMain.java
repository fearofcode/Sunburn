package org.eccasts.sunburn;

import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.uncommons.maths.random.MersenneTwisterRNG;
import org.uncommons.watchmaker.framework.CandidateFactory;

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
        
        List<SunburnGenome> population = 
            candidateFactory.generateInitialPopulation(populationSize, rng);
        
        for(SunburnGenome s : population)
            System.out.println(s.toCrossoverForm());
        
        Logger logger = Logger.getLogger(SunburnShip.class.getName());
        logger.setLevel(Level.SEVERE);
        
    }
}
