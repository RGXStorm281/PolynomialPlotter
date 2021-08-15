package model;

import java.awt.Color;
import java.util.Arrays;

public class HornerFunction extends Function implements IFunction {
	
	private double[] hornerElements;
    
	public HornerFunction(String functionName, String displayString, double[] elements)
	{
        super(functionName, displayString);
		this.hornerElements = elements;
	}
    
	public HornerFunction(String functionName, String displayString, double[] elements, Color functionColor)
	{
        super(functionName, displayString, functionColor);
		this.hornerElements = elements;
	}
        
    /**
     * @inheritDoc 
     */
	@Override
	public Koordinate calculate(double xValue) {
		Koordinate koordinate;
		double yValue = hornerElements[hornerElements.length-1];
		
		for (int i = (hornerElements.length-2); i >= 0; i--)
		{
			yValue = yValue*xValue + hornerElements[i];
		}
		
		koordinate = new Koordinate(xValue, yValue);
		return koordinate;
	}
	
	/**
	 * 
	 */
	@Override
	public IFunction getAbleitung() {
        var ableitungName = getAbleitungName();
		if(hornerElements.length < 2) {
            var elements = new double[] {0};
			return new HornerFunction(ableitungName, createDisplayStringForElements(elements), elements);
		}
		
		double[] ableitung = new double[hornerElements.length - 1];
		for(int i = hornerElements.length - 1; i > 0; i--) {
			ableitung[i - 1] = i * hornerElements[i];
		}
		
		return new HornerFunction(ableitungName, createDisplayStringForElements(ableitung), ableitung);
	}
    
    /**
     * Gibt den Namen der Ableitung zurück.
     * @return Der Name der Ableitung.
     */
    private String getAbleitungName(){
        return functionName + "'";
    }
    
    /**
     * Erstellt einen Anzeige-String für eine Funktion im Schema "-x^2 + 3x - 2"
     * @param multipliers die Horner-Elemente (müssen so mitgegeben, damit die Funktion im Konstruktor aufgerufen werden kann).
     * @return Den Anzeige-String.
     */
    private String createDisplayStringForElements(double[] hElements){
        String display = "";
        
        for(int i = 0; i < hElements.length; i++){
            var currentMultiplier = hElements[i];
            var isLast = i == hElements.length-1;
            
            if(currentMultiplier == 0){
                continue;
            }
            
            // Betrag des Horner Elements nehmen, z.B. "5".
            String summand = Double.toString(Math.abs(currentMultiplier));
            // Dann x^n dahinter schreiben.
            if(i == 1){
                summand = summand + "x";
            }else if(i > 1){
                summand = summand + "x^" + i;
            }
            
            // Plus/Minus davor schreiben.
            if(currentMultiplier < 0){
                if(isLast){
                    summand = "-" + summand;
                }else{
                    summand = " - " + summand;
                }
            }else{
                if(!isLast){
                    summand = " + " + summand;
                }
            }
            
            // zusammen setzen.
            display = summand + display;
        }
        
        return display;
    }
	
	/**
	 * Gibt das HornerElement als String eines gerundeten Doubles zurück (5 Nachkommastellen).
	 */
	@Override
	public String toString() {
		double[] roundetHornerElements = new double[hornerElements.length];
		for(int i = 0; i < hornerElements.length; i++) {
			roundetHornerElements[i] = (double) Math.round(hornerElements[i] * 100000) / 100000;
		}
		
		return Arrays.toString(roundetHornerElements);
	}
}
