package de.enwida.web.utils;

import java.math.BigInteger;
import java.security.SecureRandom;

public class ActivationIdGenerator {

	private SecureRandom random = new SecureRandom();

	  public String getActivationId()
	  {
	    return new BigInteger(130, random).toString(32);
	  }
}
