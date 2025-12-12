package application;

import java.util.List;

/**
 * Player entity with health system and push mechanics.
 */
public class Player {
    private int x, y;                    // Grid position
    private int health;                  // Current health
    private int maxHealth;               // Maximum health
    private boolean alive;               // Alive status
    private double pushCooldown;         // Push cooldown timer
    private double pushCooldownMax;      // Max push cooldown (0.5 seconds)
    
    public Player(int startX, int startY) {
        this.x = startX;
        this.y = startY;
        this.maxHealth = 3;
        this.health = maxHealth;
        this.alive = true;
        this.pushCooldown = 0;
        this.pushCooldownMax = 0.5;
    }
    
    /**
     * Updates player timers.
     */
    public void update(double deltaTime) {
        if (pushCooldown > 0) {
            pushCooldown -= deltaTime;
            if (pushCooldown < 0) pushCooldown = 0;
        }
    }
    
    /**
     * Moves player to new position.
     */
    public void moveTo(int newX, int newY) {
        this.x = newX;
        this.y = newY;
    }
    
    /**
     * Player takes damage.
     */
    public void takeDamage(int damage) {
        if (!alive) return;
        
        health -= damage;
        if (health <= 0) {
            health = 0;
            alive = false;
            System.out.println("[PLAYER] Player has died!");
        }
    }
    
    /**
     * Heals player.
     */
    public void heal(int amount) {
        if (!alive) return;
        health = Math.min(health + amount, maxHealth);
    }
    
    /**
     * Checks if player can use push ability.
     */
    public boolean canPush() {
        return pushCooldown <= 0;
    }
    
    /**
     * Activates push cooldown.
     */
    public void activatePushCooldown() {
        pushCooldown = pushCooldownMax;
    }
    
    // Getters
    public int getX() { return x; }
    public int getY() { return y; }
    public int getHealth() { return health; }
    public int getMaxHealth() { return maxHealth; }
    public boolean isAlive() { return alive; }
    public double getPushCooldown() { return pushCooldown; }
    public double getPushCooldownMax() { return pushCooldownMax; }
}