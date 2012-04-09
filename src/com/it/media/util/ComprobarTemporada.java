package com.it.media.util;

import java.util.Enumeration;
import java.util.Hashtable;

/**
 * Utilidad para comprobar si es el ultimo episodio de una temporada
 * 
 * @author Ricard Forner
 * @version 1.0
 */
public class ComprobarTemporada {

	private Hashtable<String, String> checkList;
	
	public ComprobarTemporada() {
		checkList = new Hashtable<String, String>();
	}

	/**
	 * Inicializa las cadenas de último episodio por temporada
	 * <br>Array de los pares
	 * <br>    &lt;clave serie&gt;;&lt;ultimo episodio&gt;
	 * 
	 * @param pList
	 */
	public void setEpisodesGuide(Hashtable<String, String> pList) {
		String key;
		for (Enumeration<String> e = pList.keys(); e.hasMoreElements(); ){
			key = e.nextElement();
			checkList.put(key.toLowerCase(), pList.get(key));
		}
	}

	public boolean isFinalEpisodeSeason(String in) {
		if (in == null || "".equals(in))
			return false;
		
		if (checkList != null && !checkList.isEmpty()) {
			String key;
			String value;
			// Buscando claves para <in>
			for (Enumeration<String> e = checkList.keys(); e.hasMoreElements(); ){
				key = e.nextElement();
				if (in.toLowerCase().indexOf(key) != -1) {
					// Clave <key> encontrada
					value = checkList.get(key);
					return (in.toLowerCase().indexOf(value) != -1);
				}
			}
		}
		return false;
	}

	/**
	public static void main(String[] args) {
		ComprobarTemporada o = new ComprobarTemporada();
		Hashtable<String, String> p = new Hashtable<String, String>();
		p.put("caprica", "23");
		p.put("gsg", "19");
		setEpisodesGuide(p);
		System.out.println(o.isFinalEpisodeSeason("GSG9.1x19.DVB.XviD.[www.DivxTotaL.com].avi"));
	}
	*/

}
