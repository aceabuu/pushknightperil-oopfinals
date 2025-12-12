package application;

/**
 * Goblin - Basic fast-moving enemy.
 * Stats: 2 HP, 1 Damage
 * Behavior: Fast movement, direct pathfinding to player
 */
public class Goblin extends Enemy {
    
    public Goblin(int x, int y) {
        super(x, y, EnemyType.GOBLIN);
        this.maxHp = 2;
        this.hp = maxHp;
        this.damage = 1;
        this.movementCooldownMax = 0.4; // Fast movement (0.4 seconds)
        this.movementCooldown = 0;
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
        
        // Use pathfinding to chase player
        int[] move = pathfinder.getNextMove(x, y, playerX, playerY, grid);
        
        if (move != null) {
            System.out.println("[AI] Goblin at (" + x + "," + y + ") moving towards player");
        }
        
        return move;
    }
}