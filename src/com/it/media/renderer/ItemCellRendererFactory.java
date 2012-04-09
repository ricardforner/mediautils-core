package com.it.media.renderer;

import com.it.media.renderer.common.ItemCellRenderer;
import com.it.media.renderer.impl.ItemDataCellRendererImpl;
import com.it.media.renderer.impl.ItemHeaderCellRendererImpl;
import com.it.torrent.TorrentConstants;

public class ItemCellRendererFactory {

	public static ItemCellRenderer create(String client, boolean header) {
		if (TorrentConstants.CLIENT_UTORRENT.equals(client)) {
			return (header) ? new ItemHeaderCellRendererImpl() : new ItemDataCellRendererImpl();
		} else if (TorrentConstants.CLIENT_TRANSMISSION.equals(client)) {
			return null;
		}
		return null;
	}
}
