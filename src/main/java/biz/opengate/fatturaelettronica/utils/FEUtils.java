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

    /**
     * Convert a Date object in an XMLGregorianCalendar
     *
     * @param date
     *      Date object.
     * 
     * @return XMLGregorianCalendar object with Year, Month and Day.
     * 
     * @throws DatatypeConfigurationException
     */
	public static XMLGregorianCalendar dateToXmlGregCal(Date date) throws DatatypeConfigurationException  {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		XMLGregorianCalendar xmldate = DatatypeFactory.newInstance().newXMLGregorianCalendarDate(
				cal.get(Calendar.YEAR),
				cal.get(Calendar.MONTH) + 1,
				cal.get(Calendar.DAY_OF_MONTH),
				cal.getTimeZone().getRawOffset() / 60000);
		xmldate.setTimezone(DatatypeConstants.FIELD_UNDEFINED);

		return xmldate;
	}

    /**
     * Convert a Date object in an XMLGregorianCalendar
     *
     * @param date
     *      Date object.
     * 
     * @return XMLGregorianCalendar object with Year, Month, Day, Hour, Minute, Second and Millisecond.
     * 
     * @throws DatatypeConfigurationException
     */
	public static XMLGregorianCalendar dateToXmlGregCalTime(Date date) throws DatatypeConfigurationException  {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		XMLGregorianCalendar xmldate = DatatypeFactory.newInstance().newXMLGregorianCalendar(
				cal.get(Calendar.YEAR),
				cal.get(Calendar.MONTH) + 1,
				cal.get(Calendar.DAY_OF_MONTH),
				cal.get(Calendar.HOUR),
				cal.get(Calendar.MINUTE),
				cal.get(Calendar.SECOND),
				DatatypeConstants.FIELD_UNDEFINED,
				cal.getTimeZone().getRawOffset() / 60000);
		xmldate.setTimezone(DatatypeConstants.FIELD_UNDEFINED);

		return xmldate;
	}

    /**
     * Unmarshal the electronic invoice
     *
     * @param bytes
     *      Byte array.
     * 
     * @return FatturaElettronicaType object.
     */
	public static FatturaElettronicaType unmarshal(byte[] bytes) {
		return JAXB.unmarshal(new ByteArrayInputStream(bytes),FatturaElettronicaType.class);
	}

    /**
     * Unmarshal the electronic invoice
     *
     * @param file
     *      File Object.
     *      
     * @throws IOException
	 *		If an I/O error occurs.
     * 
     * @return FatturaElettronicaType object.
     */
	public static FatturaElettronicaType unmarshal(File file) throws IOException {
		byte[] bytes = Files.readAllBytes(file.toPath());
		return JAXB.unmarshal(new ByteArrayInputStream(bytes),FatturaElettronicaType.class);
	}
	
	public static Marshaller setupMarshaller() throws JAXBException {
		Marshaller marshaller = JAXBContext.newInstance(FatturaElettronicaType.class).createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		return marshaller;
	}

    /**
     * Marshal the electronic invoice.
     *
     * @param fe
     *      Fattura elettronica.
     *
     * @throws JAXBException
     *      If any unexpected problem occurs during the marshalling.
     * 
     * @return Byte array of the marshalled object
     */
	public static byte[] marshal(FatturaElettronicaType fe) throws JAXBException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		setupMarshaller().marshal(fe, os);
		return os.toByteArray();
	}

    /**
     * Marshal the electronic invoice.
     *
     * @param fe
     *      Fattura elettronica.
     * @param path
     *      File path.
     *
     * @throws JAXBException
     *      If any unexpected problem occurs during the marshalling.
     * @throws IOException
	 *		If an I/O error occurs.
     * 
     * @return The marshalled File object with an auto generated name with code "00000"
     */
	public static File marshalToFile(FatturaElettronicaType fe, String path) throws JAXBException, IOException {
		String absolutepath = path + 
				fe.getFatturaElettronicaHeader().getDatiTrasmissione().getIdTrasmittente().getIdPaese() + 
				fe.getFatturaElettronicaHeader().getDatiTrasmissione().getIdTrasmittente().getIdCodice() +
				"_" + "00000" + ".xml";
		
		FileOutputStream os = new FileOutputStream(absolutepath);
		setupMarshaller().marshal(fe, os);
		os.close();
		return new File(absolutepath);
	}
	
    /**
     * Marshal the electronic invoice.
     *
     * @param fe
     *      Fattura elettronica.
     * @param path
     *      File path.
     * @param name
     *      File name.
     *
     * @throws JAXBException
     *      If any unexpected problem occurs during the marshalling.
     * @throws IOException
	 *		If an I/O error occurs.
     * 
     * @return The marshalled File object
     */
	public static File marshalToFile(FatturaElettronicaType fe, String path, String name) throws JAXBException, IOException {
		String absolutepath = path + name;
		
		FileOutputStream os = new FileOutputStream(absolutepath);
		setupMarshaller().marshal(fe, os);
		os.close();
		return new File(absolutepath);
	}

    /**
     * Marshal the electronic invoice directly in the specified file.
     *
     * @param fe
     *      Fattura elettronica.
     * @param file
     *      File object.
     *
     * @throws JAXBException
     *      If any unexpected problem occurs during the marshalling.
     * @throws IOException
	 *		If an I/O error occurs.
     */
	public static void marshalToFile(FatturaElettronicaType fe, File file) throws JAXBException, IOException {
		FileOutputStream os = new FileOutputStream(file);
		setupMarshaller().marshal(fe, os);
		os.close();
		return;
	}
	
	public static void main(String[] args) throws DatatypeConfigurationException{
		Date date = new GregorianCalendar(2020, 0, 1, 11, 30, 40).getTime();

		System.out.println(dateToXmlGregCal(date));
		
		System.out.println(dateToXmlGregCalTime(date));
	}
}
