package listeners;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import storage.Database;

public class DatabaseListener implements ServletContextListener
{
	@Override
	public void contextDestroyed(final ServletContextEvent sce) 
	{
		Database.getInstance().destroy();
    }
}