package application;

/**
 * Skeleton Brute - Tanky, slow-moving enemy that blocks paths.
 * Stats: 6 HP, 2 Damage
 * Behavior: Slow movement, acts as obstacle (isObstacle = true)
 */
public class SkeletonBrute extends Enemy {
    private boolean isObstacle;
    
    public SkeletonBrute(int x, int y) {
        super(x, y, EnemyType.BRUTE);
        this.maxHp = 6;
        this.hp = maxHp;
        this.damage = 2;
        this.movementCooldownMax = 1.2; // Very slow movement (1.2 seconds)
        this.movementCooldown = 0;
        this.isObstacle = true; // Blocks other enemies' paths
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
        
        // Use pathfinding but move slowly
        int[] move = pathfinder.getNextMove(x, y, playerX, playerY, grid);
        
        if (move != null) {
            System.out.println("[AI] Skeleton Brute at (" + x + "," + y + ") moving slowly towards player");
        }
        
        return move;
    }
    
    public boolean isObstacle() {
        return isObstacle;
    }
}