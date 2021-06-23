package event;

public interface FunctionListener extends java.util.EventListener{

    /**
     * Wird gecallt, wenn eine Funktion hinzugefügt wird
     * @param e 
     * @return Gibt einen boolean zurück, gibt an ob die Mitgegebene Funktion "legal" ist
     */
    boolean functionAdded(FunctionEvent e);

    /**
     * Wird gecallt, wenn eine Funktion editiert wird
     * @param e
     * @return Gibt einen boolean zurück, gibt an ob die Editierte Funtion "legal" ist
     */
    boolean functionEdited(FunctionEditedEvent e);

    /**
     * Wird gecallt wenn eine Funktion gelöscht wird
     * @param e
     */
    void functionDeleted(FunctionEvent e);
    
}
