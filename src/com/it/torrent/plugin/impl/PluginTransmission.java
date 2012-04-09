package com.it.torrent.plugin.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.util.EntityUtils;

import com.it.torrent.TorrentConstants;
import com.it.torrent.json.CreateJSONRequest;
import com.it.torrent.plugin.ConnectionParams;
import com.it.torrent.plugin.common.IPluginTorrent;
import com.it.torrent.plugin.common.PluginBase;

public class PluginTransmission extends PluginBase implements IPluginTorrent {

	public static final String SESSION_HEADER	= "X-Transmission-Session-Id";
	
	private String sessionToken;
	
	public PluginTransmission(ConnectionParams params) {
		super(params);
	}

	public String getNamePlugin() {
		return TorrentConstants.CLIENT_TRANSMISSION;
	}

	protected String getURL() {
		StringBuffer url = new StringBuffer();
		url.append("http://");
		url.append(getParams().getHostIP());
		url.append(":");
		url.append(getParams().getHostPort());
		url.append("/transmission/rpc/");
		return url.toString();
	}

	public void initialize() {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		BasicHttpContext httpContext = new BasicHttpContext();

		if (hasCredentials()) {
			httpclient.getCredentialsProvider().setCredentials(
					new AuthScope(getParams().getHostIP(), getParams().getHostPort()),
					new UsernamePasswordCredentials(getParams().getAuthUsername(), getParams().getAuthPassword()));
		}
        
		HttpPost httpPost = new HttpPost(getURL());

        if (sessionToken != null) {
        	httpPost.addHeader(SESSION_HEADER, sessionToken);
        }

        try {
        	// Execute
        	HttpResponse response = httpclient.execute(httpPost, httpContext);
        
        	// 409 error because of a session id?
        	if (response.getStatusLine().getStatusCode() == 409) {
                // Retry post, but this time with the new session token that was encapsulated in the 409 response
                sessionToken = response.getFirstHeader(SESSION_HEADER).getValue();
        	}
		} catch (Exception e) {
		} finally {
			httpclient.getConnectionManager().shutdown();
		}	
	}
	
	public String callURL(String pRequest) {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		BasicHttpContext httpContext = new BasicHttpContext();

		if (hasCredentials()) {
			httpclient.getCredentialsProvider().setCredentials(
					new AuthScope(getParams().getHostIP(), getParams().getHostPort()),
					new UsernamePasswordCredentials(getParams().getAuthUsername(), getParams().getAuthPassword()));
		}
        
		HttpPost httpPost = new HttpPost(getURL());
		
		String responseBody = null;
        try {
        	StringEntity es = new StringEntity(pRequest);
        	httpPost.setEntity(es);

        	if (sessionToken != null) {
        		httpPost.addHeader(SESSION_HEADER, sessionToken);
        	}

        	// Execute
        	HttpResponse response = httpclient.execute(httpPost, httpContext);
        	HttpEntity entity = response.getEntity();
        	responseBody = EntityUtils.toString(entity);
        	
		} catch (Exception e) {
		} finally {
			httpclient.getConnectionManager().shutdown();
		}
		return responseBody;
	}

	public String callURL(String url, HashMap<String, Object> params) {
		CreateJSONRequest o = new CreateJSONRequest();
		if (TORRENT_ADD.equals(params.get(LABEL_ACTION))) {
			o.addValue("filename", url);
			o.addBlock("arguments");
			o.addValue("method", "torrent-add");
			o.addValue("tag", 1);
			o.addBlock(null);
		} else if (TORRENT_START.equals(params.get(LABEL_ACTION))) {
			o.addValue("ids", Integer.parseInt((String)params.get(HASH_VALUE))  );
			o.addBlock("arguments");
			o.addValue("method", "torrent-start");
			o.addValue("tag", 1);
			o.addBlock(null);
		} else if (TORRENT_STOP.equals(params.get(LABEL_ACTION))) {
			o.addValue("ids", Integer.parseInt((String)params.get(HASH_VALUE))  );
			o.addBlock("arguments");
			o.addValue("method", "torrent-stop");
			o.addValue("tag", 1);
			o.addBlock(null);
		} else if (TORRENT_REMOVE.equals(params.get(LABEL_ACTION))) {
			o.addValue("ids", Integer.parseInt((String)params.get(HASH_VALUE))  );
			o.addValue("delete-local-data", false);
			o.addBlock("arguments");
			o.addValue("method", "torrent-remove");
			o.addValue("tag", 1);
			o.addBlock(null);
		} else if (TORRENT_LIST.equals(params.get(LABEL_ACTION))) {
			List<String> lista = new ArrayList<String>();
			lista.add("id");
			lista.add("name");
			lista.add("leftUntilDone");
			lista.add("percentDone");
			lista.add("status");
			lista.add("totalSize");
			lista.add("uploadRatio");
			o.addValue("fields", lista);
			o.addBlock("arguments");
			o.addValue("method", "torrent-get");
			o.addValue("tag", 1);
			o.addBlock(null);
		}
		
		return callURL(o.print());
	}	

}
