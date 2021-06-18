package event;
import java.awt.Color;
public class FunctionEvent extends java.util.EventObject{

    Color functionColor;
    String functionString;
    char functionName;

    public FunctionEvent(Object source, Color functionColor, String functionString, char functionName) {
        super(source);
        this.functionColor = functionColor;
        this.functionString = functionString;
        this.functionName = functionName;
    }

    public Color getFunctionColor(){
        return this.functionColor;
    }
    
    public String getFunctionString(){
        return this.functionString;
    }
    
    public char getFunctionChar(){
        return this.functionName;
    }
}
