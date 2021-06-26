/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import model.IFunction;

/**
 *
 * @author robinepple
 */
public interface IParser {  
	
	/**
	 * Parsed den FunctionString in eine IFunction (HornerFunction / UniversalFunction).
	 * @param function
	 * @return
	 */
    public IFunction parse(String function);
}
