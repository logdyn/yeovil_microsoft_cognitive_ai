package models;

import java.util.UUID;

public class Product
{
	private final UUID id;
	private final String name;
	private final String desc;
	private final String type;
	private final int price;
	private final int quantity;
	private final String imageURL;
	
	/**
	 * Class Constructor.
	 */
	public Product(final UUID id, final String name, final String desc, final String type, final int price, final int quantity, final String imageURL)
	{
		super();
		this.id = id;
		this.name = name;
		this.desc = desc;
		this.type = type;
		this.price = price;
		this.quantity = quantity;
		this.imageURL = imageURL;
	}
	
	/**
	 * @return the UUID of this product
	 */
	public UUID getId()
	{
		return this.id;
	}
	/**
	 * @return the name of this product
	 */
	public String getName()
	{
		return this.name;
	}
	/**
	 * @return the description of this product
	 */
	public String getDescription()
	{
		return this.desc;
	}
	/**
	 * @return the type of this product
	 */
	public String getType()
	{
		return this.type;
	}
	/**
	 * @return the price of this product as an integer of pennys.
	 */
	public int getPricePennys()
	{
		return this.price;
	}
	
	/**
	 * returns the price in the correct format depending on if the price is greater than 100 pennys. <br />
	 * e.g. price of 20 returns <tt>20p</tt> and price of 220 returns <tt>£2.20</tt>.
	 * 
	 * @return the price of this product as a String
	 */
	public String getPriceString()
	{
		String result = Integer.toString(this.price);
		if (result.length() < 3)
		{
			result = result + "p";
		}
		else
		{
			result = "£" + result.substring(0, result.length() - 2) + "." + result.substring(result.length() - 2);
		}
		return result;
	}
	/**
	 * @return the quantity of this product
	 */
	public int getQuantity()
	{
		return this.quantity;
	}
	/**
	 * @return the URL of an image of this product
	 */
	public String getImageURL()
	{
		return this.imageURL;
	}
	
}
