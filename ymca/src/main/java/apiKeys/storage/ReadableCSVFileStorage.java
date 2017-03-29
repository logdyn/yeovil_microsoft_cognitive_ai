/**
 * 
 */
package apiKeys.storage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;

import apiKeys.ApiKey;
import apiKeys.KeyType;
import apiKeys.storage.interfaces.AbstractReadableKeyStorage;

/**
 * @author Matt
 *
 */
public class ReadableCSVFileStorage extends AbstractReadableKeyStorage
{
	private static final char SEPARATOR = ',';
	
	private static final char COMMENT = '#';
	
	private String filePath;
	
	/**
	 * Class Constructor.
	 *
	 * @param path - path of csv file.
	 */
	public ReadableCSVFileStorage(final String path)
	{
		this.filePath = path;
	}
	
	/**
	 * Class Constructor.
	 */
	public ReadableCSVFileStorage()
	{
		this(".\\resources\\keys.csv");
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

	/**
	 * {@inheritDoc}
	 *
	 * @see ai_project.common.apiKeys.storage.interfaces.ApiKeyWriteableStorable#readAll()
	 */
	@Override
	public synchronized Collection<ApiKey> readAll()
	{
		final Collection<ApiKey> results = new LinkedList<>();
		try (final BufferedReader reader = new BufferedReader(new FileReader(this.filePath)))
		{
			for (String readLine = reader.readLine(); readLine != null; readLine = reader.readLine())
			{
				try
				{
					if (!readLine.startsWith(String.valueOf(ReadableCSVFileStorage.COMMENT)))
					{
						final String[] sections = readLine.split(String.valueOf(ReadableCSVFileStorage.SEPARATOR));
						if (sections.length > 2)
						{
							final Set<Long> uses = new LinkedHashSet<>(sections.length - 2);
							for (int i = 2; i < sections.length; i++)
							{
								uses.add(Long.parseUnsignedLong(sections[i], Character.MAX_RADIX));
							}
							results.add(new ApiKey(sections[1], KeyType.valueOf(sections[0]), uses));
						}
						else
						{
							results.add(new ApiKey(sections[1], KeyType.valueOf(sections[0])));
						}
					}
				}
				catch (IllegalArgumentException e)
				{
					//no Key type found
				}
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return results;
	}
}
