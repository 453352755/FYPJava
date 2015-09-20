/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Simulator;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author KSEET_000
 */
public class GridMap {

    private static int[][] map;
    private static Node[][] nodeMap;
    private static int xDim, yDim;
    private static int currNumberOfFire;

    public GridMap(int xDimension, int yDimension) {
        map = new int[yDimension][xDimension];
        nodeMap = new Node[yDimension][xDimension];
        Node newNode;
        xDim = xDimension;
        yDim = yDimension;
        currNumberOfFire = 0;
        ArrayList<Node> nodeArrayList = new ArrayList<>();

        for (int y = 0; y < yDim; y++) {
            for (int x = 0; x < xDim; x++) {
                if ((x == 0) || (y == 0) || (x == (xDim - 1)) || (y == (yDim - 1))) {
                    map[y][x] = 1;            //1 represents wall or obstacle in that node
                    newNode = new Node(x, y, true, 0);
                    nodeMap[y][x] = newNode;
                } else {
                    map[y][x] = 0;
                    newNode = new Node(x, y, false, 0.01);
                    nodeMap[y][x] = newNode;
                }
                nodeArrayList.add(newNode);
            }
        }
        printMap();
    }

    //Update the entire map based iterating through the entire nodeMap.
    public void update() {
        currNumberOfFire = 0;
        for (int y = 0; y < yDim; y++) {
            for (int x = 0; x < xDim; x++) {
                if (nodeMap[y][x].isWall()) {
                    map[y][x] = 1;            //1 represents wall or obstacle in that node
                } else if (nodeMap[y][x].isOccupied()) {
                    map[y][x] = 2;
                } else if (nodeMap[y][x].isOnFire()) {
                    map[y][x] = 3;
                    currNumberOfFire++;
                } else {
                    map[y][x] = 0;
                }
            }
        }
    }

    //Remove vehilce from the map
    public void removeVehicle(Vehicle vehicle) {
        int xCoordinate = vehicle.getXCoordinate();
        int yCoordinate = vehicle.getYCoordinate();
        
        nodeMap[yCoordinate][xCoordinate].remove(vehicle);
    }
    
    //Similar to update(), but updates only the two concerned nodes.
    public void updateMovement(Vehicle vehicle) {
        int xCoordinate = vehicle.getXCoordinate();
        int yCoordinate = vehicle.getYCoordinate();
        int prev_XCoordinate = vehicle.getPrevXCoordinate();
        int prev_YCoordinate = vehicle.getPrevYCoordinate();

        nodeMap[prev_YCoordinate][prev_XCoordinate].remove(vehicle);
        nodeMap[yCoordinate][xCoordinate].add(vehicle);
        if (nodeMap[yCoordinate][xCoordinate].isOnFire()) {
            nodeMap[yCoordinate][xCoordinate].setOnFire(false);
            currNumberOfFire--;
        }

        map[yCoordinate][xCoordinate] = 2;
        if (nodeMap[prev_YCoordinate][prev_XCoordinate].isOccupied()) {
            map[prev_YCoordinate][prev_XCoordinate] = 2;
        } else {
            map[prev_YCoordinate][prev_XCoordinate] = 0;
        }
    }

    public Node getNode(int x, int y) {
        return nodeMap[y][x];
    }

    public Node getNode(int index) {
        int i = index;
        
        int y = (index/(xDim-2))+1;
        int x = (index%(xDim-2))+1;
        
        return nodeMap[y][x];
    }

    public int getNumberOfFire() {
        return this.currNumberOfFire;
    }

    public int[][] getMap() {
        return this.map;
    }

    public void printMap() {
        for (int[] row : map) {
            for (int node : row) {
                System.out.print(node);
            }
            System.out.println();
        }
    }

    public int checkNode(int x, int y) {
        if (nodeMap[y][x].isWall()) {
            return 1;
        } else if (nodeMap[y][x].isOccupied()) {
            return 2;
        } else {
            return 0;
        }
    }

    public int getXDimension() {
        return this.xDim;
    }

    public int getYDimension() {
        return this.yDim;
    }

    public int size() {
        return (this.xDim - 2) * (this.yDim - 2);
    }
    
    public void readMap() {
        //reads a txt file and generate the map
    }

}
