package logic;

import model.IFunction;
import model.UniversalFunction;

public class UniversalParser implements IParser {

	@Override
	public IFunction parse(String function) {
		// erstellt eine neue IFunction.
		return new UniversalFunction(function);
	}

}
