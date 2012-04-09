package com.it.util;

import java.util.*;
import java.io.*;

import javax.xml.parsers.*;
import org.w3c.dom.*;

/**
* Esta clase extiende java.util.Properties y permite cargar propiedades desde un fichero
* XML del tipo:<br><br>
* &lt;properties&gt;<br>
*     &lt;property key="color1" value="#D9B400"/&gt;<br>
*     &lt;property key="color2" value="#e7e7e7"/&gt;<br>
* &lt;/properties&gt;<br><br>
* Las constantes de clase ROOT_NODE_TAG_NAME, PROPERTY_TAG_NAME, KEY_ATTRIBUTE_NAME y VALUE_ATTRIBUTE_NAME definen
* respectivamente el nombre del root node, el nombre del tag "property" y los atributos que se leerán (por defecto: "key" y "value").
* Correspondencia:<br><br>
* &lt;ROOT_NODE_TAG_NAME&gt;<br>
*     &lt;PROPERTY_TAG_NAME KEY_ATTRIBUTE_NAME="color1" VALUE_ATTRIBUTE_NAME="#D9B400"/&gt;<br>
*     &lt;PROPERTY_TAG_NAME KEY_ATTRIBUTE_NAME="color2" VALUE_ATTRIBUTE_NAME="#e7e7e7"/&gt;<br>
* &lt;/ROOT_NODE_TAG_NAME&gt;<br><br>
* El método load cargará como propiedades todos los tags "property" independientemente del nivel de profundidad
* en el que se encuentren dentro del XML (se realiza una llamada a  document.getElementsByTagName(PROPERTY_TAG_NAME)).<br>
* @version 1.0
* @author David Rodriguez - ITDeusto
*/
public class XmlProperties extends java.util.Properties
{
	private static final long serialVersionUID = 2801461045570260639L;
	public String ROOT_NODE_TAG_NAME = "properties";
	public String PROPERTY_TAG_NAME = "property";
	public String KEY_ATTRIBUTE_NAME = "key";
	public String VALUE_ATTRIBUTE_NAME = "attribute";
	
    public void load(String filename) throws FileNotFoundException, IOException {
		this.load(new File(filename));
    }
	
	public void load(File file) throws FileNotFoundException, IOException {
		this.load(new FileInputStream(file));
	}
	
	/**
	* Sobreescribe el método load de java.util.Properties para leer las propiedades
	* de un fichero XML.
	* @param in InputStream.
	*/
	public void load(InputStream in) throws IOException {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setValidating(false);
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document document = db.parse(in);
			in.close();
			// Recuperamos los elementos node.
			org.w3c.dom.NodeList propertyNodes = document.getElementsByTagName(PROPERTY_TAG_NAME);
			for (int i = 0; i < propertyNodes.getLength(); i++) {
				org.w3c.dom.Node propertyNode = propertyNodes.item(i);
				if (propertyNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
					org.w3c.dom.Element propertyElement = (org.w3c.dom.Element) propertyNode;
					if (propertyElement.getAttribute(KEY_ATTRIBUTE_NAME) != null) {
						setProperty(propertyElement.getAttribute(KEY_ATTRIBUTE_NAME),
								propertyElement.getAttribute(VALUE_ATTRIBUTE_NAME));
					}
				}
			}
		} catch (javax.xml.parsers.ParserConfigurationException pce) {
			throw new IOException(pce.getMessage());
		} catch (org.xml.sax.SAXException se) {
			throw new IOException(se.getMessage());
		}
	}
	
	/**
	* Escribe las propiedades en un fichero XML con formato para que puedan ser cargadas con 
	* el método load. Un ejemplo:<br><br>
	* &lt;properties&gt;<br>
	*     &lt;property key="color1" value="#D9B400"/&gt;<br>
	*     &lt;property key="color2" value="#e7e7e7"/&gt;<br>
	* &lt;/properties&gt;<br><br>
	* Si el atributo header es distinto de null, se añadirá a la cabecera del fichero XML.
	* El campo header se puede utilizar para escribir &lt;?xml version="1.0"?&gt;, comentarios, etc...<br><br>
	* Y, al igual que en la clase java.util.Properties:<br>
	* Properties from the defaults table of this Properties table (if any) are not written out by this method.<br>
	* After the entries have been written, the output stream is flushed. The output stream remains open after this method returns.<br>
	* @param out OutputStream.
	* @param header Cabecera del fichero XML.
	*/
	@SuppressWarnings("unchecked")
	public void store(OutputStream out, String header) throws IOException {
		if (header != null) out.write(header.getBytes());
		out.write(new String("<" + ROOT_NODE_TAG_NAME + ">\n").getBytes());
		Enumeration<String> keys = (Enumeration<String>) propertyNames();
		while (keys.hasMoreElements()) {
			String key = (String) keys.nextElement();
			out.write(new String("\t<" + PROPERTY_TAG_NAME + " " 
					+ KEY_ATTRIBUTE_NAME + "=\"" + key + "\" "
					+ VALUE_ATTRIBUTE_NAME + "=\"" + getProperty(key) + "\""
					+ "/>\n").getBytes());
		}
		out.write(new String("</" + ROOT_NODE_TAG_NAME + ">").getBytes());
		out.flush();
	}

}
