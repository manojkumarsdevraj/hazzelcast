package com.unilog.utility;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.StringReader;

import com.itextpdf.text.Document;
import com.itextpdf.text.html.simpleparser.HTMLWorker;
import com.itextpdf.text.pdf.PdfWriter;
import com.unilog.database.CommonDBQuery;

public class ConvertHtmlToPdf {

	public void gerratePdfFromHtml(StringBuilder htmlStringBuilder,String fileName){
		try {
			if(htmlStringBuilder!=null && htmlStringBuilder.length()>0 && CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("TEMPUPLOADDIRECTORYPATH")).length()>0 && CommonUtility.validateString(fileName).length()>0){
			    OutputStream file = new FileOutputStream(new File(CommonDBQuery.getSystemParamtersList().get("TEMPUPLOADDIRECTORYPATH")+"/"+fileName));
			    Document document = new Document();
			    PdfWriter.getInstance(document, file);
			    document.open();
			    HTMLWorker htmlWorker = new HTMLWorker(document);
			    System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");
			    htmlWorker.parse(new StringReader(htmlStringBuilder.toString()));
			    document.close();
			    file.close();
			}
		} catch (Exception e) {
		    e.printStackTrace();
		}
	}
	
}
