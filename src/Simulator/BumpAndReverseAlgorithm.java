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
import java.awt.Color;

/**
 *
 * @author KSEET_000
 */
public class BumpAndReverseAlgorithm {

    private LinkedList<Vehicle> vehicles;
    private LinkedList<Node> plannedPath = new LinkedList<>();
    private GridMap virtualMap;

    public BumpAndReverseAlgorithm(LinkedList<Vehicle> vehicles, GridMap current_map) {
        this.vehicles = vehicles;
        this.virtualMap = current_map;
        setupCyclicPath();
    }

    public void run() {

        for (Vehicle vehicle : vehicles) {
            int turns = speedVariationLogic(vehicle);
            //int turns = vehicle.getMaxSpeed(); //the vehicle speed determines the number of turns it has to move.
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
                Node nextNodeToVisit = getNextNode(vehicle, currNode);

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

                if (nextNodeToVisit.isOccupied()) {
                    vehicle.reverseDirection();
                    if (currNode == plannedPath.peekFirst()) {
                        vehicle.setDirection(EAST);
                    }
                    // reverse the direction of the other vehicle as well
                    // reverse the direction if and only if that vehicle is facing the current vehicle
                    Vehicle tmp = nextNodeToVisit.getVehicles().peekFirst();
                    if (currNode == getNextNode(tmp, virtualMap.getNode(tmp.getXCoordinate(), tmp.getYCoordinate()))) {
                        tmp.reverseDirection();
                    }
                } else {
                    if (nextNodeToVisit == nextNode) {
                        vehicle.move();
                        virtualMap.updateMovement(vehicle);
                    } else if (leftNode == nextNodeToVisit) {
                        vehicle.turnLeft();
                    } else if (rightNode == nextNodeToVisit) {
                        vehicle.turnRight();
                    }
                }
                turns--;
            }
        }
    }

    //a private method to setup the path of the cyclic algorithm (forming a hamiltonian path)
    private void setupCyclicPath() {

        Vehicle setupVehicle = new Vehicle(1, 1, 1, SOUTH, Color.BLUE);
        Node currNode;
        Node startNode = virtualMap.getNode(1, 1);
        Node keyNode = virtualMap.getNode(virtualMap.getXDimension() - 2, 2);
        setReturnPath(1, 1);

        do {
            //System.out.println("Setting up");
            if (keyNode.isVisited()) {
                clearReturnPath(1, 1);
            }

            int xCoordinate = setupVehicle.getXCoordinate();
            int yCoordinate = setupVehicle.getYCoordinate();
            int direction = setupVehicle.getDirection();
            currNode = virtualMap.getNode(xCoordinate, yCoordinate);
            Node leftNode;
            Node rightNode;
            Node nextNode;

            if (!plannedPath.contains(currNode)) {
                plannedPath.add(currNode);
            }

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

            if (nextNode.isWall() || nextNode.isReturnNode()) {    //next node is a wall 
                if (leftNode.isWall()) {
                    setupVehicle.turnRight();
                } else if (rightNode.isWall()) {
                    setupVehicle.turnLeft();
                } else {  //both left and right node is not a wall
                    //turn the vehicle's direction to the next highest priority node
                    //compare the last visited timestamp for each node

                    double currentTime = System.currentTimeMillis();

                    if ((currentTime - leftNode.lastVisited()) > (currentTime - rightNode.lastVisited())) {
                        setupVehicle.turnLeft();
                    } else {
                        setupVehicle.turnRight();
                    }
                }
            } else {    //nextNode is empty, not a wall or a returnNode
                if (setupVehicle.getDirection() == NORTH || setupVehicle.getDirection() == SOUTH) {
                    setupVehicle.move();
                    virtualMap.updateMovement(setupVehicle);
                } else {  //vehicle is facing EAST or WEST
                    double currentTime = System.currentTimeMillis();

                    //compare the priorities of nextNode with left and right nodes
                    //this is done by comparing the timestamps of the visits on each nodes
                    if (leftNode.isWall() || leftNode.isReturnNode()) {
                        if ((currentTime - rightNode.lastVisited()) >= (currentTime - nextNode.lastVisited())) {
                            setupVehicle.turnRight();
                        } else {
                            setupVehicle.move();
                            virtualMap.updateMovement(setupVehicle);
                        }
                    } else if (rightNode.isWall() || rightNode.isReturnNode()) {
                        if ((currentTime - leftNode.lastVisited()) >= (currentTime - nextNode.lastVisited())) {
                            setupVehicle.turnLeft();
                        } else {
                            setupVehicle.move();
                            virtualMap.updateMovement(setupVehicle);
                        }
                    }
                }
            }
            currNode = virtualMap.getNode(setupVehicle.getXCoordinate(), setupVehicle.getYCoordinate());
            //currNode.printCoordinates();
        } while (currNode != startNode);
        //remove setupVehicle
        virtualMap.getNode(1, 1).remove(setupVehicle);
    }

    //a private method to allocate the path of the vehicles to return.
    //this is to prevent the vehicles from traversing to the return path
    //necessary for cyclic algorithm
    private void setReturnPath(int startX, int startY) {
        for (int i = startX; i < virtualMap.getXDimension() - 2; i++) {
            virtualMap.getNode(i, startY).setReturnNode(true);
        }
    }

    private void clearReturnPath(int startX, int startY) {
        for (int i = startX; i < virtualMap.getXDimension() - 2; i++) {
            virtualMap.getNode(i, startY).setReturnNode(false);
        }
    }

    private Node getNextNode(Vehicle vehicle, Node currNode) {
        if (!vehicle.isReversed()) {
            if (currNode == plannedPath.peekLast()) {
                return plannedPath.peekFirst();
            } else {
                return plannedPath.get(plannedPath.indexOf(currNode) + 1);
            }
        } else {
            if (currNode == plannedPath.peekFirst()) {
                return plannedPath.peekLast();
            } else {
                return plannedPath.get(plannedPath.indexOf(currNode) - 1);
            }
        }
    }

    private Vehicle searchFrontVehicle(Vehicle v) {
        Node node = null;
        int x = v.getXCoordinate();
        int y = v.getYCoordinate();
        boolean reversed = v.isReversed();

        // locate the vehicle node
        Node currNode = virtualMap.getNode(x, y);
        int index = plannedPath.indexOf(currNode);

        do {
            if (reversed) {
                // look backwards
                index--;
            } else {
                // look forwards
                index++;
            }
            if (index >= plannedPath.size()) {
                index = 0;
            } else if (index < 0) {
                index = plannedPath.size() - 1;
            }
            node = plannedPath.get(index);
        } while (!node.isOccupied());

        return node.getVehicles().getFirst();
    }
    
    private int distanceFromFrontVehicle(Vehicle v) {
        Node node = null;
        int x = v.getXCoordinate();
        int y = v.getYCoordinate();
        int distance = 0;
        boolean reversed = v.isReversed();

        // locate the vehicle node
        Node currNode = virtualMap.getNode(x, y);
        int index = plannedPath.indexOf(currNode);

        do {
            if (reversed) {
                // look backwards
                index--;
            } else {
                // look forwards
                index++;
            }
            if (index >= plannedPath.size()) {
                index = 0;
            } else if (index < 0) {
                index = plannedPath.size() - 1;
            }
            node = plannedPath.get(index);
            distance++;
        } while (!node.isOccupied());
        return distance;
    }

    private int distanceFromBackVehicle(Vehicle v) {
        Node node = null;
        int x = v.getXCoordinate();
        int y = v.getYCoordinate();
        int distance = 0;
        boolean reversed = v.isReversed();

        // locate the vehicle node
        Node currNode = virtualMap.getNode(x, y);
        int index = plannedPath.indexOf(currNode);

        do {
            if (reversed) {
                // look backwards
                index++;
            } else {
                // look forwards
                index--;
            }
            if (index >= plannedPath.size()) {
                index = 0;
            } else if (index < 0) {
                index = plannedPath.size() - 1;
            }
            distance++;
            node = plannedPath.get(index);
        } while (!node.isOccupied());
        return distance;
    }

    private int speedVariationLogic(Vehicle v) {
        Vehicle frontVehicle = searchFrontVehicle(v);

        if (frontVehicle.isReversed() == v.isReversed()) {
            // If the vehicle infront is travelling the same direction
            int frontNodeDist = distanceFromFrontVehicle(v);
            int backNodeDist = distanceFromBackVehicle(v);

            if (frontNodeDist > backNodeDist) {
                //v.setMaxSpeed(2);
                return v.getMaxSpeed();
            } else if (frontNodeDist < backNodeDist) {
                //v.setMaxSpeed(0);
                return (v.getMaxSpeed() - 1);
            } else {
                //v.setMaxSpeed(1);
                return v.getMaxSpeed();
            }
        } else {
            return v.getMaxSpeed();
        }
    }
}
