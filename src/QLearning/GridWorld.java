package QLearning;

import java.util.HashSet;
import java.util.Set;

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

    public static double DeadPenalty = -100;
    private double DirectionProbability = 1;
    private double WallPenalty = -10;
    private double BlockPenalty = -5;
    private double defaultReward = -2;
    private double ChargingReward = 0.0;
    private double GoalReward = 100.0;
    private double defaultTraveTime = 1;
    private boolean randomTravelTime = false;
    private boolean batteryEnabled = true;
    private int fullBatterySteps = 4;

    private final int rows;
    private final int cols;

    private int numberOfSteps = 0;
    private double totalReward = 0.0;
    private double totalTravelTime = 0.0;

    private int curRow;
    private int curCol;
    private int startRow;
    private int startCol;
    private int goalRow;
    private int goalCol;
    private int remainingSteps;
    private final Location[][] location;
//    private double range;
//    private double highest, lowest;

    public GridWorld(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        remainingSteps = fullBatterySteps;
//        curRow = (int) (Math.random() * rows);
//        curCol = (int) (Math.random() * cols);
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
//                if (i == 0 || i == rows - 1 || j == 0 || j == cols - 1) {
//                    location[i][j].setIsWall(true);
//                    location[i][j].setReward(WallPenalty);
//                }
            }
        }

//        setStart(0, 0);
//        setGoal(2, 4);
//        setCharging(1, 2, true);
        //setCharging(6, 4, true);
    }

    public void setStart(int row, int col) {
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                location[i][j].setIsStart(false);
            }
        }
        location[row][col].setIsStart(true);
        startRow = row;
        startCol = col;
        curRow = row;
        curCol = col;
    }

    public void setGoal(int row, int col) {
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                location[i][j].setIsGoal(false);
                location[i][j].setReward(defaultReward);
            }
        }
        location[row][col].setIsGoal(true);
        goalRow = row;
        goalCol = col;
        location[row][col].setReward(GoalReward);
    }

    public void setCharging(int row, int col, boolean isCharging) {
        location[row][col].setIsCharging(isCharging);
        location[row][col].setReward(isCharging ? ChargingReward : defaultReward);
    }

    public void setBlock(int row, int col, boolean isBlock) {
        location[row][col].setIsBlock(isBlock);
        if (isBlock) {
            location[row][col].setReward(BlockPenalty);
            location[row][col].setLocationValue(0);
            for (int k = 0; k < location[row][col].getTravelTime().length; k++) {
                location[row][col].setTravelTime(k, 0);
                //location[row][col].setQvalue(k, 0);
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

    public void charge() {
        //TO DO
        //charge at location i,j charge to ? percent, charge for ? minutes, charge speed 

        this.remainingSteps = fullBatterySteps;
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

    public void setRemainingSteps(int remainingSteps) {
        if (remainingSteps > this.fullBatterySteps) {
            this.remainingSteps = this.fullBatterySteps;
        } else {
            this.remainingSteps = remainingSteps;
        }
    }

    public void setFullBatterySteps(int fullBatterySteps) {
        this.fullBatterySteps = fullBatterySteps;
        this.remainingSteps = fullBatterySteps;
    }

    public int getFullBatterySteps() {
        return fullBatterySteps;
    }

    public int getRemainingSteps() {
        return remainingSteps;
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

//    public void reset() {
//        totalTravelTime = 0;
////        range = 0;
////        highest = 0;
////        lowest = 0;
//        remainingSteps = fullBatterySteps;
//        numberOfSteps = 0;
//        totalReward = 0.0;
//    }
    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public double getDefaultReward() {
        return defaultReward;
    }

    public double getChargingReward() {
        return ChargingReward;
    }

    public double getGoalReward() {
        return GoalReward;
    }

    public double getDefaultTraveTime() {
        return defaultTraveTime;
    }

//    public double getHighest() {
//        return highest;
//    }
//
//    public double getLowest() {
//        return lowest;
//    }
    public double getTotalTravelTime() {
        return totalTravelTime;
    }

    public boolean isRandomTravelTime() {
        return randomTravelTime;
    }

    public boolean isBatteryEnabled() {
        return batteryEnabled;
    }

    public void setBatteryEnabled(boolean batteryEnabled) {
        this.batteryEnabled = batteryEnabled;
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

    public void setReward(int row, int col, double reward) {
        location[row][col].setReward(reward);
        System.out.print("New reward set to ");
        System.out.println(reward);
    }

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

    public void reset() {
        for (int i = 0; i < location.length; i++) {
            for (int j = 0; j < location[i].length; j++) {
                location[i][j].setLocationValue(0);
            }
        }
        totalReward = 0;
        totalTravelTime = 0;
        numberOfSteps = 0;
        remainingSteps = fullBatterySteps;
    }

    public void moveToStart() {
        curRow = startRow;
        curCol = startCol;
        if (batteryEnabled) {
            remainingSteps = fullBatterySteps;
        }
    }

    public double move(int d) {
        int actualDirection = getActualDirection(d);
        double reward;
        int newRow = curRow, newCol = curCol;
        if (location[curRow][curCol].isGoal()) {
            newRow = startRow;
            newCol = startCol;
            reward = 0;
            remainingSteps = fullBatterySteps;
        } else {
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
            if (batteryEnabled && remainingSteps <= 0) {
                newRow = startRow;
                newCol = startCol;
                reward = DeadPenalty;
                remainingSteps = fullBatterySteps + 1;
            } else if (newCol < 0 || newCol > cols - 1 || newRow < 0 || newRow > rows - 1) {//wall
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
            remainingSteps--;
            totalReward += reward;
            if (location[newRow][newCol].isCharging()) {
                charge();
            }
        }
        curRow = newRow;
        curCol = newCol;

        return reward;
    }

    public void resetPath() {
        for (int i = 0; i < location.length; i++) {
            for (int j = 0; j < location[i].length; j++) {
                location[i][j].setIsPath(false);
            }
        }
    }
}
