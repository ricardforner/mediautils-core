package com.it.torrent.json.common;

import org.apache.noggit.JSONParser;

import com.it.torrent.webui.common.ItemBase;

public class ParserJSONEmpty implements IParserJSON {

	public ItemBase[] getList() {
		return new ItemBase[]{};
	}

	public void setJSON(JSONParser js) {
	}

	public int size() {
		return 0;
	}

}
