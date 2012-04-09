package com.it.downloader.webui.impl;

import com.it.torrent.webui.common.ItemBase;
import com.it.util.StringUtil;

public class ItemJDownloader extends ItemBase {

	public static final long STATUS_FINISHED	= 0;
	public static final long STATUS_DOWNLOADING	= 1;
	
	private String hash;
	private long status;
	private String name;
	private int ratio;
	private long size;
	private long bytesPending;

	private double threshold = 100;
	
	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getStatus() {
		return isDownloaded() ? STATUS_FINISHED : STATUS_DOWNLOADING;
	}

	public void setStatus(long status) {
		this.status = -1;
	}

	public int getRatio() {
		return ratio;
	}

	public void setRatio(int ratio) {
		this.ratio = ratio;
	}

	public void setRatioPercent(String ratio) {
		int ratioValue = 0;
		if (ratio != null && !"".equals(ratio) ) {
			if (ratio.indexOf(',') != -1) {
				String sTmp = ratio.substring(0, ratio.indexOf(','));
				ratioValue = new Integer(sTmp).intValue();
			}
		}
		setRatio(ratioValue);
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}
	
	public void setBytesPending(String bytesPending) {
		long lPending = 0;
		if (bytesPending != null && !"".equals(bytesPending)) {
			if (bytesPending.indexOf(' ') != -1) {
				String sTmp = bytesPending.substring(0, bytesPending.indexOf(' '));
				lPending = new Double(sTmp).longValue();
			}
		}
		this.bytesPending = lPending;
	}

	public void setSizeFile(String size) {
		long sizeValue = 0;
		if (size != null && !"".equals(size) ) {
			if (size.indexOf(' ') != -1) {
				String sTmp = size.substring(0, size.indexOf(' '));
				// La siguiente instruccion coge los 2 decimales
				sTmp = StringUtil.replaceCadena(sTmp, ".", "");
				sizeValue = new Double(sTmp).longValue();
			}
		}
		setSize(sizeValue);
	}
	
	public String toString() {
		return "[" +
			hash + ", " +
			status + ", " +
			name + ", " +
			ratio + ", " +
			size + ", " +
			bytesPending +
		"]";
	}

	/***
	 * OBJETOS DE NEGOCIO
	 */
	public void setRatioThreshold(int threshold) {
		this.threshold = threshold;
	}

	public boolean isFinished() {
		return isDownloaded();
	}

	public boolean isSeeding() {
		return true;
	}

	public boolean isDownloaded() {
		return (this.bytesPending == 0);
	}

	public boolean isDownloading() {
		return (this.bytesPending > 0);
	}

	public boolean isCompletamenteServido() {
		return (isDownloaded() && (this.ratio>=this.threshold));
	}

}
