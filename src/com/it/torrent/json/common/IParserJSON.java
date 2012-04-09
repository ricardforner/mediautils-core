package com.it.torrent.json.common;

import org.apache.noggit.JSONParser;

import com.it.torrent.webui.common.ItemBase;

public interface IParserJSON {

	public abstract void setJSON(JSONParser js);

	public abstract ItemBase[] getList();

	public abstract int size();

}