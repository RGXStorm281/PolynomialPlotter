/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import java.util.ArrayList;
import java.util.Arrays;

import model.IFunction;

/**
 *
 * @author robinepple
 */
public class FunctionManager {
    
    private ArrayList<IFunction> functionList;
    
    private FunctionManager(){
        functionList = new ArrayList<>();
    }

    public ArrayList<IFunction> getFunctionList() {
        return functionList;
    }
    
}
