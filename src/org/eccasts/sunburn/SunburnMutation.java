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

import org.uncommons.maths.number.ConstantGenerator;
import org.uncommons.maths.number.NumberGenerator;
import org.uncommons.maths.random.Probability;
import org.uncommons.watchmaker.framework.EvolutionaryOperator;

public class SunburnMutation implements EvolutionaryOperator<SunburnGenome> {
    private final char[] alphabet;
    private final NumberGenerator<Probability> mutationProbability;

    
    public SunburnMutation(char[] alphabet, Probability mutationProbability)
    {
        this(alphabet, new ConstantGenerator<Probability>(mutationProbability));
    }

    public SunburnMutation(char[] alphabet,
                          NumberGenerator<Probability> mutationProbability)
    {
        this.alphabet = alphabet.clone();
        this.mutationProbability = mutationProbability;
    }
    @Override
    public List<SunburnGenome> apply(
            List<SunburnGenome> selectedCandidates, Random rng) {
        List<SunburnGenome> mutatedPopulation = new ArrayList<SunburnGenome>(selectedCandidates.size());
        
        for (SunburnGenome s : selectedCandidates)
        {
            mutatedPopulation.add(mutateIndividual(s, rng));
        }
        
        return mutatedPopulation;
    }

    private SunburnGenome mutateIndividual(SunburnGenome s, Random rng) {
        if(rng.nextInt() % 10 == 0) {
            if(rng.nextInt() % 2 != 0) {
                s.setPreferredRange(Math.max(s.getPreferredRange()+1, SunburnShip.MAX_PREFERRED_RANGE));
            } else {
                s.setPreferredRange(Math.max(s.getPreferredRange()+1, SunburnShip.MIN_PREFERRED_RANGE));
            }
        } else {
            StringBuilder buffer = new StringBuilder(s.getSlots());
            for (int i = 0; i < buffer.length(); i++)
            {
                if (mutationProbability.nextValue().nextEvent(rng))
                {
                    buffer.setCharAt(i, alphabet[rng.nextInt(alphabet.length)]);
                }
            }
            
            s.setSlots(buffer.toString());
        }
        
        return null;
    }

}
