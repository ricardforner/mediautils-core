package com.it.torrent.webui.common;

import com.it.media.renderer.NotifyComponent;

public abstract class ItemBase extends NotifyComponent {

	public abstract String getName();
	public abstract void setName(String name);

	public abstract long getStatus();
	public abstract void setStatus(long status);
	
	public abstract String getHash();
	public abstract void setHash(String hash);
	
	public abstract long getSize();
	public abstract void setSize(long size);

	public abstract int getRatio();
	public abstract void setRatio(int ratio);
	
	public abstract boolean isSeeding();
	
	public abstract boolean isFinished();
	
	public abstract boolean isDownloaded();
	
	public abstract boolean isDownloading();

	public abstract boolean isCompletamenteServido();
	
	public abstract void setRatioThreshold(int threshold);

}
