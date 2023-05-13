package cosc202.andie.models;

import java.awt.image.BufferedImage;
import java.util.function.Function;

import cosc202.andie.ImageOperation;
import cosc202.andie.Utils;
import cosc202.andie.ImageOperation.ImageOperationException;

public class Operations {
		private ImageOperation lastOperation;
		private Thread operationThread;

		private AndieModel model;

		public Operations(AndieModel model) {
			this.model = model;
		}

		private void applyOperation(ImageOperation operation) {
			model.getImage().apply(operation);
		}
		public void update(ImageOperation operation, boolean threadRequired) {
			//If this is a new operation, cancel the previous one
			if (lastOperation != null && lastOperation.getClass() != operation.getClass()) {
				cancel();
			}
			//Cancel any previous operation thread
			if(operationThread != null) {
				operationThread.interrupt();
				operationThread = null;

			}
			this.lastOperation = operation;

			BufferedImage baseImage = Utils.deepCopy(model.getImage().getCurrentImage());

			Function<BufferedImage, BufferedImage> operationFunction = (image) -> {
				try {
					return operation.drawPreview(image);
				} catch (ImageOperationException e) {
					return image;
				}
			};

			if (!threadRequired) {
				model.setPreviewImage(operationFunction.apply(baseImage));
				return;
			}

			OperationRunnable.OperationRunnableListener listener = (result) -> {
				model.setPreviewImage(result);
			};

			OperationRunnable operationRunable = new OperationRunnable(operationFunction, baseImage, listener);
			operationThread = new Thread(operationRunable);
			operationThread.start();

		}
		public void apply() {
			apply(lastOperation);
		}
		public void apply(ImageOperation operation) {
			cancel();
			applyOperation(operation);
		}
		public void cancel() {
			if(operationThread != null) {
				operationThread.interrupt();
				operationThread = null;
			}
			model.clearPreviewImage();
		}

	
	private class OperationRunnable implements Runnable {
		OperationRunnableListener listener;
		Function<BufferedImage, BufferedImage> operation;
		BufferedImage baseImage;
		public OperationRunnable(Function<BufferedImage, BufferedImage> operation, BufferedImage baseImage, OperationRunnableListener listener) {
			this.operation = operation;
			this.baseImage = baseImage;
			this.listener = listener;
		}
		public void run() {
			try {
			BufferedImage result = operation.apply(baseImage);

			if (Thread.interrupted()) return;
			listener.filterThreadFinished(result);
			} catch (RuntimeException e) {}
		}
		public interface OperationRunnableListener {
			public void filterThreadFinished(BufferedImage result);
		}
	}
}
