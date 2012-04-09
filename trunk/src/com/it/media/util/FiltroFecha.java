package com.it.media.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.it.media.FichaItem;
import com.it.util.FechaUtil;

/**
 * Utilidad para filtrar por fecha los arrays de fichas
 * 
 * @author Ricard Forner
 * @version 1.0
 */
public class FiltroFecha {

	private Date hoy;
	private Date ayer;
	
	public FiltroFecha() {
		hoy = new Date(System.currentTimeMillis());
		ayer = FechaUtil.addToDate(hoy, Calendar.DAY_OF_YEAR, -1);
	}
	
	/**
	 * Devuelve la fecha del filtro
	 * 
	 * @return Fecha en formato dd/mm/YYYY
	 */
	public String getFiltroFechaFormateado() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		return sdf.format(ayer);
	}
	
	/**
	 * Devuelve la fecha de hoy
	 * 
	 * @return Fecha en formato dd/mm/YYYY
	 */
	public String getFechaFormateada() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		return sdf.format(hoy);
	}
	
	/**
	 * Devuelve la lista filtrada por fecha.
	 * 
	 * @param pList Lista de entrada
	 * @return Lista filtrada por fecha
	 */
	public ArrayList<FichaItem> getLista(ArrayList<FichaItem> pList) {
		if (pList == null)
			return pList;
		
		ArrayList<FichaItem> pListSalida = new ArrayList<FichaItem>();
		for (FichaItem i: pList) {
			if (FechaUtil.compareDates(ayer, i.getFechaTipoDate()) == 0 ) {
				pListSalida.add(i);
			}
		}
		return pListSalida;
	}

	/**
	 * Devuelve true si la hora/minuto actual se encuentra en el intervalo, falso en caso contrario
	 * <br>Ejemplo: inHourMinuteInterval(new String[]{"0900", "1200"});
	 * 
	 * @param horaIntervalo Es un array de 2 posiciones: HoraMinuto Inicio y HoraMinuto Fin
	 * @return Verdadero si esta en el intervalo, falso en caso contrario
	 */
	public boolean inHourMinuteInterval(String[] horaIntervalo) {
		if (horaIntervalo.length != 2)
			return false;
		
		SimpleDateFormat sdf = new SimpleDateFormat("HHmm");
		int h = new Integer(sdf.format(hoy)).intValue();
		int limiteInf = new Integer(horaIntervalo[0]).intValue(); 
		int limiteSup = new Integer(horaIntervalo[1]).intValue();
		return (limiteInf<=h && h<=limiteSup);
	}

	/**
	 * Devuelve en numero de la semana
	 * <br>1: Domingo, 2: Lunes .... 7: Sabado
	 * 
	 * @return Nº de la semana [1..7]
	 */
	private int getDayOkWeek() {
		return FechaUtil.getField(Calendar.DAY_OF_WEEK, ayer);
	}
	
	/**
	 * Devuelve true si el dia de hoy esta en la lista, falso en caso contrario
	 * 
	 * @param dayExclude
	 * @return Devuelve verdadero si el dia de hoy esta en la lista, falso en caso contrario
	 */
	public boolean excludeDay(String[] dayExclude) {
		if (dayExclude.length == 0)
			return false;
		
		String todayNumDay = ""+getDayOkWeek();
		for (String numDay: dayExclude) {
			if (numDay.equals(todayNumDay)) {
				return true;
			}
		}
		return false;
	}

}
