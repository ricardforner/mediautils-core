package com.it.torrent.fuentes.divxtotal;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.it.media.FichaItem;
import com.it.torrent.fuentes.IListaWebParser;


/**
 * Parser de la pagina que contiene el listado de torrents.
 * <br>Fuente: Pagina de lista ficheros seccion de divxtotal
 * 
 * @author Ricard Forner
 * @version 1.0
 */
public class ListaDivxtotalWebParse implements IListaWebParser {

	public static String DOMAIN = "http://www.divxtotal.com";
	private String contenido;
	private boolean fuenteRSS;
	
	public ListaDivxtotalWebParse() {
		fuenteRSS = false;
	}

	/* (non-Javadoc)
	 * @see com.it.utorrent.fuentes.divxtotal.IListaWebParser#setContenido(java.lang.String)
	 */
	public void setContenido(String contenido) {
		this.contenido = contenido;
	}

	/* (non-Javadoc)
	 * @see com.it.utorrent.fuentes.divxtotal.IListaWebParser#isFuenteRSS()
	 */
	public boolean isFuenteRSS() {
		return fuenteRSS;
	}

	/* (non-Javadoc)
	 * @see com.it.utorrent.fuentes.divxtotal.IListaWebParser#setFuenteRSS(boolean)
	 */
	public void setFuenteRSS(boolean fuenteRSS) {
		this.fuenteRSS = fuenteRSS;
	}

	/* (non-Javadoc)
	 * @see com.it.utorrent.fuentes.divxtotal.IListaWebParser#parse()
	 */
	public ArrayList<FichaItem> parse() {
		return (fuenteRSS) ? parseRSS() : parseHTML();
	}

	/**
	 * Parse de pagina RSS
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private ArrayList<FichaItem> parseRSS() {
		ArrayList<FichaItem> lista = new ArrayList<FichaItem>();
		boolean bSalir = false;

		String searchNode = "<item>";
		String searchHREFInicio = "<link>";
		String searchHREFFin = "</link>";
		String searchNombreInicio = "<title>";
		String searchNombreFin = "</title>";
		String searchFechaInicio = "<pubDate>";
		String searchFechaFin = "</pubDate>";

		FichaItem fichaItem = null;
		while (!bSalir) {
			fichaItem = new FichaItem();
			if (contenido != null && contenido.indexOf(searchNode)!=-1) {
				// Cadena encontrada
				String a1 = new String(contenido.substring(contenido.indexOf(searchNode)+searchNode.length()));
				// Busco primera ocurrencia de nombre del fichero
				if (a1 != null && a1.indexOf(searchNombreInicio)!=-1) {
					String sNombreTmp = new String(a1.substring(a1.indexOf(searchNombreInicio)+searchNombreInicio.length()));
					String sNombre = sNombreTmp.substring(0, sNombreTmp.indexOf(searchNombreFin));
					fichaItem.setNombre(sNombre);
				}
				// Busco primera ocurrencia de fecha creacion
				if (a1 != null && a1.indexOf(searchFechaInicio)!=-1) {
					String sFechaTmp = new String(a1.substring(a1.indexOf(searchFechaInicio)+searchFechaInicio.length()));
					String sFecha = sFechaTmp.substring(0, sFechaTmp.indexOf(searchFechaFin));
					// Conversion fecha
					/*
					Date fecha = new Date();
					try {
					    SimpleDateFormat format =
					        new SimpleDateFormat("EEE, MMM dd HH:mm:ss zzz yyyy");
					    fecha = format.parse(sFecha);
					}
					catch(ParseException pe) {
					}*/
					long fecha = Date.parse(sFecha);
					SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
					sFecha = sdf.format(fecha);
					fichaItem.setFecha(sFecha);
				}
				// Busco primera ocurrencia de href
				if (a1 != null && a1.indexOf(searchHREFInicio)!=-1) {
					String sLinkTmp = new String(a1.substring(a1.indexOf(searchHREFInicio)+searchHREFInicio.length()));
					String sLink = sLinkTmp.substring(0, sLinkTmp.indexOf(searchHREFFin));
					// Proteccion por si no incluye el dominio
					if (sLink!=null && sLink.charAt(0) == '/') {
					    sLink = DOMAIN + sLink;
					}
					fichaItem.setURLFichaInformacion(sLink);
					contenido = sLinkTmp;
				}
				
				lista.add( fichaItem );
			} else {
				bSalir = true;
			}
		}
		return lista;
	}
	
	/**
	 * Parse de pagina HTML
	 * @return
	 */
	private ArrayList<FichaItem> parseHTML() {
		ArrayList<FichaItem> lista = new ArrayList<FichaItem>();
		boolean bSalir = false;

		String searchNode = "seccontnom\">";
		String searchHREFInicio = "href=\"";
		String searchHREFFin = "\"";
		String searchNombreInicio = "\">";
		String searchNombreFin = "</a>";
		String searchFechaInicio = "seccontfetam\">";
		String searchFechaFin = "</p>";

		FichaItem fichaItem = null;
		while (!bSalir) {
			fichaItem = new FichaItem();
			if (contenido != null && contenido.indexOf(searchNode)!=-1) {
				// Cadena encontrada
				String a1 = new String(contenido.substring(contenido.indexOf(searchNode)+searchNode.length()));
				// Busco primera ocurrencia de href
				if (a1 != null && a1.indexOf(searchHREFInicio)!=-1) {
					String sLinkTmp = new String(a1.substring(a1.indexOf(searchHREFInicio)+searchHREFInicio.length()));
					String sLink = sLinkTmp.substring(0, sLinkTmp.indexOf(searchHREFFin));
					// Proteccion por si no incluye el dominio
					if (sLink!=null && sLink.charAt(0) == '/') {
					    sLink = DOMAIN + sLink;
					}
					fichaItem.setURLFichaInformacion(sLink);
				}
				// Busco primera ocurrencia de nombre del fichero
				if (a1 != null && a1.indexOf(searchNombreInicio)!=-1) {
					String sNombreTmp = new String(a1.substring(a1.indexOf(searchNombreInicio)+searchNombreInicio.length()));
					String sNombre = sNombreTmp.substring(0, sNombreTmp.indexOf(searchNombreFin));
					fichaItem.setNombre(sNombre);
				}
				// Busco primera ocurrencia de fecha creacion
				if (a1 != null && a1.indexOf(searchFechaInicio)!=-1) {
					String sFechaTmp = new String(a1.substring(a1.indexOf(searchFechaInicio)+searchFechaInicio.length()));
					String sFecha = sFechaTmp.substring(0, sFechaTmp.indexOf(searchFechaFin));
					fichaItem.setFecha(sFecha);
					contenido = sFechaTmp;
				}
				
				lista.add( fichaItem );
			} else {
				bSalir = true;
			}
		}
		return lista;
	}

}
