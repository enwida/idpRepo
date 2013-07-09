/**
 * 
 */
package de.enwida.web;

import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.enwida.web.service.implementation.CookieSecurityService;
import de.enwida.web.utils.Constants;

/**
 * @author Jitin
 *
 */
public class CookieSecurityHelperTest {

	private String plaintext = "something to encrypt";
	private String encryptedText = "E7/4iATyWCOGG0wAjxlczpwgDtUyQ3Tb";
	private CookieSecurityService cookieSecurityHelper;
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		cookieSecurityHelper = new CookieSecurityService();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		cookieSecurityHelper = null;
	}

	/**
	 * Test method for {@link de.enwida.web.utils.CookieSecurityHelper#dycryptJsonString(java.lang.String)}.
	 */
	@Test
	public void testDycryptJsonString() {
		String plaintext = cookieSecurityHelper
.dycryptJsonString(
				encryptedText, Constants.ENCRYPTION_KEY);
		System.out.println(encryptedText + " => " + plaintext);
		assertNotNull(plaintext);
	}

	/**
	 * Test method for {@link de.enwida.web.utils.CookieSecurityHelper#encryptJsonString(java.lang.String)}.
	 */
	@Test
	public void testEncryptJsonString() {
		String encryptedText = cookieSecurityHelper
.encryptJsonString(
				plaintext, Constants.ENCRYPTION_KEY);
		System.out.println(plaintext + " => " + encryptedText);
		assertNotNull(encryptedText);
	}

}
