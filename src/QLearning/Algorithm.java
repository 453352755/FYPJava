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
public interface Algorithm {

    public void setGridWorld(GridWorld gw);

    public GridWorld getGridWorld();

    public double getRange();

    public double getLowest();

    public boolean moveToDir(int Left);

    public boolean doSteps(int i);

    public void setTracing(boolean b);

    public void setAlpha(double av);

    public void setAlphaFixed(boolean b);

    public void setDiscount(double dv);

    public void setGreedyProb(double gv);
    
    public void setHighestLV(boolean b);

    public double getQvalue(int row, int col, int dir);

    public boolean isOptimal(int row, int col, int dir);

    public int getOptimalDir(int row, int col);

}
