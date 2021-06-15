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
    public boolean canParse(String function);
    
    public IFunction parse(String function);
}
