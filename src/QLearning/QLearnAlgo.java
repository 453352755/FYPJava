package QLearning;

/**
 *
 * @author Dong Yubo
 */
public class QLearnAlgo implements Algorithm {

    private double discount = 0.9;
    private boolean alphaFixed = false;
    private double greedyProb = 0.8;
    private double alpha = 0.5;
    private boolean tracing = false;
    private GridWorld gw;

    private double[][][] Qvalue;
    private int[][][] visited;
    private double highest = 0, lowest = 0, range = 0;
    private boolean[][][] isOptimal;

    public QLearnAlgo(GridWorld gridWorld) {
        this.gw = gridWorld;
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

    public double getDiscount() {
        return discount;
    }

    public boolean isAlphaFixed() {
        return alphaFixed;
    }

    public double getGreedyProb() {
        return greedyProb;
    }

    public double getAlpha() {
        return alpha;
    }

    public boolean isTracing() {
        return tracing;
    }

    public GridWorld getGridWorld() {
        return gw;
    }

    @Override
    public void setDiscount(double discount) {
        this.discount = discount;
    }

    @Override
    public void setAlphaFixed(boolean alphaFixed) {
        this.alphaFixed = alphaFixed;
    }

    @Override
    public void setGreedyProb(double greedyProb) {
        this.greedyProb = greedyProb;
    }

    @Override
    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    @Override
    public void setTracing(boolean tracing) {
        this.tracing = tracing;
    }

    @Override
    public void setGridWorld(GridWorld gridWorld) {
        this.gw = gridWorld;
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

        Qvalue[oldRow][oldCol][direction] = (1 - alpha)
                * Qvalue[oldRow][oldCol][direction] + alpha * newDatum;
        updateRanges();
        updateOptimal();

        if (tracing) {
            System.out.println("reward = " + reward);
            System.out.println("new Qvalue: " + Qvalue[oldRow][oldCol][direction]);
        }
        return true;

    }

//    public double locValue(int row, int col) {
//        double val = gridWorld.location[row][col].qvalues[3];
//        for (int i = 2; i >= 0; i--) {
//            if (gridWorld.location[row][col].qvalues[i] > val) {
//                val = gridWorld.location[row][col].qvalues[i];
//            }
//        }
//        return val;
//    }
    @Override
    public boolean doSteps(int count) {

//        for (int i = 0; i < count; i++) {
//            double rand = Math.random();
//            if (rand < greedyProb) {// act greedily
//                int startDir = (int) (Math.random() * 4);
//                double bestVal = gridWorld.getLocation(gridWorld.getCurRow(),
//                        gridWorld.getCurCol()).getQvalue(startDir);
//                int bestDir = startDir;
//                for (int dir = 1; dir < 4; dir++) {
//                    startDir = (startDir + 1) % 4;
//                    if (gridWorld.getLocation(gridWorld.getCurRow(),
//                            gridWorld.getCurCol()).getQvalue(startDir) > bestVal) {
//                        bestVal = gridWorld.getLocation(gridWorld.getCurRow(),
//                                gridWorld.getCurCol()).getQvalue(startDir);
//                        bestDir = startDir;
//                    }
//                }
//                if (!moveToDir(bestDir)) {
//                    return false;
//                }
//            } else { // act randomly
//                if (!moveToDir((int) (Math.random() * 4))) {
//                    return false;
//                }
//            }
//        }
//        return true;
        for (int i = 0; i < count; i++) {
            double rand = Math.random();
            if (rand < greedyProb) {// act greedily
                int startDir = (int) (Math.random() * 4);
                int row = gw.getCurRow();
                int col = gw.getCurCol();
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

                if (!moveToDir(bestDir)) {
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
    public double getRange() {
        return range;
    }

    @Override
    public double getLowest() {
        return lowest;
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

}
