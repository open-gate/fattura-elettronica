# fattura-elettronica

fattura-elettronica è una libreria utile a semplificare molte delle complicazioni presenti nella generazione e utilizzo delle fatturazioni elettroniche

## Esempi

### Controllare una fattura

```objective-c
//Inizializza la fattura elettronica
FatturaElettronicaType fatturaElettronica;

//Genera una fattura elettronica di prova
fatturaElettronica = Test.NewFattura();

//
FatturaElettronicaValidator fev = new FatturaElettronicaValidator();

//Controlla che la fattura elettronica sia valida
fev.controllaFatturaElettronica(fatturaElettronica);

//Genera il file xml con i dati della fattura elettronica
File f = FEUtils.MarshalToFile(fatturaElettronica, "");
f.createNewFile();
```
