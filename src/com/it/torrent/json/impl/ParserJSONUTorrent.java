package com.it.torrent.json.impl;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.noggit.JSONParser;

import com.it.torrent.json.common.IParserJSON;
import com.it.torrent.webui.impl.ItemUTorrent;

/**
 * Realiza el parser JSON de la información devuelta en la
 * llamada http://[IP]:[PORT]/gui/?list=1 a la WebUI de uTorrent
 * 
 * @author Ricard Forner
 * @version 1.0
 */
public class ParserJSONUTorrent implements IParserJSON {

	private int quants;
	private ArrayList<ItemUTorrent> arrList;
	
	public ParserJSONUTorrent() {
		this.quants = 0;
		this.arrList = new ArrayList<ItemUTorrent>();
	}

	@SuppressWarnings("unused")
	private void _parserDebug(JSONParser js) throws IOException {
        int maxEv=1000;
        for (int i=0; i<maxEv; i++) {
          System.out.println("  Parser State:" + js);
          int ev = js.nextEvent();
          System.out.println("  Parser State After nextEvent:" + js);
          switch (ev) {
            case JSONParser.STRING:
              System.out.println("STRING:");
              System.out.println(js.getString());
              break;
            case JSONParser.NUMBER:
              System.out.println("NUMBER:");
              // System.out.println(js.getString());
              break;
            case JSONParser.LONG:
              System.out.println("LONG:");
              //System.out.println(js.getLong());
              System.out.println(js.getNumberChars());
              break;
            case JSONParser.NULL:
              System.out.println("NULL");
              break;
            case JSONParser.BOOLEAN:
              System.out.println("BOOLEAN:");
              // System.out.println(js.getString());
              break;
            case JSONParser.OBJECT_START:
              System.out.println("OBJECT_START");
              break;
            case JSONParser.OBJECT_END:
              System.out.println("OBJECT_END");
              break;
            case JSONParser.ARRAY_START:
              System.out.println("ARRAY_START");
              break;
            case JSONParser.ARRAY_END:
              System.out.println("ARRAY_END");
              break;
             case JSONParser.EOF:
              System.out.println("EOF");
              break;
            default:
              System.out.println("UNKNOWN_EVENT_ID:"+ev);
              break;
          }

          if (ev == JSONParser.EOF) break;
        }
	}

	private void parseOneTorrent(JSONParser js) {
		ItemUTorrent o = new ItemUTorrent();
		try {
			int ev;
			
			ev = js.nextEvent();
			// Control añadido cuando no hay elementos en la lista de "torrents"
			if (JSONParser.ARRAY_END == ev) {
				ev = js.nextEvent();
				ev = js.nextEvent();
				ev = js.nextEvent();
				return;
			}			

			o.setHash(js.getString());
			
			js.nextEvent();
			o.setStatus(Long.valueOf(""+js.getNumberChars()));
			
			js.nextEvent();
			o.setName(js.getString());
			
			js.nextEvent();
			o.setSize(Long.valueOf(""+js.getNumberChars()));
			
			js.nextEvent();
			o.setPercentProgress(Long.valueOf(""+js.getNumberChars()));
			
			js.nextEvent();
			o.setDownloaded(Long.valueOf(""+js.getNumberChars()));
			
			js.nextEvent();
			o.setUploaded(Long.valueOf(""+js.getNumberChars()));
			
			js.nextEvent();
			o.setRatio(Integer.valueOf(""+js.getNumberChars()));
			
			js.nextEvent();
			o.setUploadSpeed(Long.valueOf(""+js.getNumberChars()));
			
			js.nextEvent();
			o.setDownloadSpeed(Long.valueOf(""+js.getNumberChars()));
			
			js.nextEvent();
			o.setETA(Long.valueOf(""+js.getNumberChars()));

			js.nextEvent();
			o.setLabel(js.getString());
			
			js.nextEvent();
			o.setPeersConnected(Long.valueOf(""+js.getNumberChars()));

			js.nextEvent();
			o.setPeersInSwarm(Long.valueOf(""+js.getNumberChars()));

			js.nextEvent();
			o.setSeedsConnected(Long.valueOf(""+js.getNumberChars()));

			js.nextEvent();
			o.setSeedsInSwarn(Long.valueOf(""+js.getNumberChars()));

			js.nextEvent();
			o.setAvailability(Long.valueOf(""+js.getNumberChars()));

			js.nextEvent();
			o.setTorrentQueueOrder(Long.valueOf(""+js.getNumberChars()));

			js.nextEvent();
			o.setRemaining(Long.valueOf(""+js.getNumberChars()));

			js.nextEvent();

		} catch (IOException e) {
			e.printStackTrace();
		}
		arrList.add(o);
	}
	
	/* (non-Javadoc)
	 * @see com.it.torrent.json.IParseJSON#setJSON(org.apache.noggit.JSONParser)
	 */
	public void setJSON(JSONParser js) {
		// Procesa
		try {
			int maxEv=1000;
			boolean detectado = false;
			boolean lista = false;
			for (int i=0; i<maxEv; i++) {
				int ev = js.nextEvent();

				// Control añadido cuando no hay elementos en la lista de "torrents"
				if (ev == JSONParser.EOF) break;

				// PASO 1. deteccion de bloque
				if (ev==JSONParser.STRING && "torrents".equals(js.getString())) {
					detectado = true;
					continue;
				}
				// PASO 2. Seleccion de lista
				if (detectado && !lista) {
					if (ev==JSONParser.ARRAY_START) {
						lista = true;
					}
				}
				// PASO 3.
				if (detectado && lista && ev==JSONParser.ARRAY_END) {
					lista = false;
				}
				// PASO 4.
				if (detectado && lista) {
					parseOneTorrent(js);
				}
				

				if (ev == JSONParser.EOF) break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			this.quants = this.arrList.size();
		}
	}

	/* (non-Javadoc)
	 * @see com.it.torrent.json.IParseJSON#getList()
	 */
	public ItemUTorrent[] getList() {
		return (ItemUTorrent[]) arrList.toArray(new ItemUTorrent[]{});
	}
	
	/* (non-Javadoc)
	 * @see com.it.torrent.json.IParseJSON#size()
	 */
	public int size() {
		return this.quants;
	}
	
}
