# __Andie Image Editor__ : Group M

![Andie: A Non-Destructive Image Editor](.ReadmeAssets/Andie_Title.png)

## Download [ANDIE.jar](https://cosc202-m.cspages.otago.ac.nz/andie/Andie.jar)

---

## User guide 

__Welcome to ANDIE__

To get started with ANDIE, drag and drop an image that you'd like to edit, or open an image by going to `File` > `Open`.

Use scrolling and the shift key to pan around the image, or if you're using a trackpad, you can use two-finger panning. You can zoom in and out by holding `CTRL` and scrolling.

Image operations, like filters, colour adjustments, and transformations can be applied to your image, by clicking them in the menu bar. You can undo and redo operations by clicking the corosponding buttons in the toolbar, or by using `CTRL`+`Z` and `CTRL`+`Y` respectively.

Change Andie's language by selecting `Language`

---
## Docs
JavaDocs can be found [here](https://cosc202-m.cspages.otago.ac.nz/andie/docs).

---
## Folder Structure

The workspace contains two folders, where:

- `src`: the folder to maintain sources
- `lib`: the folder to maintain dependencies

Within `src` there is:

- Our source code, under `cosc202/andie`
- The language bundles for the languages we support, under `languages`
- Our unit tests, under `tests/cosc202/andie`

Within `src/cosc202/andie` there is:

- The main class, `Andie.java`, and other supporting classes core to the operation of Andie
- `actions`, containing the classes providing the actions for the menu bar 
- `components`, containing custom reusable swing components
- `operations`, containing grouped imageOperations to be used within actions.

---
## Testing
To test our program, we used a combination of Unit Testing, manual testing, and peer-reviewing.

After writing new code, we checked it's functionality by manually verifying its functionality, including testing it on a variety of edge-cases, for example:
- Transforming images with unusual dimensions (Very large, very small)
- Exporting images with transparency into formats that do not support transparency
- Opening images with unusual file extensions (e.g. .txt)
- Applying filters to images with unusual dimensions

We tested as a team, getting each other to run our code and try to break it.

We also used Unit Testing to further verify that the Transform operations specifically were performing as expected, given different image inputs.

In testing the filters, we had to verify that their output looked as expected. An example of how we tested our Median Filter was to apply it to a "salt and pepper" image, and verify that the noise was removed.

## Changes from the original ANDIE
We've refactored ANDIE in a number of ways, to make it easier to work with, and to add new features. 

As detailed in **Folder Structure**, we've restructured the code to be more modular, putting actions, components, and operations into their own packages.

We've added new support classes, including `LanguageConfig.java` for handling the language bundles.
- `Andie.java` has been refactored such that the UI is created in a separate method, `setup()`. This allows the UI to be reloaded when the language changes, without having to restart the program and lose any unsaved changes.
- `ImagePanel.java` has been heavily modified, with the current image now drawn in the center of the panel, and the panel being zoomable by scroll. A welcome message and ability to drag and drop an image have also been added.
- `EditableImage.java` has been added to, with support for saving and exporting images, as well as being able to test if an image has been modified since it was last saved.
- `ImageAction.java` has had an `updateState()` method added, which is to be called when the menu item becomes visible. This allows actions to be disabled depending on the current state of the image.
- We added `MenuActions.java`, a new superclass of our actions, which handles the creation of JMenuItem's for the menu bar, using the list of actions, and handles calling `updateState()` when the menu item becomes visible.

---

![Andie](.ReadmeAssets/Andie_Example_Image.png)
## Who Did What
### Bernard
- Image transform operations
	- Flip (Horizontal, Vertical)
	- Resize
	- Rotate (90˚, -90˚, 180˚)
### Blake
- Image filter operations
	- Sharpen Filter
	- Gaussian Blur
	- Median Blur
### Oliver
- Image adjustment operations
	- Brightness
	- Contrast adjustment
- Translations
	- English, Maori, French, German, Spanish, Turkish, Italian
### Jeb
- File operations
	- Open, Save, Save As, Export
- Error handling (Error messages)
- Error avoidance
	- Disabling menu items that cannot be applied to the current image
	- Warning user when closing unsaved image
- Quality of life changes
	- Welcome screen
	- Drag and drop to open an image
	- Image filling the frame by default
	- Ctrl+Scroll to zoom
	- Sliders for image filters/adjustments
- Language support
	- Loading correct messageBundle, fallback language
	- Restarting the UI when language changes