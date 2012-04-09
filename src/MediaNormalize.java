import java.io.File;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Properties;

import com.it.media.util.NormalizarFichero;

public class MediaNormalize extends MediaCommon {

	public static String CONFIGURATION_FILE = "MediaNormalize.properties";

	private boolean bAppDisabled;
	private String in_foldername;
	private Hashtable<String, String> replaceWords;

	public MediaNormalize() {
		setLogName("mediautils");
		setProjectName("MediaNormalize");
	}
	
	private void initProperties() {
		Properties propiedades = new Properties();
		try {
			InputStream inputStream = initProperties(MediaNormalize.class, CONFIGURATION_FILE);
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
			in_foldername = propiedades.getProperty("directorio_analisis");
			
			// Variables de normalizacion
			String searchWords;
			String[] searchListWords;
			replaceWords = new Hashtable<String, String>();
			for (int i=1; i<100; i++) {
				searchWords = propiedades.getProperty("strReplace"+i);
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
	
	/********************************************************************
	 * 
	 * PROCESO
	 * 
	 ********************************************************************/ 
	private void proceso() {
		File folder = null;
		try {
			folder = new File(in_foldername);
		} catch (Exception e) {
			logFile("[ERROR] El directorio " + in_foldername + " no existe.");
			return;
		}
		logFile("[INFO] Leyendo directorio " + in_foldername);
	    File[] listOfFiles = folder.listFiles();
	    if (listOfFiles == null) {
			logFile("[ERROR] No se ha podido acceder a " + in_foldername);
			return;
	    }
	    NormalizarFichero normalizarFichero = new NormalizarFichero();
	    normalizarFichero.setWords(replaceWords);
	    for (int i = 0, j = 0; i < listOfFiles.length; i++) {
	        if (listOfFiles[i].isFile()) {
	        	j++;
	        	String fichero = listOfFiles[i].getName();
	        	logFile("[INFO] "+(j)+". Fichero " + fichero + " encontrado.");
	        	boolean success = normalizarFichero.run(in_foldername, fichero);
		        if (!success) {
		        	logFile("[ERROR] " + fichero + " no se ha podido renombrar.");
		        }	        	
	        }
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
		System.out.println("MediaNormalize v1.0 (C) Ricard Forner");
		MediaNormalize obj = new MediaNormalize();
		obj.inicio();
		obj.fin();
		System.exit(0);
	}

}
