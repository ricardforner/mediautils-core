package com.it.torrent.fuentes;

/**
 * Parser de la pagina que la ficha descriptiva del torrent.
 * 
 * @author Ricard Forner
 * @version 1.0
 */
public interface IFichaWebParser {

	public abstract void setContenido(String contenido);

	public abstract String parse();

}