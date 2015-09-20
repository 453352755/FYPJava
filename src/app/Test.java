package app;

import java.util.Random;

/**
 *
 * @author Dong Yubo
 */
public class Test {

    public static void main(String[] args) {
//        int y = 0, n = 0;
//        for (int i = 0; i < 10000; i++) {
//            if (getActualDirection(1) == 1) {
//                y++;
//            }
//
//        }
//        System.out.println(y);
        Random rand = new Random();
        double low = 0, high = 0;
        for (int i = 0; i < 10000000; i++) {
            double v = rand.nextGaussian();
            low = v < low ? v : low;
            high = v > high ? v : high;

        }
            System.out.println(low);
            System.out.println(high);
    }

    public static int getActualDirection(int d) {
        double rand = Math.random();
        if (rand < 0.7) {
            return d;
        } else {
            int r = d;
            while (r == d) {
                r = (int) (Math.random() * 4);
            }
            return r;
        }
    }

}
