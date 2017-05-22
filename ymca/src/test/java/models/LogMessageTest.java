package models;

import java.util.logging.Level;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

public class LogMessageTest
{
	@Test
	public void testToJSONString()
	{
		final LogMessage original = new LogMessage("0", Level.CONFIG, "test", 0);
		Assert.assertEquals(original, new LogMessage(new JSONObject(original.toJSONString())));
	}
}
