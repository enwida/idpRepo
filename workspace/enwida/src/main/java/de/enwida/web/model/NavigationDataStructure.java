/**
 * 
 */
package de.enwida.web.model;

import java.util.List;

/**
 * @author Jitin
 *
 */
public class NavigationDataStructure {

	/**
	 * find the node with the input key present in the input list
	 * 
	 * @param key
	 * @param list
	 * @return
	 */
	public static NavigationNode findNavigationNodeByKey(final String key,final List<NavigationNode> list){
		for(final NavigationNode node: list){
			if((key!=null) && key.equalsIgnoreCase(node.getValue())){
				return node;
			}
		}
		return null;
	}

	private List<NavigationNode> list;

	/**
	 * 
	 */
	public NavigationDataStructure() {
		super();
	}

	/**
	 * @param list
	 */
	public NavigationDataStructure(List<NavigationNode> list) {
		super();
		this.list = list;
	}

	/**
	 * @return the list
	 */
	public List<NavigationNode> getList() {
		return this.list;
	}

	/**
	 * @param list
	 *            the list to set
	 */
	public void setList(List<NavigationNode> list) {
		this.list = list;
	}
}
