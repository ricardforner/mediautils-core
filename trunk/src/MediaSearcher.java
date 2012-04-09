import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

import com.it.media.FichaItem;
import com.it.media.util.FiltroFecha;
import com.it.media.util.FiltroPalabrasClave;
import com.it.torrent.fuentes.CommonWebParser;
import com.it.torrent.fuentes.IFichaWebParser;
import com.it.torrent.fuentes.IListaWebParser;

public class MediaSearcher extends MediaCommon {

	public static String CONFIGURATION_FILE = "MediaSearcher.properties";

	private String searchSite;
	private String searchWords;
	private boolean bSearchTypeRss;
	private String[] searchListWords;
	private String fuente;

	public MediaSearcher() {
		setLogName("mediautils");
		setProjectName("MediaSearcher");
	}

	private void initProperties() {
		Properties propiedades = new Properties();
		InputStream inputStream = null;
		try {
			String rutaProperties = System.getProperty(PROPERTY_PATH);
			if (rutaProperties == null) {
				Class<MediaSearcher> thisClass = MediaSearcher.class;
				ClassLoader classLoader = thisClass.getClassLoader();
				inputStream = classLoader.getResourceAsStream(CONFIGURATION_FILE);
			} else {
				inputStream = new FileInputStream(rutaProperties + CONFIGURATION_FILE);
			}
			if (inputStream != null) {
				propiedades.load(inputStream);
				inputStream.close();
			} else {
				logFile("[ERROR] Fichero de propiedades " + CONFIGURATION_FILE + " no encontrado");
			}

			fuente = propiedades.getProperty("fuente");
			searchSite = propiedades.getProperty("search_site");
			bSearchTypeRss = "1".equals(propiedades.getProperty("search_type"));
			ArrayList<String> searchWordsArr = new ArrayList<String>();
			for (int i=1; i<100; i++) {
				searchWords = propiedades.getProperty("search_words"+i);
				if (searchWords == null) {
					break;
				}
				searchListWords = searchWords.split(";");
				for (int j=0; j<searchListWords.length; j++) {
					searchWordsArr.add(searchListWords[j]);
				}
			}
			searchListWords = searchWordsArr.toArray(new String[] {});
			
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
		CommonWebParser parser = new CommonWebParser(fuente);
		ArrayList<FichaItem> listaFichas = new ArrayList<FichaItem>();
		IListaWebParser lista = parser.createImplementationLista();
		if (lista == null) {
			logFile("[ERROR] No existe implementación de lista para la fuente '"+ fuente +"'");
			return;
		}
		// PASO 1. Obtencion de todas las novedades
		logFile("[INFO] Llamada a " + searchSite);
		lista.setContenido(callURLExterna(searchSite));
		lista.setFuenteRSS(bSearchTypeRss);
		listaFichas = lista.parse();

		// PASO 2. Filtro de fecha
		FiltroFecha filtroFecha = new FiltroFecha();
		logFile("[INFO] Recuperando registros del dia " + filtroFecha.getFiltroFechaFormateado());
		ArrayList<FichaItem> listaFichasFiltrado = filtroFecha.getLista(listaFichas);

		// PASO 3. Filtro de palabras clave
		FiltroPalabrasClave searchUtil = new FiltroPalabrasClave();
		searchUtil.setSearchWords(searchListWords);
		
		// PASO 4. Obtencion de todas las fichas
		ArrayList<FichaItem> listaTorrents = new ArrayList<FichaItem>();
		FichaItem item = null;
		for (int i=0; listaFichasFiltrado!=null && i<listaFichasFiltrado.size(); i++) {
			item = listaFichasFiltrado.get(i);
			System.out.println(item.getNombre());
			// Filtro de palabras clave
			if (!searchUtil.isFound(item.getNombre())) {
				continue;
			}
			
			logFile("[INFO] " + item.getNombre() + " (" + item.getURLFichaInformacion() + ") Publicacion: " + item.getFecha() );
			IFichaWebParser ficha = parser.createImplementationFicha();
			if (ficha == null) {
				logFile("[ERROR] No existe implementación de ficha para la fuente '"+ fuente +"'");
			}
			// PASO 5. Parser en cada pagina de fichas
			if (item.getURLFichaInformacion() == null) {
				logFile("[ERROR] URL de ficha no facilitada");
			} else {
				ficha.setContenido( callURLExterna(item.getURLFichaInformacion()) );
				String sUrlDescarga = ficha.parse();
				logFile("[INFO] URL de descarga: " + sUrlDescarga);
				item.setURLDescarga(sUrlDescarga);
				listaTorrents.add(item);
			}
		}
		
		// PASO 5. Lista de descargas
		if (listaTorrents.isEmpty()) {
			logFile("[INFO] No hay elementos que cumplan los criterios de búsqueda");
		} else {
			logFile("[INFO] Inicio proceso de descargas");
		}
		for (FichaItem fichaItem: listaTorrents) {
			if (fichaItem.getURLDescarga() == null) {
				logFile("[ERROR] No disponible URL de descarga de " + fichaItem.getNombre());
			} else {
				logFile("[INFO] Obteniendo torrent de " + fichaItem.getURLDescarga());
				torrentDescarga(fichaItem.getURLDescarga());
			}
		}
	}
	
	private void inicio() {
		// LOG de inicio
		initLog();
		// Lectura properties
		initProperties();
		// Inicio del proceso
		proceso();
	}

	private void fin() {
		endLog();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("MediaSearcher v1.0 (C) Ricard Forner");
		MediaSearcher obj = new MediaSearcher();
		obj.inicio();
		obj.fin();
		System.exit(0);

	}

}
