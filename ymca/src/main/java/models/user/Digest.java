package models.user;

import java.security.NoSuchAlgorithmException;
import java.util.Objects;

import org.apache.catalina.realm.SecretKeyCredentialHandler;
import org.apache.tomcat.util.buf.HexUtils;

/**
 * Class representing a digested password.
 * 
 * @author Matt Rayner
 */
public class Digest
{
	private static final String ALGORITHM = "PBKDF2WithHmacSHA512";
	private static final int KEY_LENGTH = 256;
	private static final int ITERATIONS = 100000;
	private static final int SALT_LENGTH = 16;
	private static final String DELIMETER = "\\$";
	
	private final String Value;
	
	private String encodedPassword;
	private byte[] salt;
	private int iterations = -1;

	/**
	 * Class Constructor.
	 *
	 * @param digestedString the already digested string.
	 */
	private Digest(final CharSequence digestedString)
	{
		this.Value = digestedString.toString();
	}
	
	/**
	 * splits and stores the digested value into the separate salt, iterations, and password.
	 */
	private final void process()
	{
		final String[] sections = this.Value.split(Digest.DELIMETER);
		switch (sections.length)
		{
			case 1:
				this.salt = new byte[0];
				this.iterations = 1;
				this.encodedPassword = this.Value;
				break;
			case 3:
				this.salt = HexUtils.fromHexString(sections[0]);
				this.iterations = Integer.parseInt(sections[1]);
				this.encodedPassword = sections[2];
				break;
			default:
				throw new IllegalArgumentException("Digest format incorrect");
		}
	}

	/**
	 * Gets a digest from an already encoded String.
	 * 
	 * @param digestedString the digested string.
	 * @return a Digest.
	 */
	public static Digest fromEncrypted(final CharSequence digestedString)
	{
		Objects.requireNonNull(digestedString, "password cannot be null");
		return new Digest(digestedString);
	}
	
	/**
	 * Generates a digest from a plain text password.
	 * 
	 * @param password the password to generate a digest for.
	 * @return a Digest
	 * @throws NoSuchAlgorithmException if the digest algorithm is not found
	 */
	public static Digest fromPlainText(final CharSequence password) throws NoSuchAlgorithmException
	{
			Objects.requireNonNull(password, "password cannot be null");
			SecretKeyCredentialHandler handler = new SecretKeyCredentialHandler();
			handler.setAlgorithm(Digest.ALGORITHM);
			handler.setKeyLength(Digest.KEY_LENGTH);
			handler.setIterations(Digest.ITERATIONS);
			handler.setSaltLength(Digest.SALT_LENGTH);

			return new Digest(handler.mutate(password.toString()));
	}

	/**
	 * Gets the encoded password.
	 * 
	 * @return the encoded password.
	 */
	public String getEncodedPassword()
	{
		if (Objects.isNull(this.encodedPassword))
		{
			this.process();
		}
		return this.encodedPassword;
	}

	/**
	 * Gets the salt used for this digest.
	 * 
	 * @return this digests salt.
	 */
	public byte[] getSalt()
	{
		if (Objects.isNull(this.salt))
		{
			this.process();
		}
		return this.salt;
	}

	/**
	 * Gets the number of iterations used for this digest.
	 * 
	 * @return this digests number of iterations
	 */
	public int getIterations()
	{
		if (-1 == this.iterations)
		{
			this.process();
		}
		return this.iterations;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return this.Value;
	}
}
