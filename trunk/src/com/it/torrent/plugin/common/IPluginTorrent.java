package com.it.torrent.plugin.common;

import java.util.HashMap;

public interface IPluginTorrent {

	public static String LABEL_ACTION	= "action";
	public static String HASH_VALUE		= "hash";
	
	public static String TORRENT_ADD	= "add";
	public static String TORRENT_LIST	= "list";
	public static String TORRENT_START	= "start";
	public static String TORRENT_STOP	= "stop";
	public static String TORRENT_REMOVE	= "remove";


	public abstract void setCredentials(boolean bCredentials);

	public abstract void initialize();

	public abstract String callURL(String url, HashMap<String, Object> params);

	public abstract String getNamePlugin();

}