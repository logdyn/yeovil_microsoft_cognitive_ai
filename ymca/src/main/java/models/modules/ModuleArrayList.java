package models.modules;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

public class ModuleArrayList extends ArrayList<Module>
{
	/** serialVersionUID. */
	private static final long serialVersionUID = 7053333704471898044L;

	/**
     * Constructs an empty list with an initial capacity of ten.
     */
	public ModuleArrayList()
	{
		super();
	}
	
	/**
     * Constructs a list containing the elements of the specified
     * collection, in the order they are returned by the collection's
     * iterator.
     *
     * @param collection the collection whose elements are to be placed into this list
     * @throws NullPointerException if the specified collection is null
     */
	public ModuleArrayList(final Collection<? extends Module> collection)
	{
		super(collection);
	}
	
	/**
     * Constructs an empty list with the specified initial capacity.
     *
     * @param  initialCapacity  the initial capacity of the list
     * @throws IllegalArgumentException if the specified initial capacity
     *         is negative
     */
	public ModuleArrayList(final int initialCapacity)
	{
		super(initialCapacity);
	}
	
	/**
	 * @return a set of all javascript links used by all of these modules.
	 */
	public Set<String> getJavascriptLinks()
	{
		final Set<String> results = new LinkedHashSet<>(this.size());
		for(final Module module : this)
		{
			results.addAll(module.getJavaScriptPaths());
		}
		return results;
	}
}
