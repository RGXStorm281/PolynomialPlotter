/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.awt.Point;


public interface IFunction {
    
    public Koordinate[] calculate(double start, double end, double step);
    
}
