/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Simulator;

import java.util.LinkedList;
import static Simulator.Vehicle.EAST;
import static Simulator.Vehicle.NORTH;
import static Simulator.Vehicle.SOUTH;
import static Simulator.Vehicle.WEST;

/**
 *
 * @author KSEET_000
 */
public class ConscientiousReactiveAlgorithm {

    private LinkedList<Vehicle> vehicles;
    private GridMap virtualMap;
    //private boolean skippedTurn = false;

    public ConscientiousReactiveAlgorithm(LinkedList<Vehicle> vehicles, GridMap current_map) {
        this.vehicles = vehicles;
        this.virtualMap = current_map;
    }

    public void run() {

        for (Vehicle vehicle : vehicles) {
            //int turns = speedVariationLogic(vehicle);
            int turns = vehicle.getMaxSpeed(); //the vehicle speed determines the number of turns it has to move.
            boolean skippedTurn = vehicle.skippedTurn();

            // If vehicle have skipped its turn in the last iteration, allow it to run this time.
            if ((turns == 0) && skippedTurn) {
                turns = 1;
                vehicle.setSkippedTurn(false);
            }

            if (turns == 0) {
                // Vehicle will skip its turn in this iteration
                vehicle.setSkippedTurn(true);
            }

            while (turns != 0) {
                Node currNode = virtualMap.getNode(vehicle.getXCoordinate(), vehicle.getYCoordinate());

                int xCoordinate = vehicle.getXCoordinate();
                int yCoordinate = vehicle.getYCoordinate();
                int direction = vehicle.getDirection();

                Node nextNode;
                Node leftNode;
                Node rightNode;

                switch (direction) {
                    case NORTH:
                        nextNode = virtualMap.getNode(xCoordinate, yCoordinate - 1);
                        leftNode = virtualMap.getNode(xCoordinate - 1, yCoordinate);
                        rightNode = virtualMap.getNode(xCoordinate + 1, yCoordinate);
                        break;
                    case EAST:
                        nextNode = virtualMap.getNode(xCoordinate + 1, yCoordinate);
                        leftNode = virtualMap.getNode(xCoordinate, yCoordinate - 1);
                        rightNode = virtualMap.getNode(xCoordinate, yCoordinate + 1);
                        break;
                    case SOUTH:
                        nextNode = virtualMap.getNode(xCoordinate, yCoordinate + 1);
                        leftNode = virtualMap.getNode(xCoordinate + 1, yCoordinate);
                        rightNode = virtualMap.getNode(xCoordinate - 1, yCoordinate);
                        break;
                    case WEST:
                        nextNode = virtualMap.getNode(xCoordinate - 1, yCoordinate);
                        leftNode = virtualMap.getNode(xCoordinate, yCoordinate + 1);
                        rightNode = virtualMap.getNode(xCoordinate, yCoordinate - 1);
                        break;
                    default:
                        nextNode = null;
                        leftNode = null;
                        rightNode = null;
                        break;
                }

                double currentTime = System.currentTimeMillis();

                double nextPriority = currentTime - nextNode.lastVisited();
                double leftPriority = currentTime - leftNode.lastVisited();
                double rightPriority = currentTime - rightNode.lastVisited();

                if ((nextNode.isWall()) || nextNode.isOccupied()) {
                    nextPriority = 0;
                }
                if (leftNode.isWall() || leftNode.isOccupied()) {
                    leftPriority = 0;
                }
                if (rightNode.isWall() || rightNode.isOccupied()) {
                    rightPriority = 0;
                }
                if (leftPriority == 0 && rightPriority == 0 && nextPriority == 0) {
                    //do nothing. all of possible locations are blocked.
                } else {
                    if (leftPriority > nextPriority && leftPriority >= rightPriority) {
                        vehicle.turnLeft();
                    } else if (rightPriority > nextPriority && rightPriority > leftPriority) {
                        vehicle.turnRight();
                    } else {
                        if (!(nextPriority == 0)) {
                            vehicle.move();
                            virtualMap.updateMovement(vehicle);
                        }
                    }
                }
                turns--;
            }
        }
    }
}
