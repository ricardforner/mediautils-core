import java.io.File;
import java.io.InputStream;
import java.io.StringReader;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;

import org.apache.noggit.JSONParser;

import com.it.media.util.FiltroPalabrasClave;
import com.it.media.util.NormalizarFichero;
import com.it.torrent.json.ParserJSONFactory;
import com.it.torrent.json.common.IParserJSON;
import com.it.torrent.plugin.common.IPluginTorrent;
import com.it.torrent.webui.common.ItemBase;

public class MediaDelivery extends MediaCommon {

	public static String CONFIGURATION_FILE = "MediaDelivery.properties";

	private boolean bAppDisabled;
	private int ratio_threshold;
	private String in_foldername;
	private String out_foldername;
	private String excludeFiles;
	private String[] excludeListFiles;
	private Hashtable<String, String> outFoldersListSpecial;
	private Hashtable<String, String> replaceWords;
	
	public MediaDelivery() {
		setLogName("mediautils");
		setProjectName("MediaDelivery");
	}

	private void initProperties() {
		Properties propiedades = new Properties();
		Properties propiedadesNormalize = new Properties();
		try {
			InputStream inputStream = initProperties(MediaDelivery.class, CONFIGURATION_FILE);
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
			if (propiedades.getProperty("ratio_min") != null) {
				ratio_threshold = Integer.valueOf(propiedades.getProperty("ratio_min")).intValue();
			}
			in_foldername = propiedades.getProperty("directorio_origen");
			out_foldername = propiedades.getProperty("directorio_destino");
			excludeFiles = propiedades.getProperty("exclude_files");
			if (excludeFiles != null) {
				excludeListFiles = excludeFiles.split(";");
			}
			String outFolderSpecial;
			String[] outFolderListSpecial;
			outFoldersListSpecial = new Hashtable<String, String>();
			for (int i=1; i<100; i++) {
				outFolderSpecial = propiedades.getProperty("dir"+i);
				if (outFolderSpecial == null) {
					break;
				}
				outFolderListSpecial = outFolderSpecial.split(";");
				outFoldersListSpecial.put(outFolderListSpecial[0].toLowerCase(), outFolderListSpecial[1]);
			}

			// PROPIEDADES DE NORMALIZACION
			InputStream inputStreamNormalize = initProperties(MediaDelivery.class, MediaNormalize.CONFIGURATION_FILE);
			if (inputStreamNormalize != null) {
				propiedadesNormalize.load(inputStreamNormalize);
				inputStreamNormalize.close();
			} else {
				logFile("[ERROR] Fichero de propiedades " + MediaNormalize.CONFIGURATION_FILE + " no encontrado");
			}
			// Variables de normalizacion
			String searchWords;
			String[] searchListWords;
			replaceWords = new Hashtable<String, String>();
			for (int i=1; i<100; i++) {
				searchWords = propiedadesNormalize.getProperty("strReplace"+i);
				if (searchWords == null) {
					break;
				}
				searchListWords = searchWords.split(";");
				replaceWords.put(searchListWords[0], /*searchListWords[1]*/
						searchWords.substring(searchWords.indexOf(";")+1) );
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean move(File fileTorrent, File dirDest) {
		boolean hasError = false;
		boolean success = true;
		if (fileTorrent.isDirectory()) {
			// Si se trata de un directorio, recorre el origen y va copiando
			String sDestFull = dirDest.getAbsolutePath() + File.separatorChar + fileTorrent.getName();
			File fDestFull = new File(sDestFull);
			if (fDestFull.exists()) {
	        	logFile("[ERROR] El directorio "+ sDestFull +" ya existe.");
	        	hasError = true;
			} else {
				if (!fDestFull.mkdir()) {
		        	logFile("[ERROR] El directorio "+ sDestFull +" no puede ser creado.");
		        	hasError = true;
				}
			}
			if (!hasError) {
				// Recorre y va copiando
				String lista[] = fileTorrent.list();
				for (int i=0; lista != null && i<lista.length; i++) {
					File origen = new File(fileTorrent, lista[i]);
			        logFile("[INFO] Moviendo fichero " + origen.getName() + " de " + in_foldername + " a " + sDestFull);
					success = move(origen, fDestFull);
					if (!success) {
			        	logFile("[ERROR] " + origen.getName() + " no se ha podido copiar.");
			        	hasError = true;
					}
				}
				// Borra el directorio
				if (!hasError) {
					if (!fileTorrent.delete()) {
						logFile("[ERROR] " + fileTorrent.getName() + " no se ha podido borrar.");
					}
				}
			}
		} else {
			// Copia de fichero de origen a destino
			success = fileTorrent.renameTo(new File(dirDest, fileTorrent.getName()));
		}
		return success;
	}
	
	private String getTargetDirectory(String pKey) {
		add_debug("Llamada a funcion getTargetDirectory");
		String sTarget = new String(out_foldername);
		if (!outFoldersListSpecial.isEmpty()) {
			String candidateKey;
			add_debug("Buscando directorio destino para " + pKey);
			for (Enumeration<String> e = outFoldersListSpecial.keys(); e.hasMoreElements(); ){
				candidateKey = e.nextElement();
				if (pKey.toLowerCase().indexOf(candidateKey) != -1) {
					add_debug("Clave " + candidateKey + " encontrada.");
					sTarget = outFoldersListSpecial.get(candidateKey);
					break;
				}
			}
		}
		return sTarget;
	}
	
	/********************************************************************
	 * 
	 * PROCESO
	 * 
	 ********************************************************************/ 
	private void proceso() {
        String stream = torrentList();
        if (stream == null) {
        	logFile("[INFO] Funcion torrentList no devuelve información (null)");
        	return;
        }
        StringReader sr = new StringReader(stream);
        JSONParser js = new JSONParser(sr);
        
		IParserJSON ilp = ParserJSONFactory.create(getClientTorrent());
		ilp.setJSON(js);

		// Recupero la lista de los elementos del grupo "torrents"
		ItemBase[] list = new ItemBase[ilp.size()];
		list = ilp.getList();

		FiltroPalabrasClave searchUtil = new FiltroPalabrasClave();
		searchUtil.setSearchWords(excludeListFiles);
		
	    NormalizarFichero normalizarFichero = new NormalizarFichero();
	    normalizarFichero.setWords(replaceWords);

		// Recorro la lista
	    ItemBase obj = null;
		boolean bFicherosMovidos = false;
		for (int i=0; i<list.length; i++) {
			obj = list[i];
			obj.setRatioThreshold(ratio_threshold);
			add_debug(obj.getName() + " borrable? " + obj.isCompletamenteServido());
			if (obj.isCompletamenteServido()) {
				logFile("[INFO] Torrent " + obj.getName() + " servido completamente.");
				// 1. Parando torrent. Si esta finalizado(parado) no lo paramos
				if (!obj.isFinished()) {
					torrentAction(IPluginTorrent.TORRENT_STOP, obj.getHash());
				}
				// 2. Filtro de palabras clave
				if (searchUtil.isFound(obj.getName())) {
					logFile("[INFO] Filtrado de torrent " + obj.getName() + " y no copiado.");
					continue;
				}
		        String rutaFileIn = in_foldername + File.separatorChar + obj.getName();
		        // 3. Inicio del proceso de mover fichero
		        File fileTorrent = new File(rutaFileIn);
		        String targetFolder = getTargetDirectory(obj.getName());
		        File dirDest = new File(targetFolder);
		        // 4. Comprobacion si existe directorio
		        boolean bExistsDir = dirDest.exists();
		        if (!bExistsDir) {
		        	logFile("[ERROR] El directorio "+ targetFolder +" no es accesible.");
		        	continue;
		        } else {
			        // 4a. Comprobacion si existe el fichero
		        	boolean bExistsFile = (new File(targetFolder + File.separatorChar + obj.getName())).exists();
			        if (bExistsFile) {
			        	logFile("[ERROR] El fichero " + obj.getName() + " ya existe en " + targetFolder);
			        	continue;
			        }
		        }
		        // 5. Moviendo fichero
		        logFile("[INFO] Moviendo fichero " + obj.getName() + " de " + in_foldername + " a " + targetFolder);
		        boolean success = move(fileTorrent, dirDest);
		        if (success) {
		        	bFicherosMovidos = true;
		        } else {
		        	logFile("[ERROR] " + obj.getName() + " no se ha podido copiar.");
		        	continue;
		        }
		        // 6. Normalizacion del fichero
		        logFile("[INFO] Normalizando fichero " + obj.getName());
		        boolean successNormalizar = normalizarFichero.run(targetFolder, obj.getName());
		        if (!successNormalizar) {
		        	logFile("[ERROR] " + obj.getName() + " no se ha podido normalizar.");
		        }
		        // 7. Eliminando torrent
		        torrentAction(IPluginTorrent.TORRENT_REMOVE, obj.getHash());

			}
			obj = null;
		}
        if (!bFicherosMovidos) {
        	logFile("[INFO] No hay elementos que cumplan los criterios para ser parados/movidos.");
        }
	}
	
	private void inicio() {
		// LOG de inicio
		initLog();
		// Lectura properties
		initProperties();
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

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("MediaDelivery v1.0 (C) Ricard Forner");
		MediaDelivery obj = new MediaDelivery();
		obj.inicio();
		obj.fin();
		System.exit(0);
	}

}
