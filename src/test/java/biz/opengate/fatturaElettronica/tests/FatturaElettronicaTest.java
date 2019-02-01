package biz.opengate.fatturaElettronica.tests;

import java.io.File;
import java.nio.file.Files;
import org.junit.Test;

import biz.opengate.fatturaelettronica.FatturaElettronicaType;
import biz.opengate.fatturaelettronica.utils.FEUtils;
import biz.opengate.fatturaelettronica.validator.FatturaElettronicaValidator;
import junitx.framework.FileAssert;

public class FatturaElettronicaTest {
	@Test
	public void catalogLoads() throws Exception {
			//Inizializza la fattura elettronica
			FatturaElettronicaType fatturaElettronica, notaCredito, fatturaPA, fatturaEstera;

			//Genera una fattura elettronica di prova
			fatturaElettronica = FatturaElettronicaSemplice.newFattura();
			notaCredito = FatturaElettronicaSemplice.newNotaCredito();
			fatturaPA = FatturaElettronicaSemplice.newFatturaPA();
			fatturaEstera = FatturaElettronicaSemplice.newFatturaEstera();

			//Controlla che la fattura elettronica sia valida
			FatturaElettronicaValidator.controllaFE(fatturaElettronica);
			FatturaElettronicaValidator.controllaFE(notaCredito);
			FatturaElettronicaValidator.controllaFE(fatturaPA);
			FatturaElettronicaValidator.controllaFE(fatturaEstera);

			//Genera il file xml con i dati della fattura elettronica
			
			String tempDir = Files.createTempDirectory(null).toAbsolutePath().toString();
			System.out.println(tempDir);
			
			File fpr = FEUtils.marshalToFile(fatturaElettronica, tempDir + File.separator, "IT01234567890_00FPR.xml");
			File nc = FEUtils.marshalToFile(notaCredito, tempDir + File.separator, "IT01234567890_000NC.xml");
			File fpa = FEUtils.marshalToFile(fatturaPA, tempDir + File.separator, "IT01234567890_00FPA.xml");
			File fe = FEUtils.marshalToFile(fatturaEstera, tempDir + File.separator, "IT01234567890_000FE.xml");
			
			fpr.createNewFile();
			nc.createNewFile();
			fpa.createNewFile();
			fe.createNewFile();
			
			FileAssert.assertEquals(new File(this.getClass().getResource("/IT01234567890_00FPR.xml").getFile()),fpr);
			FileAssert.assertEquals(new File(this.getClass().getResource("/IT01234567890_000NC.xml").getFile()),nc);
			FileAssert.assertEquals(new File(this.getClass().getResource("/IT01234567890_00FPA.xml").getFile()),fpa);
			FileAssert.assertEquals(new File(this.getClass().getResource("/IT01234567890_000FE.xml").getFile()),fe);

//			File fpr = FEUtils.marshalToFile(fatturaElettronica, "src/test/resources/", "IT01234567890_00FPR.xml");
//			File nc = FEUtils.marshalToFile(notaCredito, "src/test/resources/", "IT01234567890_000NC.xml");
//			File fpa = FEUtils.marshalToFile(fatturaPA, "src/test/resources/", "IT01234567890_00FPA.xml");
//			File fe = FEUtils.marshalToFile(fatturaEstera, "src/test/resources/", "IT01234567890_000FE.xml");
//			
//			fpr.createNewFile();
//			nc.createNewFile();
//			fpa.createNewFile();
//			fe.createNewFile();
			
	}
}
