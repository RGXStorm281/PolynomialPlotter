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
        		if(this.functionNameExists(tempFunctionName)) {
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
    	
    	if(functionName >= 'a' && functionName <= 'z' && this.functionNameExists(functionName)) {
        	IFunction function = this.parse(functionString);
        	if(function != null) {
        		
        		this.functionMap.put(functionName, function);
        		return true;
        	}
    	}
    	
    	return false;
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
    		catch (Exception e) {
    			// TODO TV ExceptionHandling
    		}
    	}
    	
    	return null;
    }
}
