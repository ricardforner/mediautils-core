package com.it.torrent.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CreateJSONRequest {

	private static final String CR = "\r";
	private ArrayList<HashMap<String, String>> arrValue;
	private StringBuffer s;
	private boolean isFirst;

	public CreateJSONRequest() {
		s = new StringBuffer();
		isFirst = true;
	}
	
	public void addBlock(String label) {
		if (isFirst) {
			isFirst = false;
		} else {
			s.append(","+CR);
		}

		if (label == null) {
			s.append(getString());
		} else {
			s.append("\"");
			s.append(label);
			s.append("\": ");
			s.append("{"+CR);
			s.append(getString());
			s.append(CR+"}");
		}
	}

	public void addValue(String label, List<String> lista) {
		if (arrValue == null) {
			arrValue = new ArrayList<HashMap<String,String>>();
		}
		HashMap<String, String> map = new HashMap<String, String>();
		StringBuffer sb = new StringBuffer();
		for (int i=0; i<lista.size(); i++) {
			if (i>0) {
				sb.append(", ");
			}
			sb.append("\"");
			sb.append(lista.get(i));
			sb.append("\"");
		}
		map.put("KEY", label);
		map.put("VALUE", sb.toString() );
		map.put("TYPE", "array");
		arrValue.add(map);
	}
	
	public void addValue(String label, String value) {
		if (arrValue == null) {
			arrValue = new ArrayList<HashMap<String,String>>();
		}
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("KEY", label);
		map.put("VALUE", value);
		map.put("TYPE", "string");
		arrValue.add(map);
	}

	public void addValue(String label, int value) {
		if (arrValue == null) {
			arrValue = new ArrayList<HashMap<String,String>>();
		}
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("KEY", label);
		map.put("VALUE", ""+value);
		map.put("TYPE", "int");
		arrValue.add(map);
	}
	
	public void addValue(String label, boolean value) {
		if (arrValue == null) {
			arrValue = new ArrayList<HashMap<String,String>>();
		}
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("KEY", label);
		map.put("VALUE", ""+value);
		map.put("TYPE", "int");
		arrValue.add(map);
	}
	
	private String getString() {
		StringBuffer s = new StringBuffer();
		if (arrValue != null) {
			HashMap<String, String> map;
			for (int i=0; i<arrValue.size(); i++) {
				map = arrValue.get(i);
				if (i>0) {
					s.append("," + CR);
				}
				// CLAVE
				s.append("\"");
				s.append(map.get("KEY"));
				s.append("\": ");
				// VALOR
				if ("string".equals(map.get("TYPE"))) {
					s.append("\"");
				} else if ("array".equals(map.get("TYPE"))) {
					s.append("[ ");
				}
				s.append(map.get("VALUE"));
				if ("string".equals(map.get("TYPE"))) {
					s.append("\"");
				} else if ("array".equals(map.get("TYPE"))) {
					s.append(" ]");
				}

			}
		}
		arrValue = null;
		return s.toString();
	}
	
	public String print() {
		return "{" + CR + s.toString() + CR + "}";
	}
	
	public static void main(String[] args) {

		CreateJSONRequest o = new CreateJSONRequest();

		/*
		o.addValue("aaaa", 11);
		addBlock(null);
		o.addValue("filename", "fichero.avi");
		o.addValue("method", "torrent-add");
		o.addValue("tag", 1);
		addBlock("arguments");
		o.addValue("bbbb", "1341");
		addBlock(null);
		*/
		
		/*
		o.addValue("filename", "url");
		o.addBlock("arguments");
		o.addValue("method", "torrent-add");
		o.addValue("tag", 1);
		o.addBlock(null);
		*/
		
		List<String> lista = new ArrayList<String>();
		lista.add("id");lista.add("name");
		o.addValue("fields", lista);
		o.addBlock("arguments");
		o.addValue("method", "torrent-get");
		o.addValue("tag", 1);
		o.addBlock(null);

		System.out.println(o.print());
		
	}

}
