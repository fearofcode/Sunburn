package org.eccasts.sunburn;

import java.util.Random;
import java.util.logging.Logger;

public class SunburnShip {
    private static final int SHIELD_MULTIPLICATION_FACTOR = 3;
    public static final int MAX_PREFERRED_RANGE = 30;
    public static final int MIN_PREFERRED_RANGE = 1;
    public static final int INITIAL_PREFERRED_RANGE = 20;
    private Random rng;
    private int preferredRange;
    private String slots;
    private static final char[] ALPHABET = new char[] { 'D', 'S', 'G', 'L', 'M' };
    private static final int GENOME_LENGTH = 20;
    private String currentState;
    private int totalHits;
    private int range;
    
    public SunburnShip(Random rng) {
        this.rng = rng;
        this.preferredRange = INITIAL_PREFERRED_RANGE;
        this.range = INITIAL_PREFERRED_RANGE;
    }

    public String newRandomGenome() {
        StringBuffer sb = new StringBuffer(GENOME_LENGTH);
        
        for(int i = 1; i <= 20; i++)
            sb.append(ALPHABET[rng.nextInt(ALPHABET.length)]);
        
        return sb.toString();
    }

    public int getPreferredRange() {
        return preferredRange;
    }

    public void setPreferredRange(int preferredRange) {
        if(preferredRange >= MIN_PREFERRED_RANGE && preferredRange <= MAX_PREFERRED_RANGE)
            this.preferredRange = preferredRange;
        else
            throw new IllegalArgumentException();
    }

    public void setGenome(String genome) {
        this.slots = genome;
        
        this.currentState = developGenome();
    }

    public String developGenome() {
        StringBuffer developed = new StringBuffer();
        
        int shieldCount = 0;
        
        for(int i = 0; i < slots.length(); i++) {
            if(slots.charAt(i) == 'S')
                shieldCount++;
        }
        
        for(int i = 1; i <= shieldCount*SHIELD_MULTIPLICATION_FACTOR; i++)
            developed.append('S');
        
        for(int i = 0; i < slots.length(); i++) {
            char ch = slots.charAt(i);
            
            if(ch != 'S')
                developed.append(ch);
        }
        
        return developed.toString();
    }

    private int systemCount(char system) {
        int characterCount = 0;
        
        for(int i = 0; i < currentState.length(); i++) {
            char ch = currentState.charAt(i);
            
            if(ch == system)
                characterCount++;
        }
        return characterCount;
    }

    public int driveCount() {
        return systemCount('D');
    }

    public int gunCount() {
        return systemCount('G');
    }

    public int laserCount() {
        return systemCount('L');
    }

    public int missileCount() {
        return systemCount('M');
    }

    public int shieldCount() {
        return systemCount('S');
    }

    public double hitProbabilityGun(int range) {
        return 0.6 - (range-1.0)/38.0;
    }

    public double hitProbabilityLaser(int range) {
        return 0.35;
    }

    public double hitProbabilityMissile(int range) {
        return 0.1 + (range - 1.0)/38.0;
    }

    public void setRng(Random rng) {
        this.rng = rng;
    }

    public int fireGuns(int range) {
        int hitCount = 0;
        
        for(int i = 1; i <= gunCount(); i++) {
            double probability = hitProbabilityGun(range);
            
            if(rng.nextDouble() < probability) {
                hitCount++;
            }
        }
        
        return hitCount;
    }

    public int fireMissiles(int range) {
        int hitCount = 0;
        
        for(int i = 1; i <= missileCount(); i++) {
            double probability = hitProbabilityMissile(range);
            
            if(rng.nextDouble() < probability) {
                hitCount++;
            }
        }
        
        return hitCount;
    }

    public int fireLasers(int range) {
        int hitCount = 0;
        
        for(int i = 1; i <= laserCount(); i++) {
            double probability = hitProbabilityLaser(range);
            
            if(rng.nextDouble() < probability) {
                hitCount++;
            }
        }
        
        return hitCount;
    }

    public int fireWeapons(int range) {
        return fireGuns(range) + fireMissiles(range) + fireLasers(range);
    }

    public String takeHits(int count) {
        totalHits += count;
        
        String developed = developGenome();
        
        totalHits = Math.min(developed.length(), totalHits);
        
        currentState = developed.substring(totalHits);
        
        return currentState;
    }

    public String getCurrentState() {
        return currentState;
    }
    
    public boolean drivesLeft() {
        return currentState.indexOf('D') != -1;
    }

    public int getRange() {
        return range;
    }

    public void move() {
        if(range < preferredRange) {
            range++;
        } else if(range > preferredRange) {
            range--;
        }
    }

    public void moveWithDrives() {
        int movesToMake = Math.min(driveCount(), Math.abs(getRange()-getPreferredRange()));
        
        for(int i = 1; i <= movesToMake; i++)
            move();
    }

    public boolean isDestroyed() {
        return !(gunCount() > 0 || missileCount() > 0 || laserCount() > 0);
    }

    public boolean wonAgainst(SunburnShip enemy) {
        return !isDestroyed() && drivesLeft() && enemy.isDestroyed();
    }

    public boolean lostAgainst(SunburnShip enemy) {
        return enemy.wonAgainst(this);
    }

    public boolean drewAgainst(SunburnShip enemy) {
        return !(drivesLeft() || enemy.drivesLeft()) || 
                (isDestroyed() && enemy.isDestroyed());
    }

    public synchronized int fightAgainst(SunburnShip enemy) {
        int roundCount = 1;
        
        Logger logger = Logger.getLogger(SunburnShip.class.getName());
        
        while(true) {
            if(roundCount > 100) {
                logger.info("100 rounds played, calling it a draw");
                return 0;
            }
             
            logger.info("Round #" + roundCount);
            
            int ownHits = fireWeapons(range);
            int enemyHits = enemy.fireWeapons(enemy.getRange());
            
            logger.info("I got " + ownHits + " hits against his " + enemyHits);
            
            takeHits(enemyHits);
            enemy.takeHits(ownHits);
            
            logger.info("Me: " + getCurrentState());
            logger.info("Him: " + enemy.getCurrentState());
            
            if(wonAgainst(enemy)) {
                logger.info("I win!");
                return 1;
            } else if(lostAgainst(enemy)) {
                logger.info("I lose!");
                return -1;
            } else if(drewAgainst(enemy)) {
                logger.info("Stalemate.");
                return 0;
            }
            
            moveWithDrives();
            enemy.moveWithDrives();
            
            logger.info("My current range: " + getRange());
            logger.info("His current range: " + enemy.getRange());
            
            roundCount++;
        }
    }
}
