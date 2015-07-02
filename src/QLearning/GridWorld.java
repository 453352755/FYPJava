package QLearning;

/**
 *
 * @author Dong Yubo
 *
 */

/*
 Grid col0 col1 col2 col3 ... 
 row0 
 row1 
 row2 
 row3 
 ....
 */


public class GridWorld {

    public static final int Up = 0;
    public static final int Down = 1;
    public static final int Left = 2;
    public static final int Right = 3;

    public final int DirectionProbability = 4;
    public final double WallPenalty = -1.0;
    public final double BloackPenalty = -1.0;

    private final int rows;
    private final int cols;
    public int numberOfSteps = 0;
    public double totalReward = 0.0;
    public int curRow;
    public int curCol;
    public double[][] value;
    public Location[][] location;

    public GridWorld(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        curRow = (int) (Math.random() * rows);
        curCol = (int) (Math.random() * cols);
        value = new double[rows][cols];
        location = new Location[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                value[i][j] = 0;
                location[i][j] = new Location(i, j);

                //check walls
                if (i == 0 || i == rows-1 || j == 0 || j == cols-1) {
                    location[i][j].isWall = true;
                    location[i][j].reward = WallPenalty;
                }
            }
        }
        
        //set special location
        location[3][7].reward = 10;
        location[4][8].reward = -3;
    }

    public void reset() {
        numberOfSteps = 0;
        totalReward = 0.0;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public double getValue(int row, int col) {
        return value[row][col];
    }

    public void setValue(int row, int col, double value) {
        this.value[row][col] = value;
    }

    public Location getLocation(int row, int col) {
        return location[row][col];
    }

    public void setCurrentPosition(int row, int col) {
        this.curRow = row;
        this.curCol = col;
    }

    /**
     * 70% chance get original direction 10% chance for each other direction
     *
     * @param d
     * @return
     */
    public int getActualDirection(int d) {
        int rand = (int) (Math.random() * 10);
        if (rand < DirectionProbability) {
            return rand;
        } else {
            return d;
        }
    }

    public double move(int d) {
        int actualDirection = getActualDirection(d);
        double reward;
        int newRow = curRow, newCol = curCol;

        //calculate new postion
        switch (actualDirection) {
            case Up:
                newRow = curRow - 1;
                break;
            case Right:
                newCol = curCol + 1;
                break;
            case Down:
                newRow = curRow + 1;
                break;
            case Left:
                newCol = curCol - 1;
                break;
            default: // should never occur
            {
                newRow = 0;
                newCol = 0;
                reward = 0.0;
            }
        }

        if (newCol < 0 || newCol > cols - 1 || newRow < 0 || newRow > rows - 1) {
            reward = WallPenalty;
            newRow = curRow;
            newCol = curCol;
        } else if (location[newRow][newCol].isBlock) {
            reward = BloackPenalty;
            newRow = curRow;
            newCol = curCol;
        } else {
            reward = location[newRow][newCol].reward;
        }

        numberOfSteps++;
        totalReward += reward;
        curRow = newRow;
        curCol = newCol;
        return reward;
    }
}
