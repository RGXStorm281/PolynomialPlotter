package event;

public interface FunctionListener extends java.util.EventListener{

    void functionAdded(FunctionEvent e);
    void functionEdited(FunctionEditedEvent e);
    void functionDeleted(FunctionEvent e);
    
}
