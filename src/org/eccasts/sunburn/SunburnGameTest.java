package org.eccasts.sunburn;

import static org.junit.Assert.*;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Before;
import org.junit.Test;


public class SunburnGameTest {
    private SunburnShip ship;

    @Before public void init() {
        ship = new SunburnShip(new Random());
        ship.setGenome("GLMDSSMGLSDSMLGMLSDS");
    }
    
    @Test public void GenerateRandomShip() {
       int dCount = 0;
       
       for(int i = 1; i <= 50000; i++) {
           String genome = ship.newRandomGenome();
           
           assertEquals(20, genome.length());
           for(int j = 0; j < 20; j++)
               if(genome.charAt(j) == 'D')
                   dCount++;
           
       }

       assertTrue(dCount >= 195000 && dCount <= 205000);
    }
    
    @Test public void PreferredRange() {
       assertEquals(20, ship.getPreferredRange());
       
       boolean threwException = false;
       
       try {
           ship.setPreferredRange(31);
       } catch(IllegalArgumentException e) {
           threwException = true;
       }
       
       assertTrue(threwException);
       
       threwException = false;
       
       try {
           ship.setPreferredRange(0);
       } catch(IllegalArgumentException e) {
           threwException = true;
       }
       
       assertTrue(threwException);
    }
    
    @Test public void DevelopShipGenome() {
        assertEquals("SSSSSSSSSSSSSSSSSSGLMDMGLDMLGMLD", ship.developGenome());
    }
    
    @Test public void CountSystems() {
        assertEquals(3, ship.gunCount());
        assertEquals(4, ship.laserCount());
        assertEquals(4, ship.missileCount());
        assertEquals(18, ship.shieldCount());
        assertEquals(3, ship.driveCount());
    }
    
    @Test public void weaponProbabilities() {
        assertEquals(0.6, ship.hitProbabilityGun(1), 0.01);
        assertEquals(-0.4, ship.hitProbabilityGun(39), 0.01);
        
        for(int i = 1; i <= 30; i++)
            assertEquals(0.35, ship.hitProbabilityLaser(i), 0.01);
        
        assertEquals(0.1, ship.hitProbabilityMissile(1), 0.01);
        assertEquals(-0.9, ship.hitProbabilityMissile(-37), 0.01);
    }
    
    @Test public void FireWeapons() {
        ship.setRng(new FixedRandom(0.7));
        assertEquals(0, ship.fireGuns(1));
        
        ship.setRng(new FixedRandom(0.5));
        
        assertEquals(3, ship.fireGuns(1));
        
        ship.setRng(new Random());
        
        int hitCount = 0;
        
        for(int i = 1; i <= 10000; i++) {
            hitCount += ship.fireGuns(1);
        }
        
        assertTrue(hitCount >= 17000 && hitCount <= 19000);
        
        hitCount = 0;
        
        for(int i = 1; i <= 10000; i++) {
            hitCount += ship.fireMissiles(1);
        }
        
        assertTrue(hitCount >= 3000 && hitCount <= 5000);
        
        hitCount = 0;
        
        for(int i = 1; i <= 10000; i++) {
            hitCount += ship.fireLasers(1);
        }
        
        assertTrue(hitCount >= 13000 && hitCount <= 15000);
        
        hitCount = 0;
        
        for(int i = 1; i <= 10000; i++) {
            hitCount += ship.fireWeapons(1);
        }
        
        assertTrue(hitCount >= 33000 && hitCount <= 39000);
    }
    
    @Test public void TakeHits() {
        assertEquals("SSSSSSSSSSSSSGLMDMGLDMLGMLD", ship.takeHits(5));
        assertEquals("SSSSSSSSSSSSGLMDMGLDMLGMLD", ship.takeHits(1));
        
        assertEquals("", ship.takeHits("SSSSSSSSSSSSGLMDMGLDMLGMLD".length()+1));
    }
    
    @Test public void AnyDrivesLeft() {
        assertTrue(ship.drivesLeft());
        
        ship.setGenome("GLMDSSMGLSMSMLGMLSMS");
        ship.takeHits(6*3+4);
        
        assertFalse(ship.drivesLeft());
    }
    
    @Test public void FiringRemovesDrives() {
        // SSSSSSSSSSSSSSSSSSGLMDMGLDMLGMLD
        ship.takeHits("SSSSSSSSSSSSSSSSSS".length());
        assertEquals(3, ship.gunCount());
        ship.takeHits(1);
        assertEquals(2, ship.gunCount());
    }
    
    @Test public void MoveShip() {
        ship.setPreferredRange(25);
        assertEquals(20, ship.getRange());
        
        ship.move();
        
        assertEquals(21, ship.getRange());
        
        ship.move();
        
        assertEquals(22, ship.getRange());
        
        ship.setPreferredRange(22);
        
        ship.move();
        
        assertEquals(22, ship.getRange());
        
        ship.setPreferredRange(21);
        
        ship.move();
        
        assertEquals(21, ship.getRange());
    }
    
    @Test public void UseDrivesToMove() {
        // SSSSSSSSSSSSSSSSSSGLMDMGLDMLGMLD
        ship.setPreferredRange(24);
        ship.moveWithDrives();
        assertEquals(23, ship.getRange());
        
        ship.moveWithDrives();
        assertEquals(24, ship.getRange());
        
    }
    
    @Test public void IsDestroyed() {
        assertEquals(false, ship.isDestroyed());
        ship.takeHits("SSSSSSSSSSSSSSSSSSGLMDMGLDMLGMLD".length());
        
        assertEquals(true, ship.isDestroyed());
        
        ship.takeHits(1);
        
        assertEquals(true, ship.isDestroyed());
    }
    
    @Test public void CanWin() {
        // SSSSSSSSSSSSSSSSSSGLMDMGLDMLGMLD
        
        ship.setGenome("GLMDSSMGLSMSMLGMLSMS");
        
        SunburnShip enemy = new SunburnShip(new Random());
        enemy.setGenome("GLMDSSMGLSDSMLGMLSDS");
        enemy.takeHits("SSSSSSSSSSSSSSSSSS".length());
        assertEquals(false, ship.wonAgainst(enemy));
        enemy.takeHits("GLMDMGLDMLGMLD".length());
        assertEquals(true, ship.wonAgainst(enemy));
        
        ship.takeHits(6*3+4);
        
        assertEquals(false, ship.wonAgainst(enemy));
    }
    
    @Test public void CanLose() {
        SunburnShip enemy = new SunburnShip(new Random());
        enemy.setGenome("GLMDSSMGLSDSMLGMLSDS");
        ship.takeHits("SSSSSSSSSSSSSSSSSSGLMDMGLDMLGMLD".length());
        
        assertEquals(true, ship.lostAgainst(enemy));
    }
    
    @Test public void CanDraw() {
        SunburnShip enemy = new SunburnShip(new Random());
        enemy.setGenome("GLMDSSMGLSDSMLGMLSDS");
        ship.takeHits("SSSSSSSSSSSSSSSSSSGLMDMGLDMLGMLD".length());
        enemy.takeHits("SSSSSSSSSSSSSSSSSSGLMDMGLDMLGMLD".length());
        
        assertEquals(true, ship.drewAgainst(enemy));
    }
    
    @Test public void RunCombat() {
        SunburnShip enemy = new SunburnShip(new Random());
        enemy.setGenome("SSSSSSSSSSSSSSSSSSSGD");
        
        Logger logger = Logger.getLogger(SunburnShip.class.getName());
        logger.setLevel(Level.SEVERE);
        
        assertEquals(1, ship.fightAgainst(enemy));
        
        // against two nearly all-shield setups, draw
        ship = new SunburnShip(new Random());
        enemy = new SunburnShip(new Random());
        enemy.setGenome("SSSSSSSSSSSSSSSSSSGD");
        ship.setGenome("SSSSSSSSSSSSSSSSSSGD");
        
        assertEquals(0, ship.fightAgainst(enemy));
    }
    
    class FixedRandom extends Random
    {
        private static final long serialVersionUID = 1L;
        private double value;
        public FixedRandom(double value) { this.value = value; }
        public double nextDouble() { return value; }
    }
}
