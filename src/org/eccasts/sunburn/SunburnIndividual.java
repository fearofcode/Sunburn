package org.eccasts.sunburn;

public class SunburnIndividual {
    private String slots;
    private int preferredRange;
    
    public SunburnIndividual(String slots, int preferredRange) {
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
}
