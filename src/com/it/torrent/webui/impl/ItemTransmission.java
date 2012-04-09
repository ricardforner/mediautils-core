package com.it.torrent.webui.impl;

import com.it.torrent.webui.common.ItemBase;

/**
 * Objeto de descripción de un torrent recuperado desde la WebUI de Transmission
 * 
 * @author Ricard Forner
 * @version 1.0
 */
public class ItemTransmission extends ItemBase {

	public static final long STATUS_DOWNLOAD = 4;
	public static final long STATUS_FINISHED = 8;
	public static final long STATUS_PARADO   = 16;

	
	private String hash;
	private long status;
	private String name;
	private double percentDone;
	private long totalSize;
	private long leftUntilDone;
	private double uploadRatio;
	
	private double threshold = 1000;

	public ItemTransmission() {
		
	}
	
	public String getHash() {
		return hash;
	}
	
	public void setHash(String hash) {
		this.hash = hash;
	}
	
	public long getStatus() {
		return status;
	}
	
	public void setStatus(long status) {
		this.status = status;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public double getPercentDone() {
		return percentDone;
	}

	public void setPercentDone(double percentDone) {
		this.percentDone = percentDone;
	}

	public long getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(long totalSize) {
		this.totalSize = totalSize;
	}

	public long getLeftUntilDone() {
		return leftUntilDone;
	}

	public void setLeftUntilDone(long leftUntilDone) {
		this.leftUntilDone = leftUntilDone;
	}

	public double getUploadRatio() {
		return uploadRatio;
	}

	public void setUploadRatio(double uploadRatio) {
		this.uploadRatio = uploadRatio;
	}

	public int getRatio() {
		return (int)getUploadRatio();
	}

	public void setRatio(int ratio) {
		setUploadRatio(ratio);
	}

	public long getSize() {
		return getTotalSize();
	}

	public void setSize(long size) {
		setTotalSize(size);
	}

	public String toString() {
		return "[" +
			hash + ", " +
			status + ", " +
			name + ", " +
			percentDone + ", " +
			totalSize + ", " +
			leftUntilDone + ", " +
			uploadRatio +
		"]";
	}
	
	/***
	 * OBJETOS DE NEGOCIO
	 */
	public void setRatioThreshold(int threshold) {
		this.threshold = threshold;
	}

	public boolean isSeeding() {
		return (!isFinished());
	}

	public boolean isFinished() {
		return (this.status==STATUS_PARADO);
	}
	
	public boolean isDownloaded() {
		return (this.leftUntilDone == 0);
	}

	public boolean isDownloading() {
		return (this.status==STATUS_DOWNLOAD);
	}

	public boolean isCompletamenteServido() {
		return (isDownloaded() && ((this.uploadRatio*1000)>=this.threshold));
	}

}
