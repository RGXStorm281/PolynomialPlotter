/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import java.util.ArrayList;
import java.util.HashMap;

import model.IFunction;

/**
 *
 * @author robinepple
 */
public class FunctionManager {

    private HashMap<Character, IFunction> functionMap;
    
	private static IParser[] parserArray = new IParser[] {
    		new HornerParser(),
    		new UniversalParser()
    };
	
    public FunctionManager(){
    	// Initialisiert FunctionMap
    	functionMap = new HashMap<>();
    }
    
    /**
     * Prüft, ob functionName bereits existiert.
     * @param functionName
     * @return functionName hinterlegt.
     */
    private boolean functionNameExists(char functionName) {
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
     * @param c
     * @return Die zugehrige Funktion oder null.
     */
    public IFunction getFunction(char c){
        if(functionNameExists(c)){
            return this.functionMap.get(c);
        }
        return null;
    }
    
    /**
     * Löscht die function mit dem functionName.
     * @param functionName
     * @return function gelöscht.
     */
    public boolean delete(Character functionName) {
    	
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
    public Character parseAndAddFunction(Character functionName, String functionString) throws FunctionParsingException{
    	
        if(functionName == null) {
        	for(char tempFunctionName = 'a'; tempFunctionName <= 'z'; tempFunctionName++) {
        		if(!this.functionNameExists(tempFunctionName)) {
        			functionName = tempFunctionName;
        			break;
        		}
        	}
        	
        	if(functionName == null) {
        		throw new FunctionParsingException(ParsingResponseCode.NoMoreNamesAvailable, "Es existiert kein weiterer FunctionName (a-z).");
        	}
        }
        
        boolean added = this.add(functionName, functionString);
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
    private boolean add(char functionName, String functionString) {
    	
    	if(!functionNameAddable(functionName)) {
    		return false;
    	}
    	
    	IFunction function = this.parse(functionString);
    	if(function == null) {
    		return false;
    	}
    	
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
    public boolean add(char functionName, IFunction function) {
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
    private boolean functionNameAddable(char functionName){
        if(functionName <= 'a' || functionName >= 'z') {
            return false;
    	}
        
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
    			e.printStackTrace();
    		}
    	}
    	
    	return null;
    }
}
