package application;

import java.util.List;

/**
 * Boomer Goblin - Suicide bomber enemy.
 * Stats: 1 HP, 0 Direct Damage
 * Behavior: Rushes player, explodes on death dealing 2 damage to adjacent entities
 */
public class BoomerGoblin extends Enemy {
    private boolean hasExploded;
    
    public BoomerGoblin(int x, int y) {
        super(x, y, EnemyType.BOOMER);
        this.maxHp = 1;
        this.hp = maxHp;
        this.damage = 0; // No direct contact damage
        this.movementCooldownMax = 0.3; // Very fast movement (0.3 seconds)
        this.movementCooldown = 0;
        this.hasExploded = false;
    }
    
    @Override
    public int[] updateAI(double deltaTime, int playerX, int playerY, 
                         Pathfinder pathfinder, int[][] grid) {
        if (dead) return null;
        
        // Update cooldown
        movementCooldown -= deltaTime;
        if (movementCooldown > 0) {
            return null; // Still on cooldown
        }
        
        // Reset cooldown
        movementCooldown = movementCooldownMax;
        
        // Use pathfinding to rush player aggressively
        int[] move = pathfinder.getNextMove(x, y, playerX, playerY, grid);
        
        if (move != null) {
            System.out.println("[AI] Boomer Goblin at (" + x + "," + y + ") rushing towards player");
        }
        
        return move;
    }
    
    @Override
    public void takeDamage(int damage) {
        super.takeDamage(damage);
        
        // Explode on death
        if (dead && !hasExploded) {
            explode();
        }
    }
    
    /**
     * Triggers explosion effect.
     * Should be handled by GameLogic to damage adjacent entities.
     */
    private void explode() {
        hasExploded = true;
        System.out.println("[EXPLOSION] Boomer Goblin exploded at (" + x + "," + y + ")!");
    }
    
    /**
     * Gets explosion damage (2 damage to all adjacent tiles).
     */
    public int getExplosionDamage() {
        return 2;
    }
    
    /**
     * Gets explosion range (1 tile = adjacent only).
     */
    public int getExplosionRange() {
        return 1;
    }
    
    public boolean hasExploded() {
        return hasExploded;
    }
}