package cosc202.andie.controllers;
import cosc202.andie.ImageOperation;
import cosc202.andie.models.AndieModel;

public class AndieOperationsController {
	private AndieModel model;

	public AndieOperationsController(AndieModel model) {
		this.model = model;	
	}

	public void update(ImageOperation operation, boolean threadRequired) {
		model.operations.update(operation, threadRequired);
	}
	public void update(ImageOperation operation) {
		model.operations.update(operation, true);
	}
	public void apply() {
		model.operations.apply();
	}
	public void cancel() {
		model.operations.cancel();
	}
	public void apply(ImageOperation operation) {
		model.operations.apply(operation);
	}
	public void end(boolean applyOperation) {
		if (applyOperation) {
			model.operations.apply();
		} else {
			model.operations.cancel();
		}
	}

}
