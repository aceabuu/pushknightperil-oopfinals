package application;

import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.geometry.Pos;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.util.HashMap;
import java.util.Map;

/**
 * Game panel for rendering the grid-based game with UI elements.
 */
public class GamePanel {
    private static final int CELL_SIZE = 40;
    private static final int GRID_WIDTH = GameLogic.GRID_WIDTH;
    private static final int GRID_HEIGHT = GameLogic.GRID_HEIGHT;
    
    private GridPane gridView;
    private BorderPane mainLayout;
    private VBox topBar;
    private HBox bottomBar;
    
    private GameLogic logic;
    private Rectangle[][] gridCells;
    private Map<String, VisualEntity> entities;
    
    // UI elements
    private Text waveText;
    private Text enemiesText;
    private HBox healthBar;
    private Text cooldownText;
    
    /**
     * Creates game panel.
     */
    public GamePanel(GameLogic logic) {
        this.logic = logic;
        this.entities = new HashMap<>();
        
        initializeUI();
        initializeGrid();
        updateUI();
    }
    
    /**
     * Initializes UI components.
     */
    private void initializeUI() {
        mainLayout = new BorderPane();
        mainLayout.setStyle("-fx-background-color: #1a1a2e;");
        
        // Top bar - Wave info and health
        topBar = new VBox(10);
        topBar.setAlignment(Pos.CENTER);
        topBar.setStyle("-fx-background-color: #16213e; -fx-padding: 10;");
        
        HBox topInfo = new HBox(30);
        topInfo.setAlignment(Pos.CENTER);
        
        // Wave information
        VBox waveInfo = new VBox(5);
        waveInfo.setAlignment(Pos.CENTER);
        waveText = new Text("Wave: 1");
        waveText.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        waveText.setFill(Color.WHITE);
        enemiesText = new Text("Enemies: 0/5");
        enemiesText.setFont(Font.font("Arial", 14));
        enemiesText.setFill(Color.LIGHTGRAY);
        waveInfo.getChildren().addAll(waveText, enemiesText);
        
        // Health bar
        healthBar = new HBox(5);
        healthBar.setAlignment(Pos.CENTER);
        updateHealthBar();
        
        topInfo.getChildren().addAll(waveInfo, healthBar);
        topBar.getChildren().add(topInfo);
        
        // Bottom bar - Push cooldown
        bottomBar = new HBox();
        bottomBar.setAlignment(Pos.CENTER);
        bottomBar.setStyle("-fx-background-color: #16213e; -fx-padding: 10;");
        
        cooldownText = new Text("Push Ready [SPACE]");
        cooldownText.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        cooldownText.setFill(Color.CYAN);
        bottomBar.getChildren().add(cooldownText);
        
        mainLayout.setTop(topBar);
        mainLayout.setBottom(bottomBar);
    }
    
    /**
     * Initializes grid cells.
     */
    private void initializeGrid() {
        gridView = new GridPane();
        gridView.setAlignment(Pos.CENTER);
        gridView.setStyle("-fx-background-color: #0f3460;");
        
        gridCells = new Rectangle[GRID_WIDTH][GRID_HEIGHT];
        
        for (int x = 0; x < GRID_WIDTH; x++) {
            for (int y = 0; y < GRID_HEIGHT; y++) {
                Rectangle cell = new Rectangle(CELL_SIZE, CELL_SIZE);
                cell.setStroke(Color.rgb(20, 50, 80));
                cell.setStrokeWidth(0.5);
                
                int entityType = logic.getEntityAt(x, y);
                updateCellColor(cell, entityType);
                
                gridCells[x][y] = cell;
                gridView.add(cell, x, y);
            }
        }
        
        mainLayout.setCenter(gridView);
    }
    
    /**
     * Updates cell color based on entity type.
     */
    private void updateCellColor(Rectangle cell, int entityType) {
        switch (entityType) {
            case 0: // Empty
                cell.setFill(Color.rgb(15, 52, 96));
                break;
            case 1: // Wall
                cell.setFill(Color.rgb(60, 60, 60));
                break;
            case 2: // Enemy
                cell.setFill(Color.rgb(200, 50, 50));
                break;
            case 3: // Spikes
                cell.setFill(Color.rgb(100, 100, 100));
                break;
            case 4: // Campfire
                cell.setFill(Color.rgb(255, 150, 50));
                break;
            default:
                cell.setFill(Color.rgb(15, 52, 96));
        }
    }
    
    /**
     * Updates health bar display.
     */
    private void updateHealthBar() {
        healthBar.getChildren().clear();
        
        Text label = new Text("Health: ");
        label.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        label.setFill(Color.WHITE);
        healthBar.getChildren().add(label);
        
        int health = logic.getPlayer().getHealth();
        int maxHealth = logic.getPlayer().getMaxHealth();
        
        for (int i = 0; i < maxHealth; i++) {
            Rectangle heart = new Rectangle(25, 25);
            heart.setArcWidth(5);
            heart.setArcHeight(5);
            
            if (i < health) {
                heart.setFill(Color.RED);
                heart.setStroke(Color.DARKRED);
            } else {
                heart.setFill(Color.rgb(50, 50, 50));
                heart.setStroke(Color.rgb(30, 30, 30));
            }
            heart.setStrokeWidth(2);
            
            healthBar.getChildren().add(heart);
        }
    }
    
    /**
     * Updates UI elements.
     */
    private void updateUI() {
        // Update wave info
        SpawnSystem spawn = logic.getSpawnSystem();
        waveText.setText("Wave: " + spawn.getCurrentWave());
        
        int aliveEnemies = 0;
        for (Enemy enemy : logic.getEnemies()) {
            if (!enemy.isDead()) aliveEnemies++;
        }
        
        enemiesText.setText("Enemies: " + spawn.getEnemiesSpawned() + "/" + 
                           spawn.getEnemiesPerWave() + " (Alive: " + aliveEnemies + ")");
        
        // Update health bar
        updateHealthBar();
        
        // Update push cooldown
        Player player = logic.getPlayer();
        if (player.canPush()) {
            cooldownText.setText("Push Ready [SPACE]");
            cooldownText.setFill(Color.CYAN);
        } else {
            double cooldown = player.getPushCooldown();
            cooldownText.setText(String.format("Push Cooldown: %.1fs", cooldown));
            cooldownText.setFill(Color.GRAY);
        }
    }
    
    /**
     * Handles game update event.
     */
    public void handleEvent(GameLogic.GameUpdateEvent event) {
        switch (event.type) {
            case PLAYER_MOVE:
                handlePlayerMove(event);
                break;
            case ENEMY_MOVE:
                handleEnemyMove(event);
                break;
            case ENEMY_SPAWN:
                handleEnemySpawn(event);
                break;
            case DAMAGE:
                handleDamage(event);
                break;
            case REMOVE_ENTITY:
                handleRemoveEntity(event);
                break;
            case IMPACT:
                handleImpact(event);
                break;
            case PLAYER_DAMAGE:
                handlePlayerDamage(event);
                break;
        }
        
        updateUI();
    }
    
    private void handlePlayerMove(GameLogic.GameUpdateEvent event) {
        // Clear old position
        updateCellColor(gridCells[event.oldX][event.oldY], 0);
        
        // Draw player at new position
        gridCells[event.newX][event.newY].setFill(Color.BLUE);
    }
    
    private void handleEnemyMove(GameLogic.GameUpdateEvent event) {
        // Clear old position
        int oldType = logic.getEntityAt(event.oldX, event.oldY);
        updateCellColor(gridCells[event.oldX][event.oldY], oldType);
        
        // Draw enemy at new position
        Enemy enemy = logic.findEnemyAt(event.newX, event.newY);
        if (enemy != null) {
            Color enemyColor = getEnemyColor(enemy.getType());
            gridCells[event.newX][event.newY].setFill(enemyColor);
        }
    }
    
    private void handleEnemySpawn(GameLogic.GameUpdateEvent event) {
        Enemy enemy = logic.findEnemyAt(event.newX, event.newY);
        if (enemy != null) {
            Color enemyColor = getEnemyColor(enemy.getType());
            gridCells[event.newX][event.newY].setFill(enemyColor);
        }
    }
    
    private void handleDamage(GameLogic.GameUpdateEvent event) {
        // Flash effect
        Rectangle cell = gridCells[event.newX][event.newY];
        Color originalColor = (Color) cell.getFill();
        cell.setFill(Color.WHITE);
        
        new java.util.Timer().schedule(new java.util.TimerTask() {
            @Override
            public void run() {
                javafx.application.Platform.runLater(() -> {
                    Enemy enemy = logic.findEnemyAt(event.newX, event.newY);
                    if (enemy != null && !enemy.isDead()) {
                        cell.setFill(getEnemyColor(enemy.getType()));
                    } else {
                        updateCellColor(cell, logic.getEntityAt(event.newX, event.newY));
                    }
                });
            }
        }, 100);
    }
    
    private void handleRemoveEntity(GameLogic.GameUpdateEvent event) {
        int type = logic.getEntityAt(event.newX, event.newY);
        updateCellColor(gridCells[event.newX][event.newY], type);
    }
    
    private void handleImpact(GameLogic.GameUpdateEvent event) {
        // Impact flash
        Rectangle cell = gridCells[event.newX][event.newY];
        cell.setFill(Color.YELLOW);
        
        new java.util.Timer().schedule(new java.util.TimerTask() {
            @Override
            public void run() {
                javafx.application.Platform.runLater(() -> {
                    updateCellColor(cell, logic.getEntityAt(event.newX, event.newY));
                });
            }
        }, 150);
    }
    
    private void handlePlayerDamage(GameLogic.GameUpdateEvent event) {
        updateHealthBar();
        
        // Flash player
        Rectangle cell = gridCells[event.newX][event.newY];
        cell.setFill(Color.WHITE);
        
        new java.util.Timer().schedule(new java.util.TimerTask() {
            @Override
            public void run() {
                javafx.application.Platform.runLater(() -> {
                    cell.setFill(Color.BLUE);
                });
            }
        }, 200);
    }
    
    /**
     * Gets color for enemy type.
     */
    private Color getEnemyColor(Enemy.EnemyType type) {
        switch (type) {
            case GOBLIN:
                return Color.rgb(100, 200, 100); // Green
            case SKELETON:
                return Color.rgb(220, 220, 220); // White/Gray
            case BRUTE:
                return Color.rgb(150, 100, 200); // Purple
            case BOOMER:
                return Color.rgb(255, 100, 100); // Bright Red
            default:
                return Color.rgb(200, 50, 50);
        }
    }
    
    public Pane getGridView() {
        return mainLayout;
    }
    
    /**
     * Inner class for visual entity representation.
     */
    private static class VisualEntity {
        Rectangle rectangle;
        Text healthText;
        
        VisualEntity(Rectangle rect) {
            this.rectangle = rect;
        }
    }
}