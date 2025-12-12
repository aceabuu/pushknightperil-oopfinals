package application;

/**
 * Manages player upgrades and progression.
 */
public class UpgradeManager {
    // Upgrade levels
    private int healthUpgradeLevel;
    private int pushRangeLevel;
    private int pushCooldownLevel;
    
    // Costs
    private static final int HEALTH_UPGRADE_COST = 100;
    private static final int PUSH_RANGE_COST = 150;
    private static final int PUSH_COOLDOWN_COST = 200;
    
    public UpgradeManager() {
        this.healthUpgradeLevel = 0;
        this.pushRangeLevel = 0;
        this.pushCooldownLevel = 0;
    }
    
    /**
     * Upgrades max health.
     */
    public boolean upgradeHealth(Player player, int currency) {
        if (currency >= HEALTH_UPGRADE_COST && healthUpgradeLevel < 3) {
            healthUpgradeLevel++;
            // Apply upgrade logic (would need to modify Player class)
            System.out.println("[UPGRADE] Health upgraded to level " + healthUpgradeLevel);
            return true;
        }
        return false;
    }
    
    /**
     * Upgrades push range.
     */
    public boolean upgradePushRange(int currency) {
        if (currency >= PUSH_RANGE_COST && pushRangeLevel < 3) {
            pushRangeLevel++;
            System.out.println("[UPGRADE] Push range upgraded to level " + pushRangeLevel);
            return true;
        }
        return false;
    }
    
    /**
     * Upgrades push cooldown reduction.
     */
    public boolean upgradePushCooldown(int currency) {
        if (currency >= PUSH_COOLDOWN_COST && pushCooldownLevel < 3) {
            pushCooldownLevel++;
            System.out.println("[UPGRADE] Push cooldown upgraded to level " + pushCooldownLevel);
            return true;
        }
        return false;
    }
    
    // Getters
    public int getHealthUpgradeLevel() { return healthUpgradeLevel; }
    public int getPushRangeLevel() { return pushRangeLevel; }
    public int getPushCooldownLevel() { return pushCooldownLevel; }
}