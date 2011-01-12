package org.eccasts.sunburn;

import java.util.Random;

import org.uncommons.watchmaker.framework.factories.AbstractCandidateFactory;
import org.uncommons.watchmaker.framework.factories.StringFactory;

public class SunburnFactory extends AbstractCandidateFactory<SunburnGenome> {
    @Override
    public SunburnGenome generateRandomCandidate(Random rng) {
        StringFactory sf = new StringFactory(SunburnShip.ALPHABET, SunburnShip.GENOME_LENGTH);
        String slots = sf.generateRandomCandidate(rng);
        int preferredRange = rng.nextInt(SunburnShip.MAX_PREFERRED_RANGE)+1;
        
        return new SunburnGenome(slots, preferredRange);
    }

}
