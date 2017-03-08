package product;

import org.hamcrest.core.IsEqual;
import org.junit.Assert;
import org.junit.Test;

import models.Product;

public class ProductTest
{

	@Test
	public void testGetPriceString()
	{
		final Product product = new Product(null, null, null, null, 220, 0, null);
		Assert.assertThat(product.getPriceString(), new IsEqual<String>("£2.20"));
	}

}
