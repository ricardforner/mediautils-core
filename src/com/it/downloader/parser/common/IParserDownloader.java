package com.it.downloader.parser.common;

import com.it.torrent.webui.common.ItemBase;

public interface IParserDownloader {

	public abstract void setContent(String content);
	
	public abstract ItemBase[] getList();

	public abstract int size();

}