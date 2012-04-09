package com.it.downloader.parser;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.it.downloader.webui.impl.ItemJDownloader;

public class JDRemoteControlReader {

	private static final String PACKAGE_NODE = "package";
	@SuppressWarnings("unused")
	private static final String FILE_NODE = "file";
	
	@SuppressWarnings("unused")
	private static final String PACKAGE_ATR_ETA				= "package_ETA";
	@SuppressWarnings("unused")
	private static final String PACKAGE_ATR_LINK_INPROGRESS	= "package_linksinprogress";
	@SuppressWarnings("unused")
	private static final String PACKAGE_ATR_LINK_TOTAL		= "package_linkstotal";
	@SuppressWarnings("unused")
	private static final String PACKAGE_ATR_LOADED			= "package_loaded";
	private static final String PACKAGE_ATR_NAME			= "package_name";
	private static final String PACKAGE_ATR_PERCENT			= "package_percent";
	private static final String PACKAGE_ATR_SIZE			= "package_size";
	@SuppressWarnings("unused")
	private static final String PACKAGE_ATR_SPEED			= "package_speed";
	private static final String PACKAGE_ATR_TODO			= "package_todo";

	private ArrayList<ItemJDownloader> arrList;
	
	public void load(String xmlRecords) throws IOException {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setValidating(false);
			DocumentBuilder db;
			db = dbf.newDocumentBuilder();
			InputSource is;
			is = new InputSource();
			is.setCharacterStream(new StringReader(xmlRecords));
			
			Document doc = db.parse(is);
			doc.getDocumentElement().normalize();
			
			NodeList nodeList = doc.getElementsByTagName(PACKAGE_NODE);
			arrList = new ArrayList<ItemJDownloader>();
			ItemJDownloader itemBase = null;
			for (int i = 0; i < nodeList.getLength(); i++) {
				Node node = nodeList.item(i) ;
				if (Node.ELEMENT_NODE == node.getNodeType()) {
					Element element = (Element) node;
					itemBase = new ItemJDownloader();
					itemBase.setHash(element.getAttribute(PACKAGE_ATR_NAME));
					itemBase.setName(element.getAttribute(PACKAGE_ATR_NAME));
					itemBase.setSizeFile(element.getAttribute(PACKAGE_ATR_SIZE));
					itemBase.setRatioPercent(element.getAttribute(PACKAGE_ATR_PERCENT));
					itemBase.setBytesPending(element.getAttribute(PACKAGE_ATR_TODO));
					arrList.add(itemBase);
				}
			}
			
		} catch (ParserConfigurationException pce) {
			throw new IOException(pce.getMessage());
		} catch (SAXException saxe) {
			throw new IOException(saxe.getMessage());
		}
	}
	
	public ArrayList<ItemJDownloader> getList() {
		return arrList;
	}

}
