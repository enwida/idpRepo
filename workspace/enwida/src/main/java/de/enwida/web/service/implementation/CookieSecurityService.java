/**
 * 
 */
package de.enwida.web.service.implementation;

import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

/**
 * @author Jitin
 *
 */
public class CookieSecurityService {

	private static final String UNICODE_FORMAT = "UTF8";
	/**
	 * Triple Data Encryption Algorithm (TDEA) block cipher
	 */
	private static final String DESEDE_ENCRYPTION_SCHEME = "DESede";

	public String dycryptJsonString(String encryptedStr, String encryptionKey) {
		return decrypt(encryptedStr, encryptionKey);
	}

	public String encryptJsonString(String jsonStr, String encryptionKey) {
		return encrypt(jsonStr, encryptionKey);
	}

	private String encrypt(String unencryptedString, String encryptionKey) {
		String encryptedString = null;
		try {
			Cipher cipher = Cipher.getInstance(DESEDE_ENCRYPTION_SCHEME);
			cipher.init(Cipher.ENCRYPT_MODE, getKey(encryptionKey));
			byte[] plainText = unencryptedString.getBytes(UNICODE_FORMAT);
			byte[] encryptedText = cipher.doFinal(plainText);
			encryptedString = new String(Base64.encodeBase64(encryptedText));
		} catch (Exception e) {
			// log exception
			Logger.getLogger(getClass()).error(
					"Error encountered while encryption: ", e);
		}
		return encryptedString;
	}

	private String decrypt(String encryptedString, String encryptionKey) {
		String decryptedText = null;
		try {
			Cipher cipher = Cipher.getInstance(DESEDE_ENCRYPTION_SCHEME);
			cipher.init(Cipher.DECRYPT_MODE, getKey(encryptionKey));
			byte[] encryptedText = Base64.decodeBase64(encryptedString);
			byte[] plainText = cipher.doFinal(encryptedText);
			decryptedText = new String(plainText);
		} catch (Exception e) {
			// log exception
			Logger.getLogger(getClass()).error(
					"Error encountered while decryption: ", e);
		}
		return decryptedText;
	}

	private SecretKey getKey(String encryptionkey) throws Exception {
		byte[] arrayBytes = encryptionkey.getBytes(UNICODE_FORMAT);
		KeySpec ks = new DESedeKeySpec(arrayBytes);
		SecretKeyFactory skf = SecretKeyFactory
				.getInstance(DESEDE_ENCRYPTION_SCHEME);
		return skf.generateSecret(ks);
	}
}
