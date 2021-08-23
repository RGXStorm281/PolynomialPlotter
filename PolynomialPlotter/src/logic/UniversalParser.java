package logic;

import model.IFunction;
import model.UniversalFunction;

public class UniversalParser implements IParser {

	@Override
	public IFunction parse(String functionName, String function) throws FunctionParsingException {
		// erstellt eine neue IFunction.
		var uFunction =  new UniversalFunction(functionName, function);
		
		// prüft, ob die function generell berechenbar ist
		try {
			uFunction.calculate(0);
		}
		catch (Exception e){
			throw new FunctionParsingException(ParsingResponseCode.ParsingFailed, "UniversalFunction konnte für Wert 0 nicht berechnet werden, es wird von einem Parsingfehler ausgegangen.");
		}
		
		return uFunction;
	}

}
