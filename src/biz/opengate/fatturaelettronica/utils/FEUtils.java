package biz.opengate.fatturaelettronica.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import biz.opengate.fatturaelettronica.FatturaElettronicaType;

public class FEUtils {
	
	public static XMLGregorianCalendar dateToXmlGregCal(Date d) throws DatatypeConfigurationException {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(d);
		XMLGregorianCalendar xmldate = DatatypeFactory.newInstance().newXMLGregorianCalendarDate(cal.get(Calendar.YEAR),
				cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH), cal.getTimeZone().getRawOffset() / 60000);
		xmldate.setTimezone(DatatypeConstants.FIELD_UNDEFINED);

		return xmldate;
	}
	
	public static FatturaElettronicaType Unmarshal(byte[] bytes) {
		return JAXB.unmarshal(new ByteArrayInputStream(bytes),FatturaElettronicaType.class);
	}
	
	public static FatturaElettronicaType Unmarshal(File f) throws IOException {
		byte[] bytes = Files.readAllBytes(f.toPath());
		return JAXB.unmarshal(new ByteArrayInputStream(bytes),FatturaElettronicaType.class);
	}
	
	public static Marshaller setupMarshaller() throws JAXBException {
		Marshaller marshaller = JAXBContext.newInstance(FatturaElettronicaType.class).createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		return marshaller;
	}
	
	public static byte[] Marshal(FatturaElettronicaType fe) throws JAXBException {
		
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		setupMarshaller().marshal(fe, os);
		return os.toByteArray();
	}
	
	public static File MarshalToFile(FatturaElettronicaType fe, String path) throws JAXBException, IOException {
		
		String absolutepath = path + 
				fe.getFatturaElettronicaHeader().getDatiTrasmissione().getIdTrasmittente().getIdPaese() + 
				fe.getFatturaElettronicaHeader().getDatiTrasmissione().getIdTrasmittente().getIdCodice() +
				"_" + "00000" + ".xml";
		
		FileOutputStream os = new FileOutputStream(absolutepath);
		setupMarshaller().marshal(fe, os);
		os.close();
		return new File(absolutepath);
	}
}
