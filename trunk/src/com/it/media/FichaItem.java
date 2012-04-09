package com.it.media;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.it.media.renderer.NotifyComponent;

/**
 * Objeto interno de ficha de Torrent
 *  
 * @author Ricard Forner
 * @version 1.0
 */
public class FichaItem extends NotifyComponent {

	private String nombre;
	private String fecha;
	private String URLFichaInformacion;
	private String URLDescarga;
	private SimpleDateFormat sdf;
	
	public FichaItem() {
		sdf = new SimpleDateFormat("dd-MM-yyyy");
	}

	/**
	 * Get de la fecha (en formato dd-MM-yyyy)
	 * @return
	 */
	public String getFecha() {
		return fecha;
	}
	
	/**
	 * Get de la fecha (devuelve el tipo Date)
	 * @return
	 */
	public Date getFechaTipoDate() {
		try {
			return sdf.parse(fecha);
		} catch (ParseException e) {
		}
		return null;
	}

	/**
	 * Set del campo Fecha (en formato dd-MM-yyyy)
	 * @param fecha
	 */
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	/**
	 * Get del campo Nombre
	 * @return
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * Set del campo Nombre
	 * @param nombre
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * Get de la URL de descarga
	 * @return
	 */
	public String getURLDescarga() {
		return URLDescarga;
	}

	/**
	 * Set del la URL de descarga
	 * @param URLDescarga
	 */
	public void setURLDescarga(String URLDescarga) {
		this.URLDescarga = URLDescarga;
	}

	/**
	 * Get de la URL de la ficha de información
	 * @return
	 */
	public String getURLFichaInformacion() {
		return URLFichaInformacion;
	}

	/**
	 * Set de la URL de la ficha de información
	 * @param URLFichaInformacion
	 */
	public void setURLFichaInformacion(String URLFichaInformacion) {
		this.URLFichaInformacion = URLFichaInformacion;
	}

    // *************************************************************************
    // ** HBEAN Métodos
    // *************************************************************************
	public String hbeanGetPrimaryKey() {
		return getNombre();
	}

	public Object hbeanGetTarget() {
		return this;
	}

	@SuppressWarnings("unchecked")
	public Class hbeanGetTargetClass() {
		return getClass();
	}

}
