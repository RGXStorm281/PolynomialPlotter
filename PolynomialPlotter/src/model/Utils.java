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
     * @param startInitial Beginn des bisherigen Intervalls.
     * @param stopInitial Ende des bisherigen Intervalls.
     * @param startNew Beginn des neuen Intervalls.
     * @param stopNew Ende des neuen Intervalls.
     * @return Den entsprechenden Wert im neuen Wertebereich.
     */
    static public final double mapToInterval(double value, double startInitial, double stopInitial, double startNew, double stopNew) {
        double outgoing = startNew + (stopNew - startNew) * ((value - startInitial) / (stopInitial - startInitial));
        return outgoing;
    }
    
}
