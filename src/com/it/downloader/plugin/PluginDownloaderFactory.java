package com.it.downloader.plugin;

import com.it.downloader.DownloaderConstants;
import com.it.downloader.plugin.common.EmptyDownloaderPlugin;
import com.it.downloader.plugin.common.IDownloaderPlugin;
import com.it.downloader.plugin.impl.JDownloaderPlugin;

public class PluginDownloaderFactory {

	public static IDownloaderPlugin create(String client, ConnDownloaderParams params ) {
		if (DownloaderConstants.CLIENT_JDOWNLOADER.equals(client)) {
			return new JDownloaderPlugin(params);
		}
		return new EmptyDownloaderPlugin(params);
	}
}
