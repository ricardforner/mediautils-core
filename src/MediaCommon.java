import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import com.it.downloader.DownloaderConstants;
import com.it.downloader.plugin.ConnDownloaderParams;
import com.it.downloader.plugin.PluginDownloaderFactory;
import com.it.downloader.plugin.common.IDownloaderPlugin;
import com.it.torrent.TorrentConstants;
import com.it.torrent.plugin.ConnectionParams;
import com.it.torrent.plugin.PluginFactory;
import com.it.torrent.plugin.common.IPluginTorrent;
import com.it.util.LogManager;
import com.it.util.XmlProperties;

public class MediaCommon {

	public static final String PROPERTY_PATH	= "MEDIAUTILS_PROPERTIES";
	public static String CONFIGURATION_FILE		= "MediaCommon.xml";

	private String logName;
	private String projectName;
	private int numLogs;
	private String folderLogs;
	private String folderProperties = "";
	private boolean debug;
	private String client_torrent;
	private ConnectionParams connParams;
	private IPluginTorrent torrentPlugin;
	private IDownloaderPlugin downloaderPlugin;

	public MediaCommon() {
		XmlProperties propiedades = new XmlProperties();
		File fileXMLProperties = null;
		try {
			fileXMLProperties = new File(CONFIGURATION_FILE);
			propiedades.load(fileXMLProperties);
		} catch (FileNotFoundException fnfe) {
			System.err.println(fnfe.toString());
		} catch (IOException ioe) {
			System.err.println(ioe.toString());
		}
		
		connParams = new ConnectionParams();
		
		if (propiedades.getProperty("host_ip") != null) {
			connParams.setHostIP(propiedades.getProperty("host_ip"));
		}
		if (propiedades.getProperty("host_port") != null) {
			connParams.setHostPort(Integer.valueOf(propiedades.getProperty("host_port")).intValue());
		}
		if (propiedades.getProperty("username") != null) {
			connParams.setAuthUsername(propiedades.getProperty("username"));
		}
		if (propiedades.getProperty("password") != null) {
			connParams.setAuthPassword(propiedades.getProperty("password"));
		}
		if (propiedades.getProperty("directorio_logs") != null) {
			folderLogs = propiedades.getProperty("directorio_logs");
		}
		if (propiedades.getProperty("directorio_properties") != null
				&& !"".equals(propiedades.getProperty("directorio_properties"))) {
			folderProperties = propiedades.getProperty("directorio_properties") + File.separatorChar;
		}
		if (propiedades.getProperty("num_logs") != null) {
			numLogs = Integer.valueOf(propiedades.getProperty("num_logs")).intValue();
		}
		if (propiedades.getProperty("nivel_debug") != null) {
			debug = "1".equals(propiedades.getProperty("nivel_debug"));
		}
		if (propiedades.getProperty("client_torrent") != null) {
			client_torrent = propiedades.getProperty("client_torrent");
		}
		initClientTorrent();
	}
	
	private void initClientTorrent() {
		torrentPlugin = PluginFactory.create(client_torrent, connParams);
		torrentPlugin.initialize();
	}

	protected void initClientDownloader(String client, String jDownloaderHost, int jDownloaderPort) {
		ConnDownloaderParams params = new ConnDownloaderParams();
		params.setHostIP(jDownloaderHost);
		params.setHostPort(jDownloaderPort);
		downloaderPlugin = PluginDownloaderFactory.create(client, params);
		downloaderPlugin.initialize();
	}
	
	public boolean isDebug() {
		return debug;
	}
	
	protected String getClientTorrent() {
		return torrentPlugin.getNamePlugin();
	}

	public void setLogName(String logName) {
		this.logName = logName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	protected void add_debug(String comment) {
		if (isDebug())
			logFile("[DEBUG] " + comment);
	}
	
	protected void initLog() {
		LogManager.getInstance().init(this.folderLogs + this.logName + "-", "yyyyMMdd", this.numLogs);
		logFile("*** Aplicación " + this.projectName + " iniciada ***");
	}

	protected void endLog() {
		logFile("Aplicación " + this.projectName + " finalizada.");
		try {
			LogManager.getInstance().destroy();
		} catch (IOException e) {
		}
	}

	protected void logFile(String s) {
		// Log a fichero
		LogManager.getInstance().log(s);
	}

	protected void logFile(Throwable throwable) {
		// log a fichero
		LogManager.getInstance().log("[ERROR] ", throwable);
	}

	@SuppressWarnings("unchecked")
	protected InputStream initProperties(Class pClass, String pPropertiesName) {
		if (folderProperties == null || "".equals(folderProperties)) {
			ClassLoader classLoader = pClass.getClassLoader();
			return classLoader.getResourceAsStream(pPropertiesName);
		}
		try {
			return new FileInputStream(folderProperties + pPropertiesName);
		} catch (Exception e) { }
		return null;
	}

	protected String callURLExterna(String url) {
		DefaultHttpClient httpclient = new DefaultHttpClient();

		if (url == null) {
			logFile("[ERROR] URL contiene el valor null");
			return null;
		}

        HttpGet httpget = new HttpGet(url);
        add_debug("Executing request: " + httpget.getRequestLine());
        
        String responseBody = null;
		try {
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			responseBody = httpclient.execute(httpget, responseHandler);
			add_debug("Request response: " + responseBody);
		} catch (Exception e) {
			logFile("[ERROR] " + e.getMessage());
			logFile("[ERROR] " + e.getCause().getMessage());
		} finally {
			httpclient.getConnectionManager().shutdown();
		}
        return responseBody;
	}

	protected String encodeURL(String pUrl) {
		String newUrl = null;
		try {
			newUrl = URLEncoder.encode(pUrl, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logFile(e);
		}
		return newUrl;
	}

	// FUNCIONES DE LIST
	protected String torrentList() {
    	logFile("[INFO] Recuperando lista de cliente torrent.");
		add_debug("Llamada funcion getfiles");
		StringBuffer url = new StringBuffer();

		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put(IPluginTorrent.LABEL_ACTION, IPluginTorrent.TORRENT_LIST);

		if (TorrentConstants.CLIENT_UTORRENT.equals(client_torrent)) {
			url.append("?list=1");
		} else if (TorrentConstants.CLIENT_TRANSMISSION.equals(client_torrent)) {
			url.append("");
		}
		return torrentPlugin.callURL(url.toString(), params);
	}
	
	protected String downloaderList() {
    	logFile("[INFO] Recuperando lista de aplicación downloader.");
    	StringBuffer url = new StringBuffer();
		
    	HashMap<String, Object> params = new HashMap<String, Object>();
    	params.put(IDownloaderPlugin.LABEL_ACTION, IDownloaderPlugin.DOWNLOADER_LIST);

		if (DownloaderConstants.CLIENT_JDOWNLOADER.equals(downloaderPlugin.getNamePlugin())) {
			url.append("get/downloads/alllist");
		}
		return downloaderPlugin.callURL(url.toString(), params);
	}
	
	// FUNCIONES DE ACCION
	protected String torrentAction(String action, String hash) {
		logFile("[INFO] Accion " + action + " para el HASH " + hash);
		add_debug("Llamada funcion " + action);
		StringBuffer url = new StringBuffer();

		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put(IPluginTorrent.LABEL_ACTION, action);
		params.put(IPluginTorrent.HASH_VALUE, hash);

		if (TorrentConstants.CLIENT_UTORRENT.equals(client_torrent)) {
			url.append("?action=");
			url.append(action);
			url.append("&hash=");
			url.append(hash);
		} else if (TorrentConstants.CLIENT_TRANSMISSION.equals(client_torrent)) {

		}
		return torrentPlugin.callURL(url.toString(), params);
	}

	// FUNCIO DE ADICION EN LISTA
	protected String torrentDescarga(String pUrl) {
		logFile("[INFO] Accion add-url para la url " + pUrl);
		add_debug("Llamada funcion torrentDescarga");
		StringBuffer url = new StringBuffer();
		
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put(IPluginTorrent.LABEL_ACTION, IPluginTorrent.TORRENT_ADD);
		
		if (TorrentConstants.CLIENT_UTORRENT.equals(client_torrent)) {
			url.append("?action=add-url&s=");
			url.append(encodeURL(pUrl));
		} else if (TorrentConstants.CLIENT_TRANSMISSION.equals(client_torrent)) {
			url.append(pUrl);
		}
		return torrentPlugin.callURL(url.toString(), params);
	}

}
