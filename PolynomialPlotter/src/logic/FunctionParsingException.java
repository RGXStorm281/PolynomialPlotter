/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

/**
 *
 * @author robinepple
 */
public class FunctionParsingException extends Exception {
    
    private ParsingResponseCode response;
    
    public FunctionParsingException(String message){
        super(message);
        this.response = null;
    }

    public FunctionParsingException(ParsingResponseCode response, String message) {
        super(message);
        this.response = response;
    }

    public ParsingResponseCode getResponseCode() {
    	return response;
    }
}
