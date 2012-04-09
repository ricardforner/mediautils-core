package com.it.util;

import java.io.*;
import java.util.*;
import java.text.*;

/**
 * Utilidad para crear ficheros de logs.
 * Tiene la habilidad de mantener un nº determinado de logs en disco y borrar los más antiguos.
 * @author Daniel del Río (drio@heptiumit.com)
 * @version 2.1, 08/04/2003 (modificado por David R. para centralizar en una única instancia).
 */
public class LogManager {
    
    public static final int DEBUG_LEVEL_NONE = 0;
    public static final int DEBUG_LEVEL_LOW = 1;
    public static final int DEBUG_LEVEL_MEDIUM = 2;
    public static final int DEBUG_LEVEL_HIGH = 3;
    private static int debugLevel = DEBUG_LEVEL_MEDIUM;
    
    protected PrintStream logout;
    private FileOutputStream logoutFileOutputStream;
    private BufferedOutputStream logoutBufferedOutputStream;
    private String path;
    private int numLogs = 0;
    private Date currentDate;
    private Calendar calendar = GregorianCalendar.getInstance();
    protected SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
    
    private static LogManager instance = null;
    private static boolean initialized = false;
    
    /**
     * Inicializa el LogManager.<br>
     * Si el LogManager ya fue inicializado previamente desde otra aplicación la llamada a este método no hará nada.
     * @param path Directorio dónde guardar los logs. Ej: c:/dan/logs/
     * @param pattern El patrón de fecha, por defecto: yyyyMMdd
     * @param numLogs Número de ficehros de log a conservar en disco. Los logs más antiguos se van borrando, 0 para conservar todos los ficheros logs que se generen.
     */
    public void init(String path, String pattern, int numLogs) {
        if (!initialized) {
            this.path = path;
            this.numLogs = Math.max(0, numLogs);
            this.formatter = new SimpleDateFormat(pattern);
            currentDate = new Date(System.currentTimeMillis());
            String fileName = getFileName(currentDate);
            try {
                borrarUltimoLog(currentDate);
                File file = new File(fileName);
                if (!file.exists())
                    file.createNewFile();
                logoutFileOutputStream = new FileOutputStream(fileName, true);
                logoutBufferedOutputStream = new BufferedOutputStream(logoutFileOutputStream);
                logout = new PrintStream(logoutBufferedOutputStream, true);
                initialized = true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }
    
    /**
     * Devuelve la instancia del LogManager.
     */
    public static LogManager getInstance() {
        if(instance == null) {
            synchronized(LogManager.class) {
                if(instance == null) {
                    instance = new LogManager();
                }
            }
        }
        return instance;
    }
    
    /**
     * Manda un log al fichero de logs de la aplicación con un nivel de debug.
     * Los niveles de debug son:<br>
     * LogManager.DEBUG_LEVEL_HIGH: Muestra excepciones, mensajes de control y querys a BDD.<br>
     * LogManager.DEBUG_LEVEL_MEDIUM: Muestra excepciones y mensajes de control.<br>
     * LogManager.DEBUG_LEVEL_LOW: Muestra sólo excepciones (que en principio no deberían darse).<br>
     * LogManager.DEBUG_LEVEL_NONE: No muestra NADA.<br>
     * @param log Texto del mensaje de log.
     * @param paramDebugLevel Nivel de debug (NONE, LOW, MEDIUM o HIGH).
     */
    public void log(String log, int paramDebugLevel) {
        if (paramDebugLevel <= debugLevel)
            this.println(new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date())+" - "+log);
    }
    
    /**
     * Manda un log al fichero de logs de la aplicación.
     * @param log Texto del mensaje de log.
     */
    public void log(String log) {
        log(log, DEBUG_LEVEL_MEDIUM);
    }
    
    /**
     * Obtiene el mensaje de la excepción mediante una llamada a su método getMessage() y lo guarda en el fichero de logs.
     * @param e Excepción que se quiere enviar al archivo de logs.
     */
    public void log(Throwable e) {
        log(e.getMessage() + "\n" + getStackTraceAsString(e), DEBUG_LEVEL_LOW);
    }
    
    /**
     * Obtiene el mensaje de la excepción mediante una llamada a su método getMessage(), lo añade al parámetro log y lo guarda en el fichero de logs.<br>
     * @param log Texto del mensaje de log.
     * @param e Excepción que se quiere enviar al archivo de logs.
     */
    public void log(String log, Throwable e) {
    	log(log+ ": " + e.getMessage() + "\n" + getStackTraceAsString(e), DEBUG_LEVEL_LOW);
    }
    
    public void destroy() throws IOException {
        initialized = false;
        logoutBufferedOutputStream.close();
       	logoutFileOutputStream.close();
        logout.close();
    }
    
    //
    // Métodos privados.
    //
    
    @SuppressWarnings("unused")
	private synchronized void print(String msg) {
        checkDate();
        logout.print(msg);
    }
    
    private synchronized void println(String msg) {
        checkDate();
        logout.println(msg);
    }
    
    private void checkDate() {
        Date now = new Date(System.currentTimeMillis());
        calendar.setTime(now);
        int d = calendar.get(Calendar.DAY_OF_YEAR);
        calendar.setTime(currentDate);
        int di = calendar.get(Calendar.DAY_OF_YEAR);
        if (d != di) {
            currentDate = now;
            try {
                borrarUltimoLog(currentDate);
                logout.close();
                logout = new PrintStream(new BufferedOutputStream(
                new FileOutputStream(getFileName(currentDate), true)), true);
            } catch (Exception e) {
                com.it.util.LogManager.getInstance().log("Couldn't create logout file");
                e.printStackTrace();
            }
        }
    }
    
    private synchronized void borrarUltimoLog(Date date) {
        if (numLogs > 0) {
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_YEAR, -numLogs);
            String name = getFileName(calendar.getTime());
            File file = new File(name);
            if (file.exists())
                file.delete();
        }
    }
    
    private String getFileName(Date date) {
        return  path + formatter.format(date) + ".log";
    }
    
    private String getStackTraceAsString(Throwable e) {
        java.io.StringWriter stringWriter = new java.io.StringWriter();
        java.io.PrintWriter printWriter = new java.io.PrintWriter(stringWriter);
        e.printStackTrace(printWriter);
        StringBuffer error = stringWriter.getBuffer();
        return error.toString();
    }
    
}


