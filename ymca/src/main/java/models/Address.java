package models;

import java.util.UUID;

public class Address
{
	private final UUID id;
	private final String building;
	private final String street;
	private final String city_town;
	private final String area_code;
	private final String country;

	/**
	 * Class Constructor.
	 */
	public Address(final UUID id, final String building, final String street, final String city_town, final String area_code, final String country)
	{
		this.id = id;
		this.building = building;
		this.street = street;
		this.city_town = city_town;
		this.area_code = area_code;
		this.country = country;
	}
	
	public UUID getId()
	{
		return this.id;
	}

	/**
	 * @return the building
	 */
	public String getBuilding()
	{
		return this.building;
	}

	/**
	 * @return the street
	 */
	public String getStreet()
	{
		return this.street;
	}

	/**
	 * @return the city_town
	 */
	public String getCity_town()
	{
		return this.city_town;
	}

	/**
	 * @return the area_code
	 */
	public String getArea_code()
	{
		return this.area_code;
	}

	/**
	 * @return the country
	 */
	public String getCountry()
	{
		return this.country;
	}
	
	public static final Address NULL = new Address(null,null,null,null,null,null)
	{
		@Override
		public boolean equals(final Object obj)
		{
			return obj == null || super.equals(obj);
		}
		
	};
}
