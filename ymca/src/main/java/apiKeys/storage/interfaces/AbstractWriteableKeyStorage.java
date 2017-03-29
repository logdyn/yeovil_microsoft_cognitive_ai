package ai_project.common.apiKeys.storage.interfaces;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Map;

import ai_project.common.apiKeys.ApiKey;
import ai_project.common.apiKeys.KeyType;
import javafx.concurrent.Task;

public abstract class AbstractWriteableKeyStorage extends Task<Void>
{
	protected final Collection<ApiKey> keysToSave;

	public AbstractWriteableKeyStorage(final Collection<ApiKey> keys)
	{
		this.keysToSave = Collections.synchronizedCollection(keys);
	}
	
	public AbstractWriteableKeyStorage()
	{
		this(new LinkedHashSet<>());
	}
	
	protected abstract void saveAll();

	@Override
	public Void call()
	{
		this.saveAll();
		return null;
	}

	public void add(final ApiKey apiKey)
	{
		this.keysToSave.add(apiKey);
	}

	public void addAll(final Collection<ApiKey> apiKeys)
	{
		this.keysToSave.addAll(apiKeys);
	}
	
	public void addAll(final Map<KeyType, ApiKey> apiKeys)
	{
		this.keysToSave.addAll(apiKeys.values());
	}
}
