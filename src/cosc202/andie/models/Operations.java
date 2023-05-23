package cosc202.andie.models;

import java.awt.image.BufferedImage;
import java.util.function.Function;

import cosc202.andie.ImageOperation;
import cosc202.andie.Utils;
import cosc202.andie.ImageOperation.ImageOperationException;
import cosc202.andie.controllers.OpsController;

/**
 * <p>
 * The operations model for ANDIE. Handles applying operation lifecycles (Previewing and applying with threading)
 * </p>
 * 
 * <p> 
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">cc by-nc-sa 4.0</a>
 * </p>
 * 
 * @see AndieModel
 * @see OpsController
 * 
 * @author Jeb Nicholson
 * @version 1.0
 */
public class Operations {
		private ImageOperation lastOperation;
		private Thread operationThread;

		private AndieModel model;

		/**
		 * Create a new Operations model
		 * @param model The base AndieModel
		 */
		public Operations(AndieModel model) {
			this.model = model;
		}

		/**
		 * Apply a given operation
		 * @param operation The operation to apply
		 * @return Whether the operation was successful
		 */
		private boolean applyOperation(ImageOperation operation) {
			return model.getImage().apply(operation);
		}

		/**
		 * Update the current operation and preview it once it's done
		 * @param operation The {@link ImageOperation} to update
		 * @param threadRequired Whether the operation should be run on a separate thread (If it's slow)
		 */
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

			OperationRunnableListener listener = (result) -> {
				model.setPreviewImage(result);
			};

			OperationRunnable operationRunable = new OperationRunnable(operationFunction, baseImage, listener);
			operationThread = new Thread(operationRunable);
			operationThread.start();

		}

		/**
		 * Apply the last operation to have been updated
		 * @return Whether the operation was successful
		 */
		public boolean apply() {
			return apply(lastOperation);
		}

		/**
		 * Apply a given operation
		 * @param operation The operation to apply
		 * @return Whether the operation was successful
		 */
		public boolean apply(ImageOperation operation) {
			cancel();
			return applyOperation(operation);
		}

		/** Cancel the operation */
		public void cancel() {
			if(operationThread != null) {
				operationThread.interrupt();
				operationThread = null;
			}
			model.clearPreviewImage();
		}

	
	/** The class used in instantiating a new thread for running an operation */ 
	private class OperationRunnable implements Runnable {
		OperationRunnableListener listener;
		Function<BufferedImage, BufferedImage> operation;
		BufferedImage baseImage;

		/**
		 * Create a new OperationRunnable
		 * @param operation The operation to run
		 * @param baseImage The base image to run the operation on
		 * @param listener The listener to call when the operation is finished
		 */
		public OperationRunnable(Function<BufferedImage, BufferedImage> operation, BufferedImage baseImage, OperationRunnableListener listener) {
			this.operation = operation;
			this.baseImage = baseImage;
			this.listener = listener;
		}

		/** Run the operation */
		public void run() {
			try {
			BufferedImage result = operation.apply(baseImage);
			if (Thread.interrupted()) return;
			listener.filterThreadFinished(result);
			} catch (RuntimeException e) {
				if (e.getMessage().equals("Interrupted") || Thread.interrupted()) return;
				e.printStackTrace();
			}
		}


	}

	/** A listener for when OperationRunnables finish */
	public interface OperationRunnableListener {
		public void filterThreadFinished(BufferedImage result);
	}

}
