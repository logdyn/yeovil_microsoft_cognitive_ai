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
			for (final String javascript : module.getExternalPaths())
			{
				if (javascript.endsWith(".js"))
				{
					results.add(javascript);
				}
			}
		}
		return results;
	}
	/**
	 * @return a set of all css links used by all of these modules.
	 */
	public static final Set<String> getCssLinks(final Collection<? extends Module> modules)
	{
		final Set<String> results = new LinkedHashSet<>(modules.size());
		for(final Module module : modules)
		{
			for (final String css : module.getExternalPaths())
			{
				if (css.endsWith(".css"))
				{
					results.add(css);
				}
			}
		}
		return results;
	}
}
