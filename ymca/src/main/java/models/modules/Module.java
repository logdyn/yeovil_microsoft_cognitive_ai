package models.modules;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public enum Module
{
	TEST("Test Module", "/WEB-INF/modules/test.html"),
	WEBCAM("Webcam", "/WEB-INF/modules/webcam.html", "js/webcam.js", "js/xhttp.js", "css/webcam.css");
	
	private final String name;
	private final String contentPath;
	private final Set<String> javascriptPaths;
	
	/**
	 * Class Constructor.
	 *
	 * @param name - the name of this module
	 * @param contentPath - the path of the jsp file to include in this module.
	 */
	private Module(final String name, final String contentPath)
	{
		this.name = name;
		this.contentPath = contentPath;
		this.javascriptPaths = Collections.emptySet();
	}
	
	/**
	 * Class Constructor.
	 *
	 * @param name - the name of this module
	 * @param contentPath - the path of the jsp file to include in this module.
	 * @param javascriptPath - the path of the javascript file required by this module.
	 */
	private Module(final String name, final String contentPath, final String javascriptPath)
	{
		this.name = name;
		this.contentPath = contentPath;
		this.javascriptPaths = Collections.singleton(javascriptPath);
	}
	
	/**
	 * Class Constructor.
	 *
	 * @param name - the name of this module
	 * @param contentPath - the path of the jsp file to include in this module.
	 * @param javascriptPath - the paths of any javascript files required by this module.
	 */
	private Module(final String name, final String contentPath, final Collection<? extends String> javascriptPath)
	{
		this.name = name;
		this.contentPath = contentPath;
		this.javascriptPaths = Collections.unmodifiableSet(new LinkedHashSet<>(javascriptPath));
	}
	
	/**
	 * @return the name of this module.
	 */
	public String getName()
	{
		return this.name;
	}
	
	/**
	 * @return the path of the jsp file to include as this modules content.
	 */
	public String getContentpath()
	{
		return this.contentPath;
	}
	
	/**
	 * @return the paths of any javascript files this module uses.
	 */
	public Set<String> getJavaScriptPaths()
	{
		return this.javascriptPaths;
	}
}
