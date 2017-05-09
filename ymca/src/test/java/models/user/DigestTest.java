/**
 * 
 */
package models.user;

import java.security.NoSuchAlgorithmException;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Matt Rayner
 */
public class DigestTest
{
	private static final String testData = "testPassword";
	
	/**
	 * Test method for {@link models.user.Digest#fromPlainText(java.lang.CharSequence)}.
	 */
	@Test
	public void testFromPlainText()
	{
		try
		{
			final Digest stored = Digest.fromPlainText(DigestTest.testData);
			Assert.assertTrue("Stored password does not match input password", stored.matches(DigestTest.testData));
		}
		catch (NoSuchAlgorithmException nsae)
		{
			Assert.fail(nsae.getMessage());
		}
	}
}
