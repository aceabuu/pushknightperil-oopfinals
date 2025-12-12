package application;

/**
 * Abstract base class for all enemy types.
 * Defines common properties and behavior.
 */
public abstract class Enemy {
    // Position
    protected int x, y;
    
    // Stats
    protected int hp;                    // Current health
    protected int maxHp;                 // Maximum health
    protected int damage;                // Damage dealt to player
    protected boolean dead;              // Dead status
    
    // AI timing
    protected double movementCooldown;   // Time until next move
    protected double movementCooldownMax; // Max movement cooldown
    
    // Type identifier
    protected EnemyType type;
    
    /**
     * Enemy type enum.
     */
    public enum EnemyType {
        GOBLIN,
        SKELETON,
        BRUTE,
        BOOMER
    }
    
    /**
     * Creates an enemy at specified position with type.
     */
    public Enemy(int x, int y, EnemyType type) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.dead = false;
    }
    
    /**
     * Updates enemy AI and behavior.
     * @return Movement direction [dx, dy] or null if no move
     */
    public abstract int[] updateAI(double deltaTime, int playerX, int playerY, 
                                   Pathfinder pathfinder, int[][] grid);
    
    /**
     * Checks if enemy can attack player at target position.
     */
    public boolean canAttackPlayer(int playerX, int playerY) {
        int dx = Math.abs(x - playerX);
        int dy = Math.abs(y - playerY);
        
        // Adjacent (including diagonals)
        return dx <= 1 && dy <= 1 && !(dx == 0 && dy == 0);
    }
    
    /**
     * Takes damage.
     */
    public void takeDamage(int damage) {
        if (dead) return;
        
        hp -= damage;
        if (hp <= 0) {
            hp = 0;
            dead = true;
            System.out.println("[COMBAT] " + type + " defeated at (" + x + "," + y + ")");
        }
    }
    
    /**
     * Moves enemy to new position.
     */
    public void moveTo(int newX, int newY) {
        this.x = newX;
        this.y = newY;
    }
    
    // Getters
    public int getX() { return x; }
    public int getY() { return y; }
    public int getHp() { return hp; }
    public int getMaxHp() { return maxHp; }
    public int getDamage() { return damage; }
    public boolean isDead() { return dead; }
    public EnemyType getType() { return type; }
    public double getMovementCooldown() { return movementCooldown; }
}