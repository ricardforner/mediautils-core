package com.it.downloader.plugin.common;

import com.it.downloader.plugin.ConnDownloaderParams;

public class DownloaderPluginBase {

	private ConnDownloaderParams params;
	
	public DownloaderPluginBase(ConnDownloaderParams params) {
		this.params = (params == null) ? new ConnDownloaderParams() : params;
	}
	
	public void initialize() {
		
	}
	
	public ConnDownloaderParams getParams() {
		return params;
	}
}
