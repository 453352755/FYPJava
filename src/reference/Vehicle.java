/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Simulator;

import java.awt.Color;

/**
 *
 * @author KSEET_000
 */
public class Vehicle {

    static final int NORTH = 1;
    static final int EAST = 2;
    static final int SOUTH = 3;
    static final int WEST = 4;

    private int curr_x;  //current x-coordinate of the vehicle
    private int curr_y;  //current y-coordinate of the vehicle
    private int prev_x;  //previous x-coordinate of the vehicle
    private int prev_y;  //previous y-coordinate of the vehicle
    private int direction; //direction of the vehicle facing
    private int reverse_direction; //store the previous direction the vehicle was facing
    private boolean isReversed; //a boolean flag to indicate whether it is on reverse mode.
    private boolean skippedTurn;
    private int maxSpeed; //Speed of vehicle (variable performance between different vehicles)
    private final Color color;

    public Vehicle(int x, int y, int speed, int direction, Color color) {
        //initiate the starting point of the Vehicle
        this.curr_x = x;
        this.curr_y = y;
        this.prev_x = x;
        this.prev_y = y;
        this.maxSpeed = speed;
        this.direction = direction;
        this.isReversed = false;
        this.color = color;
        this.skippedTurn = false;
    }

    public void move() {
        this.prev_x = curr_x;
        this.prev_y = curr_y;

        if (direction == NORTH) {
            this.curr_y--;
            reverse_direction = SOUTH;
        } else if (direction == SOUTH) {
            this.curr_y++;
            reverse_direction = NORTH;
        } else if (direction == EAST) {
            this.curr_x++;
            reverse_direction = WEST;
        } else if (direction == WEST) {
            this.curr_x--;
            reverse_direction = EAST;
        }
    }

    public void reverseDirection() {
        this.isReversed = !this.isReversed;
        if(direction==NORTH) {
            this.direction = SOUTH;
        }
        else if(direction==SOUTH) {
            this.direction = NORTH;
        }
        else if(direction==EAST) {
            this.direction = WEST;
        }
        else if(direction==WEST) {
            this.direction = EAST;
        }
    }

    public void turnLeft() {
        if (direction == NORTH) {
            this.direction = WEST;
        } else if (direction == SOUTH) {
            this.direction = EAST;
        } else if (direction == EAST) {
            this.direction = NORTH;
        } else if (direction == WEST) {
            this.direction = SOUTH;
        }
    }

    public void turnRight() {
        reverse_direction = direction;
        if (direction == NORTH) {
            this.direction = EAST;
        } else if (direction == SOUTH) {
            this.direction = WEST;
        } else if (direction == EAST) {
            this.direction = SOUTH;
        } else if (direction == WEST) {
            this.direction = NORTH;
        }
    }

    public void setDirection(int new_direction) {
        reverse_direction = direction;
        this.direction = new_direction;
    }

    public void resetPosition() {
        this.curr_x = 1;
        this.curr_y = 1;
        this.prev_x = 1;
        this.prev_y = 1;
        this.isReversed = false;
        this.direction = SOUTH;
    }
    
    public void setXCoordinate(int x) {
        this.curr_x = x;
    }
    
    public void setYCoordinate(int y) {
        this.curr_y = y;
    }
    
    public int getXCoordinate() {
        return this.curr_x;
    }

    public int getYCoordinate() {
        return this.curr_y;
    }

    public int getPrevXCoordinate() {
        return this.prev_x;
    }

    public int getPrevYCoordinate() {
        return this.prev_y;
    }

    public int getDirection() {
        return this.direction;
    }
    
    public int getMaxSpeed() {
        return this.maxSpeed;
    }

    public void setMaxSpeed(int maxSpeed) {
        this.maxSpeed = maxSpeed;
    }
    
    public Color getColor() {
        return this.color;
    }
    
    public boolean isReversed() {
        return this.isReversed;
    }
    
    public boolean skippedTurn() {
        return this.skippedTurn;
    }
    
    public void setSkippedTurn(boolean b) {
        this.skippedTurn = b;
    }
}
