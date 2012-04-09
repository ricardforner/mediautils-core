package com.it.media.util;

/**
 * Utilidad para buscar palabras clave en cadenas de texto.
 * 
 * @author Ricard Forner
 * @version 1.0
 */
public class FiltroPalabrasClave {

	private String[] searchWords;

	/**
	 * Set del conjunto de palabras clave.
	 * 
	 * @param searchWords Array de strings (de palabras clave)
	 */
	public void setSearchWords(String[] searchWords) {
		this.searchWords = searchWords;
	}

	/**
	 * Busca si en la cadena facilitada como parametro hay alguna de las
	 * palabras fijadas en el metodo setSearchWorks.
	 * 
	 * Devuelve true si en la cadena facilitada como parametro contiene alguna palabra clave.
	 * 
	 * @param pCadena Cadena
	 * @return 
	 */
	public boolean isFound(String pCadena) {
		if (pCadena == null || "".equals(pCadena))
			return false;
		
		if (searchWords == null)
			return false;
		
		for (int i=0; i<searchWords.length; i++) {
			if ("".equals(searchWords[i]))
					continue;
			if (pCadena.toLowerCase().indexOf(searchWords[i].toLowerCase()) != -1) {
				return true;
			}
		}
		return false;
	}

}
