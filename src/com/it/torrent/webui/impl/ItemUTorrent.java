package com.it.torrent.webui.impl;

import com.it.torrent.webui.common.ItemBase;

/**
 * Objeto de descripción completa de un torrent recuperado desde la WebUI de uTorrent.
 *  
 * @author Ricard Forner
 * @version 1.0
 */
public class ItemUTorrent extends ItemBase {

	public static final long STATUS_SEEDING  = 201; // 201 = 128 + 64 + 8 + 1, then it is loaded, queued, checked, and started.
	public static final long STATUS_FINISHED = 136; // 136 = 128 + 8, then it is loaded, checked
	
	private String hash;
	private long status;
	private String name;
	private long size;
	private long percentProgress;
	private long downloaded;
	private long uploaded;
	private int ratio;
	private long uploadSpeed;
	private long downloadSpeed;
	private long ETA;
	private String label;
	private long peersConnected;
	private long peersInSwarm;
	private long seedsConnected;
	private long seedsInSwarn;
	private long availability;
	private long torrentQueueOrder;
	private long remaining;
	
	// variables de negocio
	private int threshold = 1000;
	
	public ItemUTorrent() {
	}

	public long getAvailability() {
		return availability;
	}

	public void setAvailability(long availability) {
		this.availability = availability;
	}

	public long getDownloaded() {
		return downloaded;
	}

	public void setDownloaded(long downloaded) {
		this.downloaded = downloaded;
	}

	public long getDownloadSpeed() {
		return downloadSpeed;
	}

	public void setDownloadSpeed(long downloadSpeed) {
		this.downloadSpeed = downloadSpeed;
	}

	public long getETA() {
		return ETA;
	}

	public void setETA(long eta) {
		ETA = eta;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getPeersConnected() {
		return peersConnected;
	}

	public void setPeersConnected(long peersConnected) {
		this.peersConnected = peersConnected;
	}

	public long getPeersInSwarm() {
		return peersInSwarm;
	}

	public void setPeersInSwarm(long peersInSwarm) {
		this.peersInSwarm = peersInSwarm;
	}

	public long getPercentProgress() {
		return percentProgress;
	}

	public void setPercentProgress(long percentProgress) {
		this.percentProgress = percentProgress;
	}

	public int getRatio() {
		return ratio;
	}

	public void setRatio(int ratio) {
		this.ratio = ratio;
	}

	public long getRemaining() {
		return remaining;
	}

	public void setRemaining(long remaining) {
		this.remaining = remaining;
	}

	public long getSeedsConnected() {
		return seedsConnected;
	}

	public void setSeedsConnected(long seedsConnected) {
		this.seedsConnected = seedsConnected;
	}

	public long getSeedsInSwarn() {
		return seedsInSwarn;
	}

	public void setSeedsInSwarn(long seedsInSwarn) {
		this.seedsInSwarn = seedsInSwarn;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public long getStatus() {
		return status;
	}

	public void setStatus(long status) {
		this.status = status;
	}

	public long getTorrentQueueOrder() {
		return torrentQueueOrder;
	}

	public void setTorrentQueueOrder(long torrentQueueOrder) {
		this.torrentQueueOrder = torrentQueueOrder;
	}

	public long getUploaded() {
		return uploaded;
	}

	public void setUploaded(long uploaded) {
		this.uploaded = uploaded;
	}

	public long getUploadSpeed() {
		return uploadSpeed;
	}

	public void setUploadSpeed(long uploadSpeed) {
		this.uploadSpeed = uploadSpeed;
	}

	public String toString() {
		return "[" +
			hash + ", " +
			status + ", " +
			name + ", " +
			size + ", " +
			percentProgress + ", " +
			downloaded + ", " +
			uploaded + ", " +
			ratio + ", " +
			uploadSpeed + ", " +
			downloadSpeed + ", " +
			ETA + ", " +
			label + ", " +
			peersConnected + ", " +
			peersInSwarm + ", " +
			seedsConnected + ", " +
			seedsInSwarn + ", " +
			availability + ", " +
			torrentQueueOrder + ", " +
			remaining +
		"]";
	}

	/***
	 * OBJETOS DE NEGOCIO
	 */
	public void setRatioThreshold(int threshold) {
		this.threshold = threshold;
	}
	
	public boolean isSeeding() {
		return (this.status==STATUS_SEEDING);
	}

	public boolean isFinished() {
		return (this.status==STATUS_FINISHED);
	}

	public boolean isDownloaded() {
		return (this.size<=this.downloaded);
	}
	
	public boolean isDownloading() {
		return (isSeeding() && this.percentProgress<1000);
	}

	public boolean isCompletamenteServido() {
		return (isDownloaded() && (this.ratio>=this.threshold));
	}

}
