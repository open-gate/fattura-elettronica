package biz.opengate.fatturaelettronica.utils;

import org.bouncycastle.cms.CMSSignedData;

public class P7mUtils {
	public static byte[] decryptP7m(byte[] inputData) throws Exception {
		CMSSignedData signature = new CMSSignedData(inputData);
		byte[] data = null;
		data = (byte[]) signature.getSignedContent().getContent();
		return data;
	}
}
