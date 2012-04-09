package com.it.media.renderer.impl;

import com.it.hbean.html.DefaultHtmlComponent;
import com.it.hbean.html.HtmlCell;
import com.it.hbean.html.HtmlTable;
import com.it.media.renderer.common.ItemCellRenderer;

public class ItemHeaderCellRendererImpl extends ItemCellRenderer {

	public HtmlCell getTableCellRendererComponent(HtmlTable table, Object obj, int row, int col) {
		HtmlCell cell = null;

		cell = new HtmlCell(new DefaultHtmlComponent(obj.toString()));;
		cell.setHtmlAttribute(javax.swing.text.html.HTML.Attribute.ALIGN, "left");
		cell.setHtmlAttribute(javax.swing.text.html.HTML.Attribute.STYLE, "font-family: Verdana, Arial; font-size: 10px; font-weight: bold;");
		if ("Tamaño".equals(table.getColumn(col).getId())) {
			cell.setHtmlAttribute(javax.swing.text.html.HTML.Attribute.ALIGN, "center");
		}
		if ("Ratio".equals(table.getColumn(col).getId())) {
			cell.setHtmlAttribute(javax.swing.text.html.HTML.Attribute.ALIGN, "center");
		}
		if ("Estado".equals(table.getColumn(col).getId())) {
			cell = new HtmlCell(new DefaultHtmlComponent(""));;
		}
		return cell;
	}

}
