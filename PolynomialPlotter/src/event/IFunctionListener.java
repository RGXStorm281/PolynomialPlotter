package event;

import logic.FunctionParsingException;

/**
 * @author robinepple
 * @author raphaelsack
 */

public interface IFunctionListener extends java.util.EventListener{

    /**
     * Wird gecallt, wenn eine Funktion hinzugefügt wird
     * @param e Das Event, das die Funktionsdetails enthält.
     * @throws FunctionParsingException Wenn das Parsen der Funktion fehlschlägt, enthält die Exception die Informationen darüber, was das Problem war.
     */
    void functionAdded(FunctionEvent e) throws FunctionParsingException;

    /**
     * Wird gecallt, wenn eine Funktion editiert wird
     * @param e Das Event, das die Funktionsdetails enthält.
     * @throws FunctionParsingException Wenn das Parsen der Funktion fehlschlägt, enthält die Exception die Informationen darüber, was das Problem war.
     */
    void functionEdited(FunctionEditedEvent e) throws FunctionParsingException;
    
    /**
     * 
     * @param e Das Event, das die Funktionsdetails enthält
     * @throws FunctionParsingException Wenn das Parsen der Funktion fehlschlägt, enthält die Exception die Informationen darüber, was das Problem war.
     */
    void functionDerived(FunctionDerivedEvent e) throws FunctionParsingException;

    /**
     * Wird gecallt wenn eine Funktion gelöscht wird
     * @param e Das Event, das die Funktionsdetails enthält.
     */
    void functionDeleted(FunctionEvent e);
    
    /**
     * Wird gecallt, wenn eine Funktion sichtbar/unsichtbar geschalten wird.
     * @param e Das Event, das die Funktionsdetails enthält.
     */
    void functionVisibilityToggled(FunctionVisibilityToggledEvent e);
    
}
