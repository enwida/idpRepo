package de.enwida.web.utils.numbers;

public class DefaultNumberFormatter implements INumberFormatter {

	@Override
	public String formatNumber(double d) {
		return String.valueOf(d);
	}

	@Override
	public String formatNumber(int i) {
		return String.valueOf(i);
	}

}
