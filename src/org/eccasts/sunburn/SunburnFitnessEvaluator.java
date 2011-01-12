package org.eccasts.sunburn;

import java.util.List;

import org.uncommons.watchmaker.framework.FitnessEvaluator;

public class SunburnFitnessEvaluator implements FitnessEvaluator<SunburnGenome> {

    @Override
    public double getFitness(SunburnGenome candidate,
            List<? extends SunburnGenome> population) {
        return 0;
    }

    @Override
    public boolean isNatural() {
        return true;
    }

}
