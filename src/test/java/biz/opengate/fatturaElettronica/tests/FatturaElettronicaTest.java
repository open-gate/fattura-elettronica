package biz.opengate.fatturaElettronica.tests;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;

import javax.xml.datatype.DatatypeConfigurationException;

import org.junit.Test;

import biz.opengate.fatturaelettronica.FatturaElettronicaType;
import biz.opengate.fatturaelettronica.utils.FEUtils;
import biz.opengate.fatturaelettronica.validator.FatturaElettronicaValidator;
import junitx.framework.FileAssert;

public class FatturaElettronicaTest {

	
	@Test
	public void catalogLoads() throws Exception {
			//Inizializza la fattura elettronica
			FatturaElettronicaType fatturaElettronica;

			//Genera una fattura elettronica di prova
			fatturaElettronica = FatturaElettronicaSemplice.newFattura();

			//Controlla che la fattura elettronica sia valida
			FatturaElettronicaValidator.controllaFE(fatturaElettronica);

			//Genera il file xml con i dati della fattura elettronica
			String tempDir = Files.createTempDirectory(null).toAbsolutePath().toString();
			System.out.println(tempDir);
			File f = FEUtils.marshalToFile(fatturaElettronica, tempDir + File.separator);
			
			f.createNewFile();
			System.out.println(f.getAbsolutePath());
			FileAssert.assertEquals(new File(this.getClass().getResource("/IT01234567890_00000.xml").getFile()),f);
			
	}
}
