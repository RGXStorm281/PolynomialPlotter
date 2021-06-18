package events;

import java.awt.Color;
import java.awt.event.*;

import logic.BusinessLogic;
import model.UniversalFunction;
import view.GUI;

public class FunctionAddedListener implements ActionListener{

    private GUI view;
    private BusinessLogic logic;

    public FunctionAddedListener(GUI view, BusinessLogic logic){
        this.view = view;
        this.logic = logic;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        // Wird aufgerufen, wenn der User auf den Ok-Button im FunctionDialog klickt
        // Später nötige Funktionen: view.getColor() -> Color ; view.getFunction() -> UniversalFunction

        // Retrieve Function and invoke logic
        view.closeFunctionDialog();
        String newFunction = view.getFunction();
        Color functionColor = view.getColor();    
        System.out.println("EVENT TRIGGERED: Function Added");
        // logic.foo(functionColor,newFunction) welche view wieder aufruft
    
    }
    
    
}
