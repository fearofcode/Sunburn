package org.eccasts.sunburn;

import java.util.List;
import java.util.Random;

import org.uncommons.maths.random.MersenneTwisterRNG;
import org.uncommons.watchmaker.framework.CandidateFactory;

public class SunburnMain {
    public static void main(String[] args) {
        new SunburnMain().runSunburn();
    }

    private void runSunburn() {
        final int populationSize = 200;
        Random rng = new MersenneTwisterRNG();
        
        CandidateFactory<SunburnIndividual> candidateFactory = new SunburnFactory();
        
        List<SunburnIndividual> population = 
            candidateFactory.generateInitialPopulation(populationSize, rng);
        
        for(SunburnIndividual s : population)
            System.out.println(s);
    }
}
