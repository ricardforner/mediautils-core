package com.it.media.renderer;

import java.util.ArrayList;
import java.util.Iterator;

import com.it.hbean.HBean;
import com.it.hbean.HBeanAttribute;
import com.it.hbean.HBeanContainer;
import com.it.hbean.HBeanPrototype;
import com.it.hbean.html.DefaultHtmlTableModel;
import com.it.hbean.html.HtmlTable;
import com.it.hbean.html.HtmlTableCellRenderer;

public class NotifyRenderer {

	private String prototypeName;
	private String iteratorName;
	private String htmlTableWidth;
	private ArrayList<NotifyComponent> listaElementos;
	private HtmlTableCellRenderer dataCellRenderer;
	private HtmlTableCellRenderer headerCellRenderer;
	
	public NotifyRenderer() {
		this.htmlTableWidth = "100%";
	}

	public String getPrototypeName() {
		return prototypeName;
	}

	public void setPrototypeName(String prototypeName) {
		this.prototypeName = prototypeName;
	}

	public String getIteratorName() {
		return iteratorName;
	}

	public void setIteratorName(String iteratorName) {
		this.iteratorName = iteratorName;
	}

	public void setHtmlTableWidth(String tableWidth) {
		this.htmlTableWidth = tableWidth;
	}

	public HtmlTableCellRenderer getDataCellRenderer() {
		return dataCellRenderer;
	}

	public void setDataCellRenderer(HtmlTableCellRenderer dataCellRenderer) {
		this.dataCellRenderer = dataCellRenderer;
	}

	public HtmlTableCellRenderer getHeaderCellRenderer() {
		return headerCellRenderer;
	}

	public void setHeaderCellRenderer(HtmlTableCellRenderer headerCellRenderer) {
		this.headerCellRenderer = headerCellRenderer;
	}

	public ArrayList<NotifyComponent> getListaElementos() {
		return listaElementos;
	}

	public void setListaElementos(ArrayList<NotifyComponent> listaElementos) {
		this.listaElementos = listaElementos;
	}

	/**
	 * Obtiene el html de la lista de elementos
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String getHtml() {
		HtmlTable table = new HtmlTable();
		table.setHtmlAttribute(javax.swing.text.html.HTML.Attribute.WIDTH, htmlTableWidth);
		DefaultHtmlTableModel model = new DefaultHtmlTableModel();

		if (getPrototypeName() == null)
			return "";
		
		HBeanPrototype prototipo = HBeanContainer.getInstance().getHBeanPrototype(getPrototypeName());
		
        // Columnas del listado
		model.addColumn("", null);
		for (Iterator<HBeanAttribute> atts = prototipo.getAttributeIterator(getIteratorName()); atts.hasNext(); ) {
			HBeanAttribute att = atts.next();
			model.addColumn(att.getDisplayName(), null);
		}
		// Filas del listado
		ArrayList<NotifyComponent> iLista = getListaElementos();
		for (int i=0; iLista!=null && i<iLista.size(); i++) {
			NotifyComponent c = iLista.get(i);
			HBean hbean = HBeanContainer.getInstance().getHBean(null, c, getPrototypeName());
			ArrayList row = new ArrayList();
			// Añadir componente en 1º columna
            row.add(c);
			// Recorre las columnas
            for (Iterator atts = hbean.getAttributeIterator(getIteratorName()); atts.hasNext();) {
                HBeanAttribute att = (HBeanAttribute) atts.next();
                String s = att.getEditor().getAsText();
                row.add(s == null ? "&nbsp;" : s);
            }
            model.addRow(row);
		}
		
		table.setModel(model);
		table.setHeaderCellRenderer(getHeaderCellRenderer());
		table.setDefaultHtmlTableCellRenderer(Object.class, getDataCellRenderer());
		
		StringBuffer sb = new StringBuffer();
		sb.append(table.getHtml());
		return sb.toString();
	}

}
