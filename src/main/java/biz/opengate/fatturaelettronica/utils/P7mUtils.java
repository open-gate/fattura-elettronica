package biz.opengate.fatturaelettronica.utils;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.cms.CMSSignedData;

public class P7mUtils {
	public static byte[] decryptP7m(byte[] inputData) throws Exception {
		byte[] data = null;
		try {
			CMSSignedData signature = new CMSSignedData(inputData);	
			data = (byte[]) signature.getSignedContent().getContent();
		} catch (Exception e) {
			CMSSignedData signature = new CMSSignedData(Base64.decodeBase64(inputData));
			data = (byte[]) signature.getSignedContent().getContent();			
		}
		return data;
	}
}