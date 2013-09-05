package de.enwida.web.utils;

import java.util.ArrayList;
import java.util.List;

public class NavigationOptions implements Cloneable {

	private boolean isDateScale = true;
	private int decimals = 2;
	private boolean hasTimeSelection = true;
	private boolean hasProductSelection = true;
	private boolean hasLineSelection = true;
	private boolean hasDownloadSVG = true;
	private boolean hasDownloadPNG = true;
	private boolean hasDownloadCSV = true;
	private boolean hasDataSheet = true;
	private List<String> colors = new ArrayList<String>();

	@Override
	public NavigationOptions clone() {
		final NavigationOptions clone = new NavigationOptions();
		
		clone.setIsDateScale(isDateScale);
		clone.setDecimals(decimals);
		clone.setHasTimeSelection(hasTimeSelection);
		clone.setHasProductSelection(hasProductSelection);
		clone.setHasLineSelection(hasLineSelection);
		clone.setHasDownloadSVG(hasDownloadSVG);
		clone.setHasDownloadPNG(hasDownloadPNG);
		clone.setHasDownloadCSV(hasDownloadCSV);
		clone.setHasDataSheet(hasDataSheet);
		clone.setColors(new ArrayList<String>(colors));
		
		return clone;
	}

	public boolean getIsDateScale() {
		return isDateScale;
	}

	public void setIsDateScale(boolean isDateScale) {
		this.isDateScale = isDateScale;
	}

	public int getDecimals() {
		return decimals;
	}

	public void setDecimals(int decimals) {
		this.decimals = decimals;
	}

	public boolean getHasTimeSelection() {
		return hasTimeSelection;
	}

	public void setHasTimeSelection(boolean hasTimeSelection) {
		this.hasTimeSelection = hasTimeSelection;
	}

	public boolean getHasProductSelection() {
		return hasProductSelection;
	}

	public void setHasProductSelection(boolean hasProductSelection) {
		this.hasProductSelection = hasProductSelection;
	}

	public boolean getHasLineSelection() {
		return hasLineSelection;
	}

	public void setHasLineSelection(boolean hasLineSelection) {
		this.hasLineSelection = hasLineSelection;
	}

	public List<String> getColors() {
		return colors;
	}

	public void setColors(List<String> colors) {
		this.colors = colors;
	}

	public boolean getHasDownloadSVG() {
		return hasDownloadSVG;
	}

	public void setHasDownloadSVG(boolean hasDownloadSVG) {
		this.hasDownloadSVG = hasDownloadSVG;
	}

	public boolean getHasDownloadPNG() {
		return hasDownloadPNG;
	}

	public void setHasDownloadPNG(boolean hasDownloadPNG) {
		this.hasDownloadPNG = hasDownloadPNG;
	}

	public boolean getHasDownloadCSV() {
		return hasDownloadCSV;
	}

	public void setHasDownloadCSV(boolean hasDownloadCSV) {
		this.hasDownloadCSV = hasDownloadCSV;
	}

	public boolean getHasDataSheet() {
		return hasDataSheet;
	}

	public void setHasDataSheet(boolean hasDataSheet) {
		this.hasDataSheet = hasDataSheet;
	}

	public void setDateScale(boolean isDateScale) {
		this.isDateScale = isDateScale;
	}
	
}
