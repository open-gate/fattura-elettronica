package biz.opengate.fatturaelettronica.validator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.XMLGregorianCalendar;

import biz.opengate.fatturaelettronica.*;
import biz.opengate.fatturaelettronica.utils.IVAUtils;

public class FatturaElettronicaContentValidator {
	
	private static List<NaturaType> natureInvalide = Arrays.asList(new NaturaType[] {NaturaType.N_2, NaturaType.N_3, NaturaType.N_6});
	
	private static List<NaturaType> natureN6 = Arrays.asList(new NaturaType[] {
			NaturaType.N_6, NaturaType.N_6_1, NaturaType.N_6_2, NaturaType.N_6_3, NaturaType.N_6_4,
			NaturaType.N_6_5, NaturaType.N_6_6, NaturaType.N_6_7, NaturaType.N_6_8, NaturaType.N_6_9
	});
	
	private static List<TipoDocumentoType> tipiDocumentoCedenteCessionarioNonEquivalenti = Arrays.asList(new TipoDocumentoType[] {
		TipoDocumentoType.TD_01, TipoDocumentoType.TD_02, TipoDocumentoType.TD_03, TipoDocumentoType.TD_06,
		TipoDocumentoType.TD_16, TipoDocumentoType.TD_17, TipoDocumentoType.TD_18, TipoDocumentoType.TD_19,
		TipoDocumentoType.TD_20, TipoDocumentoType.TD_24, TipoDocumentoType.TD_25, TipoDocumentoType.TD_28
	});
	
	private static List<TipoDocumentoType> tipiDocumentoCedenteCessionarioEquivalenti = Arrays.asList(new TipoDocumentoType[] {
		TipoDocumentoType.TD_21, TipoDocumentoType.TD_27
	});
	
	private static List<TipoDocumentoType> tipiDocumentoPaeseNoIt = Arrays.asList(new TipoDocumentoType[] {
		TipoDocumentoType.TD_17, TipoDocumentoType.TD_18, TipoDocumentoType.TD_19, TipoDocumentoType.TD_28
	});

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
		//1
		FatturaElettronicaHeaderType feHeader = fatturaElettronica.getFatturaElettronicaHeader();
		//1.1
		DatiTrasmissioneType datiTrasmissione = feHeader.getDatiTrasmissione();
		
		//Errore 426
		if(datiTrasmissione.getPECDestinatario()!=null) {
			if(!datiTrasmissione.getCodiceDestinatario().equals("0000000"))
				throw new Exception(Errori.e426b);
		}
		
		//Errore 427
		if(datiTrasmissione.getFormatoTrasmissione() == FormatoTrasmissioneType.FPA_12) {
			if(datiTrasmissione.getCodiceDestinatario().length() == 7) {
				throw new Exception(Errori.e427a);
			}
		} else {
			if(datiTrasmissione.getCodiceDestinatario().length() == 6) {
				throw new Exception(Errori.e427b);
			}
		}
		
		//Errore 428
		if(datiTrasmissione.getFormatoTrasmissione() != fatturaElettronica.getVersione())
			throw new Exception(Errori.e428);

		//IVA
		if(datiTrasmissione.getIdTrasmittente().getIdPaese().equals("IT"))
			IVAUtils.validateIVA(datiTrasmissione.getIdTrasmittente().getIdCodice());
		
		//1.2
		CedentePrestatoreType cedentePrestatore = feHeader.getCedentePrestatore();
		
		//IVA
		if(cedentePrestatore.getDatiAnagrafici().getIdFiscaleIVA().getIdPaese().equals("IT"))
			IVAUtils.validateIVA(cedentePrestatore.getDatiAnagrafici().getIdFiscaleIVA().getIdCodice());
		
		//1.3
		RappresentanteFiscaleType rappresentanteFiscale = feHeader.getRappresentanteFiscale();
		if(rappresentanteFiscale!=null) {
			//IVA
			if(rappresentanteFiscale.getDatiAnagrafici().getIdFiscaleIVA().getIdPaese().equals("IT"))
				IVAUtils.validateIVA(rappresentanteFiscale.getDatiAnagrafici().getIdFiscaleIVA().getIdCodice());
		}
		
		//1.4
		CessionarioCommittenteType cessionarioCommittente = feHeader.getCessionarioCommittente();
		
		//1.4.1
		DatiAnagraficiCessionarioType datiAnagraficiCessionario = cessionarioCommittente.getDatiAnagrafici();
		
		//Errore 417
		if(datiAnagraficiCessionario.getIdFiscaleIVA() == null && datiAnagraficiCessionario.getCodiceFiscale() == null) {
			throw new Exception(Errori.e417);
		}

		//IVA
		if(cessionarioCommittente.getDatiAnagrafici().getIdFiscaleIVA()!=null)
		{
			if(cessionarioCommittente.getDatiAnagrafici().getIdFiscaleIVA().getIdPaese().equals("IT"))
				IVAUtils.validateIVA(cessionarioCommittente.getDatiAnagrafici().getIdFiscaleIVA().getIdCodice());
		}
		
		//1.5
		TerzoIntermediarioSoggettoEmittenteType terzoIntermediarioSoggettoEmittente = feHeader.getTerzoIntermediarioOSoggettoEmittente();
		if(terzoIntermediarioSoggettoEmittente != null) {
			//IVA
			if(terzoIntermediarioSoggettoEmittente.getDatiAnagrafici().getIdFiscaleIVA().getIdPaese().equals("IT"))
				IVAUtils.validateIVA(terzoIntermediarioSoggettoEmittente.getDatiAnagrafici().getIdFiscaleIVA().getIdCodice());
		}
		
		
		// BODY
		for (FatturaElettronicaBodyType feBody : fatturaElettronica.getFatturaElettronicaBody()) {
			
			TipoDocumentoType tipoDocumento = feBody.getDatiGenerali().getDatiGeneraliDocumento().getTipoDocumento();
			
			//Errore 471
			if(tipiDocumentoCedenteCessionarioNonEquivalenti.contains(tipoDocumento)) {
				if(IVAUtils.equalsIdFiscaleIVACedenteCessionario(feHeader)) {
					throw new Exception(Errori.e471);
				}
			}
			//Errore 472
			if(tipiDocumentoCedenteCessionarioEquivalenti.contains(tipoDocumento)) {
				if(!IVAUtils.equalsIdFiscaleIVACedenteCessionario(feHeader)) {
					throw new Exception(Errori.e472);
				}
			}
			//Errore 473
			if(tipiDocumentoPaeseNoIt.contains(tipoDocumento)) {
				if(cedentePrestatore.getDatiAnagrafici().getIdFiscaleIVA().getIdPaese().equals("IT")) {
					throw new Exception(Errori.e473);
				}
			}
			
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
		
		List<BigDecimal> aliquoteIvaDatiCassaPrevidenziale = new ArrayList<BigDecimal>();
		List<BigDecimal> aliquoteIvaDettaglioLinee = new ArrayList<BigDecimal>();
		List<BigDecimal> aliquoteIvaDatiRiepilogo = new ArrayList<BigDecimal>();
		
		List<NaturaType> natureDatiCassaPrevidenziale = new ArrayList<NaturaType>();
		List<NaturaType> natureDettaglioLinee = new ArrayList<NaturaType>();
		List<NaturaType> natureDatiRiepilogo = new ArrayList<NaturaType>();
		
		//Errore 425
		if(!datiGeneraliDocumento.getNumero().matches(".*[0-9]+.*"))
			throw new Exception(Errori.e425 + "\n(" + datiGeneraliDocumento.getNumero() + ")");
		
		//Errore 415
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
			if(datiCassaPrevidenziale.getRitenuta() == RitenutaType.SI)
				datiRitenutaNecessari = true;
			
			//Errore 419
			if(!AliquoteIva.contains(datiCassaPrevidenziale.getAliquotaIVA())){
				AliquoteIva.add(datiCassaPrevidenziale.getAliquotaIVA());
				TotAliquoteIva +=1;
			}
			
			//Errore 424
			if(!datiCassaPrevidenziale.getAliquotaIVA().equals(BigDecimal.ZERO.setScale(2)) && datiCassaPrevidenziale.getAliquotaIVA().compareTo(BigDecimal.ONE) == -1) {
				throw new Exception(Errori.e424c + "\n(IVA dati cassa previdenziale: " + datiCassaPrevidenziale.getAliquotaIVA() + ")");
			}

			//Errore 445
			if(natureInvalide.contains(datiCassaPrevidenziale.getNatura())) {
				throw new Exception(Errori.e445);
			}
			
			aliquoteIvaDatiCassaPrevidenziale.add(datiCassaPrevidenziale.getAliquotaIVA());
			natureDatiCassaPrevidenziale.add(datiCassaPrevidenziale.getNatura());
		}
		
		if(datiRitenutaNecessari) {
			if(datiGeneraliDocumento.getDatiRitenuta().isEmpty()) {
				throw new Exception(Errori.e415);
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

		//Errore 411
		datiRitenutaNecessari = false;
		// 2.2 DatiBeniServizi
		DatiBeniServiziType datiBeniServizi = feBody.getDatiBeniServizi();
		
		// 2.2.1 DettaglioLinee
		for (DettaglioLineeType dettaglioLinea : datiBeniServizi.getDettaglioLinee()) {
			
			if(dettaglioLinea.getAliquotaIVA().equals(BigDecimal.ZERO.setScale(2))) {
				//Errore 400
				if(dettaglioLinea.getNatura()==null) {
					throw new Exception(Errori.e400);
				}
				//Errore 474
				if(tipoDocumento.equals(TipoDocumentoType.TD_21)) {
					throw new Exception(Errori.e474);
				}
			} else {
				//Errore 401
				if(dettaglioLinea.getNatura() != null) {
					throw new Exception(Errori.e401);
				}
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
				throw new Exception(Errori.e424a + "\n(Riga di dettaglio " + dettaglioLinea.getNumeroLinea() + " con IVA: " + dettaglioLinea.getAliquotaIVA() + ")");
			}

			//Errore 411
			if(dettaglioLinea.getRitenuta() == RitenutaType.SI)
				datiRitenutaNecessari = true;
			
			//Errore 445
			if(natureInvalide.contains(dettaglioLinea.getNatura())) {
				throw new Exception(Errori.e445);
			}
			
			aliquoteIvaDettaglioLinee.add(dettaglioLinea.getAliquotaIVA());
			natureDettaglioLinee.add(dettaglioLinea.getNatura());
		}
		
		//Errore 411
		if(datiRitenutaNecessari) {
			if(datiGeneraliDocumento.getDatiRitenuta().isEmpty()) {
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
				if(datiRiepilogo.getEsigibilitaIVA() == EsigibilitaIVAType.S && natureN6.contains(datiRiepilogo.getNatura())) {
					throw new Exception(Errori.e420);
				}
			}
			
			//Errore 421
			BigDecimal toll = new BigDecimal(0.01).setScale(2, RoundingMode.HALF_UP);
			BigDecimal mult = datiRiepilogo.getAliquotaIVA().multiply(datiRiepilogo.getImponibileImporto());
			BigDecimal div = mult.divide(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP);

			if(!(datiRiepilogo.getImposta().compareTo(div.subtract(toll)) == 1 && datiRiepilogo.getImposta().compareTo(div.add(toll)) == -1)) {
				throw new Exception(Errori.e421 + "\n(" + datiRiepilogo.getImposta() + " != " + div +")");
			}
			//Tolleranza: 1 centesimo di euro. Se la differenza tra i valori confrontati è inferiore a ±0,01 il controllo si ritiene superato

			//Errore 424
			if(!datiRiepilogo.getAliquotaIVA().equals(BigDecimal.ZERO.setScale(2)) && datiRiepilogo.getAliquotaIVA().compareTo(BigDecimal.ONE) == -1) {
				throw new Exception(Errori.e424b + "\n(IVA dati riepilogo: " + datiRiepilogo.getAliquotaIVA() + ")");
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
			
			//Errore 445
			if(natureInvalide.contains(datiRiepilogo.getNatura())) {
				throw new Exception(Errori.e445);
			}
			
			aliquoteIvaDatiRiepilogo.add(datiRiepilogo.getAliquotaIVA());
			natureDatiRiepilogo.add(datiRiepilogo.getNatura());
		}
		
		//Errore 443
		for(BigDecimal aliquotaIvaDatiCassaPrevidenziale : aliquoteIvaDatiCassaPrevidenziale) {
			if(!aliquoteIvaDatiRiepilogo.contains(aliquotaIvaDatiCassaPrevidenziale)) {
				throw new Exception(Errori.e443);
			}
		}
		for(BigDecimal aliquotaIvaDettaglioLinee : aliquoteIvaDettaglioLinee) {
			if(!aliquoteIvaDatiRiepilogo.contains(aliquotaIvaDettaglioLinee)) {
				throw new Exception(Errori.e443);
			}
		}
		
		//Errore 444
		for(NaturaType naturaDatiCassaPrevidenziale : natureDatiCassaPrevidenziale) {
			if(!natureDatiRiepilogo.contains(naturaDatiCassaPrevidenziale)) {
				throw new Exception(Errori.e444);
			}
		}
		for(NaturaType naturaDettaglioLinee : natureDettaglioLinee) {
			if(!natureDatiRiepilogo.contains(naturaDettaglioLinee)) {
				throw new Exception(Errori.e444);
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
		public static String e424a = "Errore 00424 - " + "2.2.1.12 <AliquotaIVA> non indicata in termini percentuali";
		public static String e424b = "Errore 00424 - " + "2.2.2.1< AliquotaIVA> non indicata in termini percentuali";
		public static String e424c = "Errore 00424 - " + "2.1.1.7.5 <AliquotaIVA> non indicata in termini percentuali";
		public static String e425 = "Errore 00425 - " + "2.1.1.4 <Numero> non contenente caratteri numerici";
		public static String e426a = "Errore 00426 - " + "1.1.6 <PECDestinatario> non valorizzato a fronte di 1.1.4 <CodiceDestinatario> con valore 0000000";
		public static String e426b = "Errore 00426 - " + "1.1.6 <PECDestinatario> valorizzato a fronte di 1.1.4 <CodiceDestinatario> con valore diverso da 0000000";
		public static String e427a = "Errore 00427 - " + "1.1.4 <CodiceDestinatario> di 7 caratteri a fronte di 1.1.3 <FormatoTrasmissione> con valore “FPA12”";
		public static String e427b = "Errore 00427 - " + "1.1.4 <CodiceDestinatario> di 6 caratteri a fronte di 1.1.3 <FormatoTrasmissione> con valore “FPR12”";
		public static String e428 = "Errore 00428 - " + "1.1.3 <FormatoTrasmissione> non coerente con il valore dell’attributo VERSIONE";
		public static String e429 = "Errore 00429 - " + "2.2.2.2 <Natura> non presente a fronte di 2.2.2.1 <AliquotaIVA> pari a zero";
		public static String e430 = "Errore 00430 - " + "2.2.2.2 <Natura> presente a fronte di 2.2.2.1 <AliquotaIVA> diversa da zero";
		public static String e437 = "Errore 00437 - " + "2.1.1.8.2 <Percentuale> e 2.1.1.8.3 <Importo> non presenti a fronte di 2.1.1.8.1 <Tipo> valorizzato";
		public static String e438 = "Errore 00438 - " + "2.2.1.10.2 <Percentuale> e 2.2.1.10.3 <Importo> non presenti a fronte di 2.2.1.10.1 <Tipo> valorizzato";	
		public static String e443 = "Errore 00443 - " + "Non c’è corrispondenza tra i valori indicati nell’elemento 2.2.1.12 <AliquotaIVA> o 2.1.1.7.5 <AliquotaIVA> e quelli dell’elemento 2.2.2.1 <ALiquotaIVA>";
		public static String e444 = "Errore 00444 - " + "Non c’è corrispondenza tra i valori indicati nell’elemento 2.2.1.14 <Natura> o 2.1.1.7.7 <Natura> e quelli dell’elemento 2.2.2.2 <Natura>";
		public static String e445 = "Errore 00445 - " + "Non è più ammesso il valore generico N2, N3 o N6 come codice natura dell’operazione";
		public static String e471 = "Errore 00471 - " + "Per il valore indicato nell’elemento 2.1.1.1 <TipoDocumento> il cedente/prestatore non può essere uguale al cessionario/committente";
		public static String e472 = "Errore 00472 - " + "Per il valore indicato nell’elemento 2.1.1.1 <TipoDocumento> il cedente/prestatore deve essere uguale al cessionario/committente";
		public static String e473 = "Errore 00473 - " + "Per il valore indicato nell’elemento 2.1.1.1 <TipoDocumento> il valore nell’elemento 1.2.1.1.1 <IdPaese> non è ammesso";
		public static String e474 = "Errore 00474 - " + "Per il valore indicato nell’elemento 2.1.1.1 <TipoDocumento> non sono ammesse linee di dettaglio con l’elemento 2.2.1.12 <AliquotaIVA> contenente valore zero";
		public static String eIVA = "Codice IVA non valido";
	}
}
