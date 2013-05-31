package de.enwida.web.model;

import java.util.List;

public class NavigationNode implements Comparable<NavigationNode> {

	private GenericData commonData;
	private List<NavigationNode> list;
	private String value;

	/**
	 * 
	 */
	public NavigationNode() {
		super();
	}

	/**
	 * @param list
	 * @param value
	 */
	public NavigationNode(List<NavigationNode> list, String value) {
		super();
		this.list = list;
		this.value = value;
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(NavigationNode o) {
		return this.value.compareTo(o.value);
	}

	/**
	 * @return the commonData
	 */
	public GenericData getCommonData() {
		return this.commonData;
	}

	/**
	 * @return the list
	 */
	public List<NavigationNode> getList() {
		return this.list;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return this.value;
	}

	/**
	 * @param commonData the commonData to set
	 */
	public void setCommonData(GenericData commonData) {
		this.commonData = commonData;
	}

	/**
	 * @param list the list to set
	 */
	public void setList(List<NavigationNode> list) {
		this.list = list;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "NavigationNode ["
				+ (this.commonData != null ? "commonData=" + this.commonData + ", " : "")
				+ (this.list != null ? "list=" + this.list + ", " : "")
				+ (this.value != null ? "value=" + this.value : "") + "]";
	}


}
