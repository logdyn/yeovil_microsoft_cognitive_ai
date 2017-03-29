/**
 * 
 */
package ai_project.common.apiKeys.storage;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import ai_project.common.apiKeys.ApiKey;
import ai_project.common.apiKeys.KeyType;
import ai_project.common.apiKeys.storage.interfaces.AbstractWriteableKeyStorage;

/**
 * @author Matt
 *
 */
public class WriteableCSVFileStorage extends AbstractWriteableKeyStorage
{
	private static final char SEPARATOR = ',';
	
	private static final char COMMENT = '#';
	
	private String filePath;
	
	/**
	 * Class Constructor.
	 *
	 * @param path - path of csv file.
	 */
	public WriteableCSVFileStorage(final String path)
	{
		this.filePath = path;
	}
	
	/**
	 * Class Constructor.
	 */
	public WriteableCSVFileStorage()
	{
		this(".\\resources\\keys.csv");
	}
	
	public WriteableCSVFileStorage(final Collection<ApiKey> keys)
	{
		super(keys);
	}
	
	public WriteableCSVFileStorage(final String path, final Collection<ApiKey> keys)
	{
		super(keys);
		this.filePath = path;
	}
	
	/**
	 * Set path of CSV file.
	 * 
	 * @param path - path of csv file.
	 */
	public void setPath(final String path)
	{
		this.filePath = path;
	}
	
	private void addFileHeader(final BufferedWriter bw) throws IOException
	{
		bw.write("# add keys to this file in the format:");
		bw.newLine();
		bw.write("# KeyType,Key Value");
		bw.newLine();
		bw.write("# Key type has to be one of :" + Arrays.toString(KeyType.names()));
		bw.newLine();
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @see ai_project.common.apiKeys.storage.interfaces.ApiKeyWriteableStorable#saveAll()
	 */
	@Override
	public synchronized void saveAll()
	{
		try (final BufferedWriter writer = new BufferedWriter(new FileWriter(this.filePath, false)))
		{
			final StringBuilder outputLine = new StringBuilder();
			this.addFileHeader(writer);
			synchronized (this.keysToSave)
			{
				for (final ApiKey apiKey : this.keysToSave)
				{
					outputLine.delete(0, outputLine.length());
					outputLine.append(apiKey.getKeyType().name());
					outputLine.append(WriteableCSVFileStorage.SEPARATOR);
					outputLine.append(apiKey.getKeyValueWithoutIncrementing());
					for (final Long timeUsed : apiKey.getTimesUsed())
					{
						outputLine.append(WriteableCSVFileStorage.SEPARATOR);
						outputLine.append(Long.toString(timeUsed.longValue(), Character.MAX_RADIX));
					}
					writer.write(outputLine.toString());
					writer.newLine();
				}
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
