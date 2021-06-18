package event;

import java.awt.Color;

public class FunctionEditedEvent extends FunctionEvent{
    char oldFunctionChar;
    public FunctionEditedEvent(Object source, Color functionColor, String functionString, char functionName,char oldFunctionChar) {
        super(source, functionColor, functionString, functionName);
        this.oldFunctionChar = oldFunctionChar;
    }

    public char getOldFunctionChar(){
        return this.oldFunctionChar;
    }


}
