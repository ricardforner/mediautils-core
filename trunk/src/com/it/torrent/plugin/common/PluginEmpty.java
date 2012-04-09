package com.it.torrent.plugin.common;

import java.util.HashMap;

import com.it.torrent.TorrentConstants;
import com.it.torrent.plugin.ConnectionParams;

public class PluginEmpty extends PluginBase implements IPluginTorrent {

	public PluginEmpty(ConnectionParams params) {
		super(params);
	}

	public String getNamePlugin() {
		return TorrentConstants.CLIENT_EMPTY;
	}

	public String callURL(String url, HashMap<String, Object> params) {
		return null;
	}

}
