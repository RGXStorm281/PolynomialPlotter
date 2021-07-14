/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.awt.Color;

import model.IFunction;

/**
 *
 * @author robinepple
 */
public class FunctionManager {

    private HashMap<String, IFunction> functionMap;
    
	private final IParser[] parserArray;
	
    public FunctionManager(IParser[] availableParsers){
    	// Initialisiert FunctionMap
    	functionMap = new HashMap<>();
        parserArray = availableParsers;
    }
    
    /**
     * Prüft, ob functionName bereits existiert.
     * @param functionName
     * @return functionName hinterlegt.
     */
    private boolean functionNameExists(String functionName) {
    	return this.functionMap.containsKey(functionName);
    }

    /**
     * Gibt alle geparsten functions aus.
     * @return functionMap.
     */
    public ArrayList<IFunction> getFunctionList() {
        return new ArrayList<IFunction>(this.functionMap.values());
    }
    
    /**
     * Gibt die Funktion mit dem entsprechenden Namen zurück, sofern vorhanden.
     * @param functionName
     * @return Die zugehrige Funktion oder null.
     */
    public IFunction getFunction(String functionName){
        if(functionNameExists(functionName)){
            return this.functionMap.get(functionName);
        }
        return null;
    }
    
    public void toggleFunctionVisible(String functionName){
        if(!functionNameExists(functionName)){
            return;
        }
        var function = this.functionMap.get(functionName);
        function.setVisible(!function.isVisible());
    }
    
    /**
     * Löscht die function mit dem functionName.
     * @param functionName
     * @return function gelöscht.
     */
    public boolean delete(String functionName) {
    	
    	if(functionName != null
    			&& this.functionNameExists(functionName)) {
    		this.functionMap.remove(functionName);
    		return true;
    	}
    	
    	return false;
    }
    
    /**
     * Speichert die parsed function in der internen FunctionMap.Erteilt neuen functionName, wenn dieser bisher null war.
     * @param functionName Name der function.
     * @param functionString zu parsender String.
     * @return (neuen) functionName.
     * @throws FunctionParsingException
     */
    public String parseAndAddFunction(String functionName, String functionString, Color lineColor) throws FunctionParsingException{
    	
        if(functionName == null) {
        	for(char tempFunctionName = 'a'; tempFunctionName <= 'x'; tempFunctionName++) {

        		if(!this.functionNameExists(""+tempFunctionName)) {
        			functionName = ""+tempFunctionName;
        			break;
        		}
        	}
        	
        	if(functionName == null) {
        		throw new FunctionParsingException(ParsingResponseCode.NoMoreNamesAvailable, "Es existiert kein weiterer FunctionName (a-z).");
        	}
        }
        
        boolean added = this.add(functionName, functionString, lineColor);
        if(added) {
        	return functionName;
        }
        else {
        	return null;
        }
    }
    
    /** 
     * Fügt die mitgegebene function zu der functionMap hinzu.
     * Solange der FunctionName noch nicht vergeben wurde und die function ermittelt werden konnte!
     * @param functionString
     * @return Function wurde gespeichert.
     */
    private boolean add(String functionName, String functionString, Color lineColor) {
    	
    	if(!functionNameAddable(functionName)) {
    		return false;
    	}
    	
    	IFunction function = this.parse(functionString);
    	if(function == null) {
    		return false;
    	}
        function.setColor(lineColor);
    	
		this.functionMap.put(functionName, function);
		return true;
    }
    
    /** 
     * Fügt die mitgegebene function zu der functionMap hinzu.
     * Solange der FunctionName noch nicht vergeben wurde und die function nicht null ist!
     * @param functionName Der Name der Funktion.
     * @param function Das Funktionsobjekt.
     * @return True, wenn die Funktion erfolgreich gespeichert wurde.
     */
    public boolean add(String functionName, IFunction function) {
        // Funktionsname okay?
        if(!functionNameAddable(functionName)) {
            return false;
    	}
        // Funktion okay?
        if(function == null) {
            return false;
        }
        // Dann hinzufügen.
        this.functionMap.put(functionName, function);
        return true;
    }
    
    /**
     * prüft, ob der Funktionsname für eine neue Funktion valide ist.
     * @param functionName Der Funktionsname.
     * @return True, wenn der Name okay ist.
     */
    private boolean functionNameAddable(String functionName){

        if(this.functionNameExists(functionName)){
            return false;
        }
        
        return true;
    }
    
    /**
     * Versucht mit allen Parsern den Input zu parsen.
     * @param functionString inputString.
     * @return geparste function, oder null.
     */
    private IFunction parse(String functionString) {
    	for (IParser parser:
			parserArray) {
    		try {
    			return parser.parse(functionString);
    		}
    		catch (FunctionParsingException e) {
    			// TODO Loggen
    			System.out.println(e.getResponseCode() + " - " + e.getMessage());
    		}
    	}
    	
    	return null;
    }
}
