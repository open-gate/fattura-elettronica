package biz.opengate.fatturaelettronica.validator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.XMLGregorianCalendar;

import biz.opengate.fatturaelettronica.*;

public class FatturaElettronicaContentValidator {

    /**
     * Check for extra schema errors in the electronic invoice, and
     * if the taxable amount and total price were calculated correctly
     *
     * @param fatturaElettronica
     *      Fattura elettronica.
     *
     * @throws Exception
     */
	public static void controllaContenutoFE(FatturaElettronicaType fatturaElettronica) throws Exception {
		System.out.println("Controllo Header");
		//1
		FatturaElettronicaHeaderType feHeader = fatturaElettronica.getFatturaElettronicaHeader();
		//1.1
		DatiTrasmissioneType datiTrasmissione = feHeader.getDatiTrasmissione();
		
		//Errore 426
		if(datiTrasmissione.getPECDestinatario()!=null) {
			if(!datiTrasmissione.getCodiceDestinatario().equals("0000000"))
				throw new Exception(Errori.e426);
		}
		if(datiTrasmissione.getCodiceDestinatario().equals("0000000")) {
			if(datiTrasmissione.getPECDestinatario()==null) {
				throw new Exception(Errori.e426);
			}
		}
		
		//Errore 427
		if(datiTrasmissione.getFormatoTrasmissione() == FormatoTrasmissioneType.FPA_12) {
			if(datiTrasmissione.getCodiceDestinatario().length() == 7) {
				throw new Exception(Errori.e427);
			}
		} else {
			if(datiTrasmissione.getCodiceDestinatario().length() == 6) {
				throw new Exception(Errori.e427);
			}
		}
		
		//Errore 428
		if(datiTrasmissione.getFormatoTrasmissione() != fatturaElettronica.getVersione())
			throw new Exception(Errori.e428);
		//1.4
		CessionarioCommittenteType cessionarioCommittente = feHeader.getCessionarioCommittente();
		//1.4.1
		DatiAnagraficiCessionarioType datiAnagraficiCessionario = cessionarioCommittente.getDatiAnagrafici();
		
		//Errore 417
		if(datiAnagraficiCessionario.getIdFiscaleIVA() == null && datiAnagraficiCessionario.getCodiceFiscale() == null) {
			throw new Exception(Errori.e417);
		}
		
		// BODY
		for (FatturaElettronicaBodyType feBody : fatturaElettronica.getFatturaElettronicaBody()) {
			System.out.println("Controllo Body " + feBody.getDatiGenerali().getDatiGeneraliDocumento().getNumero());
			controllaFEBody(feBody);
			FatturaElettronicaCalcoli.controllaCalcoloImponibileImporto(feBody.getDatiBeniServizi(), feBody.getDatiGenerali());
			FatturaElettronicaCalcoli.controllaCalcoloPrezzoTotale(feBody.getDatiBeniServizi().getDettaglioLinee());
		}
	}

    /**
     * Check for extra schema errors in the electronic invoice body
     *
     * @param fatturaElettronica
     *      Fattura elettronica.
     *
     * @throws Exception
     */
	public static void controllaFEBody(FatturaElettronicaBodyType feBody) throws Exception {
		
		//Errore 419
		int TotAliquoteIva = 0;
		List<BigDecimal> AliquoteIva = new ArrayList<BigDecimal>();
		
		// 2.1 DatiGenerali
		DatiGeneraliType datiGenerali = feBody.getDatiGenerali();
		// 2.1.1
		DatiGeneraliDocumentoType datiGeneraliDocumento = datiGenerali.getDatiGeneraliDocumento();
		// 2.1.1.1
		TipoDocumentoType tipoDocumento = datiGeneraliDocumento.getTipoDocumento();
		// 2.1.1.3
		XMLGregorianCalendar dataDocumento = datiGeneraliDocumento.getData();
		
		//Errore 425
		if(!datiGeneraliDocumento.getNumero().matches(".*[0-9]+.*"))
			throw new Exception(Errori.e425);
		
		DatiRitenutaType datiRitenuta;
		if(datiGeneraliDocumento.getDatiRitenuta() != null)
			datiRitenuta = datiGeneraliDocumento.getDatiRitenuta();
		else
			datiRitenuta = null;
		
		//Errore 411, 415
		boolean datiRitenutaNecessari = false;
		// 2.1.1.7
		for(DatiCassaPrevidenzialeType datiCassaPrevidenziale : datiGeneraliDocumento.getDatiCassaPrevidenziale()) {
			//Errore 413, 414
			if(datiCassaPrevidenziale.getAliquotaIVA().equals(BigDecimal.ZERO.setScale(2))) {
				if(datiCassaPrevidenziale.getNatura()==null) {
					throw new Exception(Errori.e413);
				}
			}else {
				if(datiCassaPrevidenziale.getNatura()!=null) {
					throw new Exception(Errori.e414);
				}
			}
			
			//Errore 415
			if(datiRitenutaNecessari) {
				if(datiCassaPrevidenziale.getRitenuta()==null) {
					throw new Exception(Errori.e415);
				}
			}
			
			//Errore 419
			if(!AliquoteIva.contains(datiCassaPrevidenziale.getAliquotaIVA())){
				AliquoteIva.add(datiCassaPrevidenziale.getAliquotaIVA());
				TotAliquoteIva +=1;
			}
			
			//Errore 424
			if(!datiCassaPrevidenziale.getAliquotaIVA().equals(BigDecimal.ZERO.setScale(2)) && datiCassaPrevidenziale.getAliquotaIVA().compareTo(BigDecimal.ONE) == -1) {
				throw new Exception(Errori.e424);
			}
		}
		
		//2.1.1.8 ScontoMaggiorazione
		for(ScontoMaggiorazioneType scontoMaggiorazione : datiGeneraliDocumento.getScontoMaggiorazione()){
			//Errore 437
			if(scontoMaggiorazione.getTipo()!=null) {
				if(scontoMaggiorazione.getPercentuale()==null && scontoMaggiorazione.getImporto()==null)
					throw new Exception(Errori.e437);
			}
		}
		
		// 2.1.6 DatiFattureCollegate
		for(DatiDocumentiCorrelatiType datiDocumentiCorrelati : datiGenerali.getDatiFattureCollegate()) {
			//Errore 418
			if(tipoDocumento == TipoDocumentoType.TD_04) {
				if(dataDocumento.compare(datiDocumentiCorrelati.getData()) == DatatypeConstants.LESSER) {
					throw new Exception(Errori.e418);
				}
			}
		}

		// 2.2 DatiBeniServizi
		DatiBeniServiziType datiBeniServizi = feBody.getDatiBeniServizi();
		
		// 2.2.1 DettaglioLinee
		for (DettaglioLineeType dettaglioLinea : datiBeniServizi.getDettaglioLinee()) {
			
			//Errore 400,401
			if(dettaglioLinea.getAliquotaIVA().equals(BigDecimal.ZERO.setScale(2))) {
				if(dettaglioLinea.getNatura()==null)
					throw new Exception(Errori.e400);
			} else {
				if(dettaglioLinea.getNatura() != null)
					throw new Exception(Errori.e401);
			}
			
			//Errore 419
			if(!AliquoteIva.contains(dettaglioLinea.getAliquotaIVA())) {
				AliquoteIva.add(dettaglioLinea.getAliquotaIVA());
				TotAliquoteIva +=1;
			}
			
			//2.2.1.10
			for(ScontoMaggiorazioneType scontoMaggiorazione : dettaglioLinea.getScontoMaggiorazione()) {
				
				//Errore 438
				if(scontoMaggiorazione.getTipo()!=null) {
					if(scontoMaggiorazione.getPercentuale()==null && scontoMaggiorazione.getImporto()==null)
						throw new Exception(Errori.e438);
				}
			}

			//Errore 424
			if(!dettaglioLinea.getAliquotaIVA().equals(BigDecimal.ZERO.setScale(2)) && dettaglioLinea.getAliquotaIVA().compareTo(BigDecimal.ONE) == -1) {
				throw new Exception(Errori.e424);
			}

			//Errore 411, 415
			if(dettaglioLinea.getRitenuta() == RitenutaType.SI)
				datiRitenutaNecessari = true;
			
		}
		
		//Errore 411
		if(datiRitenutaNecessari) {
			if(datiRitenuta==null) {
				throw new Exception(Errori.e411);
			}
		}
		
		//Errore 419
		if(datiBeniServizi.getDatiRiepilogo().size() < TotAliquoteIva)
			throw new Exception(Errori.e419);

		// 2.2.2 DatiRiepilogo
		for (DatiRiepilogoType datiRiepilogo : datiBeniServizi.getDatiRiepilogo()) {
			
			//Errore 420
			if(datiRiepilogo.getEsigibilitaIVA() != null && datiRiepilogo.getNatura() != null) {
				if(datiRiepilogo.getEsigibilitaIVA() == EsigibilitaIVAType.S && datiRiepilogo.getNatura() == NaturaType.N_6) {
					throw new Exception(Errori.e420);
				}
			}
			
			//Errore 421
			BigDecimal toll = new BigDecimal(0.01).setScale(2, RoundingMode.HALF_UP);
			BigDecimal mult = datiRiepilogo.getAliquotaIVA().multiply(datiRiepilogo.getImponibileImporto());
			BigDecimal div = mult.divide(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP);
//			System.out.println(div.subtract(toll)+">"+datiRiepilogo.getImposta()+"<"+div.add(toll));
			if(!(datiRiepilogo.getImposta().compareTo(div.subtract(toll)) == 1 && datiRiepilogo.getImposta().compareTo(div.add(toll)) == -1)) {
				throw new Exception(Errori.e421);
			}
			//Tolleranza: 1 centesimo di euro. Se la differenza tra i valori confrontati è inferiore a ±0,01 il controllo si ritiene superato

			//Errore 424
			if(!datiRiepilogo.getAliquotaIVA().equals(BigDecimal.ZERO.setScale(2)) && datiRiepilogo.getAliquotaIVA().compareTo(BigDecimal.ONE) == -1) {
				throw new Exception(Errori.e424);
			}
			
			//Errore 429, 430
			if(datiRiepilogo.getAliquotaIVA().equals(BigDecimal.ZERO.setScale(2))) {
				if(datiRiepilogo.getNatura()==null) {
					throw new Exception(Errori.e429);
				}
			} else {
				if(datiRiepilogo.getNatura() != null) {
					throw new Exception(Errori.e430);
				}
			}
		}

	}
	
	public static class Errori{
		public static String e400 = "Errore 00400 - " + "2.2.1.14 <Natura> non presente a fronte di 2.2.1.12 <AliquotaIVA> pari a zero";
		public static String e401 = "Errore 00401 - " + "2.2.1.14 <Natura> presente a fronte di 2.2.1.12 <AliquotaIVA> diversa da zero";
		//403 N/A
		//404 N/A
		//409 N/A
		public static String e411 = "Errore 00411 - " + "2.1.1.5 <DatiRitenuta> non presente a fronte di almeno un blocco 2.2.1 <DettaglioLinee> con 2.2.1.13 <Ritenuta> uguale a SI";
		public static String e413 = "Errore 00413 - " + "2.1.1.7.7 <Natura> non presente a fronte di 2.1.1.7.5 <AliquotaIVA> pari a zero";
		public static String e414 = "Errore 00414 - " + "2.1.1.7.7 <Natura> presente a fronte di 2.1.1.7.5 <Aliquota IVA> diversa da zero";
		public static String e415 = "Errore 00415 - " + "2.1.1.5 <DatiRitenuta> non presente a fronte di 2.1.1.7.6 <Ritenuta> uguale a SI";
		public static String e417 = "Errore 00417 - " + "1.4.1.1 <IdFiscaleIVA> e 1.4.1.2 <CodiceFiscale> non valorizzati (almeno uno dei due deve essere valorizzato)";
		public static String e418 = "Errore 00418 - " + "2.1.1.3 <Data> antecedente a 2.1.6.3 <Data>";
		public static String e419 = "Errore 00419 - " + "2.2.2 <DatiRiepilogo> non presente in corrispondenza di almeno un valore di 2.1.1.7.5 <AliquotaIVA> o 2.2.1.12 <AliquotaIVA>";
		public static String e420 = "Errore 00420 - " + "2.2.2.2 <Natura> con valore N6 (inversione contabile) a fronte di 2.2.2.7 <EsigibilitaIVA> uguale a S (scissione pagamenti)";
		public static String e421 = "Errore 00421 - " + "2.2.2.6 <Imposta> non calcolato secondo le regole definite nelle specifiche tecniche";
		public static String e422 = "Errore 00422 - " + "2.2.2.5 <ImponibileImporto> non calcolato secondo le regole definite nelle specifiche tecniche";
		public static String e423 = "Errore 00423 - " + "2.2.1.11 <PrezzoTotale> non calcolato secondo le regole definite nelle specifiche tecniche";
		public static String e424 = "Errore 00424 - " + "2.2.1.12 <AliquotaIVA> o 2.2.2.1< AliquotaIVA> o 2.1.1.7.5 <AliquotaIVA> non indicata in termini percentuali";
		public static String e425 = "Errore 00425 - " + "2.1.1.4 <Numero> non contenente caratteri numerici";
		public static String e426 = "Errore 00426 - " + "1.1.6 <PECDestinatario> non valorizzato a fronte di 1.1.4 <CodiceDestinatario> con valore 0000000, o " + 
														"1.1.6 <PECDestinatario> valorizzato a fronte di 1.1.4 <CodiceDestinatario> con valore diverso da 0000000";
		public static String e427 = "Errore 00427 - " + "1.1.4 <CodiceDestinatario> di 7 caratteri a fronte di 1.1.3 <FormatoTrasmissione> con valore “FPA12” o " + 
														"1.1.4 <CodiceDestinatario> di 6 caratteri a fronte di 1.1.3 <FormatoTrasmissione> con valore “FPR12”";
		public static String e428 = "Errore 00428 - " + "1.1.3 <FormatoTrasmissione> non coerente con il valore dell’attributo VERSIONE";
		public static String e429 = "Errore 00429 - " + "2.2.2.2 <Natura> non presente a fronte di 2.2.2.1 <AliquotaIVA> pari a zero";
		public static String e430 = "Errore 00430 - " + "2.2.2.2 <Natura> presente a fronte di 2.2.2.1 <AliquotaIVA> diversa da zero";
		public static String e437 = "Errore 00437 - " + "2.1.1.8.2 <Percentuale> e 2.1.1.8.3 <Importo> non presenti a fronte di 2.1.1.8.1 <Tipo> valorizzato";
		public static String e438 = "Errore 00438 - " + "2.2.1.10.2 <Percentuale> e 2.2.1.10.3 <Importo> non presenti a fronte di 2.2.1.10.1 <Tipo> valorizzato";
	}
	
	public static void main(String[] args) {
		BigDecimal a = new BigDecimal(1500.2449);
		BigDecimal b = new BigDecimal(1500.2458);
		
		a=a.setScale(2, RoundingMode.HALF_UP);
		b=b.setScale(2, RoundingMode.HALF_UP);
		
		System.out.println(a +"/"+b);
	}
}
