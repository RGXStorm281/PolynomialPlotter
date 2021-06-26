/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import java.util.concurrent.Callable;
import model.IFunction;
import model.Koordinate;

/**
 * Klasse, um Funktionswerte parallelisiert zu berechnen.
 * @author robinepple
 */
public class FunctionCalculationThread implements Callable<Koordinate> {
    
    private final IFunction function;
    private final double x;

    public FunctionCalculationThread(IFunction function, double x) {
        this.function = function;
        this.x = x;
    }

    /**
     * Berechnet den Funktionswert für den X-Wert und gibt das Wertepaar als Koordinate zurück.
     * @return Die Koordinate.
     */
    @Override
    public Koordinate call() {
        // Threadsicherheit durch nur lesende Zugriffe und lokale Variablen.
        return function.calculate(x);
    }
    
}
