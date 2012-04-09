package com.it.media.util;

import java.io.File;
import java.util.Enumeration;
import java.util.Hashtable;
import com.it.util.StringUtil;

/**
 * Normalizacion de textos
 * 
 * @author rforner
 * @version 1.2
 */
public class NormalizarFichero {

	private String keyGeneric = "MEDIANORMALIZEGENERIC";
	
	private Hashtable<String, String> replaceWords;

	public NormalizarFichero() {
		replaceWords = new Hashtable<String, String>();
	}
	
	/**
	 * Inicializa las cadenas de normalizacion
	 * Array de los pares
	 *     <clave>;<cadena a eliminar>
	 * 
	 * @param p
	 */
	public void setWords(Hashtable<String, String> p) {
		String key;
		for (Enumeration<String> e = p.keys(); e.hasMoreElements(); ){
			key = e.nextElement();
			replaceWords.put(key.toLowerCase(), p.get(key));
		}
	}
	
	/**
	 * Devuelve la cadena substituida
	 * 
	 * @param in
	 * @return
	 */
	protected String normalizeWords(String in) {
		String sOut = new String(in);
		if (replaceWords != null && !replaceWords.isEmpty()) {
			String key;
			String value;
			// Buscando claves para <in>
			for (Enumeration<String> e = replaceWords.keys(); e.hasMoreElements(); ){
				key = e.nextElement();
				if (in.toLowerCase().indexOf(key) != -1) {
					// Clave <key> encontrada
					value = replaceWords.get(key);
					sOut = doReplace(in, value);
					break;
				}
			}
			// Valores posibles de la Clave generica
			if (replaceWords.containsKey(keyGeneric.toLowerCase())) {
				value = replaceWords.get(keyGeneric.toLowerCase());
				sOut = doReplace(sOut, value);
			}
		}
		return sOut;
	}
	
	/**
	 * Permite definir un array de posibles valores de substitucion separados por punto-y-coma
	 * 
	 * @since 1.2 Añadido substitucion de texto <cadena orig>:<cadena nueva>
	 */
	private String doReplace(String in, String arrValue) {
		String sOut = new String(in);
		String[] sReplace = arrValue.split(";");
		for (String item: sReplace) {
			String[] sReplacePair = item.split(":");
			if (sReplacePair.length==2) {
				sOut = StringUtil.replaceCadena(sOut, sReplacePair[0], sReplacePair[1] );
			} else {
				sOut = StringUtil.replaceCadena(sOut, item, "" );
			}
		}
		return sOut;
	}

	/**
	 * Devuelve la cadena substituyendo los puntos por espacios en blanco
	 * 
	 * @param in
	 * @return
	 */
	protected String normalizePuntos(String in) {
		String sOut = new String(in);
		String sExt = "";
		// Busco extension
		if (in.lastIndexOf('.')!=-1) {
			sOut = in.substring(0,in.lastIndexOf('.'));
			sExt = in.substring(in.lastIndexOf('.'));
		}
		// Elimino puntos
		sOut = StringUtil.replaceCadena(sOut, ".", " ");
		return sOut.concat(sExt);
	}

	/**
	 * Proceso de normalizacion del fichero
	 * 
	 * @param pFolderName
	 * @param pFichero
	 * @return
	 */
	public boolean run(String pFolderName, String pFichero) {
    	String ficheroCandidato = pFichero;
    	// Proceso del fichero
    	ficheroCandidato = normalizeWords(ficheroCandidato);
    	ficheroCandidato = normalizePuntos(ficheroCandidato);
    	// Renombre del fichero
        String rutaFileIn = pFolderName + File.separatorChar + pFichero;
        String rutaFileOut = pFolderName + File.separatorChar + ficheroCandidato;
		File fIn = new File(rutaFileIn);
    	File fOut = new File(rutaFileOut);
    	return fIn.renameTo(fOut);
	}

	/**
	public static void main(String[] args) {
		NormalizarFichero o = new NormalizarFichero();
		System.out.println(o.doReplace("GSG9.1x19.[Audio.latino].DVB.XviD.[www.DivxTotaL.com].avi", ".DVB.XviD.[www.DivxTotaL.com];.[Audio.latino]:Mi texto;GSG:MiGSG"));
	}
	*/
	
}
