/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import java.util.HashMap;
import java.util.UUID;

import model.IFunction;

/**
 *
 * @author robinepple
 */
public class FunctionManager {

    private HashMap<String, IFunction> functionMap;
    
	private static IParser[] parserArray = new IParser[] {
    		new HornerParser(),
    		new PolynomParser(),
    		new UniversalParser()
    };
    
    private FunctionManager(){
    	// Initialisiert FunctionMap
    	functionMap = new HashMap<>();
    }
    
    /**
     * Prüft, ob functionName bereits existiert.
     * @param functionName
     * @return functionName hinterlegt.
     */
    public boolean functionNameExists(String functionName) {
    	return this.functionMap.containsKey(functionName);
    }

    /**
     * Gibt alle geparsten functions aus.
     * @return functionMap.
     */
    public HashMap<String, IFunction> getFunctionMap() {
        return this.functionMap;
    }
    
    /** 
     * Fügt die mitgegebene function zu der functionMap hinzu, gibt ihr dabei einen Namen.
     * @param functionString
     * @return functionName.
     */
    public String add (String functionString) {
    	for(char nameChar = 'a'; nameChar <= 'z'; nameChar++) {
    		String functionName = String.valueOf(nameChar);
    		if(this.functionNameExists(functionName)) {
    			return this.add(functionName, functionString);
    		}
    	}
    	
    	// TODO TV nicht so pfuschen!
    	return this.add(UUID.randomUUID().toString(), functionString);
    }
    
    /** 
     * Fügt die mitgegebene function zu der functionMap hinzu.
     * @param functionString
     * @return functionName.
     */
    public String add (String functionName, String functionString) {
    	// TODO TV was, wenn functionName bereits existiert?
    	IFunction function = this.parse(functionString);
    	if(function != null) {
    		this.functionMap.put(functionName, function);
        	
        	return functionName;
    	}
    	
    	return null;
    }
    
    /**
     * Löscht die function mit dem functionName.
     * @param functionName
     * @return function gelöscht.
     */
    public boolean delete (String functionName) {
    	if(this.functionNameExists(functionName)) {
    		this.functionMap.remove(functionName);
    		return true;
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
    		if(parser.canParse(functionString)) {
    			return parser.parse(functionString);
    		}
    	}
    	
    	return null;
    }
    
}
