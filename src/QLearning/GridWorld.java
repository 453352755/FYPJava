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

    private double DirectionProbability = 0.7;
    private double WallPenalty = -1.0;
    private double BlockPenalty = -1.0;
    private double defaultReward = 0.0;
    private double defaultTraveTime = 1;
    private boolean randomTravelTime = true;

    private final int rows;
    private final int cols;

    private int numberOfSteps = 0;
    private double totalReward = 0.0;
    private double totalTravelTime = 0.0;

    private int curRow;
    private int curCol;
    private final Location[][] location;
    private double range;
    private double highest, lowest;

    public GridWorld(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        curRow = (int) (Math.random() * rows);
        curCol = (int) (Math.random() * cols);
        location = new Location[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                location[i][j] = new Location(i, j);
                location[i][j].setReward(defaultReward);
                if (randomTravelTime) {
                    for (int k = 0; k < location[i][j].getTravelTime().length; k++) {
                        location[i][j].setTravelTime(k, Math.random() * 10);
                    }
                } else {
                    for (int k = 0; k < location[i][j].getTravelTime().length; k++) {
                        location[i][j].setTravelTime(k, defaultTraveTime);
                    }
                }

                //check walls
                if (i == 0 || i == rows - 1 || j == 0 || j == cols - 1) {
                    location[i][j].setIsWall(true);
                    location[i][j].setReward(WallPenalty);
                }
            }
        }

        //set special location
        location[3][7].setReward(10);
        location[4][8].setReward(-3);
        setBlock(6, 4, true);
    }

    public void setBlock(int row, int col, boolean isBlock) {
        location[row][col].setIsBlock(isBlock);
        if (isBlock) {
            location[row][col].setReward(BlockPenalty);
            location[row][col].setLocationValue(0);
            for (int k = 0; k < location[row][col].getTravelTime().length; k++) {
                location[row][col].setTravelTime(k, 0);
                location[row][col].setQvalue(k, 0);
            }
        } else {
            location[row][col].setReward(defaultReward);
            if (randomTravelTime) {
                for (int k = 0; k < location[row][col].getTravelTime().length; k++) {
                    location[row][col].setTravelTime(k, Math.random() * 10);
                }
            } else {
                for (int k = 0; k < location[row][col].getTravelTime().length; k++) {
                    location[row][col].setTravelTime(k, defaultTraveTime);
                }
            }
        }
    }

    public double getDirectionProbability() {
        return DirectionProbability;
    }

    public double getWallPenalty() {
        return WallPenalty;
    }

    public double getBlockPenalty() {
        return BlockPenalty;
    }

    public int getNumberOfSteps() {
        return numberOfSteps;
    }

    public double getTotalReward() {
        return totalReward;
    }

    public int getCurRow() {
        return curRow;
    }

    public int getCurCol() {
        return curCol;
    }

    public Location getLocation(int row, int col) {
        return location[row][col];
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

    public double getRange() {
        return range;
    }

    public double getHighest() {
        return highest;
    }

    public double getLowest() {
        return lowest;
    }

    public double getTotalTravelTime() {
        return totalTravelTime;
    }

    public boolean isRandomTravelTime() {
        return randomTravelTime;
    }

    public void setRandomTravelTime(boolean randomTravelTime) {
        this.randomTravelTime = randomTravelTime;
    }

    public void setDirectionProbability(double DirectionProbability) {
        this.DirectionProbability = DirectionProbability;
    }

    public void setDefaultTraveTime(double defaultTraveTime) {
        this.defaultTraveTime = defaultTraveTime;
    }

    public void setCurrentPosition(int row, int col) {
        this.curRow = row;
        this.curCol = col;
    }

    public void setDirectionProbability(int DirectionProbability) {
        this.DirectionProbability = DirectionProbability;
    }

    public void setWallPenalty(double WallPenalty) {
        this.WallPenalty = WallPenalty;
    }

    public void setBlockPenalty(double BlockPenalty) {
        this.BlockPenalty = BlockPenalty;
    }

    public double computeValueRange() {
        double big = location[0][0].getLocationValue();
        double small = location[0][0].getLocationValue();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                location[i][j].computeOptimal();
                if (location[i][j].getLocationValue() > big) {
                    big = location[i][j].getLocationValue();
                } else if (location[i][j].getLocationValue() < small) {
                    small = location[i][j].getLocationValue();
                }
            }
        }
        range = big - small;
        highest = big;
        lowest = small;
        return range;
    }

    public void setReward(int row, int col, double reward) {
        location[row][col].setReward(reward);
        System.out.print("New reward set to ");
        System.out.println(reward);
    }

    /**
     * 70% chance get original direction 10% chance for each other direction
     *
     * @param d
     * @return
     */
    public int getActualDirection(int d) {
        double rand = Math.random();
        if (rand < DirectionProbability) {
            return d;
        } else {
            int r = d;
            while (r == d) {
                r = (int) (Math.random() * 4);
            }
            return r;
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
                //reward = 0.0;
            }
        }

        //wall
        if (newCol < 0 || newCol > cols - 1 || newRow < 0 || newRow > rows - 1) {
            reward = WallPenalty;
            newRow = curRow;
            newCol = curCol;
        } else if (location[newRow][newCol].isBlock()) {//block
            reward = BlockPenalty;
            newRow = curRow;
            newCol = curCol;
        } else {//move
            reward = location[newRow][newCol].getReward();
            totalTravelTime += location[curRow][curCol].getTravelTime(actualDirection);
        }

        numberOfSteps++;
        totalReward += reward;
        curRow = newRow;
        curCol = newCol;
        return reward;
    }
}
