package com.it.util;

import java.io.File;
import java.util.Comparator;

public class FileOrderList implements Comparator<File> {

	public int compare(File o1, File o2) {
		return (o1.getName().toUpperCase().compareTo(o2.getName().toUpperCase()));
	}

}
