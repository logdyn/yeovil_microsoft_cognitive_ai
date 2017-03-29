package businesses.video;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import businesses.BaseBusiness;
import businesses.Controller;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

/**
 * Image Business. This class addes functionality to select and process indiviual images selected from users computer.
 * 
 * @author Matt Rayner
 */
public class ImageBusiness extends BaseBusiness
{
	/** File path of image. */
	private String imagePath = "resources/images/blank.png";
	
	/** The image to display. */
	private Image image = new Image("file:" + this.imagePath, true);
	
	/** The javafx Node to display an image. */
	private ImageView imageView;
	
	/**
	 * Class Constructor.
	 *
	 * @param controller the application controller
	 */
	public ImageBusiness(final Controller controller)
	{
		super(controller);
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @see ai_project.businesses.interfaces.Business#getTitle()
	 */
	@Override
	public String getTitle()
	{
		return "Image";
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see ai_project.businesses.interfaces.BaseBusiness#onCreate()
	 */
	@Override
	public void onCreate()
	{
		
	}

	/**
	 * Creates and returns the UI elements to insert into a tab for this business.
	 * 
	 * @return new {@link Node} for tab.
	 */
	private Node getTabNode()
	{
		Button processBtn = new Button("Process");
		processBtn.setOnAction(new ProcessBtnAction());
		Button selectBtn = new Button("Select");
		selectBtn.setOnAction(new SelectBtnAction());
		this.imageView = new ImageView(this.image);
		this.imageView.setFitWidth(500);
		this.imageView.setPreserveRatio(true);
		
		selectBtn.setMaxWidth(Double.MAX_VALUE);
		processBtn.setMaxWidth(Double.MAX_VALUE);
 
		HBox hBox = new HBox(10, selectBtn, processBtn);
		VBox vBox = new VBox(this.imageView, hBox);
		
		HBox.setHgrow(selectBtn, Priority.ALWAYS);
		HBox.setHgrow(processBtn, Priority.ALWAYS);
		VBox.setVgrow(this.imageView, Priority.ALWAYS);
		return vBox;
	}

	/**
	 * Event Handler for clicking the select button.
	 * 
	 * @author Matt Rayner
	 */
	private class SelectBtnAction implements EventHandler<ActionEvent>
	{
		@Override
		public void handle(final ActionEvent event)
		{
			FileChooser fc = new FileChooser();
			fc.setTitle("Open Image");
			final File file = fc.showOpenDialog(null);
			if (file != null)
			{
				ImageBusiness.this.imagePath = file.getPath();
				ImageBusiness.this.image = new Image("file:" + ImageBusiness.this.imagePath, true);
				ImageBusiness.this.imageView.setImage(ImageBusiness.this.image);
			}
		}
	}
	
	/**
	 * Event Handler for clicking the process button.
	 * 
	 * @author Matt Rayner
	 */
	private class ProcessBtnAction implements EventHandler<ActionEvent>
	{
		@Override
		public void handle(final ActionEvent event)
		{
			try
			{
				final VisionServiceRequest req =
					new VisionServiceRequest(
						ImageBusiness.this.imagePath,
						VisionServiceRequest.toGet.TAGS_AND_DESCRIPTION);
				req.setOnSucceeded(new EventHandler<WorkerStateEvent>()
				{
					@Override
					public void handle(final WorkerStateEvent event)
					{
						try
						{
							System.out.println(req.get());
						}
						catch (InterruptedException | ExecutionException e)
						{
							e.printStackTrace();
						}
					}
				});
				ImageBusiness.this.controller.execute(req);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
}
