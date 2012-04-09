package com.it.torrent.webui.common;

import java.util.Comparator;


public class ItemBaseComparator implements Comparator<ItemBase> {

	public static String ORDER_BY_NAME = "name";
	private String order;
	
	public ItemBaseComparator(String order) {
		this.order = order;
	}
	
	public int compare(ItemBase o1, ItemBase o2) {
		int retValue = 0;
		if (ORDER_BY_NAME.equals(order)) {
			retValue = o1.getName().toUpperCase().compareTo(o2.getName().toUpperCase());
		}
		return retValue;
	}

}
