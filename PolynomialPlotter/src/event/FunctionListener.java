package event;

public interface FunctionListener extends java.util.EventListener{

    boolean functionAdded(FunctionEvent e);
    boolean functionEdited(FunctionEditedEvent e);
    void functionDeleted(FunctionEvent e);
    
}
