package servlets;

import endpoints.Observer;

public interface ObservableServerClass
{
	public void addObserver(Observer observer);
	
	public void deleteObserver(Observer observer);
	
	public void notifyObservers(Object object);
}
