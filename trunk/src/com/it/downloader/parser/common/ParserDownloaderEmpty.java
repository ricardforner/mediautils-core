package com.it.downloader.parser.common;

import com.it.torrent.webui.common.ItemBase;

public class ParserDownloaderEmpty implements IParserDownloader {

	public ItemBase[] getList() {
		return new ItemBase[]{};
	}

	public void setContent(String content) {
	}

	public int size() {
		return 0;
	}

}
