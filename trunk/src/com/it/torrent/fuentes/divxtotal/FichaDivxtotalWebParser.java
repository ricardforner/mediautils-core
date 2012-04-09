package com.it.torrent.fuentes.divxtotal;

import com.it.torrent.fuentes.IFichaWebParser;

/**
 * Parser de la pagina que la ficha descriptiva del torrent.
 * <br>Fuente: Pagina de ficha de divxtotal
 * 
 * @author Ricard Forner
 * @version 1.0
 */
public class FichaDivxtotalWebParser implements IFichaWebParser {

	public static String DOMAIN = "http://www.divxtotal.com";
	private String contenido;

	/* (non-Javadoc)
	 * @see com.it.utorrent.fuentes.divxtotal.IFichaWebParse#setContenido(java.lang.String)
	 */
	public void setContenido(String contenido) {
		this.contenido = contenido;
	}
	
	/* (non-Javadoc)
	 * @see com.it.utorrent.fuentes.divxtotal.IFichaWebParse#parse()
	 */
	public String parse() {
		String retVal = null;
		
		String searchNode = "ficha_link_det";
		String searchHREFInicio = "href=\"";
		String searchHREFFin = "\"";
		if (contenido != null && contenido.indexOf(searchNode)!=-1) {
			// Cadena encontrada
			String a1 = new String(contenido.substring(contenido.indexOf(searchNode)+searchNode.length()));
			// Busco primera ocurrencia de href
			if (a1 != null && a1.indexOf(searchHREFInicio)!=-1) {
				String a2 = new String(a1.substring(a1.indexOf(searchHREFInicio)+searchHREFInicio.length()));
				retVal = a2.substring(0, a2.indexOf(searchHREFFin));
			}
		}
		// Proteccion por si no incluye el dominio
		if (retVal!=null && retVal.charAt(0) == '/') {
			retVal = DOMAIN + retVal;
		}
		return retVal;
	}

}
