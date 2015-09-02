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
public class ModifiedAlgo implements Algorithm{
    
    private double discount = 1;
    private boolean alphaFixed = true;
    private double greedyProb = 0.8;
    private double alpha = 1;
    private boolean tracing = false;
    private GridWorld gridWorld;

    ModifiedAlgo(GridWorld gw) {
        this.gridWorld = gw;
    }

    @Override
    public void setGridWorld(GridWorld gw) {
        this.gridWorld = gw;
    }

    @Override
    public boolean moveToDir(int direction) {
        int oldRow = gridWorld.getCurRow(), oldCol = gridWorld.getCurCol();
        double reward = gridWorld.move(direction);
        if (reward == GridWorld.DeadPenalty) {
            return false;
        }
        int newRow = gridWorld.getCurRow(), newCol = gridWorld.getCurCol();

        double newVal = gridWorld.getLocation(newRow, newCol).getLocationValue();
        
        double newDatum = reward + discount * newVal;
        gridWorld.getLocation(oldRow, oldCol).visited(direction);
        if (!alphaFixed) {
            alpha = 1.0 / gridWorld.getLocation(oldRow, oldCol).getVisit(direction);
        }
//	    alpha = 10.0/(9+visits[oldX][oldY][action]);

        if (tracing) {
            System.out.println("(" + oldRow + "," + oldCol + ") A=" + direction + " R=" + reward
                    + " (" + newRow + "," + newCol + ") newDatum=" + newDatum);
            System.out.print("     Qold=" + gridWorld.getLocation(oldRow, oldCol).getQvalue(direction)
                    + " Visits=" + gridWorld.getLocation(oldRow, oldCol).getVisit(direction));
        }

        gridWorld.getLocation(oldRow, oldCol).setQvalue(direction, (1 - alpha)
                * gridWorld.getLocation(oldRow, oldCol).getQvalue(direction) + alpha * newDatum);
        if (tracing) {
            System.out.println(" Qnew=" + gridWorld.getLocation(oldRow, oldCol).getQvalue(direction));
        }
        return true;
    }

    @Override
    public boolean doSteps(int i) {
        throw new UnsupportedOperationException("Not supported yet."); 
//To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setTracing(boolean b) {
        throw new UnsupportedOperationException("Not supported yet."); 
//To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setAlpha(double av) {
        throw new UnsupportedOperationException("Not supported yet."); 
//To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setAlphaFixed(boolean b) {
        throw new UnsupportedOperationException("Not supported yet."); 
//To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setDiscount(double dv) {
        throw new UnsupportedOperationException("Not supported yet."); 
//To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setGreedyProb(double gv) {
        throw new UnsupportedOperationException("Not supported yet."); 
//To change body of generated methods, choose Tools | Templates.
    }
    
}
