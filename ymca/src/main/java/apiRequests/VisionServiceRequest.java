package apiRequests;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.concurrent.Callable;

import javax.imageio.ImageIO;
import javax.net.ssl.HttpsURLConnection;

import models.apiKeys.ApiKey;
import models.apiKeys.KeyType;
import storage.KeyCache;

/**
 * Image Processing task.
 * 
 * @author Matt Rayner
 */
public class VisionServiceRequest implements Callable<String>
{
	/** Web address of API server. */
	private static final String WEB_ADDRESS = "https://api.projectoxford.ai/vision/v1.0/analyze?";
	/** Web address subscription key prefix. */
	private static final String SUB_KEY_PREFIX = "&subscription-key=";
	
	/** The image to process. */
	private final BufferedImage image;
	/** Type of info to get. */
	private final toGet toGet;
	
	/**
	 * Class Constructor.
	 *
	 * @param imageToProcess image to process.
	 * @param infoToGet type of info to get.
	 */
	public VisionServiceRequest(final BufferedImage imageToProcess, final toGet infoToGet)
	{
		super();
		this.image = imageToProcess;
		this.toGet = infoToGet;
		
	}
	
	/**
	 * Class Constructor.
	 *
	 * @param imagePath file path of image.
	 * @param infoToGet type of info to get.
	 * @throws IOException if an error occurs during reading.
	 */
	public VisionServiceRequest(final String imagePath, final toGet infoToGet) throws IOException
	{
		this(ImageIO.read(new File(imagePath)), infoToGet);
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @see java.util.concurrent.Callable#call()
	 */
	@Override
	public String call() throws IOException
	{
		return this.analyseImage(this.image, this.toGet);
	}
	
	/**
	 * Sends the image to the Microsoft servers for processing.
	 * 
	 * @param image the image to analyse
	 * @param infoToGet the type of into to get
	 * @return the JSON response from the server.
	 * @throws IOException
	 */
	private String analyseImage(final BufferedImage image, final toGet infoToGet) throws IOException
	{
		URL url = new URL(infoToGet.getURL(KeyCache.getInstance().getKey(KeyType.Vision)));
		final HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Content-Type", "application/octet-stream");
		connection.setRequestProperty("Content-Length", String.valueOf(0));
		connection.setDoOutput(true);
		//Send request
	    DataOutputStream wr = new DataOutputStream (
	        connection.getOutputStream());
	    ImageIO.write(image, "jpg", wr);
	    wr.close();
	    
		if (Thread.currentThread().isInterrupted())
		{
			return null;
		}
	    //Get Response
	    InputStream is = connection.getInputStream();
	    BufferedReader rd = new BufferedReader(new InputStreamReader(is));
	    StringBuffer response = new StringBuffer();
	    String line;
	    while ((line = rd.readLine()) != null) {
	      response.append(line);
	      response.append('\r');
	    }
	    
	    rd.close();
	    return response.toString();
	}
	
	/**
	 * Type of info to get for image.
	 * 
	 * @author Matt Rayner
	 */
	public enum toGet
	{
		/** Only Tags. */
		TAGS("visualFeatures=Tags"),
		/** Only Description. */
		DESCRIPTION("visualFeatures=Description"),
		/** Tags and Description. */
		TAGS_AND_DESCRIPTION("visualFeatures=Description,Tags");
		
		final String urlPostFix;
		
		/**
		 * Class Constructor.
		 *
		 * @param urlPostFix string added to end of url
		 */
		private toGet(final String urlPostFix)
		{
			this.urlPostFix = urlPostFix;
		}
		
		/**
		 * generate complete URL from key.
		 * 
		 * @param apiKey key to use.
		 * @return complete URL as String.
		 */
		public String getURL(final ApiKey apiKey)
		{
			return new StringBuilder(VisionServiceRequest.WEB_ADDRESS)
					.append(this.urlPostFix)
					.append(VisionServiceRequest.SUB_KEY_PREFIX)
					.append(apiKey.getKeyValue())
					.toString();
		}
	}
}
