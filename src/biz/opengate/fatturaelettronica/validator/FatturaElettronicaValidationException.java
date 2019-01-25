package biz.opengate.fatturaelettronica.validator;

public class FatturaElettronicaValidationException extends Exception {
	private static final long serialVersionUID = 1L;
	
	
	public FatturaElettronicaValidationException(String msg, Exception e) {
		super(msg, e);		
	}
	
	public FatturaElettronicaValidationException(String msg) {
		super(msg);
	}
}
