package biz.opengate.fatturaelettronica.validator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import biz.opengate.fatturaelettronica.*;

public class FatturaElettronicaCalcoli {
	
	//Errore 422
    /**
     * Check if the taxable amount was calculated correctly
     *
     * @param datiBeniServizi
     *      DatiBeniServiziType object.
     * @param datiGenerali
     * 		DatiGeneraliType object
     *
     * @throws Exception
     */
	public static void controllaCalcoloImponibileImporto(DatiBeniServiziType datiBeniServizi, DatiGeneraliType datiGenerali) throws Exception {
		List<BigDecimal> aliquotaIVAList = new ArrayList<BigDecimal>();
		
		for (DettaglioLineeType dl : datiBeniServizi.getDettaglioLinee()) {
			if (!aliquotaIVAList.contains(dl.getAliquotaIVA()))
				aliquotaIVAList.add(dl.getAliquotaIVA());
		}
		
		for (DatiCassaPrevidenzialeType dcp : datiGenerali.getDatiGeneraliDocumento().getDatiCassaPrevidenziale()) {
			if (!aliquotaIVAList.contains(dcp.getAliquotaIVA()))
				aliquotaIVAList.add(dcp.getAliquotaIVA());
		}
		
		BigDecimal imponibileImporto;
		BigDecimal arrotondamento;
		BigDecimal prezzoTotale;
		BigDecimal importoContrCassa;
		BigDecimal total;
		
		// Per ogni valore distinto di AliquotaIVA
		for (BigDecimal iva : aliquotaIVAList) {
			imponibileImporto = BigDecimal.ZERO;
			arrotondamento = BigDecimal.ZERO;
			prezzoTotale = BigDecimal.ZERO;
			importoContrCassa = BigDecimal.ZERO;
			
			// N
			for (DatiRiepilogoType drt : datiBeniServizi.getDatiRiepilogo()) {
				if (drt.getAliquotaIVA().equals(iva)) {
					imponibileImporto = imponibileImporto.add(drt.getImponibileImporto());
					if (drt.getArrotondamento() != null)
						arrotondamento = arrotondamento.add(drt.getArrotondamento());
				}
			}
			// M
			for (DettaglioLineeType dl : datiBeniServizi.getDettaglioLinee()) {
				if (dl.getAliquotaIVA().equals(iva))
					prezzoTotale = prezzoTotale.add(dl.getPrezzoTotale());
			}
			// P
			for (DatiCassaPrevidenzialeType dcp : datiGenerali.getDatiGeneraliDocumento().getDatiCassaPrevidenziale()) {
				if (dcp.getAliquotaIVA().equals(iva))
					importoContrCassa = importoContrCassa.add(dcp.getImportoContributoCassa());
			}
			
			total = prezzoTotale.add(importoContrCassa.add(arrotondamento));
			
//			System.out.println(
//					"IVA: " + iva.toString() + "\n" +
//					"\tPT:" + prezzoTotale.toString() +
//					"\tIC:" + importoContrCassa.toString() +
//					"\tAR:" + arrotondamento.toString() + 
//					"\tII:" + imponibileImporto.toString() +
//					" == " + total.setScale(2)
//					);
			
			BigDecimal diff = imponibileImporto.subtract(total).abs();
			
			if (diff.compareTo(BigDecimal.ONE)>=0)
				throw new Exception(FatturaElettronicaContentValidator.Errori.e422 +
						"\n(" + imponibileImporto + " != " + total.setScale(2) + ")");
			//Tolleranza: 1 euro. Se la differenza tra i valori confrontati è inferiore a ±1 il controllo si ritiene superato
		}
		
//		System.out.println("\nFine Calcolo II\n\n");
	}
	
	//Errore 423
    /**
     * Check if the total price was calculated correctly
     *
     * @param dettaglioLineeList
     *      List of DettaglioLineeType objects
     *
     * @throws Exception
     */
	public static void controllaCalcoloPrezzoTotale(List<DettaglioLineeType> dettaglioLineeList) throws Exception {
		BigDecimal scontoMaggiorazioneTot;
		BigDecimal result;
		
		for (DettaglioLineeType dettaglioLinea : dettaglioLineeList) {
			scontoMaggiorazioneTot = dettaglioLinea.getPrezzoUnitario();
			
//			System.out.print("DT: " + dettaglioLinea.getNumeroLinea());
//			if (dettaglioLinea.getScontoMaggiorazione().isEmpty()) {
////				System.out.println("\tNo Sconto/Maggiorazione");
//				continue;
//			}
			for (ScontoMaggiorazioneType scontoMaggiorazione : dettaglioLinea.getScontoMaggiorazione()) {
//				System.out.println("\t" + scontoMaggiorazione.getTipo().toString());
				if (scontoMaggiorazione.getImporto() != null) {// Importo
					if (scontoMaggiorazione.getTipo() == TipoScontoMaggiorazioneType.SC) {
//						System.out.println("\t\t-" + scontoMaggiorazione.getImporto());
						// SMT = ST - Imp
						scontoMaggiorazioneTot = scontoMaggiorazioneTot.subtract(scontoMaggiorazione.getImporto());
					} else if (scontoMaggiorazione.getTipo() == TipoScontoMaggiorazioneType.MG) {
//						System.out.println("\t\t+" + scontoMaggiorazione.getImporto());
						// SMT = MT + Imp
						scontoMaggiorazioneTot = scontoMaggiorazioneTot.add(scontoMaggiorazione.getImporto());
					}
				} else {// Percentuale
					if (scontoMaggiorazione.getTipo() == TipoScontoMaggiorazioneType.SC) {
//						System.out.println("\t\t-" + scontoMaggiorazione.getPercentuale()+"%");
						// SMT = SMT - ((SMT * Perc) / 100)
						scontoMaggiorazioneTot = scontoMaggiorazioneTot.subtract(scontoMaggiorazioneTot
								.multiply(scontoMaggiorazione.getPercentuale()).divide(new BigDecimal(100)));
					} else if (scontoMaggiorazione.getTipo() == TipoScontoMaggiorazioneType.MG) {
//						System.out.println("\t\t+" + scontoMaggiorazione.getPercentuale()+"%");
						// SMT = SMT + ((SMT * Perc) / 100)
						scontoMaggiorazioneTot = scontoMaggiorazioneTot.add(scontoMaggiorazioneTot
								.multiply(scontoMaggiorazione.getPercentuale()).divide(new BigDecimal(100)));
					}
				}
//				System.out.println("\t\tTot: "+scontoMaggiorazioneTot);
			}
			
			result = scontoMaggiorazioneTot.multiply(dettaglioLinea.getQuantita()!=null?dettaglioLinea.getQuantita():BigDecimal.ONE);
			
			result = result.setScale(2, RoundingMode.HALF_UP);
//			System.out.println("\tResult: "+dettaglioLinea.getPrezzoTotale() + "==" + result);
			
			BigDecimal diff = dettaglioLinea.getPrezzoTotale().subtract(result).abs();
			
			if (diff.compareTo(new BigDecimal("0.01")) >= 0 ) {
				throw new Exception(FatturaElettronicaContentValidator.Errori.e423 +
						"\n(" + dettaglioLinea.getPrezzoTotale() + " != " + result + ")");
			}
			//Tolleranza: 1 centesimo di euro. Se la differenza tra i valori confrontati è inferiore a ±0,01 il controllo si ritiene superato
		}
//	System.out.println("\nFine Calcolo PT\n\n");
	}
}
