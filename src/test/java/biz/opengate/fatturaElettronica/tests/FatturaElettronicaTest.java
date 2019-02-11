package biz.opengate.fatturaElettronica.tests;

import java.io.File;
import java.nio.file.Files;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

import biz.opengate.fatturaelettronica.FatturaElettronicaType;
import biz.opengate.fatturaelettronica.utils.FEUtils;
import biz.opengate.fatturaelettronica.validator.FatturaElettronicaContentValidator.Errori;
import biz.opengate.fatturaelettronica.validator.FatturaElettronicaValidator;
import junitx.framework.FileAssert;

public class FatturaElettronicaTest {
	static String tempDir;
	
	private void test(FatturaElettronicaType fatturaElettronica, String filename) throws Exception {
		System.out.println("Test " + filename);
		//Controlla che la fattura elettronica sia valida
		FatturaElettronicaValidator.controllaFE(fatturaElettronica);
		//Genera e crea il file xml
		File fpr =	FEUtils.marshalToFile(fatturaElettronica, tempDir, filename);
		fpr.createNewFile();
		//Confronta con file di test
		FileAssert.assertEquals(new File(this.getClass().getResource("/" + filename).getFile()), fpr);
	}
	
	private void testerror(String filename, String error) throws Exception {
		testerror(filename, error, "");
	}
	
	private void testerror(String filename, String error, String subcode) throws Exception {
		Matcher m = Pattern.compile("(Errore \\w*).*").matcher(error);
		m.matches();
		m.groupCount();
		String errorcode = m.group(1);
		try {
			FatturaElettronicaValidator.controllaFE(
				FEUtils.unmarshal(new File(this.getClass().getResource(filename).getFile())));
			throw new Exception("Test " + errorcode + subcode + " Fail");
		} catch (Exception e) {
			if (!e.getMessage().matches(error + "(?s).*") && !e.getMessage().equals(error))
				throw e;
		}
		System.out.println("Test " + errorcode + subcode + " OK");
		return;
	}
	
	@Test
	public void catalogLoads() throws Exception {
		//Inizializza la fattura elettronica
		FatturaElettronicaType fatturaElettronica;
		
		//Crea directory temporanea
		tempDir = Files.createTempDirectory(null).toAbsolutePath().toString() + File.separator;
		
		fatturaElettronica = FatturaElettronicaSemplice.newFattura();
		test(fatturaElettronica, "IT01234567890_00FPR.xml");
		fatturaElettronica = FatturaElettronicaSemplice.newNotaCredito();
		test(fatturaElettronica, "IT01234567890_000NC.xml");
		fatturaElettronica = FatturaElettronicaSemplice.newFatturaPA();
		test(fatturaElettronica, "IT01234567890_00FPA.xml");
		fatturaElettronica = FatturaElettronicaSemplice.newFatturaEstera();
		test(fatturaElettronica, "IT01234567890_000FE.xml");
		
		testerror("/IT08033300966_ER400.xml", Errori.e400);
		testerror("/IT08033300966_ER401.xml", Errori.e401);
		testerror("/IT08033300966_ER411.xml", Errori.e411);
		testerror("/IT08033300966_ER413.xml", Errori.e413);
		testerror("/IT08033300966_ER414.xml", Errori.e414);
		testerror("/IT08033300966_ER415.xml", Errori.e415);
		testerror("/IT08033300966_ER417.xml", Errori.e417);
		testerror("/IT08033300966_ER418.xml", Errori.e418);
		testerror("/IT08033300966_ER419.xml", Errori.e419);
		testerror("/IT08033300966_ER420.xml", Errori.e420);
		testerror("/IT08033300966_ER421.xml", Errori.e421);
		testerror("/IT08033300966_ER422.xml", Errori.e422);
		testerror("/IT08033300966_ER423.xml", Errori.e423);
		testerror("/IT08033300966_ER424a.xml", Errori.e424a, "a");
		testerror("/IT08033300966_ER424b.xml", Errori.e424b, "b");
		testerror("/IT08033300966_ER424c.xml", Errori.e424c, "c");
		testerror("/IT08033300966_ER425.xml", Errori.e425);
		testerror("/IT08033300966_ER426a.xml", Errori.e426a, "a");
		testerror("/IT08033300966_ER426b.xml", Errori.e426b, "b");
		testerror("/IT08033300966_ER427a.xml", Errori.e427a, "a");
		testerror("/IT08033300966_ER427b.xml", Errori.e427b, "b");
		testerror("/IT08033300966_ER428.xml", Errori.e428);
		testerror("/IT08033300966_ER429.xml", Errori.e429);
		testerror("/IT08033300966_ER430.xml", Errori.e430);
		testerror("/IT08033300966_ER437.xml", Errori.e437);
		testerror("/IT08033300966_ER438.xml", Errori.e438);
	}
}
