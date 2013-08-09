package de.enwida.web.service.interfaces;


public interface ICookieSecurityService {

	public String encryptJsonString(String jsonStr, String encryptionKey);

	public String decryptJsonString(String encryptedStr, String encryptionKey);

}
