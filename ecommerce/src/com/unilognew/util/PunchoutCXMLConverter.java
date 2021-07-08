package com.unilognew.util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unilog.punchout.jaxb.CXML;

public class PunchoutCXMLConverter {

	private Unmarshaller unmarshaller = null;
	private Marshaller marshaller = null;
	private static JAXBContext context = null;
	private Logger logger = LoggerFactory
			.getLogger(PunchoutCXMLConverter.class);
	
	private static PunchoutCXMLConverter punchoutCXMLConverter=null;
	
	private PunchoutCXMLConverter() {
		try {
			context = JAXBContext.newInstance(CXML.class);
			marshaller = context.createMarshaller();
			unmarshaller = context.createUnmarshaller();
		} catch (JAXBException e) {
			// nothing can be done here...
			logger.info("Could not create JAXB Context for CXML class");
		}
	}
	
	public static PunchoutCXMLConverter getInstance() {
		try{
			synchronized (PunchoutCXMLConverter.class) {
				if (punchoutCXMLConverter == null) {
					punchoutCXMLConverter = new PunchoutCXMLConverter();
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return punchoutCXMLConverter;
	}
	

	public CXML convertCXMLStringToCXMLJavaObject(String cxmlString) {
		CXML cxml = null;
		try {
			cxml = (CXML) unmarshaller.unmarshal(new StreamSource(
					new StringReader(cxmlString)));
			logger.info("CXML String has been converted to CXML Java object");

		} catch (PropertyException pe) {
			// TODO Auto-generated catch block
			logger.info("CXML String could not be converted to CXML Java object due to "+pe.getMessage());
		} catch (JAXBException jaxbe) {
			// TODO Auto-generated catch block
			logger.info("CXML String could not be converted to CXML Java object due to "+jaxbe.getMessage());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.info("CXML String could not be converted to CXML Java object due to "+e.getMessage());
		}

		return cxml;
	}

	public String convertCXMLJavaObjectToCXMLString(CXML cxml) {
		
		String finalData = "";

		OutputStream os = new OutputStream() {
			private StringBuilder string = new StringBuilder();

			@Override
			public void write(int b) throws IOException {
				this.string.append((char) b);
			}

			public String toString() {
				return this.string.toString();
			}
		};

		try {
			
			marshaller.setProperty(
					"com.sun.xml.bind.xmlHeaders",
					"<?xml version=\"1.0\"?><!DOCTYPE cXML SYSTEM \"http://xml.cXML.org/schemas/cXML/1.2.021/cXML.dtd\">");
			marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
			marshaller.marshal(cxml, os);
		}  catch (PropertyException pe) {
			// TODO Auto-generated catch block
			logger.info("CXML object could not be converted to CXML string due to "+pe.getMessage());
		} catch (JAXBException jaxbe) {
			// TODO Auto-generated catch block
			logger.info("CXML object could not be converted to CXML string due to "+jaxbe.getMessage());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.info("CXML object could not be converted to CXML string due to "+e.getMessage());
		}
		if(os!=null) {
			finalData = os.toString();
		}
		return finalData;
	}

}
