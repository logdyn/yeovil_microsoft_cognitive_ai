package models.user;

import java.security.NoSuchAlgorithmException;

import org.apache.catalina.realm.SecretKeyCredentialHandler;

public class Digest
{
	private final String encodedValue;
	private final String salt;
	private final int iterations;

	private static final String ALGORITHM = "PBKDF2WithHmacSHA512";
	private static final int KEY_LENGTH = 256;
	private static final int ITERATIONS = 100000;
	private static final int SALT_LENGTH = 16;

	private Digest(final String digestedString)
	{
		final String[] sections = digestedString.split("\\$");
		this.salt = sections[0];
		this.iterations = Integer.parseInt(sections[1]);
		this.encodedValue = sections[2];
	}

	public static Digest fromEncrypted(String digestedString)
	{
		return new Digest(digestedString);
	}
	
	public static Digest fromPlainText(final String password) throws NoSuchAlgorithmException
	{
			SecretKeyCredentialHandler handler = new SecretKeyCredentialHandler();
			handler.setAlgorithm(ALGORITHM);
			handler.setKeyLength(KEY_LENGTH);
			handler.setIterations(ITERATIONS);
			handler.setSaltLength(SALT_LENGTH);

			return new Digest(handler.mutate(password));
	}

	public String getEncodedPassword()
	{
		return this.encodedValue;
	}

	public String getSalt()
	{
		return this.salt;
	}

	public int getIterations()
	{
		return this.iterations;
	}

	@Override
	public String toString()
	{
		return String.format("%s$%d$%s", this.salt, Integer.valueOf(this.iterations), this.encodedValue);
	}
}
