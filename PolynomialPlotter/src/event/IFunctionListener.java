package event;

import logic.FunctionParsingException;

public interface IFunctionListener extends java.util.EventListener{

    /**
     * Wird gecallt, wenn eine Funktion hinzugefügt wird
     * @param e 
     * @return  Der Funktionsname, der für die angelegte Funktion vergeben wurde.
     * @throws FunctionParsingException Wenn das Parsen der Funktion fehlschlägt, enthält die Exception die Informationen darüber, was das Problem war.
     */
    char functionAdded(FunctionEvent e) throws FunctionParsingException;

    /**
     * Wird gecallt, wenn eine Funktion editiert wird
     * @param e
     * @return Der Funktionsname, den die editierte Funktion nach der Bearbeitung besitzt.
     * @throws FunctionParsingException Wenn das Parsen der Funktion fehlschlägt, enthält die Exception die Informationen darüber, was das Problem war.
     */
    char functionEdited(FunctionEditedEvent e) throws FunctionParsingException;

    /**
     * Wird gecallt wenn eine Funktion gelöscht wird
     * @param e
     */
    void functionDeleted(FunctionEvent e);
    
}
