package org.eccasts.sunburn;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.uncommons.maths.random.MersenneTwisterRNG;
import org.uncommons.watchmaker.framework.factories.StringFactory;

public class SunburnMain {
    public static void main(String[] args) throws IOException {
        new SunburnMain().runSunburn();
    }

    private void runSunburn() throws IOException {
        Random rng = new MersenneTwisterRNG();
        
        /*SunburnShip me = new SunburnShip(rng, 3);
        me.setSlots("DDDDDGGGGLLLLMMSSSS");
        SunburnShip him = new SunburnShip(rng, 17);
        him.setSlots("DGGLLMMMMMMMSSSSSSSS");
        me.fightAgainst(him);*/
        
        final int populationSize = 200;
        int generationCount = 10000;
        
        
        List<SunburnGenome> population = generateInitialPopulation(populationSize, rng);
        
        Logger logger = Logger.getLogger(SunburnShip.class.getName());
        logger.setLevel(Level.SEVERE);
        
        Logger results = Logger.getLogger(SunburnMain.class.getName());
        results.setLevel(Level.SEVERE);
        
        
        FileWriter fstream = new FileWriter(System.currentTimeMillis() + ".dat");
        BufferedWriter out = new BufferedWriter(fstream);
        
        for(int generation = 1; generation <= generationCount; generation++) {
            List<Integer> firstWinnerAndLoserIndex = findWinnerAndLoser(rng,
                    populationSize, population, results, generation);
            
            int firstWinnerIndex = firstWinnerAndLoserIndex.get(0);
            int firstLoserIndex = firstWinnerAndLoserIndex.get(1);
            
            results.info(population.get(firstWinnerIndex) + " triumphs over " + population.get(firstLoserIndex));
            
            List<Integer> secondWinnerAndLoserIndex = findWinnerAndLoser(rng,
                    populationSize, population, results, generation);
            
            int secondWinnerIndex = secondWinnerAndLoserIndex.get(0);
            int secondLoserIndex = secondWinnerAndLoserIndex.get(1);
            
            results.info(population.get(secondWinnerIndex) + " triumphs over " + population.get(secondLoserIndex));
            
            SunburnGenome parent1 = population.get(firstWinnerIndex);
            SunburnGenome parent2 = population.get(firstWinnerIndex);
            
            List<SunburnGenome> children = SunburnGenome.crossoverParents(parent1, parent2, 2, rng);
            children.set(0, children.get(0).mutateIndividual(rng));
            children.set(1, children.get(1).mutateIndividual(rng));
            
            population.set(firstLoserIndex, children.get(0));
            population.set(secondLoserIndex, children.get(1));
            
            if(generation % 100 == 0)
                writeData(generation, population, out);
        }
        
        writeData(10000, population, out);
        out.close();
        
        for(SunburnGenome s : population)
            System.out.println(s);
        
    }

    private void writeData(int generation, List<SunburnGenome> population, BufferedWriter out) throws IOException {
        Map<Character, Integer> counts = new HashMap<Character, Integer>();
        counts.put('D', 0);
        counts.put('S', 0);
        counts.put('L', 0);
        counts.put('M', 0);
        counts.put('G', 0);
        
        for(SunburnGenome g : population) {
            String slots = g.getSlots();
            
            for(int i = 0; i < slots.length(); i++) {
                char ch = slots.charAt(i);
                
                counts.put(ch, counts.get(ch)+1);
            }
        }
        
        out.write(generation + " " + counts.get('D') + " " + counts.get('S') + " " + counts.get('L') + " " + counts.get('M') + " " + counts.get('G') + "\n");
    }

    public SunburnGenome generateRandomCandidate(Random rng) {
        StringFactory sf = new StringFactory(SunburnShip.ALPHABET, SunburnShip.GENOME_LENGTH);
        String slots = sf.generateRandomCandidate(rng);
        int preferredRange = rng.nextInt(SunburnShip.MAX_PREFERRED_RANGE)+1;
        
        return new SunburnGenome(slots, preferredRange);
    }

    private List<SunburnGenome> generateInitialPopulation(int populationSize,
            Random rng) {
        List<SunburnGenome> population = new ArrayList<SunburnGenome>(populationSize);
        
        for(int i = 1; i <= populationSize; i++)
            population.add(generateRandomCandidate(rng));
        
        return population;
    }

    private List<Integer> findWinnerAndLoser(Random rng,
            final int populationSize, List<SunburnGenome> population,
            Logger results, int generation) {
        List<Integer> winnerAndLoserIndex = new ArrayList<Integer>(2);
        
        results.info("Looking for a winner and loser in generation " + generation);
        
        while(winnerAndLoserIndex.size() != 2) {
            int candidateIndex = rng.nextInt(populationSize);
            int enemyIndex = rng.nextInt(populationSize);
            SunburnGenome candidateGenome = population.get(candidateIndex);
            SunburnGenome enemyGenome = population.get(enemyIndex);
            
            SunburnShip candidateShip = new SunburnShip(rng, candidateGenome);
            SunburnShip enemyShip = new SunburnShip(rng, enemyGenome);
            
            int result = candidateShip.fightAgainst(enemyShip);
            
            if(result == 1) {
                winnerAndLoserIndex.add(candidateIndex);
                winnerAndLoserIndex.add(enemyIndex);
            } else if(result == -1) {
                winnerAndLoserIndex.add(enemyIndex);
                winnerAndLoserIndex.add(candidateIndex);
            }
        }
        return winnerAndLoserIndex;
    }
    
}
