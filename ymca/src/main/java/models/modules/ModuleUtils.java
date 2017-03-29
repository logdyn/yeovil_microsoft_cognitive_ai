package models.modules;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

public final class ModuleUtils
{
	/**
	 * @return a set of all javascript links used by all of these modules.
	 */
	public static final Set<String> getJavascriptLinks(final Collection<? extends Module> modules)
	{
		final Set<String> results = new LinkedHashSet<>(modules.size());
		for(final Module module : modules)
		{
			results.addAll(module.getJavaScriptPaths());
		}
		return results;
	}
}
