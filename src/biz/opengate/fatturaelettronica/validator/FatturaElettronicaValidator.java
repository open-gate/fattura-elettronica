package biz.opengate.fatturaelettronica.validator;

import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.helpers.DefaultHandler;

import biz.opengate.fatturaelettronica.FatturaElettronicaType;
import biz.opengate.fatturaelettronica.utils.FEUtils;

public class FatturaElettronicaValidator {
	
	public void controllaFatturaElettronica(FatturaElettronicaType fatturaElettronica) throws Exception {
		System.out.println("Controllo Fattura Elettronica");
		///////////////////////////////////////////////////////////////////////
		final Schema schema;
		try {
			SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			sf.setResourceResolver(new ResourceResolverImpl());
			InputStream schemafatturaStream = this.getClass().getClassLoader().getResourceAsStream("Schemafattura.xsd");
			schema = sf.newSchema(new StreamSource(schemafatturaStream));
		} catch (Exception e) {
			throw new Exception("Impossibile analizzare il file xsd");
		}
		///////////////////////////////////////////////////////////////////////
		ValidationEventHandlerImpl validationHandler = new ValidationEventHandlerImpl();
		try {
			JAXBContext jc = JAXBContext.newInstance(FatturaElettronicaType.class);
			Marshaller marshaller = jc.createMarshaller();
			marshaller.setSchema(schema);
			marshaller.setEventHandler(validationHandler);
			marshaller.marshal(fatturaElettronica, new DefaultHandler());
		} catch (Exception e) {
			throw new Exception("Impossibile analizzare la fattura elettronica");
		}
		if (validationHandler.hasErrors())
			throw new FatturaElettronicaValidationException(validationHandler.getResult());
		///////////////////////////////////////////////////////////////////////
		FatturaElettronicaContentValidator fecv = new FatturaElettronicaContentValidator();
		fecv.controllaContenutoFatturaElettronica(fatturaElettronica);
		
		System.out.println("Fattura valida");
	}
	
	private static class ResourceResolverImpl implements LSResourceResolver {
		public LSInput resolveResource(String type, String namespaceURI, String publicId, String systemId, String baseURI) {
			if ("xmldsig-core-schema.xsd".equals(systemId)) {
				LSInputImpl input = new LSInputImpl();
				input.setBaseURI(baseURI);
				input.setSystemId(systemId);
				input.setPublicId(publicId);
				return input;
			}
			return null;
		}
	}

	private static class LSInputImpl implements LSInput {
		private String baseURI, systemId, publicId;
		
		@Override public String getBaseURI() { return baseURI; }
		@Override public void setBaseURI(String arg0) { this.baseURI = arg0; }
		@Override public String getSystemId() { return systemId; }
		@Override public void setSystemId(String arg0) { this.systemId = arg0; }
		@Override public String getPublicId() { return publicId; }
		@Override public void setPublicId(String arg0) { this.publicId = arg0; }
		
		@Override public String getEncoding() { return "utf-8"; }
		@Override public void setEncoding(String arg0) {}
		@Override public InputStream getByteStream() { return this.getClass().getClassLoader().getResourceAsStream("xmldsig-core-schema.xsd"); }
		@Override public void setByteStream(InputStream arg0) {}
		@Override public Reader getCharacterStream() { return null; }
		@Override public void setCharacterStream(Reader arg0) {}
		@Override public String getStringData() { return ""; }
		@Override public void setStringData(String arg0) {}
		@Override public boolean getCertifiedText() { return true; }
		@Override public void setCertifiedText(boolean arg0) {}
	}
	
	private static class ValidationEventHandlerImpl implements ValidationEventHandler {
		StringBuffer result = new StringBuffer();

		public boolean handleEvent(ValidationEvent event) {
			result.append("EVENT\n");
			result.append("SEVERITY: "			+ event.getSeverity()					+ "\n");
			result.append("MESSAGE: "			+ event.getMessage()					+ "\n");
			result.append("LINKED EXCEPTION: "	+ event.getLinkedException()			+ "\n");
			result.append("LOCATOR\n");
			result.append("\t\tLINE NUMBER: "	+ event.getLocator().getLineNumber()	+ "\n");
			result.append("\t\tCOLUMN NUMBER: "	+ event.getLocator().getColumnNumber()	+ "\n");
			result.append("\t\tOFFSET: "		+ event.getLocator().getOffset()		+ "\n");
			result.append("\t\tOBJECT: "		+ event.getLocator().getObject()		+ "\n");
			result.append("\t\tNODE: "			+ event.getLocator().getNode()			+ "\n");
			result.append("\t\tURL: "			+ event.getLocator().getURL()			+ "\n");
			return true;
		}

		public String getResult() { return result.toString(); }
		public boolean hasErrors() { return result.length() > 0; }
	}
	
	public static void main(String[] args) throws Exception {
		
		FatturaElettronicaType fatturaElettronica;
		fatturaElettronica = Test.NewFattura();
		
		FatturaElettronicaValidator fev = new FatturaElettronicaValidator();
		
		fev.controllaFatturaElettronica(fatturaElettronica);
		
		File f = FEUtils.MarshalToFile(fatturaElettronica, "");
		f.createNewFile();
	}
}
