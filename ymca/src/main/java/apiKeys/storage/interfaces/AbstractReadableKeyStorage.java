package apiKeys.storage.interfaces;

import java.util.Collection;

import apiKeys.ApiKey;
import javafx.concurrent.Task;

public abstract class AbstractReadableKeyStorage extends Task<Collection<ApiKey>>
{
	@Override
	public Collection<ApiKey> call()
	{
		return this.readAll();
	}
	
	protected abstract Collection<ApiKey> readAll();
}
