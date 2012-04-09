package com.it.torrent.json;

import com.it.torrent.TorrentConstants;
import com.it.torrent.json.common.IParserJSON;
import com.it.torrent.json.common.ParserJSONEmpty;
import com.it.torrent.json.impl.ParserJSONTransmission;
import com.it.torrent.json.impl.ParserJSONUTorrent;

public class ParserJSONFactory {
	
	public static IParserJSON create(String client) {
		if (TorrentConstants.CLIENT_UTORRENT.equals(client)) {
			return new ParserJSONUTorrent();
		} else if (TorrentConstants.CLIENT_TRANSMISSION.equals(client)) {
			return new ParserJSONTransmission();
		}
		return new ParserJSONEmpty();
	}

}
