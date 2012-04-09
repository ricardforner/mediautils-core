package com.it.torrent.json.impl;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import org.apache.noggit.JSONParser;

import com.it.torrent.TorrentConstants;
import com.it.torrent.json.ParserJSONFactory;
import com.it.torrent.json.common.IParserJSON;
import com.it.torrent.webui.common.ItemBase;
import com.it.torrent.webui.impl.ItemTransmission;

/**
 * Realiza el parser JSON de la información devuelta por Transmission
 * 
 * @author Ricard Forner
 * @version 1.0
 */
public class ParserJSONTransmission implements IParserJSON {

	private int quants;
	private ArrayList<ItemTransmission> arrList;

	public ParserJSONTransmission() {
		this.quants = 0;
		this.arrList = new ArrayList<ItemTransmission>();
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
		ItemTransmission o = new ItemTransmission();
		try {
			
			js.nextEvent();
			js.getString();
			js.nextEvent();
			o.setHash( ""+Long.valueOf(""+js.getNumberChars()) );

			js.nextEvent();
			js.getString();
			js.nextEvent();
			o.setLeftUntilDone( Long.valueOf(""+js.getNumberChars()) );

			js.nextEvent();
			js.getString();
			js.nextEvent();
			o.setName( js.getString() );

			js.nextEvent();
			js.getString();
			js.nextEvent();
			o.setPercentDone( Double.valueOf(""+js.getNumberChars()) );

			js.nextEvent();
			js.getString();
			js.nextEvent();
			o.setStatus( Long.valueOf(""+js.getNumberChars()) );

			js.nextEvent();
			js.getString();
			js.nextEvent();
			o.setTotalSize( Long.valueOf(""+js.getNumberChars()) );

			js.nextEvent();
			js.getString();
			js.nextEvent();
			o.setUploadRatio( Double.valueOf(""+js.getNumberChars()) );


			js.nextEvent();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		arrList.add(o);
	}
	
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

	public ItemTransmission[] getList() {
		return (ItemTransmission[]) arrList.toArray(new ItemTransmission[]{});
	}

	public int size() {
		return this.quants;
	}

	
	public static void main(String[] args) {
		String stream = "{\"arguments\":{\"torrents\":[{\"id\":1,\"name\":\"Trauma 1x10 Globo azul.avi\"}, {\"id\":2,\"name\":\"Trauma 1x11 Globo azul.avi\"}]},\"result\":\"success\",\"tag\":1}";
		StringReader sr = new StringReader(stream);
        JSONParser js = new JSONParser(sr);
        
		IParserJSON ilp = ParserJSONFactory.create(TorrentConstants.CLIENT_TRANSMISSION);
		ilp.setJSON(js);

		ItemBase[] list = new ItemBase[ilp.size()];
		list = ilp.getList();

	    ItemBase obj = null;
		for (int i=0; i<list.length; i++) {
			obj = list[i];
			System.out.println(obj.getHash() + " - " + obj.getName());
		}
		
		// [id, name]
		//
		// sizeWhenDone
		// status
		// totalSize
		// torrentFile
		// hashString
		// leftUntilDone
		// metadataPercentComplete
		
//		TR_STATUS_CHECK_WAIT   = (1<<0)1
//		TR_STATUS_CHECK        = (1<<1)2
//		TR_STATUS_DOWNLOAD     = (1<<2)4
//		TR_STATUS_SEED         = (1<<3)8
//		TR_STATUS_STOPPED      = (1<<4)16
//
//		STATUS = mirror_dict({
//		    'check_wait'  : TR_STATUS_CHECK_WAIT,
//		    'check'       : TR_STATUS_CHECK,
//		    'downloading' : TR_STATUS_DOWNLOAD,
//		    'seeding'     : TR_STATUS_SEED,
//		    'stopped'     : TR_STATUS_STOPPED,
//		})

		
	}
	
}
