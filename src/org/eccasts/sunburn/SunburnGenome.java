package org.eccasts.sunburn;

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
        return "<" + slots + ", " + preferredRange + ">";
    }
    
    public String toCrossoverForm() {
        return slots + String.format("%02d", preferredRange);
    }
    
    public static SunburnGenome parseFromString(String crossOverString) {
        return new SunburnGenome(crossOverString.substring(0, crossOverString.length()-2), 
                new Integer(crossOverString.substring(crossOverString.length()-2)));
    }
}
