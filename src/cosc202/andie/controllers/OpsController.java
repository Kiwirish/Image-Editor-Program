package cosc202.andie.controllers;
import cosc202.andie.ImageOperation;
import cosc202.andie.models.AndieModel;

/**
 * <p>
 * The controller for in-progress imageoperations
 * </p>
 * 
 * <p> 
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">cc by-nc-sa 4.0</a>
 * </p>
 * 
 * @see AndieController
 * @see ImageOperation
 * @author Jeb Nicholson
 * @version 1.0
 */
public class OpsController {
	private AndieModel model;

	/**
	 * Create a new OpsController
	 * @param model The base model
	 */
	public OpsController(AndieModel model) {
		this.model = model;	
	}

	/**
	 * Update the current operation
	 * @param operation The {@link ImageOperation} to update
	 * @param threadRequired Whether the operation should be run on a separate thread (If it's slow)
	 */
	public void update(ImageOperation operation, boolean threadRequired) {
		model.operations.update(operation, threadRequired);
	}

	/**
	 * Updates the current operation (With threading)
	 * @param operation The {@link ImageOperation} to update
	 */
	public void update(ImageOperation operation) {
		model.operations.update(operation, true);
	}

	/** Apply the last operation to have been updated */
	public void apply() {
		model.operations.apply();
	}

	/** Cancel the operation */
	public void cancel() {
		model.operations.cancel();
	}

	/** Apply a given {@link ImageOperation} to the image */
	public void apply(ImageOperation operation) {
		model.operations.apply(operation);
	}

	/**
	 * End the current operation, by applying or canceling (Convienience method)
	 * @param applyOperation Whether to apply the operation
	 */
	public void end(boolean applyOperation) {
		if (applyOperation) {
			model.operations.apply();
		} else {
			model.operations.cancel();
		}
	}

}
