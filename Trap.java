package application;

/**
 * Trap entity - placed by player, damages enemies.
 * Can be single-use or persistent.
 */
public class Trap {
    private int x, y;                    // Grid position
    private int damage;                  // Damage dealt
    private boolean active;              // Is trap active
    private boolean persistent;          // Does trap stay after triggering
    private double cooldown;             // Cooldown before re-arming
    private double cooldownMax;          // Max cooldown time
    
    /**
     * Creates a trap at specified position.
     */
    public Trap(int x, int y, int damage, boolean persistent) {
        this.x = x;
        this.y = y;
        this.damage = damage;
        this.persistent = persistent;
        this.active = true;
        this.cooldownMax = 1.0;
        this.cooldown = 0;
    }
    
    /**
     * Updates trap state.
     */
    public void update(double deltaTime) {
        if (!active && persistent) {
            cooldown -= deltaTime;
            if (cooldown <= 0) {
                active = true; // Re-arm persistent trap
            }
        }
    }
    
    /**
     * Triggers the trap.
     */
    public void trigger() {
        if (!active) return;
        
        active = false;
        
        if (persistent) {
            cooldown = cooldownMax; // Start cooldown for re-arming
        }
        
        System.out.println("[TRAP] Trap triggered at (" + x + "," + y + ")");
    }
    
    // Getters
    public int getX() { return x; }
    public int getY() { return y; }
    public int getDamage() { return damage; }
    public boolean isActive() { return active; }
    public boolean isPersistent() { return persistent; }
}