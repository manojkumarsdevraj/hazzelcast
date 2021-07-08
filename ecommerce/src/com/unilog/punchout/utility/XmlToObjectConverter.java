package com.unilog.punchout.utility;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import com.unilog.punchout.jaxb.CXML;

public class XmlToObjectConverter {

	static JAXBContext context=null;
	static {
		try {
			context = JAXBContext.newInstance(CXML.class);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static Object convertXmlStringToJavaObject(String responseData)
	{
		Object xmlStringToObject=null;
		try {

			Unmarshaller unmarshaller= context.createUnmarshaller();
			xmlStringToObject = unmarshaller.unmarshal( new StreamSource( new StringReader( responseData ) ) );
			
			System.out.println("Converted");

		} catch (PropertyException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (JAXBException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return xmlStringToObject;
	}
	
	public static String convertJavaObjectToXmlString(Object objectName)
	{
		String finalData="";

		OutputStream os=new OutputStream()
		{
	        private StringBuilder string = new StringBuilder();

	        @Override
	        public void write(int b) throws IOException {
	            this.string.append((char) b );
	        }

	        public String toString(){
	            return this.string.toString();
	        }
	    };


		try {
			Marshaller m = context.createMarshaller();
			//m.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
			//m.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, "!DOCTYPE IDMS-XML SYSTEM \"http://xml.cXML.org/schemas/cXML/1.2.021/cXML.dtd\"");
			//m.setProperty("com.sun.xml.bind.xmlHeaders", "<?xml version=\"1.0\"?>");
			m.setProperty("com.sun.xml.bind.xmlHeaders", "<?xml version=\"1.0\"?><!DOCTYPE cXML SYSTEM \"http://xml.cXML.org/schemas/cXML/1.2.021/cXML.dtd\">");
		    m.setProperty(Marshaller.JAXB_FRAGMENT, true);
			//m.marshal(entityInquiry, System.out);
			m.marshal(objectName, os );
		} catch (PropertyException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (JAXBException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		finalData=os.toString();
		//finalData+="</IDMS-XML>";

		return finalData;
	}
	
}
