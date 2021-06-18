package logic;

import model.IFunction;
import model.UniversalFunction;

public class UniversalParser implements IParser {

	@Override
	public boolean canParse(String function) {
		UniversalFunction uvFunction = new UniversalFunction(function);
		if(uvFunction.calculate(0, 1, 1) != null) {
			return true;
		}
		
		return false;
	}

	@Override
	public IFunction parse(String function) {
		// TODO TV implement
		return null;
	}

}
