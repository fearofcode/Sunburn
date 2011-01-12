package org.eccasts.sunburn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class SunburnGenome {
    private String slots;
    private int preferredRange;
    
    public SunburnGenome(String slots, int preferredRange) {
        super();
        this.slots = slots;
        this.preferredRange = preferredRange;
    }
    
    public String getSlots() {
        return slots;
    }
    
    public void setSlots(String slots) {
        this.slots = slots;
    }
    
    public int getPreferredRange() {
        return preferredRange;
    }
    
    public void setPreferredRange(int preferredRange) {
        this.preferredRange = preferredRange;
    }
    
    public String toString() {
        char[] chars = slots.toCharArray();
        Arrays.sort(chars);
        
        return "<" + slots + ", " + preferredRange + "> (" + new String(chars) + ")";
    }
    
    public String toCrossoverForm() {
        return slots + String.format("%02d", preferredRange);
    }
    
    public static SunburnGenome parseFromString(String crossOverString) {
        return new SunburnGenome(crossOverString.substring(0, crossOverString.length()-2), 
                new Integer(crossOverString.substring(crossOverString.length()-2)));
    }
    
    public SunburnGenome mutateIndividual(Random rng) {
        SunburnGenome mutated = new SunburnGenome(new String(slots), preferredRange);
        
        if(rng.nextInt() % 10 == 0) {
            if(rng.nextInt() % 2 != 0) {
                mutated.setPreferredRange(Math.min(mutated.getPreferredRange()+1, SunburnShip.MAX_PREFERRED_RANGE));
            } else {
                mutated.setPreferredRange(Math.max(mutated.getPreferredRange()-1, SunburnShip.MIN_PREFERRED_RANGE));
            }
        } else {
            StringBuilder buffer = new StringBuilder(mutated.getSlots());
            int index = rng.nextInt(mutated.getSlots().length());
            
            buffer.setCharAt(index, SunburnShip.ALPHABET[rng.nextInt(SunburnShip.ALPHABET.length)]);
            
            mutated.setSlots(buffer.toString());
        }
        
        return mutated;
    }
    
    public static List<SunburnGenome> crossoverParents(SunburnGenome parent1,
            SunburnGenome parent2, int numberOfCrossoverPoints, Random rng) {
        if (parent1.getSlots().length() != parent2.getSlots().length())
        {
            throw new IllegalArgumentException("Cannot perform cross-over with different length parents.");
        }
        
        String parent1WithRange = parent1.toCrossoverForm();
        String parent2WithRange = parent2.toCrossoverForm();
        
        StringBuilder offspring1 = new StringBuilder(parent1WithRange);
        StringBuilder offspring2 = new StringBuilder(parent2WithRange);
        
        int firstIndex = rng.nextInt(parent1WithRange.length());
        int secondIndex = rng.nextInt(parent1WithRange.length());
        
        if(secondIndex < firstIndex) {
            int temp = firstIndex;
            firstIndex = secondIndex;
            secondIndex = temp;
        }
        
        for(int i = firstIndex; i <= secondIndex; i++) {
            char temp = offspring1.charAt(i);
            offspring1.setCharAt(i, offspring2.charAt(i));
            offspring2.setCharAt(i, temp);
        }
        
        /*// Apply as many cross-overs as required.
        for (int i = 0; i < numberOfCrossoverPoints; i++)
        {
            // Cross-over index is always greater than zero and less than
            // the length of the parent so that we always pick a point that
            // will result in a meaningful cross-over.
            int crossoverIndex = (1 + rng.nextInt(parent1WithRange.length() - 1));
            for (int j = 0; j < crossoverIndex; j++)
            {
                char temp = offspring1.charAt(j);
                offspring1.setCharAt(j, offspring2.charAt(j));
                offspring2.setCharAt(j, temp);
            }
        }*/
        
        List<SunburnGenome> result = new ArrayList<SunburnGenome>(2);
        result.add(SunburnGenome.parseFromString(offspring1.toString()));
        result.add(SunburnGenome.parseFromString(offspring1.toString()));
        
        return result;
    }
}
