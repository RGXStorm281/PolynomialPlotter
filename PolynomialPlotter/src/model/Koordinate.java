/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author robinepple
 */
public class Koordinate {
    
    private double x;
    private double y;

    public Koordinate(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "Koordinate[x: "+this.x+", y: "+this.y+"]";
    }

    public double distanceTo(Koordinate other){
        return Math.sqrt(Math.pow(other.x-this.x,2) + Math.pow(other.y-this.y,2));
    }
}
