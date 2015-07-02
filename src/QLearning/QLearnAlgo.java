
package QLearning;

/**
 *
 * @author Dong Yubo
 */
public class QLearnAlgo {

    public double discount = 0.9;
    public boolean alphaFixed = false;
    public double greedyProb;
    public double alpha;
    public boolean tracing = false;
    public GridWorld gw;

    public QLearnAlgo(GridWorld gridWorld) {
        this.gw = gridWorld;
    }

    public void dostep(int direaction) {
        int oldRow = gw.curRow, oldCol = gw.curCol;
        double reward = gw.move(direaction);
        int newRow = gw.curRow, newCol = gw.curCol;

        // update Q values
        double newVal = locValue(newRow, newCol);
        double newDatum = reward + discount * newVal;
        gw.location[oldRow][oldCol].visits[direaction]++;
        if (!alphaFixed) {
            alpha = 1.0 / gw.location[oldRow][oldCol].visits[direaction];
        }
//	    alpha = 10.0/(9+visits[oldX][oldY][action]);

        if (tracing) {
            System.out.println("(" + oldRow + "," + oldCol + ") A=" + direaction + " R=" + reward
                    + " (" + newRow + "," + newCol + ") newDatum=" + newDatum);
            System.out.print("     Qold=" + gw.location[oldRow][oldCol].qvalues[direaction]
                    + " Visits=" + gw.location[oldRow][oldCol].visits[direaction]);
        }

        gw.location[oldRow][oldCol].qvalues[direaction]
                = (1 - alpha) * gw.location[oldRow][oldCol].qvalues[direaction]
                + alpha * newDatum;
        if (tracing) {
            System.out.println(" Qnew=" + gw.location[oldRow][oldCol].qvalues[direaction]);
        }
    }

    public double locValue(int row, int col) {
        double val = gw.location[row][col].qvalues[3];
        for (int i = 2; i >= 0; i--) {
            if (gw.location[row][col].qvalues[i] > val) {
                val = gw.location[row][col].qvalues[i];
            }
        }
        return val;
    }

    public void doSteps(int count) {

        for (int i = 0; i < count; i++) {
            double rand = Math.random();
            if (rand < greedyProb) {// act greedily
                int startDir = (int) (Math.random() * 4);
                double bestVal = gw.location[gw.curRow][gw.curCol].qvalues[startDir];
                int bestDir = startDir;
                for (int dir = 1; dir < 4; dir++) {
                    startDir = (startDir + 1) % 4;
                    if (gw.location[gw.curRow][gw.curCol].qvalues[startDir] > bestVal) {
                        bestVal = gw.location[gw.curRow][gw.curCol].qvalues[startDir];
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
