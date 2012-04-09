import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Properties;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.noggit.JSONParser;

import com.it.downloader.DownloaderConstants;
import com.it.downloader.parser.ParserDownloaderFactory;
import com.it.downloader.parser.common.IParserDownloader;
import com.it.media.renderer.HBeanConfiguration;
import com.it.media.renderer.ItemCellRendererFactory;
import com.it.media.renderer.NotifyComponent;
import com.it.media.renderer.NotifyRenderer;
import com.it.media.renderer.common.ItemCellRenderer;
import com.it.media.util.ComprobarTemporada;
import com.it.media.util.FiltroFecha;
import com.it.torrent.json.ParserJSONFactory;
import com.it.torrent.json.common.IParserJSON;
import com.it.torrent.plugin.common.IPluginTorrent;
import com.it.torrent.webui.common.ItemBase;
import com.it.torrent.webui.common.ItemBaseComparator;

public class MediaNotify extends MediaCommon {

	public static String CONFIGURATION_FILE = "MediaNotify.properties";
	
	private String resourceDir;
	private boolean bAppDisabled;
	private String smtpHost;
	private String mailFrom;
	private String mailUser;
	private String mailPassword;
	private String mailTo;
	private String mailSubject;
	private int smtpPort;
	private boolean smtpUseSSL;
	private int ratio_threshold;
	private int min_seeding;
	private boolean bNotifyByEmail;
	private boolean bNotifyToNMT;
	private String notifyNMTDir;
	// Variables Downloader
	private boolean bJDownloader;
	private String jDownloaderHost;
	private int jDownloaderPort;
	
	private Hashtable<String, String> seasonLastEpisode;

	private FiltroFecha filtroFecha;
	
	public MediaNotify() {
		setLogName("mediautils");
		setProjectName("MediaNotify");
	}

	private void initContext() {
		filtroFecha = new FiltroFecha();
		resourceDir = System.getProperty("user.dir") + File.separatorChar + "resource";
	}

	private void initProperties() {
		Properties propiedades = new Properties();
		Properties propiedadesDelivery = new Properties();
		try {
			InputStream inputStream = initProperties(MediaNotify.class, CONFIGURATION_FILE);
			if (inputStream != null) {
				propiedades.load(inputStream);
				inputStream.close();
			} else {
				logFile("[ERROR] Fichero de propiedades " + CONFIGURATION_FILE + " no encontrado");
			}

			bAppDisabled = false;
			if (propiedades.getProperty("app_disabled") != null) {
				bAppDisabled = "1".equals(propiedades.getProperty("app_disabled"));
			}
			min_seeding = 0;
			if (propiedades.getProperty("min_seeding") != null) {
				min_seeding = Integer.valueOf(propiedades.getProperty("min_seeding")).intValue();
			}
			// Propiedades correo
			bNotifyByEmail = false;
			if (propiedades.getProperty("sendmail_interval") != null) {
				String interval = propiedades.getProperty("sendmail_interval");
				String[] intervalHour = interval.split("-");
				bNotifyByEmail = filtroFecha.inHourMinuteInterval(intervalHour);
			}
			if (propiedades.getProperty("sendmail_excludedays") != null) {
				String nDays = propiedades.getProperty("sendmail_excludedays");
				String[] sNDays = nDays.split(";");
				bNotifyByEmail = bNotifyByEmail & !filtroFecha.excludeDay(sNDays);
			}
			smtpHost = propiedades.getProperty("mailSMTP");
			smtpPort = 25;
			if (propiedades.getProperty("mailPort") != null) {
				smtpPort = Integer.valueOf(propiedades.getProperty("mailPort")).intValue();
			}
			smtpUseSSL = "1".equals(propiedades.getProperty("mailUseSSL"));
			mailFrom = propiedades.getProperty("mailFrom");
			mailUser = propiedades.getProperty("mailUser");
			mailPassword = propiedades.getProperty("mailPassword");
			mailTo = propiedades.getProperty("mailTo");
			mailSubject = propiedades.getProperty("mailSubject");
			// Propiedades NMT
			bNotifyToNMT = false;
			if (propiedades.getProperty("notify_nmt") != null) {
				bNotifyToNMT = "1".equals(propiedades.getProperty("notify_nmt"));
			}
			if (propiedades.getProperty("notify_nmt_interval") != null) {
				String interval = propiedades.getProperty("notify_nmt_interval");
				String[] intervalHour = interval.split("-");
				bNotifyToNMT = bNotifyToNMT & filtroFecha.inHourMinuteInterval(intervalHour);
			}
			notifyNMTDir = propiedades.getProperty("notify_dir_nmt");
			// Propiedades JDownloader
			bJDownloader = false;
			if (propiedades.getProperty("jdownloader_source") != null) {
				bJDownloader = "1".equals(propiedades.getProperty("jdownloader_source"));
			}
			jDownloaderHost = propiedades.getProperty("jdownloader_host_ip");
			jDownloaderPort = Integer.valueOf(propiedades.getProperty("jdownloader_port")).intValue();

			// PROPIEDADES DE DELIVERY
			InputStream inputStreamDelivery = initProperties(MediaNotify.class, MediaDelivery.CONFIGURATION_FILE);
			if (inputStreamDelivery != null) {
				propiedadesDelivery.load(inputStreamDelivery);
				inputStreamDelivery.close();
			} else {
				logFile("[ERROR] Fichero de propiedades " + MediaDelivery.CONFIGURATION_FILE + " no encontrado");
			}
			// Variables de delivery
			if (propiedadesDelivery.getProperty("ratio_min") != null) {
				ratio_threshold = Integer.valueOf(propiedadesDelivery.getProperty("ratio_min")).intValue();
			}
			String numEpisodes;
			String[] listNumEpisodes;
			seasonLastEpisode = new Hashtable<String, String>();
			for (int i=1; i<100; i++) {
				numEpisodes = propiedadesDelivery.getProperty("numepisodes"+i);
				if (numEpisodes == null) {
					break;
				}
				listNumEpisodes = numEpisodes.split(";");
				seasonLastEpisode.put(listNumEpisodes[0].toLowerCase(), listNumEpisodes[1]);
			}

			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initHBean() {
		HBeanConfiguration configuration = new HBeanConfiguration();
		configuration.setRepositoryDir(resourceDir);
	}

	private void initDownloader() {
		if (bJDownloader) {
			logFile("[INFO] Cargando fuente " + DownloaderConstants.CLIENT_JDOWNLOADER + ".");
			initClientDownloader(DownloaderConstants.CLIENT_JDOWNLOADER, jDownloaderHost, jDownloaderPort);
		}
	}

	private HtmlEmail prepararMail() throws EmailException {
    	logFile("[INFO] Preparando el contenido de notificación.");
		HtmlEmail email = new HtmlEmail();
		email.setAuthentication(mailUser, mailPassword);
		email.setHostName(smtpHost);
		email.setSmtpPort(smtpPort);
		email.setSSL(smtpUseSSL);
		email.addTo(mailTo);
		email.setFrom(mailFrom, "MediaUtils");
		email.setSubject(mailSubject);
		email.setTextMsg("Your email client does not support HTML messages");
		return email;
	}

	private boolean enviarMail(HtmlEmail email, String msgHtml) {
		boolean statusOk = true;
    	logFile("[INFO] Envío de estado por correo electrónico.");
		try {
			email.setHtmlMsg(msgHtml);
			email.send();
		} catch (Exception e) {
			statusOk = false;
			logFile(e);
		}
		return statusOk;
	}

	/**
	 * Notificacion por mail
	 */
	private void notifyToNMT(ArrayList<NotifyComponent> listaDescarga) {
		// Pasos previos
		if (notifyNMTDir == null || "".equals(notifyNMTDir)) {
        	logFile("[ERROR] El directorio de notificación NMT no esta definido.");
        	return;
		}
        File dirDest = new File(notifyNMTDir);
        // Comprobacion si existe directorio
        boolean bExistsDir = dirDest.exists();
        if (!bExistsDir) {
        	logFile("[ERROR] El directorio "+ notifyNMTDir+" no es accesible.");
        	return;
        }
        // Creacion fichero de estado
    	File fileDest = new File(notifyNMTDir + File.separatorChar + "[EBOX] status.html");
    	Writer output = null;
    	try {
    		output = new BufferedWriter(new FileWriter(fileDest));
    	} catch (IOException e) {
    		logFile(e);
    		return;
		}
    	
    	logFile("[INFO] Preparando el contenido de notificación.");
    	// Generacion del contenido del mensaje
		String tableWidth = "100%";
		NotifyRenderer renderer = new NotifyRenderer();
		renderer.setHtmlTableWidth(tableWidth);
		renderer.setHeaderCellRenderer(ItemCellRendererFactory.create(getClientTorrent(), true));
		renderer.setDataCellRenderer(ItemCellRendererFactory.create(getClientTorrent(), false));
		renderer.setPrototypeName("TorrentItem");
		renderer.setIteratorName("publicaNMT");
		renderer.setListaElementos(listaDescarga);

		// Preparacion contenido
		StringBuffer sbHtml = new StringBuffer();
		sbHtml.append("<h1><center>"+ filtroFecha.getFechaFormateada() +"</center></h1>\n");
		sbHtml.append("<hr>\n");
		sbHtml.append(renderer.getHtml());
		sbHtml.append("<hr>\n");
		sbHtml.append("<h2>Total: ");
		sbHtml.append(renderer.getListaElementos().size());
		sbHtml.append(" registro"+ ((renderer.getListaElementos().size()==1)?"":"s") +"</h2>\n");
	
		try {
			if (output != null) output.write(sbHtml.toString());
		} catch (IOException e) {}
		try {
			if (output != null) output.close();
		} catch (IOException e) {}
		
    	logFile("[INFO] Estado enviado a NMT.");
	}
	
	/**
	 * Notificacion por mail
	 */
	private void notifyToMail(ArrayList<NotifyComponent> listaDescarga, ArrayList<NotifyComponent> listaUltimoEpisodio) {
		// Pasos previos
		String emailCIDSeeding = resourceDir + File.separatorChar + "seeding.gif";
		String emailCIDFinished = resourceDir + File.separatorChar + "finished.gif";
		String emailCIDDownloading = resourceDir + File.separatorChar + "downloading.gif";
		String emailCIDSeeded = resourceDir + File.separatorChar + "seeded.gif";
		// Preparar correo electronico
		HtmlEmail email = null;
		String cidStatusSeeding = "";
		String cidStatusFinished = "";
		String cidStatusDownloading = "";
		String cidStatusSeeded = "";
		try {
			email = prepararMail();
			cidStatusSeeding = email.embed(new File(emailCIDSeeding), "Sirviendo");
			cidStatusFinished = email.embed(new File(emailCIDFinished), "Finalizado");
			cidStatusDownloading = email.embed(new File(emailCIDDownloading), "En descarga");
			cidStatusSeeded = email.embed(new File(emailCIDSeeded), "Servido");
		} catch (EmailException e) {
			logFile(e);
		}
		// Generacion del contenido del mensaje
		String tableWidth = "620";
		NotifyRenderer renderer = new NotifyRenderer();
		renderer.setHtmlTableWidth(tableWidth);
		renderer.setHeaderCellRenderer(ItemCellRendererFactory.create(getClientTorrent(), true));
		ItemCellRenderer dataCellRenderer = ItemCellRendererFactory.create(getClientTorrent(), false);
			dataCellRenderer.setCidStatusSeeding(cidStatusSeeding);
			dataCellRenderer.setCidStatusFinished(cidStatusFinished);
			dataCellRenderer.setCidStatusDownloading(cidStatusDownloading);
			dataCellRenderer.setCidStatusSeeded(cidStatusSeeded);
		renderer.setDataCellRenderer(dataCellRenderer);
		renderer.setPrototypeName("TorrentItem");

		// Preparacion contenido email
		StringBuffer sbHtml = new StringBuffer();
		sbHtml.append("<table width=\""+tableWidth+"\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"border:1px solid #d5d5d5;\">\n<tr>\n");
		sbHtml.append("<td height=\"20\" align=\"center\" style=\"background-color: #899ab7; color: #ffffff;\"><font size=\"1\" face=\"Verdana, Arial, Helvetica, sans-serif\">\n");
		sbHtml.append(mailSubject.toUpperCase() + " " + filtroFecha.getFechaFormateada() +"</font></td>\n");
		sbHtml.append("</tr>\n</table>\n");

		// 01. Lista en descarga
		renderer.setIteratorName("lista");
		renderer.setListaElementos(listaDescarga);
		sbHtml.append(renderer.getHtml());
		
		sbHtml.append("<table width=\""+tableWidth+"\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"border-top:1px dashed #d5d5d5;border-bottom:1px dashed #d5d5d5;\">\n<tr>\n");
		sbHtml.append("<td height=\"20\" align=\"left\" style=\"background-color: #f3f3f3; color: #3165cd;\"><font size=\"1\" face=\"Verdana, Arial, Helvetica, sans-serif\">\n");
		sbHtml.append("&nbsp;Total de ficheros procesados: ");
		sbHtml.append(renderer.getListaElementos().size());
		sbHtml.append(" registro"+ ((renderer.getListaElementos().size()==1)?"":"s") +". </font></td>\n");
		sbHtml.append("</tr>\n</table>\n");

		// 02. Ultimo episodio
		if (!listaUltimoEpisodio.isEmpty()) {
			renderer.setIteratorName("listaSeasonEnded");
			renderer.setListaElementos(listaUltimoEpisodio);
			sbHtml.append(renderer.getHtml());
				
			sbHtml.append("<table width=\""+tableWidth+"\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"border-top:1px dashed #d5d5d5;border-bottom:1px dashed #d5d5d5;\">\n<tr>\n");
			sbHtml.append("<td height=\"20\" align=\"left\" style=\"background-color: #f3f3f3; color: #3165cd;\"><font size=\"1\" face=\"Verdana, Arial, Helvetica, sans-serif\">\n");
			sbHtml.append("&nbsp;Total de cierre de temporada: ");
			sbHtml.append(renderer.getListaElementos().size());
			sbHtml.append(" serie"+ ((renderer.getListaElementos().size()==1)?"":"s") +". </font></td>\n");
			sbHtml.append("</tr>\n</table>\n");
		}
		
		// Pie del email
		sbHtml.append("<table width=\""+tableWidth+"\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\n<tr>\n");
		sbHtml.append("<td height=\"20\" align=\"left\"><font size=\"1\" face=\"Verdana, Arial, Helvetica, sans-serif\">&nbsp;<b>Estado</b>");
		sbHtml.append("&nbsp;&nbsp;<img src=\"cid:"+cidStatusDownloading+"\" border=0 alt=''>&nbsp;En descarga");
		sbHtml.append("&nbsp;&nbsp;&nbsp;<img src=\"cid:"+cidStatusFinished+"\" border=0 alt=''>&nbsp;Finalizado");
		sbHtml.append("&nbsp;&nbsp;&nbsp;<img src=\"cid:"+cidStatusSeeding+"\" border=0 alt=''>&nbsp;Sirviendo");
		sbHtml.append("&nbsp;&nbsp;&nbsp;<img align=\"bottom\"  src=\"cid:"+cidStatusSeeded+"\" border=0 alt=''>&nbsp;Servido");
		sbHtml.append("</font></td>\n");
		sbHtml.append("</tr>\n</table>\n");

		// Envio del correo electrónico
		enviarMail(email, sbHtml.toString());
	}

	/********************************************************************
	 * 
	 * PROCESO
	 * 
	 ********************************************************************/ 
	private void proceso() {

		// Recupero la lista de los elementos del grupo "torrents"
        String stream = torrentList();
        if (stream == null) {
        	logFile("[INFO] Funcion torrentList no devuelve información (null).");
        	return;
        }
        StringReader sr = new StringReader(stream);
        JSONParser js = new JSONParser(sr);
		IParserJSON ilp = ParserJSONFactory.create(getClientTorrent());
		ilp.setJSON(js);
		ItemBase[] list = new ItemBase[ilp.size()];
		list = ilp.getList();
		// Ordenacion de la lista por Nombre
		Arrays.sort(list, new ItemBaseComparator(ItemBaseComparator.ORDER_BY_NAME));
		
		// Recupero la lista de los elementos del grupo "downloader"
		ItemBase[] listDownloader = null;
		if (bJDownloader) {
	        String streamDownloader = downloaderList();
	        if (streamDownloader == null) {
	        	logFile("[INFO] Funcion downloaderList no devuelve información (null).");
	        } else {
		        IParserDownloader ipd = ParserDownloaderFactory.create(DownloaderConstants.CLIENT_JDOWNLOADER);
				ipd.setContent(streamDownloader);
				listDownloader = new ItemBase[ipd.size()];
				listDownloader = ipd.getList();
				// Ordenacion de la lista por Nombre
				Arrays.sort(listDownloader, new ItemBaseComparator(ItemBaseComparator.ORDER_BY_NAME));
	        }
		}
		
		// Comprobación de último episodio
		ComprobarTemporada comprobarTemporada = new ComprobarTemporada();
		comprobarTemporada.setEpisodesGuide(seasonLastEpisode);
		
		// Recorro la lista de TorrentItem(s)
		ArrayList<NotifyComponent> listaDescarga = new ArrayList<NotifyComponent>();
		ArrayList<ItemBase> listaPararTorrents = new ArrayList<ItemBase>();
		ArrayList<NotifyComponent> listaUltimoEpisodio = new ArrayList<NotifyComponent>();
		ItemBase obj = null;
		ItemBase objToSeed = null;
		int numObjSeeding=0;
		for (int i=0; i<list.length; i++) {
			obj = list[i];
			obj.setRatioThreshold(ratio_threshold);
			listaDescarga.add(obj);
			if (obj.isCompletamenteServido()) {
				listaPararTorrents.add(obj);
			} else {
				numObjSeeding = (obj.isSeeding()) ? numObjSeeding+1 : numObjSeeding;
				objToSeed = (objToSeed == null && (obj.isFinished())) ? obj : objToSeed;
			}
			if (comprobarTemporada.isFinalEpisodeSeason(obj.getName())) {
				listaUltimoEpisodio.add(obj);
			}
		}
		// Recorro la lista de Item(s) de Descarga
		for (int i=0; listDownloader != null && i<listDownloader.length; i++) {
			listaDescarga.add(listDownloader[i]);
		}

		// Parando torrents finalizados
		for (ItemBase item : listaPararTorrents) {
			logFile("[INFO] Torrent " + item.getName() + " servido completamente.");
			// Si esta finalizado(parado) no lo paramos
			if (item.isFinished()) {
				logFile("[INFO] Ninguna acción sobre " + item.getName() + ".");
			} else {
				torrentAction(IPluginTorrent.TORRENT_STOP, item.getHash());
			}
		}
		// Si todo esta descargado y nada sirviendo, activamos un candidato.
		if (numObjSeeding < min_seeding && objToSeed != null) {
			logFile("[INFO] Torrent " + objToSeed.getName() + " iniciado para servir.");
	        torrentAction(IPluginTorrent.TORRENT_START, objToSeed.getHash());
		}
		// Si estamos en el intervalo de envio de mail se envia notificacion por correo electronico
		if (bNotifyByEmail) {
			notifyToMail(listaDescarga, listaUltimoEpisodio);
		} else {
			logFile("[INFO] Notificación por correo electrónico fuera del intervalo de tiempo.");
		}
		// Notificacion a NMT
		if (bNotifyToNMT) {
			notifyToNMT(listaDescarga);
		} else {
			logFile("[INFO] Notificación a NMT desactivada o fuera del intervalo de tiempo.");
		}
	}
	
	private void inicio() {
		// Inicio Contexto
		initContext();
		// LOG de inicio
		initLog();
		// Lectura properties
		initProperties();
		// Inicio HBeans
		initHBean();
		// Inicio acceso fuentes Downloader
		initDownloader();
		// Inicio del proceso
		if (this.bAppDisabled) {
			logFile("[INFO] Aplicación desactivada.");
		} else {
			proceso();
		}
	}

	private void fin() {
		endLog();
	}

	public static void main(String[] args) {
		System.out.println("MediaNotify v1.0 (C) Ricard Forner");
		MediaNotify obj = new MediaNotify();
		obj.inicio();
		obj.fin();
		System.exit(0);
	}

}
