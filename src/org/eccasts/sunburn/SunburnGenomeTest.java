package org.eccasts.sunburn;

import org.junit.Test;
import static org.junit.Assert.*;


public class SunburnGenomeTest {
    @Test public void ParseIndividual() {
        SunburnGenome s = SunburnGenome.parseFromString("SLMMSGDMSMLLSLSLMSGM08");
        assertEquals(8, s.getPreferredRange());
        assertEquals("SLMMSGDMSMLLSLSLMSGM", s.getSlots());
    }
}
