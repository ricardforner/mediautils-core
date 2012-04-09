package com.it.downloader.plugin.impl;

import java.util.HashMap;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import com.it.downloader.DownloaderConstants;
import com.it.downloader.plugin.ConnDownloaderParams;
import com.it.downloader.plugin.common.DownloaderPluginBase;
import com.it.downloader.plugin.common.IDownloaderPlugin;

public class JDownloaderPlugin extends DownloaderPluginBase implements
		IDownloaderPlugin {

	public JDownloaderPlugin(ConnDownloaderParams params) {
		super(params);
	}
	
	public String getNamePlugin() {
		return DownloaderConstants.CLIENT_JDOWNLOADER;
	}

	protected String getURL() {
		StringBuffer url = new StringBuffer();
		url.append("http://");
		url.append(getParams().getHostIP());
		url.append(":");
		url.append(getParams().getHostPort());
		url.append("/");
		return url.toString();
	}

	public String callURL(String url, HashMap<String, Object> params) {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		
        StringBuffer sURL = new StringBuffer();
        sURL.append(getURL());
        sURL.append(url);
        
        HttpGet httpget = new HttpGet(sURL.toString());

        String responseBody = null;
		try {
        	// Execute
			ResponseHandler<String> responseHandler = new BasicResponseHandler();

			responseBody = httpclient.execute(httpget, responseHandler);
		} catch (Exception e) {
		} finally {
			httpclient.getConnectionManager().shutdown();
		}
        return responseBody;
	}

}
