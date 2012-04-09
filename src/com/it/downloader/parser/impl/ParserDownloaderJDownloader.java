package com.it.downloader.parser.impl;

import java.io.IOException;
import java.util.ArrayList;

import com.it.downloader.parser.JDRemoteControlReader;
import com.it.downloader.parser.common.IParserDownloader;
import com.it.downloader.webui.impl.ItemJDownloader;

/**
 * Realiza el parser de la informacion de JDownloader.
 * Se llama al add-on de Remote Control del JDownloader
 * 
 * @author Ricard Forner
 * @version 1.0
 */
public class ParserDownloaderJDownloader implements IParserDownloader {

	private int quants;
	private ArrayList<ItemJDownloader> arrList;

	public ParserDownloaderJDownloader() {
		this.quants = 0;
		this.arrList = new ArrayList<ItemJDownloader>();
	}
	
	public void setContent(String content) {
		JDRemoteControlReader reader = new JDRemoteControlReader();
		try {
			if (content != null && !"".equals(content)) {
				reader.load(content);
				arrList = reader.getList();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			this.quants = this.arrList.size();
		}
	}
	
	public ItemJDownloader[] getList() {
		return (ItemJDownloader[]) arrList.toArray(new ItemJDownloader[]{});
	}

	public int size() {
		return this.quants;
	}

}
