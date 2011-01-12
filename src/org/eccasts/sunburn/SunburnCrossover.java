// Uses code from the Watchmaker framework:

//=============================================================================
// Copyright 2006-2010 Daniel W. Dyer
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//=============================================================================
package org.eccasts.sunburn;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.uncommons.maths.random.Probability;
import org.uncommons.watchmaker.framework.operators.AbstractCrossover;

public class SunburnCrossover extends AbstractCrossover<SunburnGenome> {

    protected SunburnCrossover(int crossoverPoints,
            Probability crossoverProbability) {
        super(crossoverPoints, crossoverProbability);
    }

    @Override
    protected List<SunburnGenome> mate(SunburnGenome parent1,
            SunburnGenome parent2, int numberOfCrossoverPoints, Random rng) {
        if (parent1.getSlots().length() != parent2.getSlots().length())
        {
            throw new IllegalArgumentException("Cannot perform cross-over with different length parents.");
        }
        
        String parent1WithRange = parent1.toCrossoverForm();
        String parent2WithRange = parent2.toCrossoverForm();
        
        StringBuilder offspring1 = new StringBuilder(parent1WithRange);
        StringBuilder offspring2 = new StringBuilder(parent2WithRange);
        
        // Apply as many cross-overs as required.
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
        }
        
        List<SunburnGenome> result = new ArrayList<SunburnGenome>(2);
        result.add(SunburnGenome.parseFromString(offspring1.toString()));
        result.add(SunburnGenome.parseFromString(offspring1.toString()));
        
        return result;
    }

}
