package de.enwida.web.utils.numbers;

public class GermanNumberFormatter implements INumberFormatter {

	@Override
	public String formatNumber(double d) {
		return String.valueOf(d).replace('.', ',');
	}

	@Override
	public String formatNumber(int i) {
		return String.valueOf(i);
	}

}
