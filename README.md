## Getting Started

Welcome to the VS Code Java world. Here is a guideline to help you get started to write Java code in Visual Studio Code.

## Folder Structure

The workspace contains two folders by default, where:

- `src`: the folder to maintain sources
- `lib`: the folder to maintain dependencies

Meanwhile, the compiled output files will be generated in the `bin` folder by default.

> If you want to customize the folder structure, open `.vscode/settings.json` and update the related settings there.

## Dependency Management

The `JAVA PROJECTS` view allows you to manage your dependencies. More details can be found [here](https://github.com/microsoft/vscode-java-dependency#manage-dependencies).

Team M ReadME 

The task was divided up Where we each had three tasks:
Blake: Sharpen Filter, Gaussian Blur Filter and Median Filter. 
Ollie: Brightness adjustment, Contrast adjustment and Multilingual support.
Bernard: Image resize, Image rotations, Image flip.
Jeb: Image export, Exception handling, Other error avoidance/Prevention,

Testing

Median Filter:
Salt and pepper test - the nose of the salt and pepper gets cancelled out.

User guide 

To use out teams Image filter ANDIE either open an image file through the File menu or simply drag and drop in the window popup. Browse through the many different options provided, Note the menu options that show up grey are not applicable to that image. Whilst manipulating your image if you accidentally make a change that is no longer desirable just simply use the undo option to return the image to its previous state. After the image is how you want it use the file menu to save and or export your new image file.

Significant refactoring

The structure of our code has been further sorted into folders for different categories for example all the transform filters are in a single folder.  The Menu option will turn grey when they are unusable.The view option was changed so there an option to reset size and an option to fill the window. Ability to open an image by dragging and dropping the image onto the ANDIE window.
These quality of life changes were courtesy of Jeb