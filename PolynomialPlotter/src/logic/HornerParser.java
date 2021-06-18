package logic;

import model.IFunction;

public class HornerParser implements IParser {

	@Override
	public boolean canParse(String function) {
		// TODO TV testen
		// Überprüft, ob die function dem Format des HornerSchemas entspricht
		function.replaceAll("\\s", "");
		if(function.split("\\(").length == function.split("\\)").length // vergleicht Anzahl der "(" und ")" in dem String
				&& (function.matches("[a-z]*\\(x\\)=\\(*[0-9]*\\*{0,1}(x(\\+[0-9]{1,}){0,1}\\))*x(\\+[0-9]{1,}){0,1}")
				|| function.matches("[a-z]*\\(x\\)=[0-9]*"))) {
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
