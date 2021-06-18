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
public class Utils {
    
    /**
     * mapped einen Wert in einem Wertebereich auf einen entsprechenden Wert in einem anderen Wertebereich.
     * @param value Der Wert.
     * @param start1 Beginn des bisherigen Intervalls.
     * @param stop1 Ende des bisherigen Intervalls.
     * @param start2 Beginn des neuen Intervalls.
     * @param stop2 Ende des neuen Intervalls.
     * @return Den entsprechenden Wert im neuen Wertebereich.
     */
    static public final double mapToInterval(double value, double start1, double stop1, double start2, double stop2) {
        double outgoing = start2 + (stop2 - start2) * ((value - start1) / (stop1 - start1));
        return outgoing;
    }
    
}
