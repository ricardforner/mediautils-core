/*
 * FechaUtil.java
 *
 * Created on 28 de noviembre de 2003, 12:52
 */

package com.it.util;


import java.util.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;


/**
 * Utilidades para fechas
 * 
 * @author  Daniel del Río / Ricard Forner
 * @version 1.1
 */
public class FechaUtil {

    private static final String[] meses_es = new String[12];
    private static final String[] meses_ct = new String[12];
    private static final String[] trimestre_es = new String[4];
    private static final String[] trimestre_ct = new String[4];
    private static final String[] semestre_es = new String[2];
    private static final String[] semestre_ct = new String[2];
    private static final String[] dias_es = new String[8];
    private static final String[] dias_ct = new String[8];
    private static final String[] siglasDia_es = new String[8];
    private static final String[] siglasDia_ct = new String[8];
    
    private static final DateFormat formatter = new SimpleDateFormat("yyyyMMdd");
    
    static {
        meses_es[Calendar.JANUARY] = "Enero";
        meses_es[Calendar.FEBRUARY] = "Febrero";
        meses_es[Calendar.MARCH] = "Marzo";
        meses_es[Calendar.APRIL] = "Abril";
        meses_es[Calendar.MAY] = "Mayo";
        meses_es[Calendar.JUNE] = "Junio";
        meses_es[Calendar.JULY] = "Julio";
        meses_es[Calendar.AUGUST] = "Agosto";
        meses_es[Calendar.SEPTEMBER] = "Septiembre";
        meses_es[Calendar.OCTOBER] = "Octubre";
        meses_es[Calendar.NOVEMBER] = "Noviembre";
        meses_es[Calendar.DECEMBER] = "Diciembre";
        
        meses_ct[Calendar.JANUARY] = "Gener";
        meses_ct[Calendar.FEBRUARY] = "Febrer";
        meses_ct[Calendar.MARCH] = "Març";
        meses_ct[Calendar.APRIL] = "Abril";
        meses_ct[Calendar.MAY] = "Maig";
        meses_ct[Calendar.JUNE] = "Juny";
        meses_ct[Calendar.JULY] = "Juliol";
        meses_ct[Calendar.AUGUST] = "Agost";
        meses_ct[Calendar.SEPTEMBER] = "Septembre";
        meses_ct[Calendar.OCTOBER] = "Octubre";
        meses_ct[Calendar.NOVEMBER] = "Novembre";
        meses_ct[Calendar.DECEMBER] = "Decembre";        
        
        trimestre_es[0] = "Primer trimestre";
        trimestre_es[1] = "Segundo trimestre";
        trimestre_es[2] = "Tercer trimestre";
        trimestre_es[3] = "Cuarto trimestre";        

        trimestre_ct[0] = "1er trimestre";
        trimestre_ct[1] = "2on trimestre";
        trimestre_ct[2] = "3e trimestre";
        trimestre_ct[3] = "4rt trimestre";        

        semestre_es[0] = "Primer semestre";
        semestre_es[1] = "Segundo semestre";

        semestre_ct[0] = "1er semestre";
        semestre_ct[1] = "2on semestre";

        dias_es[Calendar.MONDAY] = "Lunes";
        dias_es[Calendar.TUESDAY] = "Martes";
        dias_es[Calendar.WEDNESDAY] = "Miércoles";
        dias_es[Calendar.THURSDAY] = "Jueves";
        dias_es[Calendar.FRIDAY] = "Viernes";
        dias_es[Calendar.SATURDAY] = "Sábado";
        dias_es[Calendar.SUNDAY] = "Domingo";

        siglasDia_es[Calendar.MONDAY] = "L";
        siglasDia_es[Calendar.TUESDAY] = "M";
        siglasDia_es[Calendar.WEDNESDAY] = "MX";
        siglasDia_es[Calendar.THURSDAY] = "J";
        siglasDia_es[Calendar.FRIDAY] = "V";
        siglasDia_es[Calendar.SATURDAY] = "S";
        siglasDia_es[Calendar.SUNDAY] = "D";
        
        dias_ct[Calendar.MONDAY] = "Dilluns";
        dias_ct[Calendar.TUESDAY] = "Dimarts";
        dias_ct[Calendar.WEDNESDAY] = "Dimecres";
        dias_ct[Calendar.THURSDAY] = "Dijous";
        dias_ct[Calendar.FRIDAY] = "Divendres";
        dias_ct[Calendar.SATURDAY] = "Dissabte";
        dias_ct[Calendar.SUNDAY] = "Diumenge";

        siglasDia_ct[Calendar.MONDAY] = "DL";
        siglasDia_ct[Calendar.TUESDAY] = "DM";
        siglasDia_ct[Calendar.WEDNESDAY] = "DX";
        siglasDia_ct[Calendar.THURSDAY] = "DJ";
        siglasDia_ct[Calendar.FRIDAY] = "DV";
        siglasDia_ct[Calendar.SATURDAY] = "DS";
        siglasDia_ct[Calendar.SUNDAY] = "DG";
        
    }
    
    public static String CASTELLANO = "es";
    public static String CATALAN = "ct";
    
    
    
    public static String getMes(String idioma, int mes) {
        if (CASTELLANO.equals(idioma))
            return meses_es[mes];
        else if (CATALAN.equals(idioma))
            return meses_ct[mes];
        else
            return null;
    }
    
    public static String getTrimestre(String idioma, int trimestre) {
        if (CASTELLANO.equals(idioma))
            return trimestre_es[trimestre-1];
        else if (CATALAN.equals(idioma))
            return trimestre_ct[trimestre-1];
        else
            return null;
    }
    
    public static String getSemestre(String idioma, int semestre) {
        if (CASTELLANO.equals(idioma))
            return semestre_es[semestre-1];
        else if (CATALAN.equals(idioma))
            return semestre_ct[semestre-1];
        else
            return null;
    }
    
    public static String getDia(String idioma, int dia) {
        if (CASTELLANO.equals(idioma))
            return dias_es[dia];
        else if (CATALAN.equals(idioma))
            return dias_ct[dia];
        else
            return null;
    }
 
    public static String getSiglasDia(String idioma, int dia) {
        if (CASTELLANO.equals(idioma))
            return siglasDia_es[dia];
        else if (CATALAN.equals(idioma))
            return siglasDia_ct[dia];
        else
            return null;        
    }

    /** 
     * Compara fechas según el día y el año sin tener en cuenta las horas y minutos.
     * @param f1 La fecha inicial
     * @param f2 La fecha final
     * @return -1 si la fecha inicial es anterior a la final, 0 si son iguales o
     * 1 si la fecha inicial es posterior a la final.
     */
    public static int compareDates(Date f1, Date f2) {
        return formatter.format(f1).compareTo(formatter.format(f2));
    }
    
    public static Date getMaximumDate(Date f1, Date f2) {
        int r = compareDates(f1, f2);
        if (r < 1)
            return f2;
        else
            return f1;
    }
    
    public static Date getMinimumDate(Date f1, Date f2) {
        int r = compareDates(f1, f2);
        if (r < 1)
            return f1;
        else
            return f2;
    }
    
    public static Date addToDate(Date f1, int field, int amount) {
        Calendar c = GregorianCalendar.getInstance();
        c.setTime(f1);
        c.add(field, amount);
        return c.getTime();
    }
    
    /**
     * @return El número de dias entre dos fechas (incluyendolas). Dias entre
     * 14/01/2004 y 16/01/2004 son 3 (14, 15, 16)
     */
    public static int getDaysBetween(Date f1, Date f2) {
        int days = 0;
        Date min = getMinimumDate(f1, f2);
        Date max = getMaximumDate(f1, f2);
        
        Calendar cmin = GregorianCalendar.getInstance();
        cmin.setTime(min);
        
        Calendar cmax = GregorianCalendar.getInstance();
        cmax.setTime(max);
        
        while (cmin.get(Calendar.YEAR) < cmax.get(Calendar.YEAR)) {
            days += cmin.getActualMaximum(Calendar.DAY_OF_YEAR) - cmin.get(Calendar.DAY_OF_YEAR) + 1;
            cmin.add(Calendar.YEAR, 1);
            cmin.set(Calendar.DAY_OF_YEAR, 1);
        }
        days += cmax.get(Calendar.DAY_OF_YEAR) - cmin.get(Calendar.DAY_OF_YEAR) + 1;
        return days;
    }

    public static int getField(int field, Date fecha) {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(fecha);
        return calendar.get(field);
    }
    
}
