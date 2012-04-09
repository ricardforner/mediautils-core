package com.it.media.renderer.common;

import com.it.hbean.html.HtmlTableCellRenderer;

public abstract class ItemCellRenderer implements HtmlTableCellRenderer {

	protected String cidStatusFinished;
	protected String cidStatusSeeding;
	protected String cidStatusDownloading;
	protected String cidStatusSeeded;

	public void setCidStatusFinished(String cidStatusFinished) {
		this.cidStatusFinished = cidStatusFinished;
	}

	public void setCidStatusSeeding(String cidStatusSeeding) {
		this.cidStatusSeeding = cidStatusSeeding;
	}

	public void setCidStatusDownloading(String cidStatusDownloading) {
		this.cidStatusDownloading = cidStatusDownloading;
	}

	public void setCidStatusSeeded(String cidStatusSeeded) {
		this.cidStatusSeeded = cidStatusSeeded;
	}

}
