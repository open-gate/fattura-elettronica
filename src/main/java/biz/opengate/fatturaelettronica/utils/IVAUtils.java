package biz.opengate.fatturaelettronica.utils;

import biz.opengate.fatturaelettronica.FatturaElettronicaHeaderType;
import biz.opengate.fatturaelettronica.IdFiscaleType;
import biz.opengate.fatturaelettronica.validator.FatturaElettronicaContentValidator.Errori;

public class IVAUtils {
	//https://it.wikipedia.org/wiki/Partita_IVA#Struttura_del_codice_identificativo_di_partita_IVA
	public static void validateIVA(String iva) throws Exception {
		if(iva.length()!=11)
			throw new Exception(Errori.eIVA);
		
		int[] code = new int[11];
		
		for (int i = 0; i < iva.length(); i++){
			code[i] = Character.getNumericValue(iva.charAt(i));
		}

		int x = 0, y=0, t, n;
		
		for(int i=0; i<6; i++) {
			n=code[i*2];
			x+=n;
		}
		
		for(int i=1; i<6; i++) {
			n=code[i*2-1];
			if(n<5)
				y+=n*2;
			else
				y+=(n*2)-9;
		}
		
		t=(x+y)%10;
		if(t!=0)
			throw new Exception(Errori.eIVA);
	}
	
	public static boolean equalsIdFiscaleIVA(IdFiscaleType idFiscaleType1, IdFiscaleType idFiscaleType2) {
		return (idFiscaleType1.getIdCodice().equals(idFiscaleType2.getIdCodice()) && (idFiscaleType1.getIdPaese().equals(idFiscaleType2.getIdPaese())));
	}
	
	public static boolean equalsIdFiscaleIVACedenteCessionario(FatturaElettronicaHeaderType feHeader) {
		return equalsIdFiscaleIVA(feHeader.getCedentePrestatore().getDatiAnagrafici().getIdFiscaleIVA(), feHeader.getCessionarioCommittente().getDatiAnagrafici().getIdFiscaleIVA());
	}
}
