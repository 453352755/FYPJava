package QLearning;

import static QLearning.GridWorld.Down;
import static QLearning.GridWorld.Left;
import static QLearning.GridWorld.Right;
import static QLearning.GridWorld.Up;

/**
 *
 * @author dong
 */
public abstract class Algorithm {

    protected double discount = 0.9;
    protected boolean alphaFixed = false;
    protected double greedyProb = 0.8;
    protected double softmaxT = 0.2;
    protected double alpha = 0.5;
    protected boolean tracing = false;
    protected boolean greedy = true;
    protected GridWorld gw;
    protected double highest = 0, lowest = 0, range = 0;

    public Algorithm(GridWorld gridWorld) {
        this.gw = gridWorld;
    }

    public void setGridWorld(GridWorld gridWorld) {
        this.gw = gridWorld;
    }

    public double getRange() {
        return range;
    }

    public double getLowest() {
        return lowest;
    }

    public GridWorld getGridWorld() {
        return gw;
    }

    public abstract boolean moveToDir(int dir);

    public abstract void setHighestLV(boolean b);

    public abstract double getQvalue(int row, int col, int dir);

    public abstract boolean isOptimal(int row, int col, int dir);

    public abstract int getOptimalDir(int row, int col);

    public abstract double[][] getQvalue(int row, int col);

    public boolean doSteps(int count) {
        for (int i = 0; i < count; i++) {
            if (greedy) {
                double rand = Math.random();
                if (rand < greedyProb) {//greedy
                    if (!moveToDir(getOptimalDir(gw.getCurRow(), gw.getCurCol()))) {
                        return false;
                    }
                } else { // act randomly
                    if (!moveToDir((int) (Math.random() * 4))) {
                        return false;
                    }
                }
            } else {//softmax
                double[] prob = new double[4];
                double sum = 0;
                for (int j = 0; j < 4; j++) {
                    prob[j] = Math.pow(Math.E, getQvalue(gw.getCurRow(), gw.getCurCol(), j) / softmaxT);
                    sum += prob[j];
                }
                for (int j = 0; j < 4; j++) {
                    prob[j] /= sum;
                }
                double[] cProb = new double[4];
                cProb[0] = prob[0];
                for (int j = 1; j < 4; j++) {
                    cProb[j] = cProb[j - 1] + prob[j];
                }
                double rand = Math.random();
                if (rand < cProb[Up]) {
                    moveToDir(Up);
                } else if (rand < cProb[Down]) {
                    moveToDir(Down);
                } else if (rand < cProb[Left]) {
                    moveToDir(Left);
                } else {
                    moveToDir(Right);
                }
            }
        }
        return true;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public void setAlphaFixed(boolean alphaFixed) {
        this.alphaFixed = alphaFixed;

    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    public void setGreedyProb(double greedyProb) {
        this.greedyProb = greedyProb;
    }

    public boolean isGreedy() {
        return greedy;
    }

    public void setGreedy(boolean greedy) {
        this.greedy = greedy;
    }

    public void setTracing(boolean tracing) {
        this.tracing = tracing;
    }
}
