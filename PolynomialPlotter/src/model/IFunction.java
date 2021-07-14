/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.awt.Color;

public interface IFunction {
    
    public Koordinate calculate(double xValue);
    
    public void setColor(Color color);
    
    public Color getColor();
    
    public void setVisible(boolean visible);
    
    public boolean isVisible();
    
}
