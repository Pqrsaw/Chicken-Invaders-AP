package game;

public class Cell {

    private int x, y;
    private String enemyType; // "NORMAL", "FAST", "ZIGZAG", "SHOOTER"
    private int counter;

    public Cell(int x, int y, String enemyType, int counter) {
        this.x = x;
        this.y = y;
        this.enemyType = enemyType;
        this.counter = counter;
    }

    public void decrementCounter() {
        if (counter > 0) {
            counter--;
        }
    }

    // Getters and Setters

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        if(counter >= 0) {
            this.counter = counter;
        }
    }

    public String getEnemyType() {
        return enemyType;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}