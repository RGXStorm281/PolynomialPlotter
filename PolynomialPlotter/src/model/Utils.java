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

    /**
     * 
     * @param value value to map
     * @param interval1 interval to map from
     * @param interval2 interval to map to
     * @return
     */
    static public final double mapToInterval(double value,Tuple<Double,Double> interval1, Tuple<Double,Double> interval2) {
        double outgoing = interval2.getItem1() + (interval2.getItem2()  - interval2.getItem1()) * ((value - interval1.getItem1()) / (interval1.getItem2() - interval1.getItem1()));
        return outgoing;
    }

    static public final int mapToDimension(double value,Tuple<Double,Double> interval1, Tuple<Integer,Integer> interval2) {
        double outgoing = interval2.getItem1() + (interval2.getItem2()  - interval2.getItem1()) * ((value - interval1.getItem1()) / (interval1.getItem2() - interval1.getItem1()));
        return (int)outgoing;
    }
    
    public static double mapToInterval(int value, Tuple<Double, Double> interval1, Tuple<Integer, Integer> interval2) {
        double outgoing = interval2.getItem1() + (interval2.getItem2()  - interval2.getItem1()) * ((value - interval1.getItem1()) / (interval1.getItem2() - interval1.getItem1()));
        return outgoing;
    }
/**
 * Beschränkt einen Wer auf ein Interval
 * @param value
 * @param interval
 * @return
 */
    public static double clamp(double value, Tuple<Double, Double>interval){
        if(value<=interval.getItem1())return interval.getItem1();
        else if(value >=interval.getItem2())return interval.getItem2();
        else return value;
    }
    
    public static int clampToDimensions(double value, Tuple<Integer, Integer>interval){
        if(value<=interval.getItem1())return interval.getItem1();
        else if(value >=interval.getItem2())return interval.getItem2();
        else return (int)value;
    }
}
