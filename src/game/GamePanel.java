package game;

import main.GameMain;
import game.enemy.*;
import game.boss.*;
import game.objects.*;
import model.User;
import audio.SoundManager;
import utils.ImageLoader;
import database.DatabaseManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

public class GamePanel extends JPanel implements ActionListener, KeyListener {

    private GameMain gameMain;
    private User user;
    private SoundManager soundManager;
    private DatabaseManager dbManager;

    private javax.swing.Timer timer;
    private boolean running;
    private boolean paused;
    private int level;
    private int score;
    private boolean gameOver;
    private boolean victory;
    private int frameCount = 0;

    private Plane plane;
    private List<Enemy> enemies;
    private List<Bullet> bullets;
    private List<Egg> eggs;
    private List<PowerUp> powerUps;
    private List<Explosion> explosions;
    private Boss currentBoss;
    private boolean bossFight;

    private Cell[][] grid;
    private int gridRows = 5;
    private int gridCols = 8;
    private double gridX;
    private double gridY;
    private double gridSpeed;
    private double gridDirection;
    private int gridDropStep;
    private double eggDropTimer;
    private double eggDropInterval;

    private BufferedImage background1;
    private BufferedImage background2;
    private int bgOffset;

    private static final int PANEL_WIDTH = 800;
    private static final int PANEL_HEIGHT = 600;
    private static final int GRID_START_X = 50;
    private static final int GRID_START_Y = 40;
    private static final int CELL_WIDTH = 60;
    private static final int CELL_HEIGHT = 55;

    // Constructor

    public GamePanel(GameMain gameMain, User user) {

        this.gameMain = gameMain;
        this.user = user;
        this.soundManager = gameMain.getSoundManager();
        this.dbManager = gameMain.getDatabaseManager();

        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setBackground(new Color(10, 10, 30));
        setFocusable(true);
        addKeyListener(this);

        background1 = ImageLoader.loadImage("background/background.jpg");
        background2 = ImageLoader.loadImage("background/background2.jpg");
        bgOffset = 0;

        initializeGame();

        timer = new javax.swing.Timer(16, this);
        timer.start();
        soundManager.playInGameMusic();
    }

    // Initialize game parameters

    private void initializeGame() {
        running = true;
        paused = false;
        gameOver = false;
        victory = false;
        level = 1;
        score = 0;
        bossFight = false;
        frameCount = 0;

        plane = new Plane(user.getSelectedPlane());
        enemies = new ArrayList<>();
        bullets = new ArrayList<>();
        eggs = new ArrayList<>();
        powerUps = new ArrayList<>();
        explosions = new ArrayList<>();

        gridX = GRID_START_X;
        gridY = GRID_START_Y;
        gridDirection = 1;

        initializeLevel();
    }

    // Load the level

    private void initializeLevel() {
        if (level <= 3) initializeGridLevel();
        else if (level == 4) initializeBoss(4);
        else if (level <= 7) initializeGridLevel();
        else if (level == 8) initializeBoss(8);
    }

    // Load the grid and its parameters

    private void initializeGridLevel() {
        bossFight = false;
        currentBoss = null;
        enemies.clear();

        gridX = GRID_START_X;
        gridY = GRID_START_Y;
        gridDirection = 1;

        double[][] levelParams = {
                {1, 3.0, 20, 2},
                {1.5, 2.0, 20, 2},
                {2.0, 1.5, 25, 3},
                {0, 0, 0, 0},
                {2.5, 1.0, 25, 3},
                {3.0, 0.8, 30, 4},
                {3.5, 0.7, 30, 4}
        };

        int idx = level <= 3 ? level - 1 : level - 1;
        if (idx >= levelParams.length) idx = levelParams.length - 1;

        gridSpeed = levelParams[idx][0];
        eggDropInterval = levelParams[idx][1];
        gridDropStep = (int)levelParams[idx][2];
        int initialCounter = (int)levelParams[idx][3];

        System.out.println("========================================");
        System.out.println("=== LEVEL " + level + " STARTED ===");
        System.out.println("Initial counter per cell: " + initialCounter);
        System.out.println("Grid drop step: " + gridDropStep);
        System.out.println("Grid position: (" + gridX + ", " + gridY + ")");
        System.out.println("========================================");

        grid = new Cell[gridRows][gridCols];
        for (int row = 0; row < gridRows; row++) {
            for (int col = 0; col < gridCols; col++) {
                int x = (int)(GRID_START_X + col * CELL_WIDTH);
                int y = (int)(GRID_START_Y + row * CELL_HEIGHT);
                String type = getEnemyTypeForLevel(level, row, col);
                Cell cell = new Cell(x, y, type, initialCounter);
                grid[row][col] = cell;
                enemies.add(createEnemy(type, x, y, cell));
            }
        }

        System.out.println("Total enemies spawned: " + enemies.size());
        System.out.println("========================================");
    }

    // Generates the enemy based on the level

    private String getEnemyTypeForLevel(int level, int row, int col) {
        Random rand = new Random();
        if (level == 1) return "NORMAL";
        if (level == 2) return rand.nextBoolean() ? "NORMAL" : "FAST";
        if (level == 3) return rand.nextBoolean() ? "NORMAL" : "ZIGZAG";
        if (level == 5) return rand.nextBoolean() ? "SHOOTER" : "FAST";
        if (level == 6) return rand.nextBoolean() ? "ZIGZAG" : "SHOOTER";
        String[] types = {"NORMAL", "FAST", "ZIGZAG", "SHOOTER"};
        return types[rand.nextInt(types.length)];
    }

    // Creates the enemy for the given cell based on the given type

    private Enemy createEnemy(String type, int x, int y, Cell cell) {
        if (type.equals("NORMAL")) return new NormalEnemy(x, y, cell);
        else if (type.equals("FAST")) return new FastEnemy(x, y, cell);
        else if (type.equals("ZIGZAG")) return new ZigzagEnemy(x, y, cell);
        else if (type.equals("SHOOTER")) return new ShooterEnemy(x, y, cell);
        else return new NormalEnemy(x, y, cell);
    }

    // Initialize the boss parameters

    private void initializeBoss(int level) {
        bossFight = true;
        enemies.clear();
        grid = null;
        currentBoss = (level == 4) ? new BossLevel4() : new BossLevel8();
        System.out.println("=== Boss Level " + level + " Started ===");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!running || paused) return;
        update();
        repaint();
    }

    // Updates the state according to the power ups activated and the position of entities

    private void update() {
        plane.update();

        for (int i = bullets.size() - 1; i >= 0; i--) {
            Bullet bullet = bullets.get(i);
            bullet.update();
            if (!bullet.isActive()) bullets.remove(i);
        }

        boolean isFrozen = plane.isFrozen();

        if (!isFrozen) {
            for (int i = eggs.size() - 1; i >= 0; i--) {
                Egg egg = eggs.get(i);
                egg.update();
                if (egg.isOffScreen(PANEL_HEIGHT)) eggs.remove(i);
            }
        }

        for (int i = powerUps.size() - 1; i >= 0; i--) {
            PowerUp powerUp = powerUps.get(i);
            powerUp.update();
            if (powerUp.isOffScreen(PANEL_HEIGHT)) powerUps.remove(i);
        }

        for (int i = explosions.size() - 1; i >= 0; i--) {
            Explosion explosion = explosions.get(i);
            explosion.update();
            if (explosion.isFinished()) explosions.remove(i);
        }

        checkBulletEnemyCollisions();

        if (bossFight && currentBoss != null && currentBoss.isAlive()) {
            checkBulletBossCollisions();
        }

        checkEggPlaneCollisions();
        checkPowerUpPlaneCollisions();

        if (!isFrozen) {
            if (!bossFight) {
                updateGrid();
            } else if (currentBoss != null && currentBoss.isAlive()) {
                updateBoss();
            }
        }

        checkChickensOutOfBounds();
        checkGameStatus();
    }

    // Updates the grid position and ends the game if and only if the chickens reach out of bounds

    private void updateGrid() {
        if (grid == null) return;

        gridX += gridSpeed * gridDirection;
        if (gridX + gridCols * CELL_WIDTH >= PANEL_WIDTH - 30 || gridX <= 30) {
            gridDirection *= -1;
            gridY += gridDropStep;

            int BOTTOM_MARGIN = 50;
            if (gridY + gridRows * CELL_HEIGHT > PANEL_HEIGHT + BOTTOM_MARGIN) {
                gameOver = true;
                running = false;
                soundManager.playGameOverSound();
                System.out.println("=== GAME OVER - Chickens reached the bottom! ===");
                return;
            }
        }

        for (int row = 0; row < gridRows; row++) {
            for (int col = 0; col < gridCols; col++) {
                if (grid[row][col] != null && grid[row][col].getCounter() > 0) {
                    for (Enemy enemy : enemies) {
                        if (enemy.getCell() == grid[row][col] && enemy.isAlive()) {
                            if (enemy.isFlying()) {
                                enemy.updateFlying();
                            } else {
                                enemy.setX((int)(gridX + col * CELL_WIDTH));
                                enemy.setY((int)(gridY + row * CELL_HEIGHT));
                                enemy.update();
                            }
                            break;
                        }
                    }
                }
            }
        }

        for (Enemy enemy : enemies) {
            if (enemy.isAlive() && enemy.isFlying()) {
                enemy.updateFlying();
            }
        }

        eggDropTimer += 0.016;
        if (eggDropTimer >= eggDropInterval) {
            eggDropTimer = 0;
            for (Enemy enemy : enemies) {
                if (!enemy.isAlive() || enemy.isFlying()) continue;

                String type = enemy.getType();

                if (type.equals("SHOOTER")) {
                    ShooterEnemy shooter = (ShooterEnemy) enemy;

                    if (shooter.canShoot() && Math.random() < 0.3) {
                        eggs.add(new Egg(enemy.getX(), enemy.getY(), "DOWN"));
                    }

                    if (shooter.canShootHorizontally() && Math.random() < 0.4) {
                        Egg horizontalEgg = shooter.shootHorizontal(plane.getX(), plane.getY());
                        if (horizontalEgg != null) {
                            eggs.add(horizontalEgg);
                        }
                    }
                } else {
                    if (Math.random() < 0.3) {
                        eggs.add(new Egg(enemy.getX(), enemy.getY(), "DOWN"));
                    }
                }
            }
        }
    }

    // Updates the boss parameters

    private void updateBoss() {
        if (currentBoss != null && currentBoss.isAlive()) {
            currentBoss.update();
            if (currentBoss.canShoot()) {
                eggs.addAll(currentBoss.shoot());
            }
        }
    }

    // Checks if the chickens are out of bound
    // if they are, the player loses and the game is over

    private void checkChickensOutOfBounds() {
        int BOTTOM_MARGIN = 30;

        for (Enemy enemy : enemies) {
            if (enemy.isAlive()) {
                if (enemy.getY() > PANEL_HEIGHT + BOTTOM_MARGIN) {
                    gameOver = true;
                    running = false;
                    soundManager.playGameOverSound();
                    System.out.println("=== GAME OVER - Chicken escaped! ===");
                    return;
                }
            }
        }
    }

    // Checks collision between bullet and entities and then performs the appropriate action

    private void checkBulletEnemyCollisions() {
        for (int i = bullets.size() - 1; i >= 0; i--) {
            Bullet bullet = bullets.get(i);
            if (!bullet.isActive()) continue;

            for (int j = enemies.size() - 1; j >= 0; j--) {
                Enemy enemy = enemies.get(j);
                if (!enemy.isAlive()) continue;

                if (bullet.getBounds().intersects(enemy.getBounds())) {
                    bullet.setActive(false);
                    enemy.takeDamage();

                    if (!enemy.isAlive()) {
                        score += enemy.getScoreValue();
                        explosions.add(new Explosion(enemy.getX(), enemy.getY()));
                        soundManager.playExplosionSound();

                        System.out.println("Enemy killed! Type: " + enemy.getType() +
                                ", Position: (" + enemy.getX() + ", " + enemy.getY() +
                                (enemy.isFlying() ? ") [FLYING]" : ")"));

                        decrementCellCounter(enemy);

                        if (Math.random() < 0.2) {
                            powerUps.add(PowerUp.randomPowerUp(enemy.getX(), enemy.getY()));
                            soundManager.playPowerUpSound();
                        }
                    }
                    break;
                }
            }
        }
        bullets.removeIf(b -> !b.isActive());
        enemies.removeIf(e -> !e.isAlive());
    }

    // Decreases the counter of cell
    // If the counter is not zero
    // it should still respawn an enemy

    private void decrementCellCounter(Enemy enemy) {
        if (grid == null || enemy == null) return;

        Cell cell = enemy.getCell();

        if (cell != null && cell.getCounter() > 0) {
            cell.decrementCounter();
            System.out.println("Cell counter decreased to: " + cell.getCounter());

            if (cell.getCounter() > 0) {
                spawnReplacementEnemy(cell);
            }

            int alive = 0;
            for (Enemy e : enemies) {
                if (e.isAlive()) alive++;
            }
            System.out.println("   Alive enemies remaining: " + alive);
        } else {
            System.out.println("WARNING: Enemy has no valid cell reference!");
        }
    }

    // Respawns enemies

    private void spawnReplacementEnemy(Cell cell) {
        if (cell == null) return;

        Random rand = new Random();
        int spawnX, spawnY = 20;

        if (rand.nextBoolean()) {
            spawnX = 20;
        } else {
            spawnX = PANEL_WIDTH - 20;
        }

        String type = cell.getEnemyType();
        Enemy newEnemy = createEnemy(type, spawnX, spawnY, cell);

        int targetX = (int)(gridX + (cell.getX() - GRID_START_X));
        int targetY = (int)(gridY + (cell.getY() - GRID_START_Y));
        newEnemy.setTarget(targetX, targetY);

        enemies.add(newEnemy);

        System.out.println("Replacement enemy spawned at (" + spawnX + ", " + spawnY +
                ") flying to (" + targetX + ", " + targetY + ")");
    }

    // Checks the collision of bullets with level 4 & 8 bosses
    // !isalive => victory

    private void checkBulletBossCollisions() {
        if (currentBoss == null || !currentBoss.isAlive()) {
            return;
        }

        for (int i = bullets.size() - 1; i >= 0; i--) {
            Bullet bullet = bullets.get(i);
            if (!bullet.isActive()) continue;

            if (bullet.getBounds().intersects(currentBoss.getBounds())) {
                bullet.setActive(false);
                currentBoss.takeDamage(plane.getDamageMultiplier());
                explosions.add(new Explosion(bullet.getX(), bullet.getY()));
                soundManager.playExplosionSound();

                if (!currentBoss.isAlive()) {
                    int currentLevel = level;

                    if (currentLevel == 4) {
                        score += 500;
                        level = 5;
                        bossFight = false;
                        currentBoss = null;
                        gridX = GRID_START_X;
                        gridY = GRID_START_Y;
                        gridDirection = 1;
                        initializeGridLevel();
                        System.out.println("=== Boss Level 4 Defeated! Moving to Level 5 ===");
                    } else if (currentLevel == 8) {
                        score += 1000;
                        victory = true;
                        running = false;
                        soundManager.playEndingMusic();
                        System.out.println("=== FINAL BOSS DEFEATED! VICTORY! ===");
                    }
                    break;
                }
            }
        }
    }

    // Checks if the plane has been hit
    // lower the lives and add an explosion if it has
    // lives = 0 => game over

    private void checkEggPlaneCollisions() {
        if (plane.isShieldActive()) return;

        for (int i = eggs.size() - 1; i >= 0; i--) {
            Egg egg = eggs.get(i);
            if (egg.getBounds().intersects(plane.getBounds())) {
                eggs.remove(i);
                plane.takeDamage();
                explosions.add(new Explosion(egg.getX(), egg.getY()));
                soundManager.playExplosionSound();
                if (plane.getLives() <= 0) {
                    gameOver = true;
                    running = false;
                    soundManager.playGameOverSound();
                    System.out.println("=== GAME OVER ===");
                }
            }
        }
    }

    // Checks if the power up is collected

    private void checkPowerUpPlaneCollisions() {
        for (int i = powerUps.size() - 1; i >= 0; i--) {
            PowerUp powerUp = powerUps.get(i);
            if (powerUp.getBounds().intersects(plane.getBounds())) {
                powerUp.applyEffect(plane);
                powerUps.remove(i);
                soundManager.playPowerUpSound();
                System.out.println("PowerUp collected: " + powerUp.getType());
            }
        }
    }

    // Logs the current state of the game

    private void checkGameStatus() {
        if (gameOver || victory) {
            running = false;
            saveGameResult();
            return;
        }

        if (!bossFight && grid != null) {
            int remainingCells = 0;
            for (int row = 0; row < gridRows; row++) {
                for (int col = 0; col < gridCols; col++) {
                    if (grid[row][col] != null && grid[row][col].getCounter() > 0) {
                        remainingCells++;
                    }
                }
            }

            int aliveEnemies = 0;
            for (Enemy enemy : enemies) {
                if (enemy.isAlive()) aliveEnemies++;
            }

            if (frameCount % 60 == 0) {
                System.out.println("=== STATUS ===");
                System.out.println("Level: " + level);
                System.out.println("Alive Enemies: " + aliveEnemies);
                System.out.println("Remaining Cells: " + remainingCells);
                System.out.println("Total Enemies in List: " + enemies.size());
                System.out.println("bossFight: " + bossFight);
                System.out.println("currentBoss: " + (currentBoss != null ? "exists" : "null"));
                System.out.println("Grid position: (" + gridX + ", " + gridY + ")");
                System.out.println("==============");
            }
            frameCount++;

            if (remainingCells == 0 && aliveEnemies == 0) {
                score += 200;
                System.out.println("=== Level " + level + " Complete! ===");
                System.out.println("All enemies and cells cleared!");

                int previousLevel = level;

                if (level == 3) {
                    level = 4;
                    System.out.println("Moving from Level " + previousLevel + " to Level " + level + " (BOSS LEVEL)");
                } else if (level == 7) {
                    level = 8;
                    System.out.println("Moving from Level " + previousLevel + " to Level " + level + " (FINAL BOSS)");
                } else {
                    level++;
                    System.out.println("Moving from Level " + previousLevel + " to Level " + level);
                }

                initializeLevel();

                if (level == 4 || level == 8) {
                    System.out.println("After initializeLevel() - bossFight: " + bossFight);
                    System.out.println("After initializeLevel() - currentBoss: " + (currentBoss != null ? "exists" : "null"));
                    if (currentBoss != null) {
                        System.out.println("Boss health: " + currentBoss.getHealthPercent());
                    }
                }
            }
        }
    }

    // Draws the power ups collected in the right corner of the screen in a stack pattern

    private void drawPowerUpStack(Graphics2D g) {
        Map<String, Long> activePowerUps = plane.getActivePowerUps();

        if (plane.getShotCount() > 1) {
            activePowerUps.put("ADD_SHOT", -1L);
        }

        if (activePowerUps.isEmpty()) return;

        int startX = PANEL_WIDTH - 180;
        int startY = 40;
        int index = 0;

        for (Map.Entry<String, Long> entry : activePowerUps.entrySet()) {
            String type = entry.getKey();
            long remaining = entry.getValue();

            int y = startY + index * 22;

            Color color = PowerUp.getColor(type);
            g.setColor(color);
            g.fillRect(startX, y + 2, 14, 14);
            g.setColor(Color.WHITE);
            g.drawRect(startX, y + 2, 14, 14);

            g.setFont(new Font("SansSerif", Font.PLAIN, 12));
            g.setColor(Color.WHITE);
            String displayName = PowerUp.getDisplayName(type);

            String timeText = "";
            if (remaining > 0) {
                timeText = String.format("%ds", remaining);
            } else if (remaining == -1) {
                timeText = "∞";
            }

            String text = displayName + (timeText.isEmpty() ? "" : " (" + timeText + ")");
            g.drawString(text, startX + 20, y + 14);

            index++;
        }
    }

    // Saves the game results in game_history table

    private void saveGameResult() {
        if (user != null) {
            if (score > user.getHighScore()) user.setHighScore(score);
            if (level > user.getLastLevel()) user.setLastLevel(level);
            dbManager.updateUser(user);
            dbManager.saveGameHistory(user.getUsername(), score, level,
                    user.isBgmEnabled(), user.isShotEnabled(),
                    user.isCrashEnabled(), user.isGameoverEnabled());
        }
    }

    // Stops the game

    public void stopGame() {
        running = false;
        if (timer != null) timer.stop();
        saveGameResult();
    }

    // Draws the appropriate announcement

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        drawBackground(g2d);
        drawHUD(g2d);

        if (gameOver) { drawGameOver(g2d); return; }
        if (victory) { drawVictory(g2d); return; }
        if (paused) { drawPause(g2d); return; }

        drawGameObjects(g2d);
    }

    // Draws the background

    private void drawBackground(Graphics2D g) {
        if (background1 != null) {
            g.drawImage(background1, 0, 0, PANEL_WIDTH, PANEL_HEIGHT, null);
        }
        else {

            // If we could not load, draw it yourself

            g.setColor(new Color(10, 10, 40));
            g.fillRect(0, 0, PANEL_WIDTH, PANEL_HEIGHT);

            g.setColor(Color.WHITE);
            for (int i = 0; i < 100; i++) {
                int x = (i * 73) % PANEL_WIDTH;
                int y = (i * 137) % PANEL_HEIGHT;
                g.fillOval(x, y, 2, 2);
            }
        }
    }

    // Draws the HUD bar at the top of the screen

    private void drawHUD(Graphics2D g) {
        g.setColor(new Color(0, 0, 0, 180));
        g.fillRect(0, 0, PANEL_WIDTH, 35);

        g.setColor(Color.WHITE);
        g.setFont(new Font("SansSerif", Font.BOLD, 14));

        int y = 22;
        g.drawString("Player: " + user.getUsername(), 15, y);
        g.drawString("Score: " + score, 160, y);
        g.drawString("Level: " + level, 310, y);
        g.drawString("Lives: " + plane.getLives(), 460, y);
        g.drawString("Shots: " + plane.getShotCount(), 580, y);

        drawPowerUpStack(g);

        if (bossFight && currentBoss != null && currentBoss.isAlive()) {
            currentBoss.draw(g);
        }
    }

    // Draws the entities using draw method

    private void drawGameObjects(Graphics2D g) {
        for (Enemy enemy : enemies) if (enemy.isAlive()) enemy.draw(g);
        for (Egg egg : eggs) egg.draw(g);
        for (Bullet bullet : bullets) bullet.draw(g);
        for (PowerUp powerUp : powerUps) powerUp.draw(g);
        plane.draw(g);
        for (Explosion explosion : explosions) explosion.draw(g);
    }

    // Draws the game over announcement

    private void drawGameOver(Graphics2D g) {
        g.setColor(new Color(0, 0, 0, 200));
        g.fillRect(0, 0, PANEL_WIDTH, PANEL_HEIGHT);
        g.setColor(Color.RED);
        g.setFont(new Font("SansSerif", Font.BOLD, 48));
        String text = "GAME OVER";
        FontMetrics fm = g.getFontMetrics();
        g.drawString(text, (PANEL_WIDTH - fm.stringWidth(text)) / 2, PANEL_HEIGHT / 2 - 20);
        g.setColor(Color.WHITE);
        g.setFont(new Font("SansSerif", Font.PLAIN, 20));
        String subText = "Score: " + score + " | Press ESC to return to menu";
        fm = g.getFontMetrics();
        g.drawString(subText, (PANEL_WIDTH - fm.stringWidth(subText)) / 2, PANEL_HEIGHT / 2 + 30);
    }

    //  Draws the victory announcement

    private void drawVictory(Graphics2D g) {
        g.setColor(new Color(0, 0, 0, 200));
        g.fillRect(0, 0, PANEL_WIDTH, PANEL_HEIGHT);
        g.setColor(Color.YELLOW);
        g.setFont(new Font("SansSerif", Font.BOLD, 48));
        String text = "VICTORY!";
        FontMetrics fm = g.getFontMetrics();
        g.drawString(text, (PANEL_WIDTH - fm.stringWidth(text)) / 2, PANEL_HEIGHT / 2 - 20);
        g.setColor(Color.WHITE);
        g.setFont(new Font("SansSerif", Font.PLAIN, 20));
        String subText = "Score: " + score + " | Press ESC to return to menu";
        fm = g.getFontMetrics();
        g.drawString(subText, (PANEL_WIDTH - fm.stringWidth(subText)) / 2, PANEL_HEIGHT / 2 + 30);
    }

    // Draws the pause announcement

    private void drawPause(Graphics2D g) {
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, PANEL_WIDTH, PANEL_HEIGHT);
        g.setColor(Color.WHITE);
        g.setFont(new Font("SansSerif", Font.BOLD, 48));
        String text = "PAUSED";
        FontMetrics fm = g.getFontMetrics();
        g.drawString(text, (PANEL_WIDTH - fm.stringWidth(text)) / 2, PANEL_HEIGHT / 2);
    }

    // Implements the keys for the game

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_ESCAPE) {
            if (gameOver || victory) {
                gameMain.endGame();
            } else {
                running = false;
                saveGameResult();
                gameMain.endGame();
            }
            return;
        }

        if (gameOver || victory) return;

        if (key == KeyEvent.VK_P) {
            paused = !paused;
            return;
        }

        switch (key) {
            case KeyEvent.VK_LEFT: case KeyEvent.VK_A: plane.setMovingLeft(true); break;
            case KeyEvent.VK_RIGHT: case KeyEvent.VK_D: plane.setMovingRight(true); break;
            case KeyEvent.VK_UP: case KeyEvent.VK_W: plane.setMovingUp(true); break;
            case KeyEvent.VK_DOWN: case KeyEvent.VK_S: plane.setMovingDown(true); break;
            case KeyEvent.VK_SPACE:
                if (!paused) {
                    bullets.addAll(plane.shoot());
                    soundManager.playShotSound();
                }
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_LEFT: case KeyEvent.VK_A: plane.setMovingLeft(false); break;
            case KeyEvent.VK_RIGHT: case KeyEvent.VK_D: plane.setMovingRight(false); break;
            case KeyEvent.VK_UP: case KeyEvent.VK_W: plane.setMovingUp(false); break;
            case KeyEvent.VK_DOWN: case KeyEvent.VK_S: plane.setMovingDown(false); break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}
}