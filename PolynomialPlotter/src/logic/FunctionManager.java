/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;
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
    
    /**
     * Ändert die Sichtbarkeit der Function zu (un-)sichtbar.
     * @param functionName
     */
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
        	functionName = getAddableFunctionName();
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
     * Leitet die mitgegebene Function ab, fügt diese als neue Function der Sammlung hinzu.
     * @param functionName
     * @return AbleitungFunctionName
     * @throws FunctionParsingException
     */
    public String functionAbleitenForFunctionName(String functionName) throws FunctionParsingException {
    	
    	IFunction ableitung =  getFunction(functionName).getAbleitung();
    	if(!functionNameAddable(ableitung.getFunctionName())) {
    		throw new FunctionParsingException(ParsingResponseCode.NameAlreadyTaken, "Die Ableitungsfunktion " + ableitung.getFunctionName() + " existiert bereits.");
    	}
    	
    	boolean ableitungAdded = this.add(ableitung.getFunctionName(), ableitung);
    	if(!ableitungAdded) {
    		throw new FunctionParsingException(ParsingResponseCode.AbleitenFailed, "Es ist ein unbekannter Fehler beim Hinzufügen der abgeleiteten Function aufgetreten.");
    	}

		return ableitung.getFunctionName();
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
    	
    	IFunction function = this.parse(functionName, functionString);
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
    private IFunction parse(String functionName, String functionString) {
    	for (IParser parser:
			parserArray) {
    		try {
    			return parser.parse(functionName, functionString);
    		}
    		catch (FunctionParsingException e) {
    			Logger.getGlobal().severe(e.getResponseCode() + " - " + e.getMessage());
    		}
    	}
    	
    	return null;
    }
    
    /**
     * Versucht der Function einen Namen zu geben, der noch nicht hinterlegt ist.
     * @return unbenutzter FunctionName.
     * @throws FunctionParsingException
     */
    private String getAddableFunctionName() throws FunctionParsingException {
    	
    	for(char tempFunctionName = 'a'; tempFunctionName <= 'x'; tempFunctionName++) {
    		if(!this.functionNameExists(""+tempFunctionName)) {
    			return "" + tempFunctionName;
    		}
    	}
    	
    	// Wenn kein Name von a-w frei ist, so wird es mit zweistelligen Namen versucht
    	for(char tempFunctionName1 = 'a'; tempFunctionName1 <= 'x'; tempFunctionName1++) {
        	for(char tempFunctionName2 = 'a'; tempFunctionName2 <= 'x'; tempFunctionName2++) {
        		if(!this.functionNameExists("" + tempFunctionName1 + tempFunctionName2)) {
        			return "" + tempFunctionName1 + tempFunctionName2;
        		}
        	}
    	}
    	
    	// Wenn keiner der 23^2 Namen verfügbar ist, dann such dir gefälligst einen selbst aus ;)
		throw new FunctionParsingException(ParsingResponseCode.NoMoreNamesAvailable, "Es existiert kein weiterer FunctionName (a-z).");
    }
}
