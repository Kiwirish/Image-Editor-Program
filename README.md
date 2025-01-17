# __Andie Image Editor__ : Group M

![Andie: A Non-Destructive Image Editor](.ReadmeAssets/Andie_Title.png)

<!-- ## Download [ANDIE.jar](https://cosc202-m.cspages.otago.ac.nz/andie/Andie.jar)

--- -->
<!-- Noticed last minute that JAR would compile, but couldn't locate resources, so I cannot include it -->

## User guide 

__Welcome to ANDIE__

To get started with ANDIE, drag and drop an image that you'd like to edit, or open an image by going to `File` > `Open`.

Use scrolling and the shift key to pan around the image, or if you're using a trackpad, you can use two-finger panning. You can zoom in and out by holding `CTRL` and scrolling.

Image operations like filters, colour adjustments, and transformations can be applied to your image, by clicking them in the menu bar. You can undo and redo operations by clicking the corosponding buttons in the toolbar, or by using `CTRL`+`Z` and `CTRL`+`Y`.

You can draw shapes onto images in Andie by using the shapes tools. Click to select the line, rectangle or elipse tool in the toolbar, and drag to draw onto your image. You can hold `SHIFT` to have these shapes maintain their ratios, and `CTRL` to have them scale from the start of your drag.

To change the active fill color, stroke color, or stroke width, just click the corrosponding button in the toolbar.

Switch to the select tool to select regions of the image, also by dragging. Once a region has been selected, press the crop button to crop your image.

If you want to save a sequence of operations for later use, use the macros menu to record a macro, apply the desired operations, finish your recording, and save the resulting macro file to your computer. To reapply the recorded operations, use `Macros` > `Apply a Macro`.

Andie is a **Non-Destructive** image editor, so any operations you make will be stored alongside the image when you saved, and the image itself will be left untouched. If you want to export your modifications to a new image, use the export menu by going to `File` > `Export`. Select a format, and save your new image.

Change Andie's language by selecting `Language`

*A note about keyboard shortcuts: While the above guide mentions keyboard shortcuts involving `CTRL`, these shortcuts will instead be mapped to `COMMAND` if Andie is running on MacOS, as this is generally what Mac users would expect*


*Toolbar icons from [Google Material Symbols and Icons](https://fonts.google.com/icons), licensed under the [Apache License Version 2.0](https://www.apache.org/licenses/LICENSE-2.0)*

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

- The main class, `Andie.java`, and other supporting classes core to the operation of Andie.
- `actions`, containing the classes defining the "actions" Andie can perform. (displayed in the menu bar)
- `components`, containing custom reusable swing components.
- `operations`, containing grouped imageOperations to be used within actions.
- `tools`, containing different tools that can be activated and used on the image. 
- `models` containing the "business logic" of the program, including the `EditableImage` class, which stores the image and operations applied to it.
- `controllers` containing classes that glue the UI and models together, responsible for handling IO & Threading among other things.

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


In our testing, we found one interesting quirk of how our operations were applied: In "palletised" `png` images, the limited color pallete is maintained by ANDIE, such that no colors not already in the images's pallete may be added. This leads to some unexpected results from filters, and unexpected hatching/dithering done by the Graphics2D API when approximating a custom color, while painting onto a png with a severely limited color pallete.

We've decided to leave this quirk in, rather than coerce images with limited color palletes into a different format while they are edited.

It did cause us some issues though; the "Line" shape in Andie is antialised, which did not play well with the limited color pallete of the image, leading to lines not functioning as expected. We fixed this by only enabling antialiasing when the image stores color with at least 5 bits.

---

## Changes from the original ANDIE
We've refactored ANDIE in a number of ways, to make it easier to work with, and to add new features. 

As detailed in **Folder Structure**, we've restructured the code to be more modular, putting actions, components, and operations into their own packages.

We've added new support classes, including `LanguageConfig.java` for handling the language bundles.
ANDIE has be refactored to (loosely) follow the [MVC](https://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93controller) design pattern, with business logic, including the `EditableImage`, image previewing, overlays, active tools, operations and macros, as well as any other "state" being handled by the Model classes in `models/`. Controllers, which bridge the view to the model are defined in `controllers/`, and the view is defined in `AndieView.java`.

The reason behind this change was to make ANDIE more extendable, and to keep areas of the code less tightly coupled with each other. Given the few features we were required to add, this was not strictly necessary, but I felt it was a good idea to make the code more maintainable.

Refactoring ANDIE to MVC means ANDIE relies heavily on the [Observer Pattern](https://en.wikipedia.org/wiki/Observer_pattern), with listeners being used all over the code to notify ui components, controllers, and even other models of updates to the model.
Given the large number of listeners, we initially had a severe memory leak caused by listeners not being removed when the image was closed, and the images they referenced not being garbage collected. See: [The lapsed listener problem](https://en.wikipedia.org/wiki/Lapsed_listener_problem). This had to be fixed by adding unsubscribe methods for every type of listener, and making sure that controllers, models and swing components were notified of their removal, and could unsubscribe themselves to clean up.

---

![Andie](.ReadmeAssets/Andie_demo_images.png)

## Who Did What (Second Deliverable)

### Bernard
- Toolbar (*Defined within `AndieView`*)
- Code commenting
### Blake
- Emboss Filter (With all 8 directions)
- Sobel Filter
### Oliver
- Helped with Emboss & Sobel filters
- Added further translations for new strings
- Code commenting
### Jeb
- Refactoring
- Added operation live previewing (with threading)
- Macros
  - Including a Macros panel, showing a list of operations as they are recorded
  - Ability to record, save and load macros
- Shape operations
	- Line, Rectangle, Ellipse
- Added new `ImagePanView` for proper image panel zooming and panning 
- Added support for tools
	- Added Line, Rectangle, and Ellipse tools
- Select tool
  - Created an overlay system for overlaying graphics onto the panel, allowing for the select tool to be implemented
  - Created select tool with animation
- Crop operation
- Gitlab pipeline
---

![Andie](.ReadmeAssets/Andie_Example_Image.png)

## Who Did What (First Deliverable)
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