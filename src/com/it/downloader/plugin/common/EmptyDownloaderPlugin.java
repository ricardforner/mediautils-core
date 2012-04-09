package com.it.downloader.plugin.common;

import java.util.HashMap;

import com.it.downloader.DownloaderConstants;
import com.it.downloader.plugin.ConnDownloaderParams;

public class EmptyDownloaderPlugin extends DownloaderPluginBase implements
		IDownloaderPlugin {

	public EmptyDownloaderPlugin(ConnDownloaderParams params) {
		super(params);
	}

	public String getNamePlugin() {
		return DownloaderConstants.CLIENT_EMPTY;
	}

	public String callURL(String url, HashMap<String, Object> params) {
		return null;
	}

}
