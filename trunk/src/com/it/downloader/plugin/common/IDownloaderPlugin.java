package com.it.downloader.plugin.common;

import java.util.HashMap;

public interface IDownloaderPlugin {

	public static String LABEL_ACTION		= "action";
	public static String DOWNLOADER_LIST	= "list";
	
	public abstract void initialize();

	public abstract String callURL(String url, HashMap<String, Object> params);

	public abstract String getNamePlugin();

}
