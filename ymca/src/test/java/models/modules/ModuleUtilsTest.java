/**
 * 
 */
package models.modules;

import org.hamcrest.core.IsEqual;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Matt Rayner
 */
public class ModuleUtilsTest
{
	private static final int testData[][] =
		{
		// {size, columns, rows}
				{0, 0, 0},
				{1, 1, 1},
				{2, 2, 1},
				{3, 2, 2},
				{4, 2, 2},
				{5, 3, 2},
				{6, 3, 2},
				{7, 3, 3},
				{8, 3, 3},
				{9, 3, 3},
				{10, 4, 3},
				{11, 4, 3},
				{12, 4, 3},
				{13, 4, 4},
				{14, 4, 4},
				{15, 4, 4},
				{16, 4, 4}
		};
	/**
	 * Test method for {@link models.modules.ModuleUtils#getColumns(int)}.
	 */
	@Test
	public void testGetColumns()
	{
		for (int data[] : ModuleUtilsTest.testData)
		{
			Assert.assertThat(ModuleUtils.getColumns(data[0]), new IsEqual<Integer>(data[1]));
		}
	}

	/**
	 * Test method for {@link models.modules.ModuleUtils#getRows(int)}.
	 */
	@Test
	public void testGetRows()
	{
		for (int data[] : ModuleUtilsTest.testData)
		{
			Assert.assertThat(ModuleUtils.getRows(data[0]), new IsEqual<Integer>(data[2]));
		}
	}

}
