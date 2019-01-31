package biz.opengate.fatturaElettronica.tests;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.xml.datatype.DatatypeConfigurationException;
import biz.opengate.fatturaelettronica.*;
import biz.opengate.fatturaelettronica.utils.FEUtils;

public class FatturaElettronicaSemplice {
	public static FatturaElettronicaType newFattura() throws DatatypeConfigurationException {
		FatturaElettronicaType fatturaElettronica = new FatturaElettronicaType();
			fatturaElettronica.setVersione(FormatoTrasmissioneType.FPR_12);
		
			FatturaElettronicaHeaderType fatturaElettronicaHeader = new FatturaElettronicaHeaderType();
			
				DatiTrasmissioneType datiTrasmissione = new DatiTrasmissioneType();
					IdFiscaleType idTrasmittente = new IdFiscaleType();
						idTrasmittente.setIdPaese("IT");
						idTrasmittente.setIdCodice("01234567890");
					datiTrasmissione.setIdTrasmittente(idTrasmittente);
					datiTrasmissione.setProgressivoInvio("000");
					datiTrasmissione.setFormatoTrasmissione(FormatoTrasmissioneType.FPR_12);
					datiTrasmissione.setCodiceDestinatario("0000000");
					datiTrasmissione.setPECDestinatario("mail@pec.it");
				fatturaElettronicaHeader.setDatiTrasmissione(datiTrasmissione);
				
				CedentePrestatoreType cedentePrestatore = new CedentePrestatoreType();
					DatiAnagraficiCedenteType datiAnagraficiCedente = new DatiAnagraficiCedenteType();
						IdFiscaleType idFiscaleCedente = new IdFiscaleType();
							idFiscaleCedente.setIdPaese("IT");
							idFiscaleCedente.setIdCodice("01234567890");
						datiAnagraficiCedente.setIdFiscaleIVA(idFiscaleCedente);
						datiAnagraficiCedente.setCodiceFiscale("01234567890");
						AnagraficaType anagraficaCedente = new AnagraficaType();
							anagraficaCedente.setDenominazione("Nome Cedente Prestatore");
						datiAnagraficiCedente.setAnagrafica(anagraficaCedente);
						datiAnagraficiCedente.setRegimeFiscale(RegimeFiscaleType.RF_01);
					cedentePrestatore.setDatiAnagrafici(datiAnagraficiCedente);
					IndirizzoType sedeCedente = new IndirizzoType();
						sedeCedente.setIndirizzo("Via a");
						sedeCedente.setCAP("12345");
						sedeCedente.setComune("Comune");
						sedeCedente.setProvincia("MI");
						sedeCedente.setNazione("IT");
					cedentePrestatore.setSede(sedeCedente);
					IscrizioneREAType iscrizioneREA = new IscrizioneREAType();
						iscrizioneREA.setUfficio("MI");
						iscrizioneREA.setNumeroREA("1234567");
						iscrizioneREA.setCapitaleSociale(new BigDecimal(10000.00).setScale(2));
						iscrizioneREA.setSocioUnico(SocioUnicoType.SU);
						iscrizioneREA.setStatoLiquidazione(StatoLiquidazioneType.LN);
					cedentePrestatore.setIscrizioneREA(iscrizioneREA);
				fatturaElettronicaHeader.setCedentePrestatore(cedentePrestatore);
				
				CessionarioCommittenteType cessionarioCommittente = new CessionarioCommittenteType();
					DatiAnagraficiCessionarioType datiAnagraficiCessionario = new DatiAnagraficiCessionarioType();
						IdFiscaleType idFiscaleCessionario = new IdFiscaleType();
							idFiscaleCessionario.setIdPaese("IT");
							idFiscaleCessionario.setIdCodice("01234567890");
						datiAnagraficiCessionario.setIdFiscaleIVA(idFiscaleCessionario);
						datiAnagraficiCessionario.setCodiceFiscale("01234567890");
						AnagraficaType anagraficaCessionario = new AnagraficaType();
							anagraficaCessionario.setDenominazione("Nome Cedente Prestatore");
						datiAnagraficiCessionario.setAnagrafica(anagraficaCessionario);
					cessionarioCommittente.setDatiAnagrafici(datiAnagraficiCessionario);
					IndirizzoType sedeCessionario = new IndirizzoType();
						sedeCessionario.setIndirizzo("Via a");
						sedeCessionario.setCAP("12345");
						sedeCessionario.setComune("Comune");
						sedeCessionario.setProvincia("MI");
						sedeCessionario.setNazione("IT");
					cessionarioCommittente.setSede(sedeCessionario);
				fatturaElettronicaHeader.setCessionarioCommittente(cessionarioCommittente);
			
			fatturaElettronica.setFatturaElettronicaHeader(fatturaElettronicaHeader);
			
			FatturaElettronicaBodyType fatturaElettronicaBody = new FatturaElettronicaBodyType();
			
				DatiGeneraliType datiGenerali = new DatiGeneraliType();
					DatiGeneraliDocumentoType datiGeneraliDocumento = new DatiGeneraliDocumentoType();
						datiGeneraliDocumento.setTipoDocumento(TipoDocumentoType.TD_01);
						datiGeneraliDocumento.setDivisa("EUR");
						datiGeneraliDocumento.setData(FEUtils.dateToXmlGregCal(new Date()));
						datiGeneraliDocumento.setNumero("1");
						datiGeneraliDocumento.setImportoTotaleDocumento(new BigDecimal(100.00).setScale(2));
						datiGeneraliDocumento.setArrotondamento(new BigDecimal(0.00).setScale(2));
					datiGenerali.setDatiGeneraliDocumento(datiGeneraliDocumento);
				fatturaElettronicaBody.setDatiGenerali(datiGenerali);
				
				DatiBeniServiziType datiBeniServizi = new DatiBeniServiziType();
					List<DettaglioLineeType> dettagliLinee = new ArrayList<DettaglioLineeType>();
						DettaglioLineeType dettaglioLinea = new DettaglioLineeType();
							dettaglioLinea.setNumeroLinea(10);
							dettaglioLinea.setDescrizione("A");
							dettaglioLinea.setQuantita(new BigDecimal(1.00).setScale(2));
							dettaglioLinea.setPrezzoUnitario(new BigDecimal(100.00).setScale(2));
							dettaglioLinea.setPrezzoTotale(new BigDecimal(100.00).setScale(2));
							dettaglioLinea.setAliquotaIVA(new BigDecimal(10.00).setScale(2));
						dettagliLinee.add(dettaglioLinea);
						dettaglioLinea = new DettaglioLineeType();
							dettaglioLinea.setNumeroLinea(20);
							dettaglioLinea.setDescrizione("B");
							dettaglioLinea.setQuantita(new BigDecimal(1.00).setScale(2));
							dettaglioLinea.setPrezzoUnitario(new BigDecimal(100.00).setScale(2));
							dettaglioLinea.setPrezzoTotale(new BigDecimal(100.00).setScale(2));
							dettaglioLinea.setAliquotaIVA(new BigDecimal(10.00).setScale(2));
						dettagliLinee.add(dettaglioLinea);
						datiBeniServizi.getDettaglioLinee().addAll(dettagliLinee);
					DatiRiepilogoType datiRiepilogo = new DatiRiepilogoType();
						datiRiepilogo.setAliquotaIVA(new BigDecimal(10.00).setScale(2));
						datiRiepilogo.setImponibileImporto(new BigDecimal(200.00).setScale(2));
						datiRiepilogo.setImposta(new BigDecimal(20.00).setScale(2));
						datiRiepilogo.setEsigibilitaIVA(EsigibilitaIVAType.I);
					datiBeniServizi.getDatiRiepilogo().add(datiRiepilogo);
				fatturaElettronicaBody.setDatiBeniServizi(datiBeniServizi);
				
				DatiPagamentoType datiPagamento = new DatiPagamentoType();
					datiPagamento.setCondizioniPagamento(CondizioniPagamentoType.TP_01);
					DettaglioPagamentoType dettaglioPagamento = new DettaglioPagamentoType();
						dettaglioPagamento.setModalitaPagamento(ModalitaPagamentoType.MP_05);
						dettaglioPagamento.setDataScadenzaPagamento(FEUtils.dateToXmlGregCal(new Date()));
						dettaglioPagamento.setImportoPagamento(new BigDecimal(200.00).setScale(2));
					datiPagamento.getDettaglioPagamento().add(dettaglioPagamento);
				fatturaElettronicaBody.getDatiPagamento().add(datiPagamento);
				
			fatturaElettronica.getFatturaElettronicaBody().add(fatturaElettronicaBody);
		return fatturaElettronica;
	}
}
