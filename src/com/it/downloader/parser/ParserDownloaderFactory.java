package com.it.downloader.parser;

import com.it.downloader.DownloaderConstants;
import com.it.downloader.parser.common.IParserDownloader;
import com.it.downloader.parser.common.ParserDownloaderEmpty;
import com.it.downloader.parser.impl.ParserDownloaderJDownloader;

public class ParserDownloaderFactory {

	public static IParserDownloader create(String client) {
		if (DownloaderConstants.CLIENT_JDOWNLOADER.equals(client)) {
			return new ParserDownloaderJDownloader();
		}
		return new ParserDownloaderEmpty();
	}
}
