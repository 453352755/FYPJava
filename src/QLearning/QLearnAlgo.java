
package QLearning;

/**
 *
 * @author Dong Yubo
 */
public class QLearnAlgo {

    private double discount = 0.9;
    private boolean alphaFixed = false;
    private double greedyProb;
    private double alpha;
    private boolean tracing = false;
    private GridWorld gridWorld;

    public QLearnAlgo(GridWorld gridWorld) {
        this.gridWorld = gridWorld;
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
        return gridWorld;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public void setAlphaFixed(boolean alphaFixed) {
        this.alphaFixed = alphaFixed;
    }

    public void setGreedyProb(double greedyProb) {
        this.greedyProb = greedyProb;
    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    public void setTracing(boolean tracing) {
        this.tracing = tracing;
    }

    public void setGridWorld(GridWorld gridWorld) {
        this.gridWorld = gridWorld;
    }

    public void dostep(int direction) {
        int oldRow = gridWorld.getCurRow(), oldCol = gridWorld.getCurCol();
        double reward = gridWorld.move(direction);
        int newRow = gridWorld.getCurRow(), newCol = gridWorld.getCurCol();

        // update Q values
        //double newVal = locValue(newRow, newCol);
        double newVal = gridWorld.getLocation(newRow, newCol).getLocationValue();
        double newDatum = reward + discount * newVal;
        gridWorld.getLocation(oldRow,oldCol).visited(direction);
        if (!alphaFixed) {
            alpha = 1.0 / gridWorld.getLocation(oldRow,oldCol).getVisit(direction);
        }
//	    alpha = 10.0/(9+visits[oldX][oldY][action]);

        if (tracing) {
            System.out.println("(" + oldRow + "," + oldCol + ") A=" + direction + " R=" + reward
                    + " (" + newRow + "," + newCol + ") newDatum=" + newDatum);
            System.out.print("     Qold=" + gridWorld.getLocation(oldRow,oldCol).getQvalue(direction)
                    + " Visits=" + gridWorld.getLocation(oldRow,oldCol).getVisit(direction));
        }

        gridWorld.getLocation(oldRow,oldCol).setQvalue(direction, (1 - alpha) * 
                gridWorld.getLocation(oldRow,oldCol).getQvalue(direction)+ alpha * newDatum);
        if (tracing) {
            System.out.println(" Qnew=" + gridWorld.getLocation(oldRow,oldCol).getQvalue(direction));
        }
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

    public void doSteps(int count) {

        for (int i = 0; i < count; i++) {
            double rand = Math.random();
            if (rand < greedyProb) {// act greedily
                int startDir = (int) (Math.random() * 4);
                double bestVal = gridWorld.getLocation()[gridWorld.getCurRow()][gridWorld.getCurCol()].getQvalue(startDir);
                int bestDir = startDir;
                for (int dir = 1; dir < 4; dir++) {
                    startDir = (startDir + 1) % 4;
                    if (gridWorld.getLocation()[gridWorld.getCurRow()][gridWorld.getCurCol()].getQvalue(startDir) > bestVal) {
                        bestVal = gridWorld.getLocation()[gridWorld.getCurRow()][gridWorld.getCurCol()].getQvalue(startDir);
                        bestDir = startDir;
                    }
                }
                dostep(bestDir);
            } else { // act randomly
                dostep((int) (Math.random() * 4));
            }
        }
    }

}
