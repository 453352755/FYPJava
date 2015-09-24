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
public abstract class Algorithm {

    protected double discount = 0.9;
    protected boolean alphaFixed = false;
    protected double greedyProb = 0.8;
    protected double alpha = 0.5;
    protected boolean tracing = false;
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
            double rand = Math.random();
            if (rand < greedyProb) {// act greedily
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

    public void setTracing(boolean tracing) {
        this.tracing = tracing;
    }
}
