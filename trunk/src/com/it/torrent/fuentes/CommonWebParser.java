package com.it.torrent.fuentes;

import com.it.torrent.fuentes.divxtotal.FichaDivxtotalWebParser;
import com.it.torrent.fuentes.divxtotal.ListaDivxtotalWebParse;

public class CommonWebParser {

	public static String FUENTE_DIVXTOTAL = "divxtotal";
	private String fuente;
	
	public CommonWebParser(String fuente) {
		this.fuente = fuente;
	}
	
	public IFichaWebParser createImplementationFicha() {
		if (FUENTE_DIVXTOTAL.equals(fuente)) {
			return new FichaDivxtotalWebParser();
		}
		return null;
	}

	public IListaWebParser createImplementationLista() {
		if (FUENTE_DIVXTOTAL.equals(fuente)) {
			return new ListaDivxtotalWebParse();
		}
		return null;
	}

}
