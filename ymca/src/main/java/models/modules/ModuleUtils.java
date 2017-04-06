package models.modules;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public final class ModuleUtils
{
	private static final Collection<Module> DEFAULT_MODULES = new LinkedHashSet<>();
	
	static
	{
		ModuleUtils.DEFAULT_MODULES.add(Module.WEBCAM);
		ModuleUtils.DEFAULT_MODULES.add(Module.CONTROLS);
		ModuleUtils.DEFAULT_MODULES.add(Module.OUTPUT_LOG);
	}
	
	public static final Collection<Module> getDefaultModules()
	{
		return Collections.unmodifiableCollection(ModuleUtils.DEFAULT_MODULES);
	}
	
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
	
	/**
	 * @param moduleCount number of modules to display
	 * @return the number of columns to display modules in.
	 */
	public static final int getColumns (final int moduleCount)
	{
		return (int) Math.ceil(Math.sqrt(moduleCount));
	}
	
	/**
	 * @param moduleCount number of modules to display
	 * @return the number of rows to display modules in.
	 */
	public static final int getRows (final int moduleCount)
	{
		if (moduleCount == 0)
		{
			return 0;
		}
		return (int) Math.ceil(moduleCount / Math.ceil(Math.sqrt(moduleCount)));
	}
}
