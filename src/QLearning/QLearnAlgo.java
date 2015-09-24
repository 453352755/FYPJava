package QLearning;

/**
 *
 * @author Dong Yubo
 */
public class QLearnAlgo extends Algorithm {
    private double[][][] Qvalue;
    private int[][][] visited;
    private boolean[][][] isOptimal;

    public QLearnAlgo(GridWorld gw) {
        super(gw);
        Qvalue = new double[gw.getRows()][gw.getCols()][4];
        for (int i = 0; i < Qvalue.length; i++) {
            for (int j = 0; j < Qvalue[i].length; j++) {
                for (int k = 0; k < Qvalue[i][j].length; k++) {

                    Qvalue[i][j][k] = 0;

                }
            }
        }

        visited = new int[gw.getRows()][gw.getCols()][4];
        for (int i = 0; i < visited.length; i++) {
            for (int j = 0; j < visited[i].length; j++) {
                for (int k = 0; k < visited[i][j].length; k++) {

                    visited[i][j][k] = 0;

                }
            }
        }

        isOptimal = new boolean[gw.getRows()][gw.getCols()][4];
        for (int i = 0; i < visited.length; i++) {
            for (int j = 0; j < visited[i].length; j++) {
                for (int k = 0; k < visited[i][j].length; k++) {

                    isOptimal[i][j][k] = true;

                }
            }
        }
    }

    @Override
    public double[][] getQvalue(int row, int col) {

        double[][] qvalue = new double[1][4];
        qvalue[0] = Qvalue[row][col].clone();
//        for (int i = 0; i < 4; i++) {
//            System.out.println(Qvalue[row][col][i]);
//            System.out.println(qvalue[0][i]);
//        }
        return qvalue;
    }

    @Override
    public boolean moveToDir(int direction) {
        int oldRow = gw.getCurRow(), oldCol = gw.getCurCol();
        double reward = gw.move(direction);
//        if (reward == GridWorld.DeadPenalty) {
//            return false;
//        }

        int newRow = gw.getCurRow(), newCol = gw.getCurCol();
        double newVal = gw.getLocation(newRow, newCol).getLocationValue();
        double newDatum = reward + discount * newVal;
        visited[oldRow][oldCol][direction]++;
        if (!alphaFixed) {
            alpha = 1.0 / visited[oldRow][oldCol][direction];
        }
        if (gw.getLocation(newRow, newCol).isGoal()) {
            Qvalue[oldRow][oldCol][direction] = reward;
        } else {
            Qvalue[oldRow][oldCol][direction] = (1 - alpha)
                    * Qvalue[oldRow][oldCol][direction] + alpha * newDatum;
        }
        updateRanges();
        updateOptimal();

        if (tracing) {
            System.out.println("reward = " + reward);
            System.out.println("new Qvalue: " + Qvalue[oldRow][oldCol][direction]);
        }
        return true;

    }

//    @Override
//    public boolean doSteps(int count) {
//        for (int i = 0; i < count; i++) {
//            double rand = Math.random();
//            if (rand < greedyProb) {// act greedily
//                if (!moveToDir(getOptimalDir(gw.getCurRow(), gw.getCurCol()))) {
//                    return false;
//                }
//            } else { // act randomly
//                if (!moveToDir((int) (Math.random() * 4))) {
//                    return false;
//                }
//            }
//        }
//        return true;
//    }

    @Override
    public int getOptimalDir(int row, int col) {
        int startDir = (int) (Math.random() * 4);
        int bat = gw.getRemainingSteps();

        double bestVal = Qvalue[row][col][startDir];
        int bestDir = startDir;
        for (int dir = 1; dir < 4; dir++) {
            startDir = (startDir + 1) % 4;
            if (Qvalue[row][col][startDir] > bestVal) {
                bestVal = Qvalue[row][col][startDir];
                bestDir = startDir;
            }
        }
        return bestDir;
    }

    @Override
    public double getQvalue(int row, int col, int dir) {
        return Qvalue[row][col][dir];
    }

    @Override
    public boolean isOptimal(int row, int col, int dir) {
        return isOptimal[row][col][dir];
    }

    private void updateRanges() {
        for (int i = 0; i < Qvalue.length; i++) {
            for (int j = 0; j < Qvalue[i].length; j++) {
                for (int k = 0; k < Qvalue[i][j].length; k++) {

                    double value = Qvalue[i][j][k];
                    highest = value > highest ? value : highest;
                    lowest = value < lowest ? value : lowest;

                }
            }
        }
        range = highest - lowest;
    }

    private void updateOptimal() {
        double[] qvalues = Qvalue[gw.getCurRow()][gw.getCurCol()];
        double big = qvalues[0];
        for (int i = 0; i < qvalues.length; i++) {
            if (qvalues[i] > big) {
                big = qvalues[i];
            }
        }
        gw.getLocation(gw.getCurRow(), gw.getCurCol()).setLocationValue(big);
        for (int i = 0; i < qvalues.length; i++) {
            isOptimal[gw.getCurRow()][gw.getCurCol()][i] = qvalues[i] == big;
        }
    }

    @Override
    public void setHighestLV(boolean b) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
