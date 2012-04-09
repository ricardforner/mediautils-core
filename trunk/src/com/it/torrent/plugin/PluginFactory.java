package com.it.torrent.plugin;

import com.it.torrent.TorrentConstants;
import com.it.torrent.plugin.common.IPluginTorrent;
import com.it.torrent.plugin.common.PluginEmpty;
import com.it.torrent.plugin.impl.PluginTransmission;
import com.it.torrent.plugin.impl.PluginUTorrent;

public class PluginFactory {

	public static IPluginTorrent create(String client, ConnectionParams params) {
		if (TorrentConstants.CLIENT_UTORRENT.equals(client)) {
			return new PluginUTorrent(params);
		} else if (TorrentConstants.CLIENT_TRANSMISSION.equals(client)) {
			return new PluginTransmission(params);
		}
		return new PluginEmpty(params);
	}

}
