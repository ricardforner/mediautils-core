package com.it.media.renderer.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import com.it.downloader.webui.impl.ItemJDownloader;
import com.it.hbean.html.DefaultHtmlComponent;
import com.it.hbean.html.HtmlCell;
import com.it.hbean.html.HtmlTable;
import com.it.media.renderer.common.ItemCellRenderer;
import com.it.torrent.webui.impl.ItemUTorrent;
import com.it.util.StringUtil;

public class ItemDataCellRendererImpl extends ItemCellRenderer {

	public HtmlCell getTableCellRendererComponent(HtmlTable table, Object obj, int row, int col) {
		HtmlCell cell = null;
		
		// Objeto en la columna 0
        Object objValue = table.getModel().getValueAt(row, 0);
		// Objeto NotifyComponent en la columna 0
		if (col==0) {
			cell = DefaultCellRenderer(table, null, row, col);
			return cell;
		}
		
		cell = DefaultCellRenderer(table, obj.toString(), row, col);
		if (objValue instanceof ItemUTorrent) {
			if ("Tamaño".equals(table.getColumn(col).getId())) {
				long value = new Long(obj.toString()).longValue();
				DecimalFormat myFormatter = new DecimalFormat("###,###");
				String medida = (value>999999999) ? "GB" : "MB"; 
				value = (value>999999999) ? value/1000000 : value/1000;
				String output = myFormatter.format(value);
				output = (output.length()>3) ? output.substring(0,output.length()-2) : "<1";
				output+=" ";
				output+=medida;
				cell = DefaultCellRenderer(table, output, row, col);
				cell.setHtmlAttribute(javax.swing.text.html.HTML.Attribute.ALIGN, "right");
			} else if ("Finalizado".equals(table.getColumn(col).getId())) {
				cell = DefaultCellRenderer(table, getHtmlBoolean(
						((ItemUTorrent)objValue).isDownloaded()), row, col);
				cell.setHtmlAttribute(javax.swing.text.html.HTML.Attribute.ALIGN, "center");
			} else if ("Servido".equals(table.getColumn(col).getId())) {
				cell = DefaultCellRenderer(table, getHtmlBoolean(
						((ItemUTorrent)objValue).isCompletamenteServido()), row, col);
				cell.setHtmlAttribute(javax.swing.text.html.HTML.Attribute.ALIGN, "center");
			} else if ("Ratio".equals(table.getColumn(col).getId())) {
				double m = new Double(obj.toString()).doubleValue();
				String output = ""+round(m/1000, 2);
				output = StringUtil.rightPadding(output, '0', 4);
				cell = DefaultCellRenderer(table, output, row, col);
				cell.setHtmlAttribute(javax.swing.text.html.HTML.Attribute.ALIGN, "center");
			} else if ("Estado".equals(table.getColumn(col).getId())) {
				cell = DefaultCellRenderer(table, getStatus((ItemUTorrent)objValue), row, col);
				cell.setHtmlAttribute(javax.swing.text.html.HTML.Attribute.ALIGN, "left");
			}
		} else if (objValue instanceof ItemJDownloader) {
			if ("Tamaño".equals(table.getColumn(col).getId())) {
				double m = new Double(obj.toString()).doubleValue();
				String output = ""+round(m/100, 2);
				//output = StringUtil.rightPadding(output, '0', 4);
				if (output.indexOf('.') != -1) {
					output+=(output.substring(output.indexOf('.')).length()<3)?"0":"";
				}
				output = output.substring(0, output.length()-1);
				output+=" ";
				output+="MB";
				cell = DefaultCellRenderer(table, output, row, col);
				cell.setHtmlAttribute(javax.swing.text.html.HTML.Attribute.ALIGN, "right");
			} else if ("Finalizado".equals(table.getColumn(col).getId())) {
				cell = DefaultCellRenderer(table, getHtmlBoolean(
						((ItemJDownloader)objValue).isDownloaded()), row, col);
				cell.setHtmlAttribute(javax.swing.text.html.HTML.Attribute.ALIGN, "center");
			} else if ("Servido".equals(table.getColumn(col).getId())) {
				cell = DefaultCellRenderer(table, "--", row, col);
				cell.setHtmlAttribute(javax.swing.text.html.HTML.Attribute.ALIGN, "center");
			} else if ("Ratio".equals(table.getColumn(col).getId())) {
				double m = new Double(obj.toString()).doubleValue();
				String output = ""+round(m/100, 2);
				output = StringUtil.rightPadding(output, '0', 4);
				cell = DefaultCellRenderer(table, output, row, col);
				cell.setHtmlAttribute(javax.swing.text.html.HTML.Attribute.ALIGN, "center");
			} else if ("Estado".equals(table.getColumn(col).getId())) {
				cell = DefaultCellRenderer(table, getStatus((ItemJDownloader)objValue), row, col);
				cell.setHtmlAttribute(javax.swing.text.html.HTML.Attribute.ALIGN, "left");
			}
		} else {
			cell = DefaultCellRenderer(table, obj.toString(), row, col);
		}
		
		cell.setHtmlAttribute(javax.swing.text.html.HTML.Attribute.STYLE, "font-family: Arial; font-size: 11px;");
		cell.setHtmlAttribute(javax.swing.text.html.HTML.Attribute.HEIGHT, "12");
		
		return cell;
	}

	protected HtmlCell DefaultCellRenderer(HtmlTable table, String obj, int row, int col) {
		HtmlCell cell = null;
		// Renderers por defecto
		if (obj == null || "".equals(obj)) {
			cell = new HtmlCell(new DefaultHtmlComponent());
		} else {
			cell = new HtmlCell(new DefaultHtmlComponent(obj));
		}
		return cell;
	}

	private String getHtmlBoolean(boolean activo) {
		if (activo)
			return "Si"; //"<img src=\"cid:"+cid+"\" border=0 alt=''>";
		else
			return "No";
	}
	
	private String getStatus(ItemUTorrent item) {
		if (item.isCompletamenteServido()) {
			return (cidStatusSeeded == null || "".equals(cidStatusSeeded))
				? "" : "<img src=\"cid:"+cidStatusSeeded+"\" border=0 alt=''>";
		} else if (ItemUTorrent.STATUS_FINISHED == item.getStatus()) {
			return (cidStatusFinished == null || "".equals(cidStatusFinished))
				? "" : "<img src=\"cid:"+cidStatusFinished+"\" border=0 alt=''>";
		} else if (item.isDownloading()) {
			return (cidStatusDownloading == null || "".equals(cidStatusDownloading))
				? "" : "<img src=\"cid:"+cidStatusDownloading+"\" border=0 alt=''>";
		} else if (ItemUTorrent.STATUS_SEEDING == item.getStatus()) {
			return (cidStatusSeeding == null || "".equals(cidStatusSeeding))
				? "" : "<img src=\"cid:"+cidStatusSeeding+"\" border=0 alt=''>";
		} else {
			return "";
		}
	}

	private String getStatus(ItemJDownloader item) {
		if (item.isDownloaded()) {
			return (cidStatusFinished == null || "".equals(cidStatusFinished))
			? "" : "<img src=\"cid:"+cidStatusFinished+"\" border=0 alt=''>";
		} else if (item.isDownloading()) {
			return (cidStatusDownloading == null || "".equals(cidStatusDownloading))
				? "" : "<img src=\"cid:"+cidStatusDownloading+"\" border=0 alt=''>";
		} else {
			return "";
		}
	}

	public double round(double d, int decimalPlace){
		BigDecimal bd = new BigDecimal(Double.toString(d));
		bd = bd.setScale(decimalPlace,BigDecimal.ROUND_HALF_DOWN);
		return bd.doubleValue();
	}

}
