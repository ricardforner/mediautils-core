package com.it.torrent.plugin.impl;

import java.util.HashMap;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import com.it.torrent.TorrentConstants;
import com.it.torrent.plugin.ConnectionParams;
import com.it.torrent.plugin.common.IPluginTorrent;
import com.it.torrent.plugin.common.PluginBase;

public class PluginUTorrent extends PluginBase implements IPluginTorrent {

	public PluginUTorrent(ConnectionParams params) {
		super(params);
	}

	public String getNamePlugin() {
		return TorrentConstants.CLIENT_UTORRENT;
	}

	protected String getURL() {
		StringBuffer url = new StringBuffer();
		url.append("http://");
		url.append(getParams().getHostIP());
		url.append(":");
		url.append(getParams().getHostPort());
		url.append("/gui/");
		return url.toString();
	}

	public String callURL(String url) {
		DefaultHttpClient httpclient = new DefaultHttpClient();

		if (hasCredentials()) {
			httpclient.getCredentialsProvider().setCredentials(
					new AuthScope(getParams().getHostIP(), getParams().getHostPort()),
					new UsernamePasswordCredentials(getParams().getAuthUsername(), getParams().getAuthPassword()));
		}
		
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

	public String callURL(String url, HashMap<String, Object> params) {
		return callURL(url);
	}	

}
