/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package QLearning;

/**
 *
 * @author dong
 */
public class ModifiedAlgo extends Algorithm {
    private double warmStartK = 0.1;

    private boolean highestLocationValue = false;
    private double[][][][] Qvalue;
    private int[][][][] visited;
    private boolean[][][][] isOptimal;

    ModifiedAlgo(GridWorld gw) {
        super(gw);
        Qvalue = new double[gw.getRows()][gw.getCols()][gw.getFullBatterySteps() + 1][4];
        if (gw.getGoal() != null) {
            warmStart();
        } else {
            for (int i = 0; i < Qvalue.length; i++) {
                for (int j = 0; j < Qvalue[i].length; j++) {
                    for (int k = 0; k < Qvalue[i][j].length; k++) {
                        for (int l = 0; l < Qvalue[i][j][k].length; l++) {
                            Qvalue[i][j][k][l] = 0;
                        }
                    }
                }
            }
        }

        visited = new int[gw.getRows()][gw.getCols()][gw.getFullBatterySteps() + 1][4];
        for (int i = 0; i < visited.length; i++) {
            for (int j = 0; j < visited[i].length; j++) {
                for (int k = 0; k < visited[i][j].length; k++) {
                    for (int l = 0; l < visited[i][j][k].length; l++) {
                        visited[i][j][k][l] = 0;
                    }
                }
            }
        }

        isOptimal = new boolean[gw.getRows()][gw.getCols()][gw.getFullBatterySteps() + 1][4];
        for (int i = 0; i < isOptimal.length; i++) {
            for (int j = 0; j < isOptimal[i].length; j++) {
                for (int k = 0; k < isOptimal[i][j].length; k++) {
                    for (int l = 0; l < isOptimal[i][j][k].length; l++) {
                        isOptimal[i][j][k][l] = true;
                    }
                }
            }
        }
    }

    public void warmStart() {
        int row = gw.getGoalRow();
        int col = gw.getGoalCol();
        for (int i = 0; i < Qvalue.length; i++) {
            for (int j = 0; j < Qvalue[i].length; j++) {
                for (int k = 0; k < Qvalue[i][j].length; k++) {//battery dimension 
                    for (int l = 0; l < Qvalue[i][j][k].length; l++) {
                        double diff_expectedTime = k - (Math.abs(row - i) + Math.abs(col - j))
                                * gw.getDefaultMean();
                        Qvalue[i][j][k][l] = gw.getGoalReward()/(1+Math.exp(-warmStartK*diff_expectedTime));
                    }
                }
            }
        }
    }

    @Override
    public double[][] getQvalue(int row, int col) {
        return Qvalue[row][col];
    }

    @Override
    public boolean moveToDir(int direction) {
        int oldRow = gw.getCurRow(), oldCol = gw.getCurCol(), oldBat = gw.getRemainingSteps();
        double reward = gw.move(direction);
//        if (reward == GridWorld.DeadPenalty) {
//            return false;
//        }
        int newRow = gw.getCurRow(), newCol = gw.getCurCol(), newBat = gw.getRemainingSteps();

        double newVal = getLocationValue(newRow, newCol, newBat);

        double newDatum = reward + discount * newVal;
        visited[oldRow][oldCol][oldBat][direction]++;
        if (!alphaFixed) {
            alpha = 1.0 / visited[oldRow][oldCol][oldBat][direction];
        }
//	    alpha = 10.0/(9+visits[oldX][oldY][action]);
//        if (tracing) {
//            System.out.println("(" + oldRow + "," + oldCol + ") A=" + direction + " R=" + reward
//                    + " (" + newRow + "," + newCol + ") newDatum=" + newDatum);
//            System.out.print("     Qold=" + gw.getLocation(oldRow, oldCol).getQvalue(direction)
//                    + " Visits=" + gw.getLocation(oldRow, oldCol).getVisit(direction));
//        }
        if (gw.getLocation(newRow, newCol).isGoal()) {
            Qvalue[oldRow][oldCol][oldBat][direction] = (1 - alpha)
                    * Qvalue[oldRow][oldCol][oldBat][direction] + alpha * reward;
        } else {
            Qvalue[oldRow][oldCol][oldBat][direction] = (1 - alpha)
                    * Qvalue[oldRow][oldCol][oldBat][direction] + alpha * newDatum;
        }
        updateRanges();
        updateOptimal();
        if (tracing) {
            System.out.println("Reward: " + reward);
            System.out.println("Location: " + oldRow + ", " + oldCol + ", " + oldBat);
            System.out.println("new Qvalue: " + Qvalue[oldRow][oldCol][oldBat][direction]);
            printLocationValues();
        }
        return true;
    }

    public double getLocationValue(int row, int col, int bat) {
        double value = Qvalue[row][col][bat][0];
        for (int i = 1; i < Qvalue[row][col][bat].length; i++) {
            if (Qvalue[row][col][bat][i] > value) {
                value = Qvalue[row][col][bat][i];
            }
        }
        return value;
    }

    @Override
    public boolean doSteps(int count) {
        for (int i = 0; i < count; i++) {
            double rand = Math.random();
            if (rand < greedyProb) {// act greedily
//                int startDir = (int) (Math.random() * 4);
//                int row = gw.getCurRow();
//                int col = gw.getCurCol();
//                int bat = gw.getRemainingSteps();
//
//                double bestVal = Qvalue[row][col][bat][startDir];
//                int bestDir = startDir;
//                for (int dir = 1; dir < 4; dir++) {
//                    startDir = (startDir + 1) % 4;
//                    if (Qvalue[row][col][bat][startDir] > bestVal) {
//                        bestVal = Qvalue[row][col][bat][startDir];
//                        bestDir = startDir;
//                    }
//                }

                if (!moveToDir(getOptimalDir(gw.getCurRow(), gw.getCurCol()))) {
                    return false;
                }
            } else { // act randomly
                if (!moveToDir((int) (Math.random() * 4))) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public int getOptimalDir(int row, int col) {
        int startDir = (int) (Math.random() * 4);
        int bat = gw.getRemainingSteps();

        double bestVal = Qvalue[row][col][bat][startDir];
        int bestDir = startDir;
        for (int dir = 1; dir < 4; dir++) {
            startDir = (startDir + 1) % 4;
            if (Qvalue[row][col][bat][startDir] > bestVal) {
                bestVal = Qvalue[row][col][bat][startDir];
                bestDir = startDir;
            }
        }
        return bestDir;
    }

    public boolean isHighestLocationValue() {
        return highestLocationValue;
    }

    public void setHighestLocationValue(boolean highestLocationValue) {
        this.highestLocationValue = highestLocationValue;
    }

    @Override
    public double getQvalue(int row, int col, int dir) {
        double qv = Qvalue[row][col][0][dir];
        for (int i = 0; i < Qvalue[row][col].length; i++) {
            if (Qvalue[row][col][i][dir] > qv) {
                qv = Qvalue[row][col][i][dir];
            }
        }
        return highestLocationValue ? qv : Qvalue[row][col][gw.getRemainingSteps()][dir];
    }

    @Override
    public boolean isOptimal(int row, int col, int dir) {
        return isOptimal[row][col][gw.getRemainingSteps()][dir];
    }

    private void updateRanges() {
        for (int i = 0; i < Qvalue.length; i++) {
            for (int j = 0; j < Qvalue[i].length; j++) {
                for (int k = 0; k < Qvalue[i][j].length; k++) {
                    for (int l = 0; l < Qvalue[i][j][k].length; l++) {
                        double value = Qvalue[i][j][k][l];
                        highest = value > highest ? value : highest;
                        lowest = value < lowest ? value : lowest;
                    }
                }
            }
        }
        range = highest - lowest;
    }

    private void updateOptimal() {
        double[] qvalues = Qvalue[gw.getCurRow()][gw.getCurCol()][gw.getRemainingSteps()];
        double big = qvalues[0];
        for (int i = 0; i < qvalues.length; i++) {
            if (qvalues[i] > big) {
                big = qvalues[i];
            }
        }

        for (int i = 0; i < qvalues.length; i++) {
            isOptimal[gw.getCurRow()][gw.getCurCol()][gw.getRemainingSteps()][i] = qvalues[i] == big;
        }
        if (highestLocationValue) {
            double lv = Qvalue[gw.getCurRow()][gw.getCurCol()][0][0];
            for (int i = 0; i < gw.getFullBatterySteps(); i++) {
                for (int j = 0; j < 4; j++) {
                    if (Qvalue[gw.getCurRow()][gw.getCurCol()][i][j] > lv) {
                        lv = Qvalue[gw.getCurRow()][gw.getCurCol()][i][j];
                    }
                }
            }
            gw.getLocation(gw.getCurRow(), gw.getCurCol()).setLocationValue(lv);
        } else {
            gw.getLocation(gw.getCurRow(), gw.getCurCol()).setLocationValue(big);
        }
    }

    private void printLocationValues() {
        for (int i = 0; i < Qvalue.length; i++) {
            for (int j = 0; j < Qvalue[i].length; j++) {
                System.out.println("row " + i + " col " + j + ":");
                for (int k = 0; k < Qvalue[i][j].length; k++) {
                    for (int l = 0; l < Qvalue[i][j][k].length; l++) {
                        System.out.format("%6.2f | ", Qvalue[i][j][k][l]);
                    }
                    System.out.print("; ");
                }
                System.out.println("");
            }
        }
    }

    @Override
    public void setHighestLV(boolean b) {
        highestLocationValue = b;
        updateOptimal();
    }
}
