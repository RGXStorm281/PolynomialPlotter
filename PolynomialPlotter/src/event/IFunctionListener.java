package event;

import logic.FunctionParsingException;

public interface IFunctionListener extends java.util.EventListener{

    /**
     * Wird gecallt, wenn eine Funktion hinzugefügt wird
     * @param e 
     * @throws FunctionParsingException Wenn das Parsen der Funktion fehlschlägt, enthält die Exception die Informationen darüber, was das Problem war.
     */
    void functionAdded(FunctionEvent e) throws FunctionParsingException;

    /**
     * Wird gecallt, wenn eine Funktion editiert wird
     * @param e
     * @throws FunctionParsingException Wenn das Parsen der Funktion fehlschlägt, enthält die Exception die Informationen darüber, was das Problem war.
     */
    void functionEdited(FunctionEditedEvent e) throws FunctionParsingException;

    /**
     * Wird gecallt wenn eine Funktion gelöscht wird
     * @param e
     */
    void functionDeleted(FunctionEvent e);
    
}
