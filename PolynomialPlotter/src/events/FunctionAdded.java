package events;

import java.awt.event.*;

import logic.BusinessLogic;
import view.GUI;

public class FunctionAdded implements ActionListener{

    private GUI view;
    private BusinessLogic logic;

    public FunctionAdded(GUI view){
        this.view = view;

    }
    @Override
    public void actionPerformed(ActionEvent e) {
        view.closeDialog();
        System.out.println(view.getColor());
        System.out.println(view.getFunction());
    }
    
    
}
