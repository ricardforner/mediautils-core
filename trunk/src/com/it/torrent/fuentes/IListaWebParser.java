package com.it.torrent.fuentes;

import java.util.ArrayList;

import com.it.media.FichaItem;

/**
 * Parser de la pagina que contiene el listado de torrents.
 * 
 * @author Ricard Forner
 * @version 1.0
 */
public interface IListaWebParser {

	public abstract void setContenido(String contenido);

	public abstract boolean isFuenteRSS();

	public abstract void setFuenteRSS(boolean fuenteRSS);

	public abstract ArrayList<FichaItem> parse();

}