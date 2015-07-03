/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Simulator;

import java.util.LinkedList;

/**
 *
 * @author KSEET_000
 */
public class Node {
    private int x,y;
    private double lastVisited;
    private double timeTaken; //the time taken between the two last visits
    private double fireTimestamp; //timestamp the moment the node caught fire
    private double fireDiscoveryTime;   //time taken to discover the fire
    private boolean isVisited;
    private boolean isWall;
    private boolean isOccupied;
    private boolean isReturnNode;
    private boolean isOnFire;
    private boolean isHotspot;
    private double fireProbability; //the % of how fire-prone the node is
    private LinkedList<Vehicle> vehicles = new LinkedList<Vehicle>();   //each node may contain multiple vehicles (i.e. the starting node)
    
    public Node(int x, int y, boolean isWall, double fireProbability) {
        this.x = x;
        this.y = y;
        this.isWall = isWall;
        this.isOccupied = false;
        this.isReturnNode = false;
        this.isVisited = false;
        this.lastVisited = System.currentTimeMillis();
        this.timeTaken = 0;
        this.isOnFire = false;
        this.isHotspot = false;
        this.fireTimestamp = 0;
        this.fireProbability = fireProbability;
    }
    
    public double lastVisited() {   //returns the timestamp of the node last visited by a vehicle
        return lastVisited;
    }
    
    public int getX() {
        return this.x;
    }
    
    public int getY() {
        return this.y;
    }
    
    public void setHotspot(boolean hotspot) {
        this.isHotspot = hotspot;
    }
    
    public boolean isHotspot() {
        return this.isHotspot;
    }
    
    public void setFireProbability(double fireProbability) {
        this.fireProbability = fireProbability;
    }
    
    public double getFireProbability() {
        return this.fireProbability;
    }
    
    public void setWall(boolean isWall) {
        this.isWall = isWall;
    }
    
    public boolean isWall() {
        return this.isWall;
    }
    
    public void add(Vehicle vehicle) {
        this.vehicles.add(vehicle);
        this.isVisited = true;
        this.isOccupied = true;
        timestamp();
    }
    
    public void remove(Vehicle vehicle) {
        this.vehicles.remove(vehicle);
        if(this.vehicles.isEmpty())
            this.isOccupied = false;
        else
            this.isOccupied = true;
        
        timestamp();
    }
    
    private void timestamp() {
        //timestamp on when this node was last visited by a vehicle
        double currTime = System.currentTimeMillis();
        this.timeTaken = currTime-this.lastVisited;
        this.lastVisited = currTime;
    }
    
    public double timeTaken() {
        return this.timeTaken;
    }
    
    public boolean isOccupied() {
        return this.isOccupied;
    }
    
    public boolean isVisited() {
        return this.isVisited;
    }
    
    public void setVisited(boolean bool) {
        this.isVisited = bool;
    }
    
    public void setReturnNode(boolean isReturnNode) {
        this.isReturnNode = isReturnNode;
    }
    
    public boolean isReturnNode() {
        return this.isReturnNode;
    }
    
    public void setOnFire(boolean onFire) {
        if(!this.isOnFire) {
            this.isOnFire = true;
            this.fireTimestamp = System.currentTimeMillis();
        }
        else {
            this.isOnFire=false;
            this.fireDiscoveryTime = System.currentTimeMillis() - this.fireTimestamp;
        }
    }
    
    public boolean isOnFire() {
        return this.isOnFire;
    }
    
    public double getFireDiscoveryTime() {
        double discoveryTime = this.fireDiscoveryTime;
        this.fireDiscoveryTime = 0; //reset the timer
        return discoveryTime;
    }
    
    public void printCoordinates() {
        System.out.println("xCoordinate: " + this.getX());
        System.out.println("YCoordinate: " + this.getY());
    }
    
    public LinkedList<Vehicle> getVehicles() {
        return this.vehicles;
    }
            
}
